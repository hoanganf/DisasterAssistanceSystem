package an.it.views.weekview;

import java.util.Calendar;

/**
 * Created by Raquib on 1/6/2015.
 */
public interface DateTimeInterpreter {
    String dateToString(Calendar date);
    String timeToString(int hour);
}
