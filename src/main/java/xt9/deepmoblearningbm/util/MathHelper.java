package xt9.deepmoblearningbm.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by xt9 on 2018-06-30.
 */
public class MathHelper {
    public static double ensureRange(double value, double min, double max) {
        return min(max(value, min), max);
    }

    public static int ensureRange(int value, int min, int max) {
        return min(max(value, min), max);
    }
}
