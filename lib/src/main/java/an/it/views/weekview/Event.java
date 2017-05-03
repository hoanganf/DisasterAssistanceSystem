package an.it.views.weekview;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import an.it.utils.Comparison;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class Event {
    private long mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private String mName;
    private String mDescription;
    private int mBackgroundColor = Color.BLUE;
    private int mTextColor = Color.WHITE;

    public Event() {
    }

    /**
     * Initializes the event for week view.
     *
     * @param id          The id of the event.
     * @param name        Name of the event.
     * @param startYear   Year when the event starts.
     * @param startMonth  Month when the event starts.
     * @param startDay    Day when the event starts.
     * @param startHour   Hour (in 24-hour format) when the event starts.
     * @param startMinute Minute when the event starts.
     * @param endYear     Year when the event ends.
     * @param endMonth    Month when the event ends.
     * @param endDay      Day when the event ends.
     * @param endHour     Hour (in 24-hour format) when the event ends.
     * @param endMinute   Minute when the event ends.
     */
    public Event(long id, String name, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        this.mId = id;

        this.mStartTime = Calendar.getInstance();
        this.mStartTime.set(Calendar.YEAR, startYear);
        this.mStartTime.set(Calendar.MONTH, startMonth - 1);
        this.mStartTime.set(Calendar.DAY_OF_MONTH, startDay);
        this.mStartTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.mStartTime.set(Calendar.MINUTE, startMinute);

        this.mEndTime = Calendar.getInstance();
        this.mEndTime.set(Calendar.YEAR, endYear);
        this.mEndTime.set(Calendar.MONTH, endMonth - 1);
        this.mEndTime.set(Calendar.DAY_OF_MONTH, endDay);
        this.mEndTime.set(Calendar.HOUR_OF_DAY, endHour);
        this.mEndTime.set(Calendar.MINUTE, endMinute);

        this.mName = name;
    }

    /**
     * Initializes the event for week view.
     *
     * @param id          The id of the event.
     * @param name        Name of the event.
     * @param description The location of the event.
     * @param startTime   The time when the event starts.
     * @param endTime     The time when the event ends.
     */
    public Event(long id, String name, String description, Calendar startTime, Calendar endTime) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
    }

    /**
     * Initializes the event for week view.
     *
     * @param id          The id of the event.
     * @param name        Name of the event.
     * @param description The location of the event.
     * @param startTime   The time when the event starts.
     * @param endTime     The time when the event ends.
     * @param textColor   color
     */
    public Event(long id, String name, String description, Calendar startTime, Calendar endTime, int backColor, int textColor) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mBackgroundColor = backColor;
        this.mTextColor = textColor;
    }


    /**
     * Initializes the event for week view.
     *
     * @param id        The id of the event.
     * @param name      Name of the event.
     * @param startTime The time when the event starts.
     * @param endTime   The time when the event ends.
     */
    public Event(long id, String name, Calendar startTime, Calendar endTime) {
        this(id, name, null, startTime, endTime);
    }


    public Calendar getStartTime() {

        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
        if (mStartTime != null) {
            if (mEndTime != null) {
                if (mEndTime.getTimeInMillis() <= mStartTime.getTimeInMillis()) {
                    mEndTime.setTimeInMillis(mStartTime.getTimeInMillis());
                }
            } else {
                mEndTime = (Calendar) mStartTime.clone();
            }
        }
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        this.mEndTime = endTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Event) {
            Event that = (Event) o;
            return mId == that.mId;
        } else return false;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));//=mId
    }

    public List<Event> splitWeekViewEvent() {
        //This function splits the WeekViewEvent in WeekViewEvents by day
        List<Event> events = new ArrayList<Event>();
        // The first millisecond of the next day is still the same day. (no need to split events for this).
        if (!Comparison.isSameDay(mStartTime, mEndTime)) {
            Calendar endTime = (Calendar) mStartTime.clone();
            endTime.set(Calendar.HOUR_OF_DAY, 23);
            endTime.set(Calendar.MINUTE, 59);//set end time 23:59
            endTime.set(Calendar.SECOND, 59);
            Log.e("MINUTE", endTime.get(Calendar.DAY_OF_MONTH) + "");
            Log.e("MINUTE", endTime.get(Calendar.HOUR_OF_DAY) + "");
            Log.e("MINUTE", endTime.get(Calendar.MINUTE) + "");
            //add splitted part to events
            Event eventOfThisPart = new Event(mId, mName, mDescription, mStartTime, endTime);
            eventOfThisPart.setBackgroundColor(mBackgroundColor);
            eventOfThisPart.setTextColor(mTextColor);
            events.add(eventOfThisPart);
            int countOfDay = mEndTime.get(Calendar.DAY_OF_YEAR) - mStartTime.get(Calendar.DAY_OF_YEAR);
            if (countOfDay > 1) {
                // Add middle days.
                Calendar middleDay = (Calendar) mStartTime.clone();
                for (int i = 1; i < countOfDay; i++) {
                    middleDay.add(Calendar.DATE, 1);
                    middleDay.set(Calendar.HOUR_OF_DAY, 0);
                    middleDay.set(Calendar.MINUTE, 0);

                    Calendar endOfmiddleDay = (Calendar) middleDay.clone();
                    endOfmiddleDay.set(Calendar.HOUR_OF_DAY, 23);
                    endOfmiddleDay.set(Calendar.MINUTE, 59);
                    endTime.set(Calendar.SECOND, 59);
                    events.add(new Event(mId, mName, null, middleDay, endOfmiddleDay, mBackgroundColor, mTextColor));
                }
            }
            // Add last part.
            Calendar startTime = (Calendar) mEndTime.clone();
            startTime.set(Calendar.HOUR_OF_DAY, 0);
            startTime.set(Calendar.MINUTE, 0);
            events.add(new Event(mId, mName, mDescription, startTime, mEndTime, mBackgroundColor, mTextColor));
        } else {
            events.add(this);
        }
        return events;
    }


    /**
     * Checks if two events overlap.
     *
     * @param event2 The second event.
     * @return true if the events overlap.
     */
    public boolean isCollideWith(Event event2) {
        long start1 = this.getStartTime().getTimeInMillis();
        long end1 = this.getEndTime().getTimeInMillis();
        long start2 = event2.getStartTime().getTimeInMillis();
        long end2 = event2.getEndTime().getTimeInMillis();
        return !((start1 >= end2) || (end1 <= start2));
    }

}
