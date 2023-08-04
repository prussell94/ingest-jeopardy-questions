package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RetrieveContestantsData {

    JSONArray jsonData;
    static DatabaseConnect db_conn = new DatabaseConnect();

    public static void createContestantsTable(boolean shouldDropTable) throws SQLException {
        Statement st = db_conn.connect().createStatement();

        String dropTable= "";

        if(shouldDropTable==true) {
            dropTable = """
        DROP TABLE IF EXISTS Contestants; 
        """;
        }

        String createTable = """
                CREATE TABLE Contestants (
                ID int,
                Name text,
                Notes text,
                Games_Played int,
                Total_Winnings int);
                """;

        createTable = dropTable+createTable;

        st.executeUpdate(createTable);
    }

//    public static String query(String )

    public static void retrieveData(Query contestantsQuery) throws SQLException, ParseException, IOException {
//        URL url = new URL(contestantsQuery.getQueryStatement());
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.connect();
//
//        int responseCode = conn.getResponseCode();
//
//        // 200 OK
//        if (responseCode != 200) {
//            throw new RuntimeException("HttpResponseCode: " + responseCode);
//        } else {
//
//            StringBuilder informationString = new StringBuilder();
//            Scanner scanner = new Scanner(url.openStream());
//
//            while (scanner.hasNext()) {
//                informationString.append(scanner.nextLine());
//            }
//
//            //Close the scanner
//            scanner.close();
//
//            //JSON simple library Setup with Maven is used to convert strings to JSON
//            JSONParser parse = new JSONParser();
//            JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(informationString));
//
//            JSONArray data = (JSONArray) dataObject.get("data");
//            //Get the first JSON object in the JSON array
//            if (dataObject.size() == 0) {
//                return;
//            }
//            dataObject.get("data");

        JSONArray data = RetrieveDataUtils.retrieveUnparsedData(contestantsQuery);

        Connection new_conn = db_conn.connect();
            for (int j = 0; j < data.size(); j++) {
                JSONObject firstData = (JSONObject) data.get(j);

                Long id = (Long) firstData.get("id");
                String name = (String) firstData.get("name");
                String notes = (String) firstData.get("notes");
                Long gamesPlayed = (Long) firstData.get("games_played");
                Long totalWinnings = (Long) firstData.get("total_winnings");

                PreparedStatement pStmt = new_conn.prepareStatement("insert into contestants values(?,?,?,?,?)");

                pStmt.setLong(1, id);
                pStmt.setString(2, name);
                pStmt.setString(3, notes);
                pStmt.setLong(4, gamesPlayed);
                pStmt.setLong(5, totalWinnings);

                pStmt.execute();

                pStmt.close();
            }
        }

//    public static void getNumberOfElements()


    public static void retrieveLoopedData(int offset, int numberOfElements) throws SQLException, ParseException, IOException {
        for(int i=0; offset<numberOfElements; offset+=100) {

////                Query clueQuery = new Query.Builder("clues")
////                        .setLimit(100)
////                        .setOrder("id")
////                        .setSort("asc")
////                        .setOffset(OFFSET).build();
////
////                RetrieveCluesData.retrieveCluesData(clueQuery);
//

//
//                Query gamesQuery = new Query.Builder("games")
//                        .setLimit(100)
//                        .setOrder("id")
//                        .setSort("asc")
//                        .setOffset(OFFSET).build();
//                System.out.println(gamesQuery.getQueryStatement());
//                RetrieveGamesData.retrieveData(gamesQuery);

            Query contestantssQuery = new Query.Builder("contestants")
                    .setLimit(100)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();

            System.out.println(contestantssQuery.getQueryStatement());
            RetrieveContestantsData.retrieveData(contestantssQuery);
        }
    }
}
