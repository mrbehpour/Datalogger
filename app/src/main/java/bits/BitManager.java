package bits;

/**
 * Created by h.eskandari on 5/29/2018.
 */

public class BitManager {
    public static String toBitString(final long n) {
        final char[] bit = new char[64];
        long mask = 1L;
        for(int i = 0; i < 64; i++) {
            final long bitval = n & mask;
            if(bitval == 0) {
                bit[63 - i] = '0';
            } else {
                bit[63 - i] = '1';
            }
            mask <<= 1;
        }
        return String.valueOf(bit);
    }

}
