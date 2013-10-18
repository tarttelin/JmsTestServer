package com.pyruby.jms.message;

import javax.jms.JMSException;
import javax.jms.TextMessage;


public class StubTextMessage extends StubMessage implements TextMessage {
    private String text;
    public StubTextMessage(String text) { this.text = text;}

    @Override
    public void setText(String text) throws JMSException {
        this.text = text;
    }

    @Override
    public String getText() throws JMSException {
        return text;
    }
}
