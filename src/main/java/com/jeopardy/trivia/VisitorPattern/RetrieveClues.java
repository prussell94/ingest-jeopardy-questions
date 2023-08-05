package com.jeopardy.trivia.VisitorPattern;

import com.jeopardy.trivia.DatabaseConnect;
import com.jeopardy.trivia.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class RetrieveClues implements TableElement {

    static DatabaseConnect db_conn;
    static com.jeopardy.trivia.Query query;
    static int totalNumberOfElements;
    static int offset;

    public static DatabaseConnect getDb_conn() {
        return db_conn;
    }

    public static Query getQuery() {
        return query;
    }

    public static int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    public static int getOffset() {
        return offset;
    }

    public RetrieveClues(DatabaseConnect db_conn, com.jeopardy.trivia.Query query, int totalNumberOfElements,
                         int offset){
        this.db_conn = db_conn;
        this.query = query;
        this.totalNumberOfElements = totalNumberOfElements;
        this.offset = offset;
    }

    public static void retrieveData(Query clueQuery) throws SQLException, IOException, ParseException {

        URL url = new URL(clueQuery.getQueryStatement());

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
            if (dataObject.size() == 0) {
                return;
            }
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

                PreparedStatement pStmt = new_conn.prepareStatement("insert into clues values(?,?,?,?,?,?,?,?)");

                pStmt.setLong(1, questionId);
                pStmt.setLong(2, game_id);
                pStmt.setLong(3, value);
                pStmt.setBoolean(4, daily_double);
                pStmt.setString(5, round);
                pStmt.setString(6, category);
                pStmt.setString(7, clue);
                pStmt.setString(8, response);

                pStmt.execute();

                pStmt.close();
            }
        }
    }

    @Override
    public void accept(DatabaseVisitor visitor) throws SQLException, ParseException, IOException {
        visitor.visit(this);
    }
}
