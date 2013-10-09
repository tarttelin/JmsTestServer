package com.pyruby.jms;


import com.pyruby.jms.message.StubTextMessage;

import javax.jms.*;
import java.io.Serializable;

public class StubConnectionFactory implements ConnectionFactory {
    private final StubConnection connection;

    public StubConnectionFactory(JmsServer jmsServer) {
        this.connection = new StubConnection(jmsServer);
    }

    @Override
    public Connection createConnection() throws JMSException {
        return connection;
    }

    @Override
    public Connection createConnection(String userName, String password) throws JMSException {
        return connection;
    }

    public StubConnection getConnection() {
        return connection;
    }

    public static class StubConnection implements Connection {
        private JmsServer jmsServer;

        public StubConnection(JmsServer jmsServer) {
            this.jmsServer = jmsServer;
        }

        @Override
        public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
            return new StubSession(jmsServer);
        }

        @Override
        public String getClientID() throws JMSException {
            return null;
        }

        @Override
        public void setClientID(String clientID) throws JMSException {}

        @Override
        public ConnectionMetaData getMetaData() throws JMSException {
            return null;
        }

        @Override
        public ExceptionListener getExceptionListener() throws JMSException {
            return null;
        }

        @Override
        public void setExceptionListener(ExceptionListener listener) throws JMSException {}

        @Override
        public void start() throws JMSException {}

        @Override
        public void stop() throws JMSException {}

        @Override
        public void close() throws JMSException {}

        @Override
        public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
            return null;
        }

        @Override
        public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
            return null;
        }

        protected static class StubSession implements Session {
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
            public BytesMessage createBytesMessage() throws JMSException { return null; }
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
    }
}

