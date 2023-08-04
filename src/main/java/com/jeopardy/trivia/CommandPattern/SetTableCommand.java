package com.jeopardy.trivia.CommandPattern;

import com.jeopardy.trivia.Query;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class SetTableCommand implements Command {

    private DatabaseRetriever databaseRetriever;
    private Query query;

    public SetTableCommand(DatabaseRetriever dRetriever, Query query) {
        this.databaseRetriever=dRetriever;
        this.query=query;
    }

    @Override
    public void execute() throws SQLException, ParseException, IOException {
            this.databaseRetriever.setTable(query);
    }
}
