package com.pyruby.queue;


import org.eclipse.jetty.server.Server;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JettyRunner {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("startup.xml");
        Server server = (Server) ctx.getBean("server");
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
