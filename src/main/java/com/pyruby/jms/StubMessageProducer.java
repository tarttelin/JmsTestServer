package com.pyruby.jms;

import javax.jms.*;

public class StubMessageProducer implements MessageProducer {
    private Destination destination;

    public StubMessageProducer(Destination destination) {
        this.destination = destination;
    }

    @Override
    public void send(Message message) throws JMSException {
        send(destination, message);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        send(destination, message, deliveryMode, priority, timeToLive);
    }

    @Override
    public void send(Destination destination, Message message) throws JMSException {
        send(destination, message, 1, 1, 0);
    }

    @Override
    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        JmsServer.getInstance().sendMessage(destination, message);
    }

    public void setDisableMessageID(boolean value) throws JMSException { }
    public boolean getDisableMessageID() throws JMSException { return false; }
    public void setDisableMessageTimestamp(boolean value) throws JMSException { }
    public boolean getDisableMessageTimestamp() throws JMSException { return false; }
    public void setDeliveryMode(int deliveryMode) throws JMSException { }
    public int getDeliveryMode() throws JMSException { return 0; }
    public void setPriority(int defaultPriority) throws JMSException { }
    public int getPriority() throws JMSException { return 0; }
    public void setTimeToLive(long timeToLive) throws JMSException { }
    public long getTimeToLive() throws JMSException { return 0; }
    public Destination getDestination() throws JMSException { return destination; }
    public void close() throws JMSException { }
}