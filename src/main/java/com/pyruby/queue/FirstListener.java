package com.pyruby.queue;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class FirstListener {

    private SimpleJdbcInsert template;

    public FirstListener(SimpleJdbcInsert template) {
        this.template = template.withTableName("MESSAGE");
    }

    public void first(final String message) {
        template.execute(new MapSqlParameterSource().addValue("content", message));
    }

    public void second(String message) throws InterruptedException {
        System.out.println("I ain't processin' your stinkin' message");
    }
}
