package cc.fascinated.gravestones.utils;

import java.util.concurrent.TimeUnit;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 16/06/2021
 */
public class Utils {

    /**
     * Returns the shortened formatted time until the provided {@link Long} is present
     * Formatted as: "1mo 2w 3d 4h 5m 6s"
     *
     * @param end The time to get until
     * @return The shortened formatted time until the provided date
     */
    public static String getTimeUntil(long end) {
        if (end == -1) {
            return "Permanent";
        }
        long left = end - System.currentTimeMillis();
        StringBuilder leftString = new StringBuilder();
        if (left <= 0) {
            return "now";
        }
        TimeUnit[] units = {TimeUnit.DAYS, TimeUnit.DAYS, TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS};
        int[] amounts = {30, 7, 1, 1, 1, 1, 1};
        String[] suffixes = {"mo ", "w ", "d ", "h ", "m ", "s"};
        for (int i = 0; i < 6; i++) {
            long asMillis = units[i].toMillis(amounts[i]);
            if (left - asMillis >= 1) {
                leftString.append(left / asMillis).append(suffixes[i]);
                left %= asMillis;
            }
        }
        return leftString.toString();
    }

}
