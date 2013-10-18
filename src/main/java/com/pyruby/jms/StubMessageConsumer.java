package com.pyruby.jms;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class StubMessageConsumer implements MessageConsumer {
    private final String destinationName;
    private JmsServer jmsServer;
    private StubSession session;
    private JmsServer.DestinationMessages destination;
    private MessageListener listener;

    public StubMessageConsumer(JmsServer jmsServer, StubSession stubSession, String destinationName) {
        this.jmsServer = jmsServer;
        session = stubSession;
        this.destinationName = destinationName;
        this.destination = jmsServer.getDestination(destinationName);
    }

    public Message receive() throws JMSException {
        return receive(30 * 60 * 1000);
    }

    public Message receive(long timeout) throws JMSException {
        Message message = jmsServer.nextMessage(this, timeout, TimeUnit.MILLISECONDS);
        if (message != null && listener != null) {
            listener.onMessage(message);
        }
        return message;
    }

    public Message receiveNoWait() throws JMSException {
        return receive(1);
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void close() throws JMSException { this.listener = null;}
    public String getMessageSelector() throws JMSException { return null; }
    public MessageListener getMessageListener() throws JMSException { return listener; }
    public void setMessageListener(MessageListener listener) throws JMSException {
        this.listener = listener;
        destination.setListener(listener);
    }

    public Session getSession() {
        return session;
    }
}
