package com.jeopardy.trivia;

import com.jeopardy.trivia.VisitorPattern.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class RetrieveAllData {

    private static final int NUMBER_OF_DAYS= 1000;
    private static final int NUMBER_OF_CLUES=367364;
    private static final int NUMBER_OF_GAMES=6314;
    private static final int NUMBER_OF_CONTESTANTS=12028;
    private static int OFFSET=0;

    URL url;
    Connection sqlConn = null;

    static DatabaseConnect db_conn = new DatabaseConnect();

    public static void main(String[] args) throws SQLException, IOException, ParseException, InterruptedException {

        DatabaseVisitor databaseVisitor = new DatabaseVisitorImpl();

        // Retrieving categories
        RetrieveCategoriesData.createCategoriesTable(false);
        Query categoriesQuery = new Query.Builder("categories").build();
//        RetrieveCategoriesData.retrieveCategoriesData(categoriesQuery);

        RetrieveCategories setCategories = new RetrieveCategories(db_conn, categoriesQuery);

        // Retrieving Seasons
        RetrieveSeasonsData.createSeasonsTable(false);
        Query seasonsQuery = new Query.Builder("seasons").build();
//        RetrieveSeasonsData.retrieveSeasonsData(seasonsQuery);

        RetrieveSeasons setSeasons = new RetrieveSeasons(db_conn, seasonsQuery);

        // Retrieve Clues
        RetrieveCluesData.createCluesTable(true);
        Query clueQuery = new Query.Builder("clues")
                        .setLimit(100)
                        .setOrder("id")
                        .setSort("asc")
                        .setOffset(OFFSET).build();

        RetrieveClues setClues = new RetrieveClues(db_conn, clueQuery, NUMBER_OF_CLUES, OFFSET);

        // Retrieve Contestants
        RetrieveContestantsData.createContestantsTable(true);
        Query contestantsQuery = new Query.Builder("contestants")
                .setLimit(100)
                .setOrder("id")
                .setSort("asc")
                .setOffset(OFFSET).build();

        RetrieveContestants setContestants = new RetrieveContestants(db_conn, contestantsQuery, NUMBER_OF_CONTESTANTS, OFFSET);
//
//        // Retrieve Games
        RetrieveGamesData.createGamesTable(true);
        Query gamesQuery = new Query.Builder("games")
                .setLimit(100)
                .setOrder("id")
                .setSort("asc")
                .setOffset(OFFSET).build();

        RetrieveGames setGames = new RetrieveGames(db_conn, gamesQuery, NUMBER_OF_CONTESTANTS, OFFSET);

        setClues.accept(databaseVisitor);
        setCategories.accept(databaseVisitor);
        setSeasons.accept(databaseVisitor);
        setContestants.accept(databaseVisitor);
        setGames.accept(databaseVisitor);

//        RetrieveCluesData.createCluesTable(false);
//        RetrieveContestantsData.createContestantsTable(false);

        // Retrieving seasons
//        RetrieveSeasonsData.createSeasonsTable(true);
//        Query minSeasonsQuery = new Query.Builder("seasons").setOrder("id").setSort("asc").build();
//        Query maxSeasonsQuery = new Query.Builder("seasons").setOrder("id").setSort("desc").build();
//
//        JSONObject data = (JSONObject) RetrieveDataUtils.retrieveUnparsedData(minSeasonsQuery).get(0);
//        JSONObject endData = (JSONObject) RetrieveDataUtils.retrieveUnparsedData(maxSeasonsQuery).get(0);
//
//        Object firstData = data.get("id");
//        Object lastData = endData.get("id");
//        System.out.println(RetrieveDataUtils.retrieveUnparsedData(maxSeasonsQuery).size());
//        System.out.println(firstData);
//        System.out.println(lastData);
//        System.out.println(maxSeasonsQuery.getQueryStatement());
////        RetrieveSeasonsData.retrieveSeasonsData(minSeasonsQuery);
////        RetrieveSeasonsData.retrieveSeasonsData(maxSeasonsQuery);
//
//        Query seasonsQuery = new Query.Builder("seasons").build();
//        RetrieveSeasonsData.retrieveSeasonsData(seasonsQuery);

        // Retrieving Games
//        RetrieveGamesData.createGamesTable(true);
////        Query gamesQuery = new Query.Builder("games")
////                .setLimit(100)
////                .setOrder("id")
////                .setSort("asc")
////                .setOffset(0).build();
////        RetrieveGamesData.retrieveData(gamesQuery);
//
//        // Retrieving categories
//        RetrieveCategoriesData.createCategoriesTable(false);
//        Query categoriessQuery = new Query.Builder("categories").build();
//        RetrieveCategoriesData.retrieveCategoriesData(categoriessQuery);
//
////        RetrieveDataUtils.setDailyData(NUMBER_OF_GAMES, gamesQuery, OFFSET);
//
//        int numberOfRequests=0;
//        int totalNumberOfRequests=0;
//        int waitTimeInSeconds=1;
//
////             for(int i=0; i<NUMBER_OF_DAYS-NUMBER_OF_DAYS+100; i++) {
////        for(int i=0; i<NUMBER_OF_CONTESTANTS; OFFSET+=100) {
////            for(int i=0; i<NUMBER_OF_QUESTIONS; OFFSET+=100) {
//        for(int i=0; OFFSET<NUMBER_OF_CONTESTANTS; OFFSET+=100) {
//
//////                Query clueQuery = new Query.Builder("clues")
//////                        .setLimit(100)
//////                        .setOrder("id")
//////                        .setSort("asc")
//////                        .setOffset(OFFSET).build();
//////
//////                RetrieveCluesData.retrieveCluesData(clueQuery);
////
//
////
////                Query gamesQuery = new Query.Builder("games")
////                        .setLimit(100)
////                        .setOrder("id")
////                        .setSort("asc")
////                        .setOffset(OFFSET).build();
////                System.out.println(gamesQuery.getQueryStatement());
////                RetrieveGamesData.retrieveData(gamesQuery);
//
//                Query contestantssQuery = new Query.Builder("contestants")
//                        .setLimit(100)
//                        .setOrder("id")
//                        .setSort("asc")
//                        .setOffset(OFFSET).build();
//
//                System.out.println(contestantssQuery.getQueryStatement());
//                RetrieveContestantsData.retrieveData(contestantssQuery);
//            }

    }

    // let's calculate so that the number of loops is totalNumberOfElements/offset and round up always
//    public static int totalNumberOfLoops(int offset, int totalNumberOfElements) {
//        double totalNumberOfLoops = totalNumberOfElements/offset;
//        int iterations = (int) Math.ceil(totalNumberOfLoops);
//        return iterations;
//    }

//    public static void retrieveSpecificData(int initialOffset, int numberOfElements, Query query, String tableName) throws Exception {
//        int iterations = totalNumberOfLoops(initialOffset, numberOfElements);
//        int offset = initialOffset;
//        for (int i = 0; offset < iterations; offset += initialOffset) {
//            Query tableQuery = new Query.Builder(tableName)
//                    .setLimit(initialOffset)
//                    .setOrder("id")
//                    .setSort("asc")
//                    .setOffset(offset).build();
//            System.out.println(query.getQueryStatement());
//            RetrieveContestantsData.retrieveData(tableQuery);
//        }
//    }
}
