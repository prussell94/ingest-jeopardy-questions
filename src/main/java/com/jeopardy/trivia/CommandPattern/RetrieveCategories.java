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

public class RetrieveCategories implements DatabaseRetriever {

    @Override
    public void createTable(boolean shouldDropTable) throws SQLException {
        Statement st = db_conn.connect().createStatement();

        String dropTable= "";

        if(shouldDropTable==true) {
            dropTable = """
        DROP TABLE IF EXISTS Categories; 
        """;
        }

        String createTable = """
                CREATE TABLE IF NOT EXISTS Categories (
                Category text,
                Clue_count int);
                """;

        createTable = dropTable+createTable;

        st.executeUpdate(createTable);
    }

    @Override
    public void setTable(Query query) throws ParseException, IOException, SQLException {
        JSONArray data = RetrieveDataUtils.retrieveUnparsedData(query);

        Connection new_conn = db_conn.connect();
        for (int j = 0; j < data.size(); j++) {
            JSONObject firstData = (JSONObject) data.get(j);

            String category = (String) firstData.get("category");
            Long clue_count = (Long) firstData.get("clue_count");

            PreparedStatement pStmt = new_conn.prepareStatement("insert into categories values(?,?)");
            pStmt.setString(1, category);
            pStmt.setLong(2, clue_count);

            pStmt.execute();
            pStmt.close();
        }
    }
}
