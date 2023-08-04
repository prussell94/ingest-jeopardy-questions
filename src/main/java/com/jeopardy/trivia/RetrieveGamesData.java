package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RetrieveGamesData implements RetrieveSpecificData {

    JSONArray jsonData;
    static DatabaseConnect db_conn = new DatabaseConnect();

    public static void createGamesTable(boolean shouldDropTable) throws SQLException {
        Statement st = db_conn.connect().createStatement();

        String dropTable= "";

        if(shouldDropTable==true) {
            dropTable = """
        DROP TABLE IF EXISTS Games; 
        """;
        }

        String createTable = """
                CREATE TABLE Games (
                ID int,
                Episode_Num int,
                Season_ID int,
                Air_Date text,
                Notes text,
                Contestant1 int,
                Contestant2 int,
                Contestant3 int,
                Winner int,
                Score1 int,
                Score2 int,
                Score3 int);
                """;

        createTable = dropTable+createTable;

        st.executeUpdate(createTable);
    }

    public static void retrieveData(Query gamesQuery) throws SQLException, IOException, ParseException {
        JSONArray data = RetrieveDataUtils.retrieveUnparsedData(gamesQuery);

        Connection new_conn = db_conn.connect();
        for (int j = 0; j < data.size(); j++) {
            JSONObject firstData = (JSONObject) data.get(j);

            Long id = (Long) firstData.get("id");
            Long episodeNum = (Long) firstData.get("episode_num");
            Long seasonId = (Long) firstData.get("season_id");
            String airDate = (String) firstData.get("air_date");
            String notes = (String) firstData.get("notes");
            Long contestant1 = (Long) firstData.get("contestant1");
            Long contestant2 = (Long) firstData.get("contestant2");
            Long contestant3 = (Long) firstData.get("contestant3");
            Long winner = (Long) firstData.get("winner");
            Long score1 = (Long) firstData.get("score1");
            Long score2 = (Long) firstData.get("score2");
            Long score3 = (Long) firstData.get("score3");

            PreparedStatement pStmt = new_conn.prepareStatement("insert into games values(?,?,?,?,?,?,?,?,?,?,?,?)");

            pStmt.setLong(1, id);
            pStmt.setLong(2, episodeNum);
            pStmt.setLong(3, seasonId);
            pStmt.setString(4, airDate);
            pStmt.setString(5, notes);
            pStmt.setLong(6, contestant1);
            pStmt.setLong(7, contestant2);
            pStmt.setLong(8, contestant3);
            pStmt.setLong(9, winner);
            pStmt.setLong(10, score1);
            pStmt.setLong(11, score2);
            pStmt.setLong(12, score3);

            pStmt.execute();

            pStmt.close();
        }
    }
}
