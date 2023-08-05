package com.jeopardy.trivia.VisitorPattern;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface TableElement {

    public void accept(DatabaseVisitor visitor) throws SQLException, ParseException, IOException;
}
