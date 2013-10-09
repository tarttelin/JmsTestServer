package com.pyruby.jms;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;

public abstract class StubDestination implements Destination {

    public abstract String getName();

    public static class StubQueue extends StubDestination implements Queue {
        private final String name;

        public StubQueue(String name) {
            this.name = name;
        }

        @Override
        public String getQueueName() throws JMSException {
            return name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
