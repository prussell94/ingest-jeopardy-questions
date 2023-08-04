package com.jeopardy.trivia.CommandPattern;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class TableInvoker {
    public Command command;

    public TableInvoker(Command c){
        this.command=c;
    }

    public void execute() throws SQLException, ParseException, IOException {
        this.command.execute();
    }
}
