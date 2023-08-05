package com.jeopardy.trivia.CommandPattern;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface Command {

    void execute() throws SQLException, ParseException, IOException;
}
