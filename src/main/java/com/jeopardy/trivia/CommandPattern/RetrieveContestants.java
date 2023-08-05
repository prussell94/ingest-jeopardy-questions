package com.jeopardy.trivia.CommandPattern;

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

public class RetrieveContestants implements DatabaseRetriever {
    @Override
    public void createTable(boolean shouldDropTable) throws SQLException {
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

    @Override
    public void setTable(Query query) throws SQLException, ParseException, IOException {
        JSONArray data = RetrieveDataUtils.retrieveUnparsedData(query);

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
}
