package com.jeopardy.trivia.CommandPattern;

import com.jeopardy.trivia.DatabaseConnect;
import com.jeopardy.trivia.Query;
import com.jeopardy.trivia.RetrieveDataUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RetrieveSeasons implements DatabaseRetriever {
    @Override
    public void createTable(boolean shouldDropTable) throws SQLException {
        Statement st = db_conn.connect().createStatement();

        String dropTable = "";

        if (shouldDropTable == true) {
            dropTable = """
                    DROP TABLE IF EXISTS Seasons; 
                    """;
        }

        String createTable = """
                CREATE TABLE IF NOT EXISTS Seasons (
                ID int,
                Season_Name text,
                Start_Date text,
                End_Date text,
                Total_Games int);
                """;

        createTable = dropTable + createTable;

        st.executeUpdate(createTable);
    }

    @Override
    public void setTable(Query query) throws SQLException, ParseException, IOException {
        JSONArray data = RetrieveDataUtils.retrieveUnparsedData(query);

        Connection new_conn = db_conn.connect();
        for (int j = 0; j < data.size(); j++) {
            JSONObject firstData = (JSONObject) data.get(j);

            Long id = (Long) firstData.get("id");
            String seasonName = (String) firstData.get("season_name");
            String startDate = (String) firstData.get("start_date");
            String endDate = (String) firstData.get("end_date");
            Long totalGames = (Long) firstData.get("total_games");

            PreparedStatement pStmt = new_conn.prepareStatement("insert into seasons values(?,?,?,?,?)");

            pStmt.setLong(1, id);
            pStmt.setString(2, seasonName);
            pStmt.setString(3, startDate);
            pStmt.setString(4, endDate);
            pStmt.setLong(5, totalGames);

            pStmt.execute();

            pStmt.close();
        }
    }
}
