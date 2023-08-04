package com.jeopardy.trivia.VisitorPattern;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface DatabaseVisitor {

    void visit(RetrieveClues retrieveClues) throws ParseException, IOException, SQLException;
    void visit(RetrieveCategories retrieveCategories) throws SQLException, ParseException, IOException;
    void visit(RetrieveContestants retrieveContestants) throws ParseException, IOException, SQLException;
    void visit(RetrieveGames retrieveGames) throws SQLException, ParseException, IOException;
    void visit(RetrieveSeasons retrieveSeasons) throws ParseException, IOException, SQLException;
}
