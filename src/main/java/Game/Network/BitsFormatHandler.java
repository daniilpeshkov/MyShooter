package Game.Network;

public class BitsFormatHandler {
    public final static int x = 1;
    public final static int y = 5;
    public final static int r = 9;
    public final static int fi = 13;
    public final static int id = 17;

    public final static int pFi = 1;

    public static void writeFloatBits(float f, byte[] bytes, int begin) {
        int number = Float.floatToIntBits(f);

        writeIntBits(number, bytes, begin);
    }

    public static float readFloatBits(byte bytes[], int begin) {
        return Float.intBitsToFloat(readIntBits(bytes, begin));
    }

    public static void writeIntBits(int number, byte[] bytes, int begin) {
        bytes[begin] = (byte) (number >> 24);
        bytes[begin + 1] = (byte) (number >> 16);
        bytes[begin + 2] = (byte) (number >> 8);
        bytes[begin + 3] = (byte) number;
    }

    public static int readIntBits(byte bytes[], int begin) {
        return bytes[begin] << 24 | bytes[begin + 1] << 16 |
                bytes[begin + 2] << 8 | bytes[begin + 3];
    }
}
