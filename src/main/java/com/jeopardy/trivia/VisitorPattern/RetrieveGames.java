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

public class RetrieveGames implements TableElement {

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

    public RetrieveGames(DatabaseConnect db_conn, com.jeopardy.trivia.Query query, int totalNumberOfElements,
                         int offset){
        this.db_conn = db_conn;
        this.query = query;
        this.totalNumberOfElements = totalNumberOfElements;
        this.offset = offset;
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

    @Override
    public void accept(DatabaseVisitor visitor) throws SQLException, ParseException, IOException {
        visitor.visit(this);
    }
}
