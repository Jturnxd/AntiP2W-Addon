package antip2w.tools.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {

    public static final Random RANDOM = new Random();
    public static final NumberFormat DECIMAL_FORMAT_2 = new DecimalFormat("#0.00");

    public static String stringifyCollection(Collection<?> collection) {
        StringBuilder sb = new StringBuilder("[");

        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            sb.append(o);

            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append(']');

        return sb.toString();
    }

    public static String stringifyMap(Object... pairs) {
        if ((pairs.length & 1) == 1) {
            throw new AssertionError("pairs.length is odd");
        }

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < pairs.length >>> 1; i++) {
            Object key = pairs[i * 2];
            Object value = pairs[i * 2 + 1];

            sb.append(key);
            sb.append(" = ");
            sb.append(value);

            if (i != (pairs.length >>> 1) - 1) {
                sb.append(", ");
            }
        }

        sb.append(']');

        return sb.toString();
    }

    public static String getHumanReadableSize(int size, char unit) {
        if (size < 1024) {
            return size + " " + unit;
        } else if (size < 1024 * 1024) {
            return DECIMAL_FORMAT_2.format(size / 1024) + " k" + unit;
        } else {
            return DECIMAL_FORMAT_2.format(size / (1024 * 1024)) + " M" + unit;
        }
    }

    private static final char[] possibleChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z', '_'
    };

    public static char getRandom0_9a_z_() {
        return Util.possibleChars[RANDOM.nextInt(Util.possibleChars.length)];
    }

    public static String getRandom0_9a_z_(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(getRandom0_9a_z_());
        }

        return sb.toString();
    }

    public static String leftZeroPad(String str, int size) {
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        return "0".repeat(pads) + str;
    }

    public static void breakPoint() {
    }

    public static <T> T make(Supplier<T> factory) {
        return factory.get();
    }

    public static <T> T make(T object, Consumer<? super T> initializer) {
        initializer.accept(object);
        return object;
    }

    public static <T> T getRandom(T[] array) {
        return array[(RANDOM.nextInt(array.length))];
    }

    public static <T> T getRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

}
