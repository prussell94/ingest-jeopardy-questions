package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class RetrieveData {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveData.class);

    URL url;

    static DatabaseConnect db_conn = new DatabaseConnect();

    public static void getData(URL url, HttpURLConnection connection) throws IOException, ParseException, SQLException {
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

            //JSON simple library Setup with Maven is used to convert strings to JSON
            JSONParser parse = new JSONParser();
            JSONObject dataObject = (JSONObject) parse.parse(String.valueOf(informationString));

            JSONArray data = (JSONArray) dataObject.get("data");
            //Get the first JSON object in the JSON array
//            if (dataObject.size() == 0) {
//                continue;
//            }

            dataObject.get("data");
            Connection new_conn = db_conn.connect();
            for (int j = 0; j < data.size(); j++) {
                JSONObject firstData = (JSONObject) data.get(j);

                String round = (String) firstData.get("round");
                String response = (String) firstData.get("response");
                Boolean daily_double = (Boolean) firstData.get("daily_double");
                String clue = (String) firstData.get("clue");
                Long questionId = (Long) firstData.get("id");
                String category = (String) firstData.get("category");
                Long value = (Long) firstData.get("value");
                Long game_id = (Long) firstData.get("game_id");

                PreparedStatement pStmt = new_conn.prepareStatement("insert into Contestants values(?,?,?,?,?,?,?,?)");
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
