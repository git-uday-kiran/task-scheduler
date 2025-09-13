package bit.bees.taskscheduler.util;

import java.util.Random;

public final class ThroatUtils {

    private static final Random RANDOM = new Random();

    public static void smallThroat() {
        long throatMillis = 1000 + RANDOM.nextLong(2000); // 1 - 3 seconds
        throat(throatMillis);
    }

    public static void mediumThroat() {
        long throatMillis = 3000 + RANDOM.nextLong(3000); // 3 - 6 seconds
        throat(throatMillis);
    }

    public static void largeThroat() {
        long throatMillis = 5000 + RANDOM.nextLong(5000); // 5 - 10 seconds
        throat(throatMillis);
    }

    private static void throat(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private ThroatUtils() {
    }
}
