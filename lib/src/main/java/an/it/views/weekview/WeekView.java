package an.it.views.weekview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import an.it.utils.Comparison;
import an.it.utils.Convert;
import an.it.R;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://alamkanak.github.io/
 */
public class WeekView extends View {

    private enum Direction {
        NONE, LEFT, RIGHT, VERTICAL
    }

    private final Context mContext;
    private Paint mPaintTimeColumnText;
    private float mTimeTextWidth;
    private float mTimeTextHeight;
    private Paint mPaintHeaderTextPaint;
    private float mHeaderTextHeight;
    private float mHeaderHeight;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private PointF mCurrentOrigin;
    private Direction mCurrentScrollDirection = Direction.NONE;
    private Paint mHeaderBackgroundPaint;
    private float mWidthPerDay;
    private Paint mDayBackgroundPaint;
    private Paint mHourSeparatorPaint;
    private float mHeaderMarginBottom;
    private Paint mTodayBackgroundPaint;
    private Paint mFutureBackgroundPaint;
    private Paint mPastBackgroundPaint;
    private Paint mCurrentTimeLinePaint;
    private Paint mTodayHeaderTextPaint;
    private Paint mEventBackgroundPaint;
    private float mTimeColumnWidth;
    private List<List<EventRect>> mEventsList;
    private TextPaint mEventTextPaint;
    private Paint mHeaderColumnBackgroundPaint;
    private Direction mCurrentFlingDirection = Direction.NONE;
    private ScaleGestureDetector mScaleDetector;
    private boolean mIsZooming = false;
    private boolean mShowFirstDayOfWeekFirst;
    private float mDifferentPixcel = 0;
    private int mDefaultEventColor;
    private int mMinimumFlingVelocity = 0;
    // Attributes and their default values.
    private int mHourHeight = 50;
    private int mMinHourHeight = 0; //no minimum specified (will be dynamic, based on screen)
    private int mEffectiveMinHourHeight = mMinHourHeight; //compensates for the fact that you can't keep zooming out.
    private int mMaxHourHeight = 250;
    private int mFirstDayOfWeek = Calendar.MONDAY;
    private int mTextSize = 12;
    private int mHeaderColumnPadding = 10;
    private int mHeaderTextColor;
    private int mLeftTimeTextColor;
    private int mNumberOfVisibleDays = 7;
    private int mHeaderRowPadding = 10;
    private int mHeaderRowBackgroundColor;//header background color
    private int mDayBackgroundColor;//background color
    private int mPastBackgroundColor; //
    private int mFutureBackgroundColor;
    private int mTodayBackgroundColor;//
    private int mHeaderColumnBackgroundColor = Color.WHITE;
    private int mCurrentTimeLineColor;
    private int mHourSeparatorColor;
    private int mTodayHeaderTextColor;
    private int mCurrentTimeLineWeight;
    private int mHourSeparatorWeight;
    private int mEventTextSize;
    private int mEventTextColor;
    private int mEventPadding = 8;
    private boolean mIsFirstDraw = true;
    private boolean mAreDimensionsInvalid = true;
    private int mEventMarginVertical = 0;
    private float mXScrollingSpeed = 1f;
    private double mScrollToHour = -1;
    private Calendar mScrollToDay = null;
    private int mEventCornerRadius = 0;
    private boolean mShowCurrentTimeLine;
    private boolean mShowDistinctPastFutureColor = false;
    private boolean mHorizontalFlingEnabled = true;
    private boolean mVerticalFlingEnabled = true;
    private int mAllDayEventHeight = 100;
    private float mScrollDuration = 1;
    private boolean isScrollPage;
    private Calendar mToday;
    private int mLeftDayInWeekView;
    private float mTimeNetworkZoneLeft;
    private float mTimeNetworkZoneTop;
    // Listeners.
    private EventClickListener mEventClickListener;
    private EventLongPressListener mEventLongPressListener;
    private EmptyViewClickListener mEmptyViewClickListener;
    private EmptyViewLongPressListener mEmptyViewLongPressListener;
    private DateTimeInterpreter mDateTimeInterpreter;

    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // Check if view is zoomed.
            if (!mIsZooming) {
                switch (mCurrentScrollDirection) {
                    case NONE: {
                        // Allow scrolling only in one direction.
                        if (Math.abs(distanceX) > Math.abs(distanceY)) {
                            if (distanceX > 0) {
                                mCurrentScrollDirection = Direction.LEFT;
                            } else {
                                mCurrentScrollDirection = Direction.RIGHT;
                            }
                        } else {
                            mCurrentScrollDirection = Direction.VERTICAL;
                        }
                        break;
                    }
                    case LEFT: {
                        // Change direction if there was enough change.
                        if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX < -ViewConfiguration.get(mContext).getScaledTouchSlop())) {
                            mCurrentScrollDirection = Direction.RIGHT;
                        }
                        break;
                    }
                    case RIGHT: {
                        // Change direction if there was enough change.
                        if (Math.abs(distanceX) > Math.abs(distanceY) && (distanceX > ViewConfiguration.get(mContext).getScaledTouchSlop())) {
                            mCurrentScrollDirection = Direction.LEFT;
                        }
                        break;
                    }
                }

                // Calculate the new origin after scroll.
                switch (mCurrentScrollDirection) {
                    case LEFT:
                    case RIGHT:
                        mCurrentOrigin.x -= distanceX * mXScrollingSpeed;
                        ViewCompat.postInvalidateOnAnimation(WeekView.this);
                        break;
                    case VERTICAL:
                        mCurrentOrigin.y -= distanceY;
                        ViewCompat.postInvalidateOnAnimation(WeekView.this);
                        break;
                }
            }
            return true;

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!mIsZooming && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && mCurrentFlingDirection == Direction.NONE && (mHorizontalFlingEnabled || mVerticalFlingEnabled)) {
                mCurrentFlingDirection = mCurrentScrollDirection;
                mScroller.forceFinished(true);
                switch (mCurrentFlingDirection) {
                    case LEFT:
                    case RIGHT:
                        if (mHorizontalFlingEnabled) {
                            if (isScrollPage) {
                                float scrollSpace = (mWidthPerDay + mHourSeparatorWeight) * mNumberOfVisibleDays;
                                if (Math.abs(velocityX) > scrollSpace) {
                                    int sign = velocityX > 0 ? -1 : 1;
                                    goToNearestOrigin(sign * scrollSpace);
                                }
                            } else {
                                mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, (int) (velocityX * mXScrollingSpeed), (int) mCurrentOrigin.y, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                            }
                        }
                        break;
                    case VERTICAL:
                        if (mVerticalFlingEnabled) {
                            mScroller.fling((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, 0, (int) velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, (int) -(mHourHeight * 24 + mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2 - getHeight()), 0);
                        }
                        break;
                }

                ViewCompat.postInvalidateOnAnimation(WeekView.this);
            }
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // If the tap was on an event then trigger the callback.
            if (mEventsList != null && mEventClickListener != null) {
                for (List<EventRect> reversedEventRects : mEventsList) {
                    for (EventRect event : reversedEventRects) {
                        if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                            mEventClickListener.onEventClick(event.originalEvent, event.rectF);
                            playSoundEffect(SoundEffectConstants.CLICK);
                            return super.onSingleTapConfirmed(e);
                        }
                    }
                }
            }

            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewClickListener != null && e.getX() > mTimeColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mEmptyViewClickListener.onEmptyViewClicked(selectedTime);
                }
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (mEventLongPressListener != null && mEventsList != null) {
                for (List<EventRect> reversedEventRects : mEventsList) {
                    for (EventRect event : reversedEventRects) {
                        if (event.rectF != null && e.getX() > event.rectF.left && e.getX() < event.rectF.right && e.getY() > event.rectF.top && e.getY() < event.rectF.bottom) {
                            mEventLongPressListener.onEventLongPress(event.originalEvent, event.rectF);
                            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                            return;
                        }
                    }
                }
            }

            // If the tap was on in an empty space, then trigger the callback.
            if (mEmptyViewLongPressListener != null && e.getX() > mTimeColumnWidth && e.getY() > (mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)) {
                Calendar selectedTime = getTimeFromPoint(e.getX(), e.getY());
                if (selectedTime != null) {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    mEmptyViewLongPressListener.onEmptyViewLongPress(selectedTime);
                }
            }
        }
    };

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Hold references.
        mContext = context;
        // Get the attribute values (if any).
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeekView, 0, 0);
        try {
            mFirstDayOfWeek = a.getInteger(R.styleable.WeekView_firstDayOfWeek, mFirstDayOfWeek);
            mHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_hourHeight, Convert.PixelFromDp(mContext, 20));
            mMinHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_minHourHeight, Convert.PixelFromDp(mContext, 5));
            mEffectiveMinHourHeight = mMinHourHeight;
            mMaxHourHeight = a.getDimensionPixelSize(R.styleable.WeekView_maxHourHeight, Convert.PixelFromDp(mContext, 250));
            mTextSize = a.getDimensionPixelSize(R.styleable.WeekView_textSize1, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, context.getResources().getDisplayMetrics()));
            mHeaderColumnPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerColumnPadding, Convert.PixelFromDp(mContext, 5));
            mHeaderTextColor = a.getColor(R.styleable.WeekView_headerTextColor, Color.WHITE);
            mLeftTimeTextColor = a.getColor(R.styleable.WeekView_leftTimeTextColor, Color.parseColor("#3d3d3d"));
            mNumberOfVisibleDays = a.getInteger(R.styleable.WeekView_noOfVisibleDays, mNumberOfVisibleDays);
            mShowFirstDayOfWeekFirst = a.getBoolean(R.styleable.WeekView_showFirstDayOfWeekFirst, true);
            mHeaderRowPadding = a.getDimensionPixelSize(R.styleable.WeekView_headerRowPadding, Convert.PixelFromDp(mContext, 10));
            mHeaderRowBackgroundColor = a.getColor(R.styleable.WeekView_headerRowBackgroundColor, Color.parseColor("#ff4F81BD"));
            mDayBackgroundColor = a.getColor(R.styleable.WeekView_dayBackgroundColor, Color.WHITE);
            mFutureBackgroundColor = a.getColor(R.styleable.WeekView_futureBackgroundColor, Color.WHITE);
            mPastBackgroundColor = a.getColor(R.styleable.WeekView_pastBackgroundColor, Color.parseColor("#D3DFEE"));
            mCurrentTimeLineColor = a.getColor(R.styleable.WeekView_currentTimeLineColor, Color.parseColor("#32CD32"));
            mCurrentTimeLineWeight = a.getDimensionPixelSize(R.styleable.WeekView_currentTimeLineWeight, Convert.PixelFromDp(mContext, (float) 0.1));
            mHourSeparatorColor = a.getColor(R.styleable.WeekView_hourSeparatorColor, Color.parseColor("#625f78"));
            mTodayBackgroundColor = a.getColor(R.styleable.WeekView_todayBackgroundColor, Color.WHITE);
            mHourSeparatorWeight = a.getDimensionPixelSize(R.styleable.WeekView_hourSeparatorWeight, Convert.PixelFromDp(mContext, (float) 0.1));
            mTodayHeaderTextColor = a.getColor(R.styleable.WeekView_todayHeaderTextColor, Color.parseColor("#82b2f0"));
            mEventTextSize = a.getDimensionPixelSize(R.styleable.WeekView_eventTextSize, Convert.SpFromPixel(mContext, 8));
            mEventTextColor = a.getColor(R.styleable.WeekView_eventTextColor, Color.WHITE);
            mEventPadding = a.getDimensionPixelSize(R.styleable.WeekView_eventPadding, Convert.PixelFromDp(mContext, 1));
            mHeaderColumnBackgroundColor = a.getColor(R.styleable.WeekView_headerColumnBackground, Color.WHITE);
            mEventMarginVertical = a.getDimensionPixelSize(R.styleable.WeekView_eventMarginVertical, mEventMarginVertical);
            mXScrollingSpeed = a.getFloat(R.styleable.WeekView_xScrollingSpeed, mXScrollingSpeed);
            mEventCornerRadius = a.getDimensionPixelSize(R.styleable.WeekView_eventCornerRadius, mEventCornerRadius);
            mShowDistinctPastFutureColor = a.getBoolean(R.styleable.WeekView_showDistinctPastFutureColor, true);
            mShowCurrentTimeLine = a.getBoolean(R.styleable.WeekView_showNowLine, true);
            mHorizontalFlingEnabled = a.getBoolean(R.styleable.WeekView_horizontalFlingEnabled, true);
            mVerticalFlingEnabled = a.getBoolean(R.styleable.WeekView_verticalFlingEnabled, true);
            mAllDayEventHeight = a.getDimensionPixelSize(R.styleable.WeekView_allDayEventHeight, mAllDayEventHeight);
            mScrollDuration = a.getFloat(R.styleable.WeekView_scrollDuration, (float) 0.3);
            isScrollPage = a.getBoolean(R.styleable.WeekView_isScrollPage, true);
            // Set default event color.
            mDefaultEventColor = Color.parseColor("#f57f68");
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mToday = Calendar.getInstance();
        mToday.set(Calendar.HOUR_OF_DAY, 0);
        mToday.set(Calendar.MINUTE, 0);
        mToday.set(Calendar.SECOND, 0);
        mToday.set(Calendar.MILLISECOND, 0);
        // Scrolling initialization.
        mGestureDetector = new GestureDetectorCompat(mContext, mGestureListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mScroller = new OverScroller(mContext, new FastOutLinearInInterpolator());
        }
        mMinimumFlingVelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
        // Measure settings for time column.
        mPaintTimeColumnText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTimeColumnText.setTextAlign(Paint.Align.RIGHT);
        mPaintTimeColumnText.setTextSize(mTextSize);
        mPaintTimeColumnText.setColor(mLeftTimeTextColor);
        Rect rect = new Rect();
        mPaintTimeColumnText.getTextBounds("00 PM", 0, "00 PM".length(), rect);//get bound in to rect
        mTimeTextHeight = rect.height();
        mHeaderMarginBottom = mTimeTextHeight / 2;
        calculateTimeColumnWidth();

        // Measure settings for header row.
        mPaintHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintHeaderTextPaint.setColor(mHeaderTextColor);
        mPaintHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mPaintHeaderTextPaint.setTextSize(mTextSize);
        mPaintHeaderTextPaint.getTextBounds("00 PM", 0, "00 PM".length(), rect);
        mHeaderTextHeight = rect.height();
        mPaintHeaderTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        // Prepare header background paint.
        mHeaderBackgroundPaint = new Paint();
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);

        // Prepare day background color paint.
        mDayBackgroundPaint = new Paint();
        mDayBackgroundPaint.setColor(mDayBackgroundColor);
        mFutureBackgroundPaint = new Paint();
        mFutureBackgroundPaint.setColor(mFutureBackgroundColor);
        mPastBackgroundPaint = new Paint();
        mPastBackgroundPaint.setColor(mPastBackgroundColor);

        // Prepare hour separator color paint.
        mHourSeparatorPaint = new Paint();
        mHourSeparatorPaint.setStyle(Paint.Style.STROKE);
        mHourSeparatorPaint.setStrokeWidth(mHourSeparatorWeight);
        mHourSeparatorPaint.setColor(mHourSeparatorColor);

        // Prepare the "now" line color paint
        mCurrentTimeLinePaint = new Paint();
        mCurrentTimeLinePaint.setStrokeWidth(mCurrentTimeLineWeight);
        mCurrentTimeLinePaint.setColor(mCurrentTimeLineColor);

        // Prepare today background color paint.
        mTodayBackgroundPaint = new Paint();
        mTodayBackgroundPaint.setColor(mTodayBackgroundColor);

        // Prepare today header text color paint.
        mTodayHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayHeaderTextPaint.setTextAlign(Paint.Align.CENTER);
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mTodayHeaderTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTodayHeaderTextPaint.setColor(mTodayHeaderTextColor);

        // Prepare event background color.
        mEventBackgroundPaint = new Paint();
        mEventBackgroundPaint.setColor(mDefaultEventColor);

        // Prepare header column background color.
        mHeaderColumnBackgroundPaint = new Paint();
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);

        // Prepare event text size and color.
        mEventTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mEventTextPaint.setStyle(Paint.Style.FILL);
        mEventTextPaint.setColor(mEventTextColor);
        mEventTextPaint.setTextSize(mEventTextSize);
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {//if << not using this function zoom
            mScaleDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
                @Override
                public void onScaleEnd(ScaleGestureDetector detector) {
                    mIsZooming = false;
                }

                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    mIsZooming = true;
                    return true;
                }

                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                        mHourHeight = Math.round(mHourHeight * detector.getScaleFactor());
                        // Calculate the new height due to the zooming.
                        if (mHourHeight > 0) {
                            if (mHourHeight < mEffectiveMinHourHeight)
                                mHourHeight = mEffectiveMinHourHeight;
                            else if (mHourHeight > mMaxHourHeight) mHourHeight = mMaxHourHeight;
                            mCurrentOrigin.y = (mCurrentOrigin.y / mHourHeight) * mHourHeight;
                        }

                    }
                    invalidate();
                    return true;
                }
            });
        }
    }

    /**
     * Initialize time column width. Calculate value with all possible hours (supposed widest text).
     */
    private void calculateTimeColumnWidth() {
        mTimeTextWidth = 0;
        for (int i = 0; i < 24; i++) {
            // Measure time string and get max width.
            String time = getDateTimeInterpreter().timeToString(i);
            if (time == null) {
                throw new IllegalStateException("A DateTime Interpreter must not return null time");
            } else {
                mTimeTextWidth = Math.max(mTimeTextWidth, mPaintTimeColumnText.measureText(time));
            }
        }
        mCurrentOrigin = new PointF(0f, 0f);
    }

    // fix rotation changes
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mAreDimensionsInvalid = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAreDimensionsInvalid) {
            mEffectiveMinHourHeight = Math.max(mMinHourHeight, (int) ((getHeight() - mHeaderHeight - mHeaderRowPadding * 2 - mHeaderMarginBottom) / 24));
            if (mScrollToDay != null)
                goToDate(mScrollToDay);

            if (mScrollToHour >= 0)
                goToHour(mScrollToHour);
            mAreDimensionsInvalid = false;
        }
        // Calculate the available width for each day.
        mTimeColumnWidth = mTimeTextWidth + mHeaderColumnPadding * 2;
        mWidthPerDay = (getWidth() - mTimeColumnWidth - mHourSeparatorWeight * (mNumberOfVisibleDays - 1)) / mNumberOfVisibleDays;
        //set init
        if (mIsFirstDraw) {
            mIsFirstDraw = false;
            // If the week view is being drawn for the first time, then consider the first day of the week.
            if (mNumberOfVisibleDays >= 7 && mToday.get(Calendar.DAY_OF_WEEK) != mFirstDayOfWeek && mShowFirstDayOfWeekFirst) {
                mDifferentPixcel = (mWidthPerDay + mHourSeparatorWeight) * (mToday.get(Calendar.DAY_OF_WEEK) - mFirstDayOfWeek);
                mCurrentOrigin.x += mDifferentPixcel;
            }
        }
        // Consider scroll offset.
        // If the new mCurrentOrigin.y is invalid, make it valid.
        float currentOriginMaxY = getHeight() - (mHourHeight + mHourSeparatorWeight) * 24 - mHeaderHeight - mHeaderRowPadding * 2 - mHeaderMarginBottom - mTimeTextHeight / 2;
        if (mCurrentOrigin.y < currentOriginMaxY)
            mCurrentOrigin.y = currentOriginMaxY;

        // Don't put an "else if" because it will trigger a glitch when completely zoomed out and
        // scrolling vertically.
        if (mCurrentOrigin.y > 0) {
            mCurrentOrigin.y = 0;
        }

        mLeftDayInWeekView = (int) -(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mHourSeparatorWeight)));
        mTimeNetworkZoneLeft = mCurrentOrigin.x + (mWidthPerDay + mHourSeparatorWeight) * mLeftDayInWeekView + mTimeColumnWidth;
        // Draw the header row.
        drawHeaderRow(canvas);
        // Draw the time column and all the axes/separators.
        drawTimeColumn(canvas);
        //draw timeNetWorkZone
        mTimeNetworkZoneTop = mHeaderHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + mTimeTextHeight / 2 + mHeaderMarginBottom;
        drawTimeNetworkZone(canvas);
        //draw Events
        drawEvents(canvas);
    }


    private void drawTimeColumn(Canvas canvas) {
        // Draw the background color for the header column.
        canvas.drawRect(0, mHeaderHeight + mHeaderRowPadding * 2, mTimeColumnWidth, getHeight(), mHeaderColumnBackgroundPaint);
        // Clip to paint in left column only.
        canvas.clipRect(0, mHeaderHeight + mHeaderRowPadding * 2, mTimeColumnWidth, getHeight(), Region.Op.REPLACE);
        float top = mHeaderHeight + mHeaderRowPadding * 2 + mCurrentOrigin.y + mHeaderMarginBottom;
        for (int i = 0; i < 24; i++) {
            // Draw the text if its y position is not outside of the visible area. The pivot point of the text is the point at the bottom-right corner.
            String time = getDateTimeInterpreter().timeToString(i);
            if (time == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null time");
            else if (top < getHeight())
                canvas.drawText(time, mTimeTextWidth + mHeaderColumnPadding, top + mTimeTextHeight, mPaintTimeColumnText);
            top += mHourHeight;
        }
    }

    private void drawHeaderRow(Canvas canvas) {
        mHeaderHeight = mHeaderTextHeight * 2;
        //draw Header Background
        canvas.drawRect(0, 0, WeekView.this.getWidth(), mHeaderHeight + mHeaderRowPadding * 2, mHeaderBackgroundPaint);        // Clip to paint header row only.
        canvas.clipRect(mTimeColumnWidth, 0, getWidth(), mHeaderHeight + mHeaderRowPadding * 2, Region.Op.REPLACE);
        // Draw the header row texts.
        float timeNetworkZoneX = mTimeNetworkZoneLeft;
        float textY = mHeaderTextHeight + mHeaderRowPadding;
        for (int dayNumber = mLeftDayInWeekView; dayNumber <= mLeftDayInWeekView + mNumberOfVisibleDays; dayNumber++) {
            // Check if the day is today.
            Calendar day = (Calendar) mToday.clone();
            day.add(Calendar.DATE, dayNumber);
            boolean sameDay = Comparison.isSameDay(day, mToday);

            // Draw the day labels.
            String dayLabel = getDateTimeInterpreter().dateToString(day);
            if (dayLabel == null)
                throw new IllegalStateException("A DateTimeInterpreter must not return null date");
            else {
                drawText(dayLabel, timeNetworkZoneX + mWidthPerDay / 2, textY, sameDay ? mTodayHeaderTextPaint : mPaintHeaderTextPaint, canvas);
                timeNetworkZoneX += mWidthPerDay + mHourSeparatorWeight;
            }
        }

    }

    private void drawTimeNetworkZone(Canvas canvas) {
        canvas.clipRect(mTimeColumnWidth, mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2, getWidth(), getHeight(), Region.Op.REPLACE);
        float timeNetworkZoneX = mTimeNetworkZoneLeft;
        for (int dayNumber = mLeftDayInWeekView; dayNumber <= mLeftDayInWeekView + mNumberOfVisibleDays; dayNumber++) {
            Calendar day = (Calendar) mToday.clone();
            day.add(Calendar.DATE, dayNumber);

            // Draw background color for each day.
            float start = (timeNetworkZoneX < mTimeColumnWidth ? mTimeColumnWidth : timeNetworkZoneX);
            // Check if the day is today.
            boolean sameDay = Comparison.isSameDay(day, mToday);
            if (mWidthPerDay + mHourSeparatorWeight + timeNetworkZoneX - start > 0) {
                if (mShowDistinctPastFutureColor) {
                    boolean isWeekend = day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
                    Paint pastPaint = mPastBackgroundPaint;
                    Paint futurePaint = mFutureBackgroundPaint;
                    if (sameDay) {
                        Calendar now = Calendar.getInstance();
                        float beforeNow = (now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) / 60.0f) * mHourHeight;
                        canvas.drawRect(start, mTimeNetworkZoneTop, timeNetworkZoneX + mWidthPerDay, mTimeNetworkZoneTop + beforeNow, pastPaint);
                        canvas.drawRect(start, mTimeNetworkZoneTop + beforeNow, timeNetworkZoneX + mWidthPerDay, getHeight(), futurePaint);
                    } else if (day.before(mToday)) {
                        canvas.drawRect(start, mTimeNetworkZoneTop, timeNetworkZoneX + mWidthPerDay, getHeight(), pastPaint);
                    } else {
                        canvas.drawRect(start, mTimeNetworkZoneTop, timeNetworkZoneX + mWidthPerDay, getHeight(), futurePaint);
                    }
                } else {
                    canvas.drawRect(start, mTimeNetworkZoneTop, timeNetworkZoneX + mWidthPerDay, getHeight(), sameDay ? mTodayBackgroundPaint : mDayBackgroundPaint);
                }
            }
            //Draw column for hours
            canvas.drawLine(timeNetworkZoneX, mTimeNetworkZoneTop, timeNetworkZoneX, getHeight(), mHourSeparatorPaint);
            // Draw the line at the current time.
            if (mShowCurrentTimeLine && sameDay) {
                Calendar now = Calendar.getInstance();
                float currentTimeLineY = (now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE) / 60.0f) * mHourHeight;
                canvas.drawLine(start, mTimeNetworkZoneTop + currentTimeLineY, timeNetworkZoneX + mWidthPerDay, mTimeNetworkZoneTop + currentTimeLineY, mCurrentTimeLinePaint);
            }
            // In the next iteration, start from the next day.
            timeNetworkZoneX += mWidthPerDay + mHourSeparatorWeight;
        }
        // Draw the lines for hours.
        float lineY = mTimeNetworkZoneTop;
        for (int hourNumber = 0; hourNumber < 24; hourNumber++) {
            canvas.drawLine(0, lineY, getWidth(), lineY, mHourSeparatorPaint);
            lineY += mHourHeight + mHourSeparatorWeight;
        }

    }

    /**
     * Get the time and date where the user clicked on.
     *
     * @param x The x position of the touch event.
     * @param y The y position of the touch event.
     * @return The time and date at the clicked position.
     */
    private Calendar getTimeFromPoint(float x, float y) {
        int leftDaysWithGaps = (int) -(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mHourSeparatorWeight)));
        float startPixel = mCurrentOrigin.x + (mWidthPerDay + mHourSeparatorWeight) * leftDaysWithGaps +
                mTimeColumnWidth;
        for (int dayNumber = leftDaysWithGaps + 1;
             dayNumber <= leftDaysWithGaps + mNumberOfVisibleDays + 1;
             dayNumber++) {
            float start = (startPixel < mTimeColumnWidth ? mTimeColumnWidth : startPixel);
            if (mWidthPerDay + startPixel - start > 0 && x > start && x < startPixel + mWidthPerDay) {
                Calendar day = (Calendar) mToday.clone();
                day.add(Calendar.DATE, dayNumber - 1);
                float pixelsFromZero = y - mCurrentOrigin.y - mHeaderHeight
                        - mHeaderRowPadding * 2 - mTimeTextHeight / 2 - mHeaderMarginBottom;
                int hour = (int) (pixelsFromZero / mHourHeight);
                int minute = (int) (60 * (pixelsFromZero - hour * mHourHeight) / mHourHeight);
                day.add(Calendar.HOUR, hour);
                day.set(Calendar.MINUTE, minute);
                return day;
            }
            startPixel += mWidthPerDay + mHourSeparatorWeight;
        }
        return null;
    }

    /**
     * Draw all the events of a particular day.
     *
     * @param canvas The canvas to draw upon.
     */
    private void drawEvents(Canvas canvas) {
        canvas.clipRect(mTimeColumnWidth, mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom + mTimeTextHeight / 2, getWidth(), getHeight(), Region.Op.REPLACE);
        if (mEventsList != null && mEventsList.size() > 0) {
            float timeNetworkZoneX = mTimeNetworkZoneLeft;
            for (int dayNumber = mLeftDayInWeekView; dayNumber <= mLeftDayInWeekView + mNumberOfVisibleDays; dayNumber++) {
                Calendar day = (Calendar) mToday.clone();
                day.add(Calendar.DATE, dayNumber);

                for (List<EventRect> groupEvents : mEventsList) {
                    float j = 0;
                    for (EventRect event : groupEvents) {
                        if (Comparison.isSameDay(event.getEvent().getStartTime(), day)) {
                            // Calculate top.
                            float top = (mHourHeight + mHourSeparatorWeight) * (event.getEvent().getStartTime().get(Calendar.HOUR_OF_DAY) + event.getEvent().getStartTime().get(Calendar.MINUTE) / 60f) + mTimeNetworkZoneTop + mEventMarginVertical;
                            // Calculate bottom.
                            float bottom = (mHourHeight + mHourSeparatorWeight) * (event.getEvent().getEndTime().get(Calendar.HOUR_OF_DAY) + event.getEvent().getEndTime().get(Calendar.MINUTE) / 60f) + mTimeNetworkZoneTop - mEventMarginVertical;
                            // Calculate left and right.
                            float left = timeNetworkZoneX + j * mWidthPerDay / groupEvents.size() + mHourSeparatorWeight;
                            float right = left + 1f * mWidthPerDay / groupEvents.size();
                            /*Log.e("LEFT",""+left);
                            Log.e("RIGHT",""+right);
                            Log.e("TOP",""+top);
                            Log.e("BOTTOM",""+bottom);*/
                            // Draw the event and the event name on top of it.
                            if (left < right &&
                                    left < getWidth() &&
                                    top < getHeight() &&
                                    right > mTimeColumnWidth &&
                                    bottom > mHeaderHeight + mHeaderRowPadding * 2 + mTimeTextHeight / 2 + mHeaderMarginBottom
                                    ) {
                                if (event.getRectF() == null)
                                    event.setRectF(new RectF(left, top, right, bottom));
                                else event.getRectF().set(left, top, right, bottom);
                                if (event.getOriginalEvent().getBackgroundColor() == 0)
                                    mEventBackgroundPaint.setColor(mDefaultEventColor);
                                else
                                    mEventBackgroundPaint.setColor(event.getOriginalEvent().getBackgroundColor());
                                canvas.drawRoundRect(event.getRectF(), mEventCornerRadius, mEventCornerRadius, mEventBackgroundPaint);
                                drawEventTitle(event.getEvent(), event.getRectF(), canvas, top, left);
                            }
                        }
                        j++;

                    }
// In the next iteration, start from the next day.

                }
                timeNetworkZoneX += (mWidthPerDay + mHourSeparatorWeight);
            }
        }
    }

    /**
     * Draw the name of the event on top of the event rectangle.
     *
     * @param event        The event of which the title (and location) should be drawn.
     * @param rect         The rectangle on which the text is to be drawn.
     * @param canvas       The canvas to draw upon.
     * @param originalTop  The original top position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     * @param originalLeft The original left position of the rectangle. The rectangle may have some of its portion outside of the visible area.
     */
    private void drawEventTitle(Event event, RectF rect, Canvas canvas, float originalTop, float originalLeft) {
        if (rect.right - rect.left - mEventPadding * 2 < 0) return;
        if (rect.bottom - rect.top - mEventPadding * 2 < 0) return;
        // Prepare the name of the event.
        SpannableStringBuilder bob = new SpannableStringBuilder();
        if (event.getName() != null) {
            bob.append(event.getName());
            bob.setSpan(new StyleSpan(Typeface.BOLD), 0, bob.length(), 0);
            bob.append(' ');
        }

        // Prepare the location of the event.
        if (event.getDescription() != null) {
            bob.append("\n" + event.getDescription());
        }
        if (event.getTextColor() == 0)
            mEventTextPaint.setColor(mEventTextColor);
        else
            mEventTextPaint.setColor(event.getTextColor());
        int availableHeight = (int) (rect.bottom - originalTop - mEventPadding * 2);
        int availableWidth = (int) (rect.right - originalLeft - mEventPadding * 2);

        // Get text dimensions.
        StaticLayout textLayout = new StaticLayout(bob, mEventTextPaint, availableWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        int lineHeight = textLayout.getHeight() / textLayout.getLineCount();

        if (availableHeight >= lineHeight) {
            // Calculate available number of line counts.
            int availableLineCount = availableHeight / lineHeight;
            do {
                // Ellipsize text to fit into event rect.
                textLayout = new StaticLayout(TextUtils.ellipsize(bob, mEventTextPaint, availableLineCount * availableWidth, TextUtils.TruncateAt.END), mEventTextPaint, (int) (rect.right - originalLeft - mEventPadding * 2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // Reduce line count.
                availableLineCount--;

                // Repeat until text is short enough.
            } while (textLayout.getHeight() > availableHeight);

            // Draw text.
            canvas.save();
            canvas.translate(originalLeft + mEventPadding, originalTop + mEventPadding);
            textLayout.draw(canvas);
            canvas.restore();
        }
    }


    /**
     * A class to hold reference to the events and their visual representation. An EventRect is
     * actually the rectangle that is drawn on the calendar for a given event. There may be more
     * than one rectangle for a single event (an event that expands more than one day). In that
     * case two instances of the EventRect will be used for a single event. The given event will be
     * stored in "originalEvent". But the event that corresponds to rectangle the rectangle
     * instance will be stored in "event".
     */
    private class EventRect {
        private Event event;
        private Event originalEvent;
        private RectF rectF;

        /**
         * Create a new instance of event rect. An EventRect is actually the rectangle that is drawn
         * on the calendar for a given event. There may be more than one rectangle for a single
         * event (an event that expands more than one day). In that case two instances of the
         * EventRect will be used for a single event. The given event will be stored in
         * "originalEvent". But the event that corresponds to rectangle the rectangle instance will
         * be stored in "event".
         *
         * @param event         Represents the event which this instance of rectangle represents.
         * @param originalEvent The original event that was passed by the user.
         * @param rectF         The rectangle.
         */
        public EventRect(Event event, Event originalEvent, RectF rectF) {
            this.event = event;
            this.rectF = rectF;
            this.originalEvent = originalEvent;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public Event getOriginalEvent() {
            return originalEvent;
        }

        public void setOriginalEvent(Event originalEvent) {
            this.originalEvent = originalEvent;
        }

        public RectF getRectF() {
            return rectF;
        }

        public void setRectF(RectF rectF) {
            this.rectF = rectF;
        }
    }

    public void setEventList(ArrayList<Event> lst) {
        if (mEventsList == null) {
            mEventsList = new ArrayList<List<EventRect>>();
        }
        mEventsList.clear();
        for (Event event : lst) {
            addEvent(event);
        }
        notifyDatasetChanged();

    }

    public void clear() {
        if (mEventsList != null) {
            mEventsList.clear();
            invalidate();
        }
    }

    public boolean deleteEvent(Event e) {
        boolean returnValue = false;
        if (mEventsList != null) {
            for (List<EventRect> eventGroups : mEventsList) {
                int i = 0;
                while (i < eventGroups.size()) {
                    EventRect groupEvent = eventGroups.get(i);
                    if (groupEvent.getOriginalEvent().getId() == e.getId()) {
                        returnValue = true;
                        eventGroups.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        }
        if (returnValue) invalidate();
        return returnValue;
    }

    /**
     * Cache the event for smooth scrolling functionality.
     *
     * @param event The event to cache.
     */
    public void addEvent(Event event) {
        if (mEventsList == null) mEventsList = new ArrayList<List<EventRect>>();
        if (event.getStartTime().compareTo(event.getEndTime()) < 0) {
            List<Event> splitedEvents = event.splitWeekViewEvent();
            for (Event eventSpl : splitedEvents) {
                EventRect eventSplRect = new EventRect(eventSpl, event, null);
                boolean isPlaced = false;
                for (List<EventRect> eventGroups : mEventsList) {
                    boolean isBreak = false;
                    for (EventRect groupEvent : eventGroups) {
                        if (Comparison.isSameDay(groupEvent.getEvent().getStartTime(), eventSpl.getStartTime()) && groupEvent.getEvent().isCollideWith(eventSpl)) {
                            eventGroups.add(eventSplRect);
                            isPlaced = true;
                            isBreak = true;
                            break;
                        }
                    }
                    if (isBreak) break;
                }
                if (!isPlaced) {
                    List<EventRect> newGroup = new ArrayList<EventRect>();
                    newGroup.add(eventSplRect);
                    mEventsList.add(newGroup);
                }

            }
        }
    }

    /**
     * Sorts the events in ascending order.
     *
     * @param events The events to be sorted.
     */
    private void sortEvents(List<? extends Event> events) {
        if (events != null && events.size() > 0) {
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event event1, Event event2) {
                    long start1 = event1.getStartTime().getTimeInMillis();
                    long start2 = event2.getStartTime().getTimeInMillis();
                    int comparator = start1 > start2 ? 1 : (start1 < start2 ? -1 : 0);
                    if (comparator == 0) {
                        long end1 = event1.getEndTime().getTimeInMillis();
                        long end2 = event2.getEndTime().getTimeInMillis();
                        comparator = end1 > end2 ? 1 : (end1 < end2 ? -1 : 0);
                    }
                    return comparator;
                }
            });
        }
    }


    /**
     * Checks if time1 occurs after (or at the same time) time2.
     *
     * @param time1 The time to check.
     * @param time2 The time to check against.
     * @return true if time1 and time2 are equal or if time1 is after time2. Otherwise false.
     */
    private boolean isTimeAfterOrEquals(Calendar time1, Calendar time2) {
        return !(time1 == null || time2 == null) && time1.getTimeInMillis() >= time2.getTimeInMillis();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        mAreDimensionsInvalid = true;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to setting and getting the properties.
    //
    /////////////////////////////////////////////////////////////////

    public void setOnEventClickListener(EventClickListener listener) {
        this.mEventClickListener = listener;
    }

    public EventClickListener getEventClickListener() {
        return mEventClickListener;
    }


    public EventLongPressListener getEventLongPressListener() {
        return mEventLongPressListener;
    }

    public void setEventLongPressListener(EventLongPressListener eventLongPressListener) {
        this.mEventLongPressListener = eventLongPressListener;
    }

    public void setEmptyViewClickListener(EmptyViewClickListener emptyViewClickListener) {
        this.mEmptyViewClickListener = emptyViewClickListener;
    }

    public EmptyViewClickListener getEmptyViewClickListener() {
        return mEmptyViewClickListener;
    }

    public void setEmptyViewLongPressListener(EmptyViewLongPressListener emptyViewLongPressListener) {
        this.mEmptyViewLongPressListener = emptyViewLongPressListener;
    }

    public EmptyViewLongPressListener getEmptyViewLongPressListener() {
        return mEmptyViewLongPressListener;
    }

    private void drawText(String text, float fromX, float fromY, Paint mTextPaint, Canvas canvas) {
        for (String line : text.split("\n")) {
            canvas.drawText(line, fromX, fromY, mTextPaint);
            fromY += mTextPaint.descent() - mTextPaint.ascent();
        }
    }

    /**
     * Get the interpreter which provides the text to show in the header column and the header row.
     *
     * @return The date, time interpreter.
     */
    public DateTimeInterpreter getDateTimeInterpreter() {
        if (mDateTimeInterpreter == null) {
            mDateTimeInterpreter = new DateTimeInterpreter() {

                @Override
                public String dateToString(Calendar date) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d \nE", Locale.getDefault());/*new SimpleDateFormat(" M/dd\nEEEEE", Locale.getDefault()) :*/
                        return sdf.format(date.getTime()).toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }

                @Override
                public String timeToString(int hour) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, 0);

                    try {
                        SimpleDateFormat sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
                        return sdf.format(calendar.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            };
        }
        return mDateTimeInterpreter;
    }

    /**
     * Set the interpreter which provides the text to show in the header column and the header row.
     *
     * @param dateTimeInterpreter The date, time interpreter.
     */
    public void setDateTimeInterpreter(DateTimeInterpreter dateTimeInterpreter) {
        this.mDateTimeInterpreter = dateTimeInterpreter;

        // Refresh time column width.
        calculateTimeColumnWidth();
    }


    /**
     * Get the number of visible days in a week.
     *
     * @return The number of visible days in a week.
     */
    public int getNumberOfVisibleDays() {
        return mNumberOfVisibleDays;
    }

    /**
     * Set the number of visible days in a week.
     *
     * @param numberOfVisibleDays The number of visible days in a week.
     */
    public void setNumberOfVisibleDays(int numberOfVisibleDays) {
        this.mNumberOfVisibleDays = numberOfVisibleDays;
        mCurrentOrigin.x = 0;
        mCurrentOrigin.y = 0;
        invalidate();
    }

    public int getHourHeight() {
        return mHourHeight;
    }

    public void setHourHeight(int hourHeight) {
        mHourHeight = hourHeight;
        invalidate();
    }

    public int getFirstDayOfWeek() {
        return mFirstDayOfWeek;
    }

    /**
     * Set the first day of the week. First day of the week is used only when the week view is first
     * drawn. It does not of any effect after user starts scrolling horizontally.
     * <p>
     * <b>Note:</b> This method will only work if the week view is set to display more than 6 days at
     * once.
     * </p>
     *
     * @param firstDayOfWeek The supported values are {@link Calendar#SUNDAY},
     *                       {@link Calendar#MONDAY}, {@link Calendar#TUESDAY},
     *                       {@link Calendar#WEDNESDAY}, {@link Calendar#THURSDAY},
     *                       {@link Calendar#FRIDAY}.
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek;
        invalidate();
    }

    public boolean isShowFirstDayOfWeekFirst() {
        return mShowFirstDayOfWeekFirst;
    }

    public void setShowFirstDayOfWeekFirst(boolean show) {
        mShowFirstDayOfWeekFirst = show;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTodayHeaderTextPaint.setTextSize(mTextSize);
        mPaintHeaderTextPaint.setTextSize(mTextSize);
        mPaintTimeColumnText.setTextSize(mTextSize);
        invalidate();
    }

    public int getHeaderColumnPadding() {
        return mHeaderColumnPadding;
    }

    public void setHeaderColumnPadding(int headerColumnPadding) {
        mHeaderColumnPadding = headerColumnPadding;
        invalidate();
    }

    public int getHeaderTextColor() {
        return mHeaderTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        mHeaderTextColor = headerTextColor;
        mPaintHeaderTextPaint.setColor(mHeaderTextColor);
        invalidate();
    }

    public int getHeaderRowPadding() {
        return mHeaderRowPadding;
    }

    public void setHeaderRowPadding(int headerRowPadding) {
        mHeaderRowPadding = headerRowPadding;
        invalidate();
    }

    public int getHeaderRowBackgroundColor() {
        return mHeaderRowBackgroundColor;
    }

    public void setHeaderRowBackgroundColor(int headerRowBackgroundColor) {
        mHeaderRowBackgroundColor = headerRowBackgroundColor;
        mHeaderBackgroundPaint.setColor(mHeaderRowBackgroundColor);
        invalidate();
    }

    public int getDayBackgroundColor() {
        return mDayBackgroundColor;
    }

    public void setDayBackgroundColor(int dayBackgroundColor) {
        mDayBackgroundColor = dayBackgroundColor;
        mDayBackgroundPaint.setColor(mDayBackgroundColor);
        invalidate();
    }

    public int getHourSeparatorColor() {
        return mHourSeparatorColor;
    }

    public void setHourSeparatorColor(int hourSeparatorColor) {
        mHourSeparatorColor = hourSeparatorColor;
        mHourSeparatorPaint.setColor(mHourSeparatorColor);
        invalidate();
    }

    public int getTodayBackgroundColor() {
        return mTodayBackgroundColor;
    }

    public void setTodayBackgroundColor(int todayBackgroundColor) {
        mTodayBackgroundColor = todayBackgroundColor;
        mTodayBackgroundPaint.setColor(mTodayBackgroundColor);
        invalidate();
    }

    public int getTodayHeaderTextColor() {
        return mTodayHeaderTextColor;
    }

    public void setTodayHeaderTextColor(int todayHeaderTextColor) {
        mTodayHeaderTextColor = todayHeaderTextColor;
        mTodayHeaderTextPaint.setColor(mTodayHeaderTextColor);
        invalidate();
    }

    public int getEventTextSize() {
        return mEventTextSize;
    }

    public void setEventTextSize(int eventTextSize) {
        mEventTextSize = eventTextSize;
        mEventTextPaint.setTextSize(mEventTextSize);
        invalidate();
    }

    public int getEventTextColor() {
        return mEventTextColor;
    }

    public void setEventTextColor(int eventTextColor) {
        mEventTextColor = eventTextColor;
        mEventTextPaint.setColor(mEventTextColor);
        invalidate();
    }

    public int getEventPadding() {
        return mEventPadding;
    }

    public void setEventPadding(int eventPadding) {
        mEventPadding = eventPadding;
        invalidate();
    }

    public Calendar getFirstVisibleDay() {
        // Iterate through each day.
        Calendar calendar = (Calendar) mToday.clone();
        calendar.add(Calendar.DATE, (int) (-(Math.ceil(mCurrentOrigin.x / (mWidthPerDay + mHourSeparatorWeight)))));
        return calendar;
    }

    public int getHeaderColumnBackgroundColor() {
        return mHeaderColumnBackgroundColor;
    }

    public void setHeaderColumnBackgroundColor(int headerColumnBackgroundColor) {
        mHeaderColumnBackgroundColor = headerColumnBackgroundColor;
        mHeaderColumnBackgroundPaint.setColor(mHeaderColumnBackgroundColor);
        invalidate();
    }

    public int getDefaultEventColor() {
        return mDefaultEventColor;
    }

    public void setDefaultEventColor(int defaultEventColor) {
        mDefaultEventColor = defaultEventColor;
        invalidate();
    }

    public int getEventCornerRadius() {
        return mEventCornerRadius;
    }

    /**
     * Set corner radius for event rect.
     *
     * @param eventCornerRadius the radius in px.
     */
    public void setEventCornerRadius(int eventCornerRadius) {
        mEventCornerRadius = eventCornerRadius;
    }

    public int getEventMarginVertical() {
        return mEventMarginVertical;
    }

    /**
     * Set the top and bottom margin of the event. The event will release this margin from the top
     * and bottom edge. This margin is useful for differentiation consecutive events.
     *
     * @param eventMarginVertical The top and bottom margin.
     */
    public void setEventMarginVertical(int eventMarginVertical) {
        this.mEventMarginVertical = eventMarginVertical;
        invalidate();
    }

    /**
     * Get the scrolling speed factor in horizontal direction.
     *
     * @return The speed factor in horizontal direction.
     */
    public float getXScrollingSpeed() {
        return mXScrollingSpeed;
    }

    /**
     * Sets the speed for horizontal scrolling.
     *
     * @param xScrollingSpeed The new horizontal scrolling speed.
     */
    public void setXScrollingSpeed(float xScrollingSpeed) {
        this.mXScrollingSpeed = xScrollingSpeed;
    }


    /**
     * Whether past and future days should have two different background colors. The past and
     * future day colors are defined by the attributes `futureBackgroundColor` and
     * `pastBackgroundColor`.
     *
     * @return True if past and future days should have two different background colors.
     */
    public boolean isShowDistinctPastFutureColor() {
        return mShowDistinctPastFutureColor;
    }

    /**
     * Set whether weekends should have a background color different from the normal day background
     * color. The past and future day colors are defined by the attributes `futureBackgroundColor`
     * and `pastBackgroundColor`.
     *
     * @param showDistinctPastFutureColor True if past and future should have two different
     *                                    background colors.
     */
    public void setShowDistinctPastFutureColor(boolean showDistinctPastFutureColor) {
        this.mShowDistinctPastFutureColor = showDistinctPastFutureColor;
        invalidate();
    }

    /**
     * Get whether the week view should fling horizontally.
     *
     * @return True if the week view has horizontal fling enabled.
     */
    public boolean isHorizontalFlingEnabled() {
        return mHorizontalFlingEnabled;
    }

    /**
     * Set whether the week view should fling horizontally.
     *
     * @return True if it should have horizontal fling enabled.
     */
    public void setHorizontalFlingEnabled(boolean enabled) {
        mHorizontalFlingEnabled = enabled;
    }

    /**
     * Get whether the week view should fling vertically.
     *
     * @return True if the week view has vertical fling enabled.
     */
    public boolean isVerticalFlingEnabled() {
        return mVerticalFlingEnabled;
    }

    /**
     * Set whether the week view should fling vertically.
     *
     * @return True if it should have vertical fling enabled.
     */
    public void setVerticalFlingEnabled(boolean enabled) {
        mVerticalFlingEnabled = enabled;
    }

    /**
     * Get the height of AllDay-events.
     *
     * @return Height of AllDay-events.
     */
    public int getAllDayEventHeight() {
        return mAllDayEventHeight;
    }

    /**
     * Set the height of AllDay-events.
     */
    public void setAllDayEventHeight(int height) {
        mAllDayEventHeight = height;
    }

    /**
     * Get scroll duration
     *
     * @return scroll duration
     */
    public float getScrollDuration() {
        return mScrollDuration;
    }

    /**
     * Set the scroll duration
     */
    public void setScrollDuration(int scrollDuration) {
        mScrollDuration = scrollDuration;
    }

    /////////////////////////////////////////////////////////////////
    //
    //      Functions related to scrolling.
    //
    /////////////////////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mScaleDetector.onTouchEvent(event);
        }
        boolean val = mGestureDetector.onTouchEvent(event);

        // Check after call of mGestureDetector, so mCurrentFlingDirection and mCurrentScrollDirection are set.
        if (event.getAction() == MotionEvent.ACTION_UP && !mIsZooming && mCurrentFlingDirection == Direction.NONE) {
            if (mCurrentScrollDirection == Direction.RIGHT || mCurrentScrollDirection == Direction.LEFT) {
                goToNearestOrigin();
            }
            mCurrentScrollDirection = Direction.NONE;
        }

        return val;
    }

    private void goToNearestOrigin(float dx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            int nearestOrigin = 0;
            double leftDays = 0;
            float scrollSpace = 0;
            if (isScrollPage) {
                scrollSpace = (mWidthPerDay + mHourSeparatorWeight) * mNumberOfVisibleDays;
            } else {
                scrollSpace = mWidthPerDay + mHourSeparatorWeight;
            }

            //scroll calculate
            float realPixcel = mCurrentOrigin.x - mDifferentPixcel;
            leftDays = Math.round(realPixcel / scrollSpace);
            nearestOrigin = (int) (realPixcel - leftDays * scrollSpace + dx);
            if (nearestOrigin != 0) {
                // Stop current animation.
                mScroller.forceFinished(true);
                // Snap to date.
                mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, -nearestOrigin, 0, (int) (Math.abs(dx) * mScrollDuration));
                ViewCompat.postInvalidateOnAnimation(WeekView.this);
            }

        } else {
            Toast.makeText(mContext, "Can't not do this action", Toast.LENGTH_SHORT).show();
        }

        // Reset scrolling and fling direction.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }

    private void goToNearestOrigin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            int nearestOrigin = 0;
            double leftDays = 0;
            float scrollSpace = 0;
            if (isScrollPage) {
                scrollSpace = (mWidthPerDay + mHourSeparatorWeight) * mNumberOfVisibleDays;
            } else {
                scrollSpace = mWidthPerDay + mHourSeparatorWeight;
            }

            //scroll calculate
            float realPixcel = mCurrentOrigin.x - mDifferentPixcel;
            leftDays = Math.round(realPixcel / scrollSpace);
            nearestOrigin = (int) (realPixcel - leftDays * scrollSpace);
            if (nearestOrigin != 0) {
                // Stop current animation.
                mScroller.forceFinished(true);
                // Snap to date.
                mScroller.startScroll((int) mCurrentOrigin.x, (int) mCurrentOrigin.y, -nearestOrigin, 0, (int) (Math.abs(nearestOrigin) * mScrollDuration));
                ViewCompat.postInvalidateOnAnimation(WeekView.this);
            }

        } else {
            Toast.makeText(mContext, "Can't not do this action", Toast.LENGTH_SHORT).show();
        }

        // Reset scrolling and fling direction.
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (mScroller.isFinished()) {
                if (mCurrentFlingDirection != Direction.NONE) {
                    // Snap to day after fling is finished.
                    goToNearestOrigin();
                }
            } else {
                if (mCurrentFlingDirection != Direction.NONE && forceFinishScroll()) {
                    goToNearestOrigin();
                } else if (mScroller.computeScrollOffset()) {
                    mCurrentOrigin.y = mScroller.getCurrY();
                    mCurrentOrigin.x = mScroller.getCurrX();
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
        }

    }

    /**
     * Check if scrolling should be stopped.
     *
     * @return true if scrolling should be stopped before reaching the end of animation.
     */
    private boolean forceFinishScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // current velocity only available since api 14
            return mScroller.getCurrVelocity() <= mMinimumFlingVelocity;
        } else {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    //
    //      Public methods.
    //
    /////////////////////////////////////////////////////////////////

    /**
     * Show today on the week view.
     */
    public void goToToday() {
        Calendar today = Calendar.getInstance();
        goToDate(today);
    }

    /**
     * Show a specific day on the week view.
     *
     * @param date The date to show.
     */
    public void goToDate(Calendar date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mScroller.forceFinished(true);
        }
        mCurrentScrollDirection = mCurrentFlingDirection = Direction.NONE;

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long day = 1000L * 60L * 60L * 24L;
        long dateInMillis = date.getTimeInMillis() + date.getTimeZone().getOffset(date.getTimeInMillis());
        long todayInMillis = today.getTimeInMillis() + today.getTimeZone().getOffset(today.getTimeInMillis());
        long dateDifference = (dateInMillis / day) - (todayInMillis / day);
        mCurrentOrigin.x = -dateDifference * (mWidthPerDay + mHourSeparatorWeight);
        invalidate();
    }

    /**
     * Refreshes the view and loads the events again.
     */
    public void notifyDatasetChanged() {
        invalidate();
    }

    /**
     * Vertically scroll to a specific hour in the week view.
     *
     * @param hour The hour to scroll to in 24-hour format. Supported values are 0-24.
     */
    public void goToHour(double hour) {
        if (mAreDimensionsInvalid) {
            mScrollToHour = hour;
            return;
        }

        int verticalOffset = 0;
        if (hour > 24)
            verticalOffset = mHourHeight * 24;
        else if (hour > 0)
            verticalOffset = (int) (mHourHeight * hour);

        if (verticalOffset > mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom)
            verticalOffset = (int) (mHourHeight * 24 - getHeight() + mHeaderHeight + mHeaderRowPadding * 2 + mHeaderMarginBottom);

        mCurrentOrigin.y = -verticalOffset;
        invalidate();
    }

    /**
     * Get the first hour that is visible on the screen.
     *
     * @return The first hour that is visible.
     */
    public double getFirstVisibleHour() {
        return -mCurrentOrigin.y / mHourHeight;
    }


/////////////////////////////////////////////////////////////////
//
//      Interfaces.
//
/////////////////////////////////////////////////////////////////

    public interface EventClickListener {
        /**
         * Triggered when clicked on one existing event
         *
         * @param event:     event clicked.
         * @param eventRect: view containing the clicked event.
         */
        void onEventClick(Event event, RectF eventRect);
    }

    public interface EventLongPressListener {
        /**
         * Similar to {@link EventClickListener} but with a long press.
         *
         * @param event:     event clicked.
         * @param eventRect: view containing the clicked event.
         */
        void onEventLongPress(Event event, RectF eventRect);
    }

    public interface EmptyViewClickListener {
        /**
         * Triggered when the users clicks on a empty space of the calendar.
         *
         * @param time: {@link Calendar} object set with the date and time of the clicked position on the view.
         */
        void onEmptyViewClicked(Calendar time);
    }

    public interface EmptyViewLongPressListener {
        /**
         * Similar to {@link EmptyViewClickListener} but with long press.
         *
         * @param time: {@link Calendar} object set with the date and time of the long pressed position on the view.
         */
        void onEmptyViewLongPress(Calendar time);
    }
}
