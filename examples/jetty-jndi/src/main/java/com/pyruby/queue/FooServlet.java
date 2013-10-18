package com.pyruby.queue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


public class FooServlet implements HttpRequestHandler {

    private final String name;
    private JdbcTemplate template;

    public FooServlet(String name, JdbcTemplate template) {
        this.name = name;
        this.template = template;
    }

    @Override
    public void handleRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.printf("<html><body>Hello %s", name);
        writer.write("<ul>");
        List<Map<String,Object>> rows = template.queryForList("select * from message");
        for (Map<String, Object> row : rows) {
            writer.printf("<ul>%s</ul>", row.get("content"));
        }
        writer.write("</ul></body></html>");
        writer.flush();
    }
}
