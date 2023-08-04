package com.jeopardy.trivia.CommandPattern;

import java.sql.SQLException;

public class CreateTableCommand implements Command{

    private DatabaseRetriever databaseRetriever;

    public CreateTableCommand(DatabaseRetriever dReceiver) {
        this.databaseRetriever=dReceiver;
    }

    @Override
    public void execute() throws SQLException {
        this.databaseRetriever.createTable(false);
    }
}
