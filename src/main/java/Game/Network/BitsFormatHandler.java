package Game.Network;

import java.nio.ByteBuffer;

public class BitsFormatHandler {
    public final static int x = 1;
    public final static int y = 5;
    public final static int r = 9;
    public final static int fi = 13;
    public final static int id = 17;

    public final static int pFi = 1;

    public static void writeFloatBits(float f, byte[] bytes, int begin) {
        byte[] temp = ByteBuffer.allocate(4).putFloat(f).array();

        bytes[begin] = temp[0];
        bytes[begin + 1] = temp[1];
        bytes[begin + 2] = temp[2];
        bytes[begin + 3] = temp[3];
    }

    public static float readFloatBits(byte bytes[], int begin) {
        return ByteBuffer.wrap(bytes, begin, 4).getFloat();
    }

    public static void writeIntBits(int number, byte[] bytes, int begin) {
        byte[] temp = ByteBuffer.allocate(4).putInt(number).array();

        bytes[begin] = temp[0];
        bytes[begin + 1] = temp[1];
        bytes[begin + 2] = temp[2];
        bytes[begin + 3] = temp[3];
    }

    public static int readIntBits(byte bytes[], int begin) {
        return ByteBuffer.wrap(bytes, begin, 4).getInt();
    }
}
