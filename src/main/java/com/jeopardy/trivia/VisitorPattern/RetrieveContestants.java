package com.jeopardy.trivia.VisitorPattern;

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

public class RetrieveContestants implements TableElement {

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

    public RetrieveContestants(DatabaseConnect db_conn, Query contestantsQuery, int numberOfContestants, int offset) {
        this.db_conn = db_conn;
        this.query = contestantsQuery;
        this.totalNumberOfElements = numberOfContestants;
        this.offset = offset;
    }

    public static void retrieveData(Query contestantsQuery) throws SQLException, ParseException, IOException {
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

    @Override
    public void accept(DatabaseVisitor visitor) throws SQLException, ParseException, IOException {
        visitor.visit(this);
    }
}
