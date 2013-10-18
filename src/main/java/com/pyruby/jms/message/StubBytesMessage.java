package com.pyruby.jms.message;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


public class StubBytesMessage extends StubMessage implements BytesMessage {
    private List<Byte> bytes = new LinkedList<Byte>();

    @Override
    public long getBodyLength() throws JMSException {
        return bytes.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean readBoolean() throws JMSException {
        return bytes.remove(0).equals((byte)1);
    }

    @Override
    public byte readByte() throws JMSException {
        return bytes.remove(0);
    }

    @Override
    public int readUnsignedByte() throws JMSException {
        return readByte() & 0xff;
    }

    @Override
    public short readShort() throws JMSException {
        return (short) ((readUnsignedByte() << 8) + readUnsignedByte());
    }

    @Override
    public int readUnsignedShort() throws JMSException {
        return readShort() & 0xffff;
    }

    @Override
    public char readChar() throws JMSException {
        return (char) readUnsignedShort();

    }

    @Override
    public int readInt() throws JMSException {
        return (readUnsignedShort() << 16) + readUnsignedShort();
    }

    @Override
    public long readLong() throws JMSException {
        return (readInt() << 32) + readInt();
    }

    @Override
    public float readFloat() throws JMSException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws JMSException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readUTF() throws JMSException {
        int len = readUnsignedShort();
        if (bytes.size() < len) throw new JMSException("Ran out of bytes");
        byte[] target = new byte[len];
        for (int i = 0; i < len; i++) {
            target[i] = bytes.remove(0);
        }
        try {
            return new String(target, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JMSException("Not a UTF-8 string");
        }
    }

    @Override
    public int readBytes(byte[] value) throws JMSException {
        int count = 0;
        while (count < value.length) {
            if (bytes.isEmpty()) break;
            value[count] = bytes.remove(0);
            count++;
        }
        return count;
    }

    @Override
    public int readBytes(byte[] value, int length) throws JMSException {
        int count = 0;
        while (count < length) {
            if (bytes.isEmpty()) break;
            value[count] = bytes.remove(0);
            count++;
        }
        return count;
    }

    @Override
    public void writeBoolean(boolean value) throws JMSException {
        if (value)
            bytes.add((byte)1);
        else
            bytes.add((byte)0);
    }

    @Override
    public void writeByte(byte value) throws JMSException {
        bytes.add(value);
    }

    @Override
    public void writeShort(short value) throws JMSException {
        bytes.add((byte) (value >> 8));
        bytes.add((byte) value);
    }

    @Override
    public void writeChar(char value) throws JMSException {
        writeShort((short) value);
    }

    @Override
    public void writeInt(int value) throws JMSException {
        writeShort((short) (value >> 16));
        writeShort((short) value);
    }

    @Override
    public void writeLong(long value) throws JMSException {
        writeInt((int)(value >> 32));
        writeInt((int)value);
    }

    @Override
    public void writeFloat(float value) throws JMSException {
        writeInt(Float.floatToIntBits(value));
    }

    @Override
    public void writeDouble(double value) throws JMSException {
        writeLong(Double.doubleToLongBits(value));
    }

    @Override
    public void writeUTF(String value) throws JMSException {
        try {
            byte[] enc = value.getBytes("UTF-8");
            writeShort((short) enc.length);
            writeBytes(enc);
        } catch (UnsupportedEncodingException e) {
            throw new JMSException("Not a UTF-8 string");
        }
    }

    @Override
    public void writeBytes(byte[] value) throws JMSException {
        for (byte b : value) {
            bytes.add(b);
        }
    }

    @Override
    public void writeBytes(byte[] value, int offset, int length) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeObject(Object value) throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reset() throws JMSException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
