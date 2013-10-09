package com.pyruby.jms;


import com.pyruby.jms.message.StubTextMessage;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.*;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StubConnectionFactoryTest {
    @Test
    public void testSend() throws Exception {
        JmsServer.getInstance().reset();
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
    public void testRead() throws Exception {
        JmsServer.getInstance().reset();
        StubDestination.StubQueue q = new StubDestination.StubQueue("myqueue");
        StubConnectionFactory f = (StubConnectionFactory) JmsServer.createConnectionFactory();

        DefaultMessageListenerContainer c = new DefaultMessageListenerContainer();
        c.setConnectionFactory(f);
        c.setDestination(q);
        c.setSessionTransacted(true);

        TestListener listener = new TestListener();
        c.setMessageListener(listener);
        c.initialize();
        c.start();
        StubTextMessage message = new StubTextMessage("my message");
        JmsServer.getInstance().getDestination(q).blocked = true;
        JmsServer.getInstance().sendMessage(q, message);
        assertEquals(false, message.delivered);
        message.deliver();
        assertEquals(true, message.delivered);

        JmsServer.getInstance().getDestination(q).awaitAllMessagesProcessed(2000, TimeUnit.MILLISECONDS);
        assertEquals("my message", listener.received);
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
