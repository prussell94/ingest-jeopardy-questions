package com.jeopardy.trivia.CommandPattern;

import com.jeopardy.trivia.DatabaseConnect;
import com.jeopardy.trivia.Query;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public interface DatabaseRetriever {
    static DatabaseConnect db_conn = new DatabaseConnect();

    public void createTable(boolean shouldDropTable) throws SQLException;
    public void setTable(Query query) throws ParseException, IOException, SQLException;
}
