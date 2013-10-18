package com.pyruby.jms;


import com.pyruby.jms.message.StubTextMessage;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

import javax.jms.*;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class JmsServerTest {

    @Before
    public void setUp() {
        JmsServer.getInstance().reset();
    }

    @Test
    public void jmsTemplate_send_shouldAddAMessageToTheDestination() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("myqueue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        JmsTemplate template = new JmsTemplate(f);
        template.setDefaultDestination(q);

        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("foo");
            }
        });

        assertEquals(1, JmsServer.getInstance().getDestination("myqueue").allMessages.size());
    }

    @Test
    public void springJmsMessageListener_shouldReceiveMessagesFromTheDestination() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("somequeue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        TestListener listener = createTestListener(q, f);
        StubTextMessage message = new StubTextMessage("my message");
        JmsServer.getInstance().sendMessage(q, message);

        JmsServer.getInstance().getDestination(q).awaitAllMessagesProcessed(2000, TimeUnit.MILLISECONDS);
        assertEquals("my message", listener.received);
    }

    @Test
    public void springSimpleMessageListenerContainer_shouldReceiveMessagesFromTheDestination() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("somequeue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        SimpleMessageListenerContainer c = new SimpleMessageListenerContainer();
        c.setDestination(q);
        c.setConnectionFactory(f);
        c.setSessionTransacted(true);

        TestListener listener = new TestListener();
        c.setMessageListener(listener);
        c.initialize();
        c.start();
        StubTextMessage message = new StubTextMessage("my message");
        JmsServer.getInstance().sendMessage(q, message);

        JmsServer.getInstance().getDestination(q).awaitAllMessagesProcessed(2000, TimeUnit.MILLISECONDS);
        assertEquals("my message", listener.received);
    }

    @Test
    public void springTemplate_receive_shouldReceiveMessagesFromTheDestination() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("myqueue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        JmsTemplate template = new JmsTemplate(f);
        template.setDefaultDestination(q);

        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("foo");
            }
        });

        Message message = template.receive();
        assertEquals("foo", ((TextMessage) message).getText());
    }

    @Test
    public void springTemplate_shouldSendOtherTypesOfMessageToo() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("myqueue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        JmsTemplate template = new JmsTemplate(f);
        template.setDefaultDestination(q);
        final String content = "Mary had a little lamb";
        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                BytesMessage msg = session.createBytesMessage();
                msg.writeUTF(content);
                return msg;
            }
        });

        BytesMessage message = (BytesMessage) template.receive();

        assertEquals(content, message.readUTF());
    }

    private TestListener createTestListener(StubDestination.StubQueue q, StubConnectionFactory f) {
        DefaultMessageListenerContainer c = new DefaultMessageListenerContainer();
        c.setConnectionFactory(f);
        c.setDestination(q);
        c.setSessionTransacted(true);

        TestListener listener = new TestListener();
        c.setMessageListener(listener);
        c.initialize();
        c.start();
        return listener;
    }

    @Test
    public void block_shouldPreventQueuedMessagesFromBeingDelivered() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("myqueue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        TestListener listener = createTestListener(q, f);
        StubTextMessage message = new StubTextMessage("next message");
        JmsServer.getInstance().getDestination(q).block();
        JmsServer.getInstance().sendMessage(q, message);
        assertEquals(false, message.delivered);

        assertEquals(true, JmsServer.getInstance().getDestination(q).isBlocked());
        message.awaitReceipt(50, TimeUnit.MILLISECONDS);
        assertNull(listener.received);
    }

    @Test
    public void message_deliver_shouldDeliverJustThatMessage_givenABlockedQueue() throws Exception {
        StubDestination.StubQueue q = new StubDestination.StubQueue("some queue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        TestListener listener = createTestListener(q, f);
        StubTextMessage message = new StubTextMessage("some message");
        StubTextMessage unblockedMessage = new StubTextMessage("some other message");
        StubTextMessage lastMessage = new StubTextMessage("some last message");
        JmsServer.getInstance().getDestination(q).block();
        JmsServer.getInstance().sendMessage(q, message);
        JmsServer.getInstance().sendMessage(q, unblockedMessage);
        JmsServer.getInstance().sendMessage(q, lastMessage);
        unblockedMessage.deliver();

        assertEquals(true, JmsServer.getInstance().getDestination(q).isBlocked());
        unblockedMessage.awaitReceipt(2000, TimeUnit.MILLISECONDS);
        assertEquals(unblockedMessage.getText(), listener.received);
    }

    private static class TestListener implements MessageListener {
        volatile String received;
        @Override
        public void onMessage(Message message) {
            try {
                received = ((StubTextMessage)message).getText();
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
