package com.pyruby.jms;


import javax.jms.*;

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

    }

}

