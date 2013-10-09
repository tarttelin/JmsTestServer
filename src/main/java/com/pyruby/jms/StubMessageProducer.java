package com.pyruby.jms;

import javax.jms.*;

public class StubMessageProducer implements MessageProducer {
    private Destination destination;

    public StubMessageProducer(Destination destination) {
        this.destination = destination;
    }

    @Override
    public void setDisableMessageID(boolean value) throws JMSException { }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDisableMessageTimestamp(boolean value) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setDeliveryMode(int deliveryMode) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setPriority(int defaultPriority) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getPriority() throws JMSException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setTimeToLive(long timeToLive) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Destination getDestination() throws JMSException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(Message message) throws JMSException {
        send(destination, message);
    }

    @Override
    public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void send(Destination destination, Message message) throws JMSException {
        JmsServer.getInstance().sendMessage(destination, message);
    }

    @Override
    public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
