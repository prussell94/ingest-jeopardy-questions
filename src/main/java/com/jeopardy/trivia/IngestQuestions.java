package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;

public class IngestQuestions {

    private static final int NUMBER_OF_DAYS= 1000;
    private static final int NUMBER_OF_QUESTIONS=367364;
    private static int OFFSET=61200;

    URL url;
    Connection sqlConn = null;

    static DatabaseConnect db_conn = new DatabaseConnect();

    public static void main(String[] args) {

        //"cluebase.lukelav.in/clues?limit=100&order_by=category&sort=desc"
        /*id	integer
        game_id	integer	The id for the game in which this clue aired
        value	integer	The dollar value of the clue. Higher dollar value implies higher difficulty.
        daily_double	boolean	Boolean flag for if the clue was a daily double (always False, this info was not scraped but may be added at a later date)
        round	String	The round this clue appeared in. “J!” for the Jeopardy! round or “DJ!” for Double Jeopardy!. No Final Jeopardy round clues were scraped
        category	String	The category of the clue
        clue	String	The clue text
        response	String	The correct response to the clue*/

         try {
//             Connection db_conn1 = db_conn.connect();
             Statement st = db_conn.connect().createStatement();
             Statement statement = db_conn.connect().createStatement();
//             statement.execute("DROP TABLE IF EXISTS clues; \n" +
//                     "    CREATE TABLE clues (\n" +
//                     "    ID int,\n" +
//                     "    Game_ID int,\n" +
//                     "    Value int,\n" +
//                     "    Daily_Double boolean,\n" +
//                     "    Round text,\n" +
//                     "    CategoryTitle varchar(255),\n" +
//                     "    Clue  text, \n" +
//                     "    Response text);");

             st.executeUpdate("CREATE TABLE IF NOT EXISTS Categories (\n" +
                     "    Category text,\n" +
                     "    Clue_count int\n);");

             /*
             id	integer
            episode_num	integer	The episode number (more descriptive than the id)
            season_id	integer	The id for the season in which this game aired
            air_date	String	Air date of this episode, in the format of “YYYY-MM-DD”
            notes	String	Special notes about the episode. Includes notable players, tournament details, and other fun tidbits.
            contestant1	integer	The id of the first contestant
            contestant2	integer	The id of the second contestant
            contestant3	integer	The id of the third contestant
            winner	integer	The id of the winner of the game
            score1	integer	The final score of the first contestant
            score2	integer	The final score of the second contestant
            score3	integer	The final score of the third contestant
              */

             st.executeUpdate("CREATE TABLE IF NOT EXISTS Games (\n" +
                     "    ID int,\n" +
                     "    Episode_Num int,\n" +
                     "    Season_ID int,\n" +
                     "    Air_Date text,\n" +
                     "    Notes text,\n" +
                     "    Contestant1 int,\n" +
                     "    Contestant2 int,\n" +
                     "    Contestant3 int, \n" +
                     "    Winner int, \n" +
                     "    Score1 int, \n" +
                     "    Score2 int, \n" +
                     "    Score3 int\n);");

             /*
              * id	integer
              * name	String	Contestant’s name
              * notes	String	Contestant’s intro, including job and hometown
              * games_played	integer	Total games played. May be inaccurate if the contestant has played tournament games.
              * total_winnings	integer	Total winnings. May be inaccurate if the contestant has played tournament games.
              */
             st.executeUpdate("CREATE TABLE IF NOT EXISTS Contestants (\n" +
                     "    ID int,\n" +
                     "    Name text,\n" +
                     "    Notes text,\n" +
                     "    Games_Played int,\n" +
                     "    Total_Winnings int\n);");

             /*
                id	integer
                season_name	String	Name of the season (usually “Season [Number]”)
                start_date	String	Air date of the first episode in this season, in the format of “YYYY-MM-DD”
                end_date	String	Air date of the first episode in this season, in the format of “YYYY-MM-DD”
                total_games	integer	Total games documented in this season
              */
             st.executeUpdate("CREATE TABLE IF NOT EXISTS Seasons (\n" +
                     "    ID int,\n" +
                     "    Season_Name text,\n" +
                     "    Start_Date text,\n" +
                     "    End_Date text,\n" +
                     "    Total_Games int\n);");

             st.close();

//             createSt.executeQuery("SELECT QuestionId")
             int numberOfRequests=0;
             int totalNumberOfRequests=0;
             int waitTimeInSeconds=1;

//             for(int i=0; i<NUMBER_OF_DAYS-NUMBER_OF_DAYS+100; i++) {
             for(int i=0; i<NUMBER_OF_QUESTIONS; OFFSET+=100) {
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
                 LocalDate dayOfGame = LocalDate.now().minusDays(i+220);
                 LocalDate nextDayAfterGame = LocalDate.now().minusDays(i-1+220);
                 if(dayOfGame.getDayOfWeek() == DayOfWeek.SATURDAY || dayOfGame.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("skip");
                 }
                 else {

                     URL url = new URL("http://cluebase.lukelav.in/clues?limit=100&order_by=id&sort=asc&offset="+OFFSET);

                     System.out.println(url);

                     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                     conn.setRequestMethod("GET");
                     conn.connect();

                     int responseCode = conn.getResponseCode();
//                     System.out.println(responseCode);
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

                         //JSON simple library Setup with Maven is used to convert strings to JSON
                         JSONParser parse = new JSONParser();
                         JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(informationString));

                         JSONArray data = (JSONArray) dataObject.get("data");
                         //Get the first JSON object in the JSON array
                         if(dataObject.size()==0) {
                             continue;
                         }

                         dataObject.get("data");
                         Connection new_conn = db_conn.connect();
                         for(int j=0; j<data.size(); j++) {
                             JSONObject firstData = (JSONObject) data.get(j);

                             String round = (String) firstData.get("round");
                             String response = (String) firstData.get("response");
                             Boolean daily_double = (Boolean) firstData.get("daily_double");
                             String clue = (String) firstData.get("clue");
                             Long questionId = (Long) firstData.get("id");
                             String category = (String) firstData.get("category");
                             Long value = (Long) firstData.get("value");
                             Long game_id = (Long) firstData.get("game_id");

                             PreparedStatement pStmt = new_conn.prepareStatement("insert into clues values(?,?,?,?,?,?,?,?)");
//                             pStmt.setLong(1, questionId);
//                             pStmt.setLong(2, game_id);
//                             pStmt.setLong(3, value);
//                             pStmt.setBoolean(4, daily_double);
//                             pStmt.setString(5, round);
//                             pStmt.setString(6, category);
//                             pStmt.setString(7, clue);
//                             pStmt.setString(8, response);

                             pStmt.setLong(1, questionId);
                             pStmt.setLong(2, game_id);
                             pStmt.setLong(3, value);
                             pStmt.setBoolean(4, daily_double);
                             pStmt.setString(5, round);
                             pStmt.setString(6, category);
                             pStmt.setString(7, clue);
                             pStmt.setString(8, response);

                             pStmt.execute();
//                             pStmt.executeUpdate();

                             pStmt.close();
                         }
                     }
                 }
             }
         } catch (Exception e) {
                e.printStackTrace();
         }
     }

    public static String escapeMetaCharacters(String inputString){
        final String[] metaCharacters = {"\\","^","$","{","}","[","]","(",")",".","*","+","?","|","<",">","-","&","%",",",";","'","-",":","."};

        for (int i = 0 ; i < metaCharacters.length ; i++){
            if(inputString.contains(metaCharacters[i])){
                inputString = inputString.replace(metaCharacters[i],"\\"+metaCharacters[i]);
            }
        }
        return inputString;
    }

    protected void finalize(Connection conn) throws Throwable
    {
        try { conn.close(); }
        catch (SQLException e) {
            e.printStackTrace();
        }
        super.finalize();
    }

}
