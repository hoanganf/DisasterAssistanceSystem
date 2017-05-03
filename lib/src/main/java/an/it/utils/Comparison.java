package an.it.utils;

import java.util.Calendar;

/**
 * Created by hoang on 6/9/2016.
 */
public class Comparison {
    public static boolean isSameDay(Calendar dayOne, Calendar dayTwo) {
        return dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR) && dayOne.get(Calendar.DAY_OF_YEAR) == dayTwo.get(Calendar.DAY_OF_YEAR);
    }
}
