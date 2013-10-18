package com.pyruby.jms.message;


import org.junit.Test;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

import static org.junit.Assert.assertEquals;

public class StubBytesMessageTest {

    @Test
    public void writeBoolean_shouldBeReadable() throws JMSException {
        BytesMessage m = new StubBytesMessage();
        m.writeBoolean(true);
        assertEquals(true, m.readBoolean());
        m.writeBoolean(false);
        assertEquals(false, m.readBoolean());
    }

    @Test
    public void writeByte_shouldBeReadable() throws JMSException {
        BytesMessage m = new StubBytesMessage();
        m.writeByte((byte) 98);
        assertEquals(98, m.readByte());
    }

    @Test
    public void writeShort_shouldBeReadable() throws JMSException {
        BytesMessage m = new StubBytesMessage();
        m.writeShort((short) 2125);
        assertEquals(2125, m.readShort());
    }

    @Test
    public void writeInt_shouldBeReadable() throws JMSException {
        BytesMessage m = new StubBytesMessage();
        m.writeInt(123456789);
        assertEquals(123456789, m.readInt());
    }

    @Test
    public void writeUtf_shouldBeReadable() throws JMSException {
        BytesMessage m = new StubBytesMessage();
        m.writeUTF("I want ~ wiggle");
        assertEquals("I want ~ wiggle", m.readUTF());
    }

}
