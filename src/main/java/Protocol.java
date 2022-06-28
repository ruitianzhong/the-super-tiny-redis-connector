import commands.CommandArguments;
import commands.Rawable;
import exceptions.RedisConnectionException;
import exceptions.RedisException;
import util.RedisInputStream;
import util.RedisOutputStream;

import java.io.IOException;

public class Protocol {
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 6379;

    public static final int DEFAULT_TIME_OUT = Integer.MAX_VALUE;

    public static final void sendCommand(final CommandArguments commandArguments, final RedisOutputStream out) {
        try {
            out.write((byte) '*');
            out.writeIntCrLf(commandArguments.size());
            for (Rawable temp : commandArguments) {
                out.write((byte) '$');
                byte[] b = temp.getRaw();
                out.writeIntCrLf(b.length);
                out.write(b);
                out.writeCrLf();
            }
        } catch (IOException ioe) {
            throw new RedisConnectionException(ioe);
        }
    }

    public static Object read(RedisInputStream ris) {
        return process(ris);
    }

    private static Object process(RedisInputStream ris) {
        byte firstByte = ris.readByte();
        switch (firstByte) {
            case '+':
                return processStateCodeReply(ris);
            case '$':
                return processBulkReply(ris);
            case '-':
                throw new RedisException("Error handling is not available.");
            default:
                throw new RedisException("current version do no support the protocol.");
        }

    }

    private static Object processStateCodeReply(RedisInputStream ris) {
        return ris.readLineBytes();
    }

    private static Object processBulkReply(RedisInputStream ris) {
        int count = ris.readIntCrLf();
        if (count == -1) {
            return null;
        }
        byte[] buf = new byte[count];
        for (int i = 0; i < count; i++) {
            buf[i] = ris.readByte();
        }
        ris.readByte();
        ris.readByte();
        return buf;
    }

    private Object processError(RedisInputStream ris) {
        return null;
        //TODO : error handling.
    }
}
