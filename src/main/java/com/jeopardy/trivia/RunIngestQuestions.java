package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class RunIngestQuestions implements Job {

    private static final int NUMBER_OF_DAYS= 1000;

    static DatabaseConnect db_conn = new DatabaseConnect();

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        try {

            URL url;
            Connection sqlConn = null;

//             Connection db_conn1 = db_conn.connect();
            Statement st = db_conn.connect().createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Questions (\n" +
                    "    QuestionID int,\n" +
                    "    Answer varchar(255),\n" +
                    "    Question varchar(650),\n" +
                    "    Value int,\n" +
                    "    Airdate int,\n" +
                    "    CategoryId int,\n" +
                    "    GameId int,\n" +
                    "    CategoryTitle varchar(255),\n" +
                    "    CluesCount int);");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS QuestionsTesting (\n" +
                    "    QuestionID int,\n" +
                    "    Answer varchar(255)\n);");

            st.close();

//             createSt.executeQuery("SELECT QuestionId")
            int numberOfRequests=0;
            int totalNumberOfRequests=0;
            int waitTimeInSeconds=1;

            for(int i=0; i<NUMBER_OF_DAYS-NUMBER_OF_DAYS+100; i++) {
                System.out.println("New Request ----------------------------------------------------------");
                numberOfRequests++;
                totalNumberOfRequests++;
                if(numberOfRequests>5) {
                    int multiplier=totalNumberOfRequests/100;
                    Thread.sleep(1000*waitTimeInSeconds*(1+multiplier));
                    numberOfRequests=0;
                    if(totalNumberOfRequests>1000) {
                        totalNumberOfRequests=0;
                    }
                }
                LocalDate dayOfGame = LocalDate.now().minusDays(i+120);
                LocalDate nextDayAfterGame = LocalDate.now().minusDays(i-1+120);
                if(dayOfGame.getDayOfWeek() == DayOfWeek.SATURDAY || dayOfGame.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("skip");
                }
                else {
                    int yearOfGame = dayOfGame.getYear();
                    int monthOfYear = dayOfGame.getMonthValue();
                    int dayOfMonth = dayOfGame.getDayOfMonth();

                    int nextDayYearOfGame = nextDayAfterGame.getYear();
                    int nextDayMonthOfYear = nextDayAfterGame.getMonthValue();
                    int nextDayDayOfMonth = nextDayAfterGame.getDayOfMonth();

                    url = new URL("http://jservice.io/api/clues?min_date="+yearOfGame+"-"+monthOfYear+"-"+
                            dayOfMonth+"&max_date="+nextDayYearOfGame+"-"+nextDayMonthOfYear+"-"+nextDayDayOfMonth);
                    System.out.println(url);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int responseCode = conn.getResponseCode();

                    // 200 OK
                    if (responseCode != 200) {
                        throw new RuntimeException("HttpResponseCode: " + responseCode);
                    } else {

                        StringBuilder informationString = new StringBuilder();
                        Scanner scanner = new Scanner(url.openStream());

                        while (scanner.hasNext()) {
                            informationString.append(scanner.nextLine());
                        }
                        //Close the scanner
                        scanner.close();

//                    System.out.println(informationString);


                        //JSON simple library Setup with Maven is used to convert strings to JSON
                        JSONParser parse = new JSONParser();
                        JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

                        //Get the first JSON object in the JSON array
                        if(dataObject.size()==0) {
                            continue;
                        }
                        System.out.println(dataObject.get(0));
                        Connection new_conn = db_conn.connect();
                        for(int j=0; j<dataObject.size(); j++) {
                            JSONObject questionData = (JSONObject) dataObject.get(j);
                            JSONObject categoryData = (JSONObject) questionData.get("category");

                            System.out.println(questionData.get("question"));
                            System.out.println(questionData.get("answer"));
                            System.out.println(questionData.get("id"));
                            System.out.println(questionData.get("value"));
                            System.out.println(questionData.get("airdate"));
                            System.out.println(questionData.get("category_id"));
                            System.out.println(questionData.get("game_id"));
                            System.out.println(questionData.get("category"));
                            System.out.println(categoryData.get("title"));
                            System.out.println(categoryData.get("clues_count"));

                            Long id = (Long) questionData.get("id");
                            String answer = (String) questionData.get("answer");
                            String question = (String) questionData.get("question");
                            Long value = (Long) questionData.get("value");
                            if(value==null) {
                                value= Long.valueOf(0);
                            }
                            String airdate = (String) questionData.get("airdate");
                            Long categoryId = (Long) questionData.get("category_id");
                            Long gameId = (Long) questionData.get("game_id");
//                         Object category = questionData.get("category");
                            String categoryTitle = (String) categoryData.get("title");
                            Long cluesCount = (Long) categoryData.get("clues_count");

                            PreparedStatement pStmt = new_conn.prepareStatement("insert into Questions values(?,?,?,?,?,?,?,?,?)");
                            pStmt.setLong(1, id);
                            pStmt.setString(2, answer);
                            pStmt.setString(3, question);
//                             Integer newValue = -1;
//                             value = value == null ? value : newValue;
                            System.out.println(value);
                            pStmt.setLong(4, value);
                            pStmt.setString(5, airdate);
                            pStmt.setLong(6, categoryId);
                            pStmt.setLong(7, gameId);
                            pStmt.setString(8, categoryTitle);
                            pStmt.setLong(9, cluesCount);

                            pStmt.executeUpdate();
                            pStmt.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
