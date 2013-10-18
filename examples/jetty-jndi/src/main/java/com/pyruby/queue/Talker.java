package com.pyruby.queue;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class Talker {

    private final JmsTemplate template;

    public Talker(JmsTemplate template) {
        this.template = template;
    }

    public void queueMessages() {
        template.send(chat("John"));
        template.send(chat("Paul"));
        template.send(chat("Ringo"));
        template.send(chat("Arthur"));
    }

    private MessageCreator chat(final String message) {
        return new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        };
    }

}
