package com.pyruby.jms;


import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * This is the primary class for the JMS classes to call back into to do all the work for message
 * sending, receiving, resetting destinations, blocking on messages, preventing delivery of messages etc.
 * From a testing perspective, this is the place to go to get hold of the destination messages and interact
 * with them.
 */
public class JmsServer {
    private static JmsServer instance = new JmsServer();
    private Map<String, DestinationMessages> destinations = new HashMap<String, DestinationMessages>();
    final WeakHashMap<Session, List<StubMessage>> messagesInFlight =  new WeakHashMap<Session, List<StubMessage>>(5);
    private StubConnectionFactory connectionFactory;

    public static JmsServer getInstance() {
        return instance;
    }

    /**
     * Factory method to retrieve the ConnectionFactory to be used either straight, or bound to JNDI.
     * @return connectionFactory
     */
    public static ConnectionFactory createConnectionFactory() {
        return getInstance().connectionFactory;
    }

    private JmsServer() {
        connectionFactory = new StubConnectionFactory(this);
    }


    public DestinationMessages getDestination(javax.jms.Destination destination) {
        return getDestination(((StubDestination)destination).getName());
    }

    public DestinationMessages getDestination(String destinationName) {
        DestinationMessages messages = destinations.get(destinationName);
        if (messages == null) {
            messages = new DestinationMessages(messagesInFlight);
            destinations.put(destinationName, messages);
        }
        return messages;
    }

    public void sendMessage(javax.jms.Destination destination, Message message) {
        getDestination(destination).send((StubMessage) message);
    }

    Message nextMessage(StubMessageConsumer consumer, long timeout, TimeUnit timeUnit) {
        return getDestination(consumer.getDestinationName()).poll(consumer, timeout, timeUnit);
    }

    void commitMessages(Session session) {

        List<StubMessage> stubMessages = messagesInFlight.get(session);
        if (stubMessages != null) {
            for(StubMessage message : stubMessages) {
                message.processed();
            }
        }
    }

    public void drainDestination(String destinationName) {
        getDestination(destinationName).drain();
    }

    public void drainDestinations() {
        for(String destination : destinations.keySet()) {
            drainDestination(destination);
        }
    }

    public void reset() {
        drainDestinations();
        destinations.clear();
        messagesInFlight.clear();
    }

    static class DestinationMessages {
        public final Deque<StubMessage> allMessages = new LinkedList<StubMessage>();
        private final LinkedBlockingDeque<StubMessage> messagesAwaitingDelivery = new LinkedBlockingDeque<StubMessage>(100);
        private final WeakHashMap<Session, List<StubMessage>> messagesInFlight;
        private boolean blocked = false;

        public void drain() {
            allMessages.clear();
            for(StubMessage m : messagesAwaitingDelivery) {
                m.processed();
            }
        }

        public boolean isBlocked() {
            return blocked;
        }

        public void unblockDelivery() {
            blocked = false;
            for(StubMessage message : allMessages) {
                message.deliver();
            }
        }

        public void awaitAllMessagesProcessed(long timeout, TimeUnit timeUnit) throws InterruptedException {
            unblockDelivery();
            StubMessage lastMessage = allMessages.peekLast();
            if (lastMessage != null) {
                lastMessage.awaitReceipt(timeout, timeUnit);
            }
        }

        public void block() {
            blocked = true;
        }

        private DestinationMessages(WeakHashMap<Session, List<StubMessage>> messagesInFlight) {
            this.messagesInFlight = messagesInFlight;
        }

        private void send(StubMessage message) {
            allMessages.add(message);
            message.deliverTo(messagesAwaitingDelivery, blocked);
        }

        private StubMessage poll(StubMessageConsumer consumer, long timeout, TimeUnit timeUnit) {
            try {
                StubMessage message = messagesAwaitingDelivery.poll(timeout, timeUnit);
                List<StubMessage> messages = messagesInFlight.get(consumer.getSession());
                if(messages == null) {
                    messages = new ArrayList<StubMessage>();
                    messagesInFlight.put(consumer.getSession(), messages);
                }
                messages.add(message);
                return message;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
