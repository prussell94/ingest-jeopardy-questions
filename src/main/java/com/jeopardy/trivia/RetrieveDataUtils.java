package com.jeopardy.trivia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class RetrieveDataUtils {

    public static JSONArray retrieveUnparsedData(Query query) throws ParseException, IOException {
        URL url = new URL(query.getQueryStatement());
        JSONArray data=null;

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

            //Get the first JSON object in the JSON array
            if (dataObject.size() == 0) {
                return data;
            }

            data = (JSONArray) dataObject.get("data");

            dataObject.get("data");
        }
        return data;
    }

    public static void retrieveSpecficData(int offset, int numberOfElements, Query query, Callable<Query> func) throws Exception {
        for (int i = 0; offset < numberOfElements; offset += 100) {
            Query gamesQuery = new Query.Builder("games")
                    .setLimit(100)
                    .setOrder("id")
                    .setSort("asc")
                    .setOffset(offset).build();
            System.out.println(gamesQuery.getQueryStatement());
            func.call();
        }
    }

    public static void throttleData(int numberOfRequests, int totalNumberOfRequests, int waitTimeInSeconds) throws InterruptedException {
        numberOfRequests++;
        totalNumberOfRequests++;
        if (numberOfRequests > 5) {
            int multiplier = totalNumberOfRequests / 100;
            Thread.sleep(1000 * waitTimeInSeconds * (1 + multiplier));
            numberOfRequests = 0;
            if (totalNumberOfRequests > 1000) {
                totalNumberOfRequests = 0;
            }
        }
    }

    // let's calculate so that the number of loops is totalNumberOfElements/offset and round up always
    public static int totalNumberOfLoops(int offset, int totalNumberOfElements) {
        double totalNumberOfLoops = totalNumberOfElements/offset;
        int iterations = (int) Math.ceil(totalNumberOfLoops);
        return iterations;
    }

    public static void setDailyData(int numberOfIterations, Query query, int offset) throws InterruptedException, SQLException, ParseException, IOException {
        int numberOfRequests = 0;
        int totalNumberOfRequests = 0;
        int waitTimeInSeconds = 0;
        for (int i = 0; i < numberOfIterations; i += 1) {
            numberOfRequests++;
            totalNumberOfRequests++;
            if (numberOfRequests > 5) {
                int multiplier = totalNumberOfRequests / 100;
                Thread.sleep(1000 * waitTimeInSeconds * (1 + multiplier));
                numberOfRequests = 0;
                if (totalNumberOfRequests > 1000) {
                    totalNumberOfRequests = 0;
                }
            }
            LocalDate dayOfGame = LocalDate.now().minusDays(i + 220);
            if (dayOfGame.getDayOfWeek() == DayOfWeek.SATURDAY || dayOfGame.getDayOfWeek() == DayOfWeek.SUNDAY) {
                System.out.println("skip");
                System.out.println(offset);
                continue;
            } else {
                Query gamesQuery = new Query.Builder("games")
                        .setLimit(100)
                        .setOrder("id")
                        .setSort("asc")
                        .setOffset(offset).build();

//                RetrieveGamesData.retrieveGamesData(gamesQuery);

                offset += 100;
            }
        }
    }
}
