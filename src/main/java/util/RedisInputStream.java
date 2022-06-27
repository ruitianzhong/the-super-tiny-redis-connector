package util;

import exceptions.RedisConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class RedisInputStream extends FilterInputStream {


    private static final int INPUT_BUFFER_SIZE = Integer.parseInt(
            System.getProperty("redis.bufferSize", "8192"));
    int count, limit;
    private byte[] buf;
    private InputStream inputStream;

    public RedisInputStream(InputStream in) {
        this(in, INPUT_BUFFER_SIZE);
    }

    public RedisInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("size <=0");
        }
        buf = new byte[size];
        inputStream = in;
    }

    public int readIntCrLf() {
        return (int) readLongCrLf();
    }

    public long readLongCrLf() {
        final byte[] buf = this.buf;
        ensureFill();
        final boolean isNegative = buf[count] == '-';//do not move the counter?
        if (isNegative) {
            count++;
        }
        long value = 0;
        while (true) {

            ensureFill();
            int b = buf[count++];
            if (b == '\r') {
                ensureFill();
                if (buf[count++] != '\n')//something wrong?
                {
                    throw new RedisConnectionException("Unexpected character!");
                }
                break;
            } else {
                value = value * 10 + (b - '0');//extra "+" fatal error
            }

        }
        return isNegative ? -value : value;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureFill();
        final int length = Math.min(len, limit - count);
        System.arraycopy(buf, count, b, off, length);
        count += length;
        return length;
    }

    public byte readByte() {
        ensureFill();
        return buf[count++];
    }


    public byte[] readLineBytes() {
        ensureFill();
        int pos = count;
        while (true) {
            if (pos == limit) {
                return readLineBytesSlowly();
            }
            if (buf[pos++] != '\r') {
                if (pos == limit) {
                    return readLineBytesSlowly();
                }
            }
            if (buf[pos++] == '\n') {
                break;
            }
        }
        final int n = pos - count - 2;
        final byte[] lineBytes = new byte[n];
        System.arraycopy(buf, count, lineBytes, 0, n);
        count = pos;
        return lineBytes;
    }

    private byte[] readLineBytesSlowly() {
        ByteArrayOutputStream bos = null;
        while (true) {

            ensureFill();
            byte b = buf[count++];
            if (b == '\r') {
                ensureFill();
                byte c = buf[count++];
                if (c == '\n') {
                    break;
                }
                if (bos == null) {//lazy loading
                    bos = new ByteArrayOutputStream();
                }
                bos.write(b);
                bos.write(c);
            }

        }
        return bos == null ? new byte[0] : bos.toByteArray();//why ?
    }

    public String readLine() {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            ensureFill();
            byte b = buf[count++];
            if (b == '\r') {
                ensureFill();
                byte c = buf[count++];
                if (c == '\n') {
                    break;
                }
                sb.append((char) b);
                sb.append((char) c);
            } else {
                sb.append((char) b);//normal character
            }
        }
        final String reply = sb.toString();
        if (reply.length() == 0) {
            throw new RedisConnectionException("Seemingly, the server has closed the connection");
        }
        return reply;
    }

    private void ensureFill() throws RedisConnectionException {
        if (count >= limit) {//have something wrong here?
            try {
                limit = in.read(buf);//blocking here?
                count = 0;
                if (limit == -1) {
                    throw new RedisConnectionException("Unexpected end of stream");
                }
            } catch (IOException ioe) {
                throw new RedisConnectionException(ioe);
            }
        }

    }
}
