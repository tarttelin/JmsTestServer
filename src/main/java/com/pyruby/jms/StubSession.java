package com.pyruby.jms;

import com.pyruby.jms.message.StubBytesMessage;
import com.pyruby.jms.message.StubTextMessage;

import javax.jms.*;
import java.io.Serializable;

class StubSession implements Session {
    private JmsServer jmsServer;

    public StubSession(JmsServer jmsServer) {
        this.jmsServer = jmsServer;
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        return new StubTextMessage(null);
    }

    @Override
    public TextMessage createTextMessage(String text) throws JMSException {
        return new StubTextMessage(text);
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        return new StubBytesMessage();
    }

    @Override
    public boolean getTransacted() throws JMSException {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        return 1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void commit() throws JMSException {
        jmsServer.commitMessages(this);
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        return new StubMessageProducer(destination);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        return createConsumer(destination, null);
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
        return new StubMessageConsumer(jmsServer, this, ((StubDestination)destination).getName());
    }

    @Override
    public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean NoLocal) throws JMSException {
        return createConsumer(destination, messageSelector);
    }

    @Override
    public Queue createQueue(String queueName) throws JMSException { return null; }
    public Topic createTopic(String topicName) throws JMSException { return null; }
    public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException { return null; }
    public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal) throws JMSException { return null; }
    public QueueBrowser createBrowser(Queue queue) throws JMSException { return null; }
    public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException { return null; }
    public TemporaryQueue createTemporaryQueue() throws JMSException { return null; }
    public TemporaryTopic createTemporaryTopic() throws JMSException { return null; }
    public void unsubscribe(String name) throws JMSException { }
    public MapMessage createMapMessage() throws JMSException { return null; }
    public Message createMessage() throws JMSException { return null; }
    public ObjectMessage createObjectMessage() throws JMSException { return null; }
    public ObjectMessage createObjectMessage(Serializable object) throws JMSException { return null; }
    public StreamMessage createStreamMessage() throws JMSException { return null; }
    public void rollback() throws JMSException {}
    public void close() throws JMSException {}
    public void recover() throws JMSException {}
    public MessageListener getMessageListener() throws JMSException { return null; }
    public void setMessageListener(MessageListener listener) throws JMSException {}
    public void run() {}
}
