package com.jeopardy.trivia.VisitorPattern;

import com.jeopardy.trivia.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseVisitorImpl implements DatabaseVisitor {

    static final int OFFSET = 100;

    @Override
    public void visit(RetrieveClues retrieveClues) throws ParseException, IOException, SQLException {
        int totalNumberOfElements = RetrieveClues.getTotalNumberOfElements();
        int initialOffset = OFFSET;

        for (int offset = 0; offset < totalNumberOfElements; offset += initialOffset) {
            Query cluesQuery = new Query.Builder("clues")
                    .setLimit(initialOffset)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();
            RetrieveClues.retrieveData(cluesQuery);
        }
    }

    @Override
    public void visit(RetrieveCategories retrieveCategories) throws SQLException, ParseException, IOException {
        DatabaseConnect db_conn = RetrieveCategories.getDb_conn();
        Query query = RetrieveCategories.getQuery();
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

    @Override
    public void visit(RetrieveContestants retrieveContestants) throws ParseException, IOException, SQLException {
        int totalNumberOfElements = RetrieveContestants.getTotalNumberOfElements();
        int initialOffset = OFFSET;

        for (int offset = 0; offset < totalNumberOfElements; offset += initialOffset) {
            Query contestantsQuery = new Query.Builder("contestants")
                    .setLimit(initialOffset)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();
            RetrieveContestants.retrieveData(contestantsQuery);
        }
    }

    @Override
    public void visit(RetrieveGames retrieveGames) throws ParseException, IOException, SQLException {
        int totalNumberOfElements = RetrieveGames.getTotalNumberOfElements();
        int initialOffset = OFFSET;

        for (int offset = 0; offset < totalNumberOfElements; offset += initialOffset) {
            Query gamesQuery = new Query.Builder("games")
                    .setLimit(initialOffset)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();
            RetrieveGames.retrieveData(gamesQuery);
        }
    }

    @Override
    public void visit(RetrieveSeasons retrieveSeasons) throws ParseException, IOException, SQLException {
        DatabaseConnect db_conn = RetrieveSeasons.getDb_conn();
        Query query = RetrieveSeasons.getQuery();
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
