package antip2w.tools.util;

public class BinaryStringifier {

    private static final String HEADER = """
        +--------+-------------------------------------------------+------------------+
        | Offset |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |   Decoded text   |
        +--------+-------------------------------------------------+------------------+""";
    private static final String FOOTER = """
        +--------+-------------------------------------------------+------------------+""";

    public static String stringify(byte[] bytes) {
        StringBuilder s = new StringBuilder(HEADER);
        int linesRequired = (bytes.length >> 4) + 1;
        for (int line = 0; line < linesRequired; line++) {
            s.append('\n').append("|  ");
            String paddedOffset = Util.leftZeroPad(Integer.toHexString(line << 4), 4);
            s.append(paddedOffset).append("  | ");
            for (int i = 0; i < 16; i++) {
                int index = (line << 4) + i;
                if (index > bytes.length - 1) {
                    s.append("   ");
                } else {
                    byte b = bytes[index];
                    String paddedByte = Util.leftZeroPad(Integer.toHexString(Byte.toUnsignedInt(b)), 2);
                    s.append(paddedByte).append(' ');
                }
            }
            s.append("| ");
            for (int row = 0; row < 16; row++) {
                int index = (line << 4) + row;
                if (index >= bytes.length) {
                    s.append(" ");
                } else {
                    byte b = bytes[index];
                    s.append(getCharFromByte(b));
                }
            }
            s.append(" |");
        }
        return s.append('\n').append(FOOTER).toString();
    }

    private static char getCharFromByte(byte b) {
        if (b < 32 || b == 127) {
            return '.';
        }

        return (char) b;
    }

}
