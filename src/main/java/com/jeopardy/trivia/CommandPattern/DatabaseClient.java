package com.jeopardy.trivia.CommandPattern;

import com.jeopardy.trivia.Query;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseClient {
    private static final int NUMBER_OF_CLUES=367364;
    private static final int NUMBER_OF_CONTESTANTS=12028;
    private static final int NUMBER_OF_GAMES=6314;

    public static void main(String[] args) throws SQLException, IOException, ParseException {

        int initialOffset=100;

        // Retrieving clues
        com.jeopardy.trivia.CommandPattern.RetrieveClues retrieveClues = new com.jeopardy.trivia.CommandPattern.RetrieveClues();
        retrieveClues.createTable(true);
        for(int offset = 0; offset < NUMBER_OF_CLUES; offset += initialOffset) {
            Query cluesQuery = new Query.Builder("clues")
                    .setLimit(initialOffset)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();

            retrieveClues.setTable(cluesQuery);
        }

        // Retrieving contestants
        com.jeopardy.trivia.CommandPattern.RetrieveContestants retrieveContestants = new com.jeopardy.trivia.CommandPattern.RetrieveContestants();
        retrieveContestants.createTable(true);
        for(int offset = 0; offset < NUMBER_OF_CONTESTANTS; offset += initialOffset) {
            Query contestantsQuery = new Query.Builder("contestants")
                        .setLimit(initialOffset)
                        .setOrder("id")
                        .setSort("asc")
                        .setOffset(offset).build();

            retrieveContestants.setTable(contestantsQuery);
        }

        // Retrieving games
        com.jeopardy.trivia.CommandPattern.RetrieveGames retrieveGames = new com.jeopardy.trivia.CommandPattern.RetrieveGames();
        retrieveGames.createTable(true);

        for(int offset = 0; offset < NUMBER_OF_GAMES; offset += initialOffset) {
            Query gamesQuery = new Query.Builder("games")
                    .setLimit(initialOffset)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();

            retrieveGames.setTable(gamesQuery);
        }

        // Retrieving categories
        com.jeopardy.trivia.CommandPattern.RetrieveCategories retrieveCategories = new com.jeopardy.trivia.CommandPattern.RetrieveCategories();
        retrieveCategories.createTable(false);

        Query categoriesQuery = new Query.Builder("categories").build();
        retrieveCategories.setTable(categoriesQuery);

        // Retrieving Seasons
        com.jeopardy.trivia.CommandPattern.RetrieveSeasons retrieveSeasons = new com.jeopardy.trivia.CommandPattern.RetrieveSeasons();
        retrieveSeasons.createTable(false);

        Query seasonsQuery = new Query.Builder("seasons").build();
        retrieveSeasons.setTable(seasonsQuery);
    }
}
