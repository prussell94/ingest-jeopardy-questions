package com.jeopardy.trivia;

public class Query {

    //required
    String model;
    String queryStatement;

    //optional
    String limit;
    String offset;
    String order_by;
    String sort;
    String category;
    String difficulty;

    public Query(Builder builder) {
        this.model=builder.model;
        this.limit=builder.limit!=null ? builder.limit : "";
        this.offset=builder.offset!=null ? builder.offset : "";
        this.order_by=builder.order!=null ? builder.order : "";
        this.sort=builder.sort!=null ? builder.sort : "";
        this.category=builder.category!=null ? builder.category : "";
        this.difficulty=builder.difficulty!=null ? builder.difficulty : "";

        this.queryStatement="http://cluebase.lukelav.in/"
                +this.model
                +this.limit
                +this.offset
                +this.order_by
                +this.sort
                +this.category
                +this.difficulty;

        this.queryStatement=this.queryStatement.replaceFirst("&", "?");
    }

    public String getQueryStatement() {
        return this.queryStatement;
    }

    public static class Builder {

        //required
        String model;

        //optional
        String limit;
        String offset;
        String order;
        String sort;
        String category;
        String difficulty;

        public Builder(String model) {
            this.model=model;
        }

        public Builder setLimit(Object limit) {
            this.limit="&limit="+limit;
            return this;
        }

        public Builder setOffset(Object offset) {
            this.offset="&offset="+offset;
            return this;
        }

        public Builder setOrder(Object order_by) {
            this.order="&order="+order_by;
            return this;
        }

        public Builder setSort(Object sort) {
            this.sort="&sort="+sort;
            return this;
        }

        public Builder setCategory(Object category) {
            this.category="&category="+category;
            return this;
        }

        public Builder setDifficulty(Object difficulty) {
            this.difficulty="&difficulty="+difficulty;
            return this;
        }

        public Query build() {
            return new Query(this);
        }
    }
}
