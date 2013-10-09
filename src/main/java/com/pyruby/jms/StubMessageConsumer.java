package com.pyruby.jms;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class StubMessageConsumer implements MessageConsumer {
    private JmsServer jmsServer;
    private StubConnectionFactory.StubConnection.StubSession session;
    private String destinationName;

    public StubMessageConsumer(JmsServer jmsServer, StubConnectionFactory.StubConnection.StubSession stubSession, String destinationName) {
        this.jmsServer = jmsServer;
        session = stubSession;
        this.destinationName = destinationName;
    }

    public Message receive() throws JMSException {
        return receive(30 * 60 * 1000);
    }

    public Message receive(long timeout) throws JMSException {
        return jmsServer.nextMessage(this, timeout, TimeUnit.MILLISECONDS);
    }

    public Message receiveNoWait() throws JMSException {
        return receive(1);
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void close() throws JMSException { }
    public String getMessageSelector() throws JMSException { return null; }
    public MessageListener getMessageListener() throws JMSException { return null; }
    public void setMessageListener(MessageListener listener) throws JMSException { }

    public Session getSession() {
        return session;
    }
}
