package com.jeopardy.trivia.VisitorPattern;

import com.jeopardy.trivia.DatabaseConnect;
import com.jeopardy.trivia.Query;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;

public class RetrieveCategories implements TableElement {

    static DatabaseConnect db_conn;
    static com.jeopardy.trivia.Query query;

    public RetrieveCategories(DatabaseConnect db_conn, com.jeopardy.trivia.Query query){
        this.db_conn = db_conn;
        this.query = query;
    }

    public static DatabaseConnect getDb_conn() {
        return db_conn;
    }

    public static Query getQuery() {
        return query;
    }

    @Override
    public void accept(DatabaseVisitor visitor) throws SQLException, ParseException, IOException {
        visitor.visit(this);
    }
}
