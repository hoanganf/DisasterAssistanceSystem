package an.it.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import an.it.R;
import an.it.utils.Convert;

/**
 * Created by hoang on 6/17/2016.
 */
public class ColorPickerSquare extends View {
    private Context mContext;
    private int mCurrentColor;
    private float mCircleRadius;
    private float mPadding;
    private float mHueBarWeight;
    private float mChoosedColorBarWidth;
    private float mChooseColorZoneLeft;
    private float mChooseColorZoneRight;
    private int mChooseColorZoneWidth;
    private int mChooseColorZoneHeight;


    private Paint mSatValPaint;
    private Paint mSatValTrackerPaint;

    private Paint mHuePaint;
    private Paint mHueTrackerPaint;

    private Paint mAlphaPaint;
    private Paint mAlphaTextPaint;
    private Paint mCurrentColorPaint;

    private Shader mValShader;
    private Shader mSatShader;
    private Shader mHueShader;
    private Shader mAlphaShader;

    private int mAlpha = 0xff;
    private float mCurrentHue = 360f;
    private float mSat = 0f;
    private float mVal = 0f;

    private String mAlphaSliderText = "";
    private int mSliderTrackerColor = 0xff1c1c1c;
    private int mBorderColor = 0xff6E6E6E;

    private RectF mSatValRect;
    private RectF mHueRect;
    private RectF mAlphaRect;
    private int[] mHueColors;
    private LastTouchedPanel mLastTouchedPanel;

    private enum LastTouchedPanel {
        HUE, SAT_VAL, ALPHA
    }

    public ColorPickerSquare(Context c, AttributeSet attrs) {
        super(c,attrs);
        this.mContext = c;
        // Get the current hue from the current color and update the main
        // color field
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPicker);
            mCircleRadius = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerCircleRadius, Convert.PixelFromDp(c, 10));
            mCurrentColor = (a.getColor(R.styleable.ColorPicker_pickerColor, Color.RED));
            mPadding = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerPadding, Convert.PixelFromDp(c, 10));
            mHueBarWeight = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerHueBarWeight, Convert.PixelFromDp(c, 38));
            mChoosedColorBarWidth = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerChoosedColorBarWidth, Convert.PixelFromDp(c, 22));
            a.recycle();
        }

        mChooseColorZoneLeft = mPadding + mChoosedColorBarWidth;
        initPaintTools();

    }
    private void initPaintTools() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mSatValPaint = new Paint();
        mSatValTrackerPaint = new Paint();
        mHuePaint = new Paint();
        mHueTrackerPaint = new Paint();
        mAlphaPaint = new Paint();
        mAlphaTextPaint = new Paint();
        mCurrentColorPaint = new Paint();


        mSatValTrackerPaint.setStyle(Paint.Style.STROKE);
        mSatValTrackerPaint.setStrokeWidth(Convert.PixelFromDp(mContext, 2));
        mSatValTrackerPaint.setAntiAlias(true);

        mHueTrackerPaint.setColor(mSliderTrackerColor);
        mHueTrackerPaint.setStyle(Paint.Style.STROKE);
        mHueTrackerPaint.setStrokeWidth(Convert.PixelFromDp(mContext, 2));
        mHueTrackerPaint.setAntiAlias(true);

        mCurrentColorPaint.setColor(mCurrentColor);
        mAlphaTextPaint.setColor(0xff1c1c1c);
        mAlphaTextPaint.setTextSize(Convert.SpFromPixel(mContext, 12));
        mAlphaTextPaint.setAntiAlias(true);
        mAlphaTextPaint.setTextAlign(Paint.Align.CENTER);
        mAlphaTextPaint.setFakeBoldText(true);


    }

    private void drawHue(Canvas canvas) {
        if (mHueColors == null) {
            mHueColors = new int[361];
            int count = 0;
            for (int i = mHueColors.length - 1; i >= 0; i--, count++) {
                mHueColors[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
            }
            float left = getWidth() - mHueBarWeight - mPadding;
            float top = mPadding;
            float bottom = getHeight() - mPadding;
            float right = getWidth() - mPadding;
            if (mHueRect == null) {
                mHueRect = new RectF(left, top, right, bottom);
            } else mHueRect.set(left, top, right, bottom);
        }

        if (mHueShader == null) {
            mHueShader = new LinearGradient(mHueRect.left, mHueRect.top, mHueRect.left, mHueRect.bottom, mHueColors, null, Shader.TileMode.CLAMP);
            mHuePaint.setShader(mHueShader);
        }

        canvas.drawRect(mHueRect, mHuePaint);
//draw choose point
        float m2PXFromDP = Convert.PixelFromDp(mContext, 2);
        float mCurrentHueY = (int) (mHueRect.height() - (mCurrentHue * mHueRect.height() / 360f) + mHueRect.top);
        //   p.x = (int) rect.left;

        RectF r = new RectF();
        r.left = mHueRect.left;
        r.right = mHueRect.right;
        r.top = mCurrentHueY - m2PXFromDP;
        r.bottom = mCurrentHueY + m2PXFromDP;
        canvas.drawRoundRect(r, m2PXFromDP, m2PXFromDP, mHueTrackerPaint);
    }

    private void drawChoosedColor(Canvas canvas) {
        mCurrentColorPaint.setColor(mCurrentColor);
        canvas.drawRect(mPadding,mPadding,mPadding+mChoosedColorBarWidth,getHeight()-mPadding, mCurrentColorPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Display all the colors of the hue bar with lines
        mChooseColorZoneRight = getWidth() - mPadding;
        mChooseColorZoneWidth = (int) (mChooseColorZoneRight - mChooseColorZoneLeft);
        mChooseColorZoneHeight = (int) (getHeight() - mPadding - mPadding - mPadding);
        drawHue(canvas);
        // Display the main field colors using LinearGradient
        //drawChooseColor(canvas);
        drawSatVal(canvas);
        drawChoosedColor(canvas);

       /* // Set the text color according to the brightness of the color
        if (Color.red(mCurrentColor) + Color.green(mCurrentColor) + Color.blue(mCurrentColor) < 384)
            mPaint.setColor(Color.WHITE);
        else
            mPaint.setColor(Color.BLACK);
        canvas.drawText("Pick", 74, 340, mPaint);*/

        // Draw a 'button' with the default color
/*        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDefaultColor);
        canvas.drawRect(138, 316, 266, 356, mPaint);*/

     /*   // Set the text color according to the brightness of the color
        if (Color.red(mDefaultColor) + Color.green(mDefaultColor)
                + Color.blue(mDefaultColor) < 384)
            mPaint.setColor(Color.WHITE);
        else
            mPaint.setColor(Color.BLACK);
        canvas.drawText("Pick", 202, 340,
                mPaint);*/
    }

    private void drawSatVal(Canvas canvas) {
        if (mSatValRect == null) {
            float left = mPadding * 2 + mChoosedColorBarWidth;
            float top = mPadding;
            float bottom = getHeight() - mPadding;
            float right = getWidth() - mPadding * 2 - mHueBarWeight;
            mSatValRect = new RectF(left, top, right, bottom);
        }

        if (mValShader == null) {
            mValShader = new LinearGradient(mSatValRect.left, mSatValRect.top, mSatValRect.left, mSatValRect.bottom,
                    0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        }

        int rgb = Color.HSVToColor(new float[]{mCurrentHue, 1f, 1f});
        mSatShader = new LinearGradient(mSatValRect.left, mSatValRect.top, mSatValRect.right, mSatValRect.top,
                0xffffffff, rgb, Shader.TileMode.CLAMP);
        ComposeShader mShader = new ComposeShader(mValShader, mSatShader, PorterDuff.Mode.MULTIPLY);
        mSatValPaint.setShader(mShader);

        canvas.drawRect(mSatValRect, mSatValPaint);

        Point p = new Point((int) (mSat * mSatValRect.width() + mSatValRect.left), (int) ( mVal* mSatValRect.height() + mSatValRect.top));

        mSatValTrackerPaint.setColor(0xff000000);
        canvas.drawCircle(p.x, p.y, mCircleRadius - Convert.PixelFromDp(mContext, 2), mSatValTrackerPaint);

        mSatValTrackerPaint.setColor(0xffdddddd);
        canvas.drawCircle(p.x, p.y, mCircleRadius, mSatValTrackerPaint);

    }

    private boolean moveTrackersIfNeeded(MotionEvent event) {
        boolean update = false;
        float startX = event.getX();
        float startY = event.getY();
        if (mHueRect.contains(startX, startY)) {
            mLastTouchedPanel = LastTouchedPanel.HUE;
            mCurrentHue = 360f - ((startY - mHueRect.top) * 360f / mHueRect.height());
            update = true;
        } else if (mSatValRect.contains(startX, startY)) {
            mLastTouchedPanel = LastTouchedPanel.ALPHA;
            mSat = (startX-mSatValRect.left) / mSatValRect.width();
            mVal = (startY-mSatValRect.top) / mSatValRect.height();
            update = true;
        } else if (mAlphaRect != null && mAlphaRect.contains(startX, startY))

        {

          /*  mLastTouchedPanel = PANEL_ALPHA;

            mAlpha = pointToAlpha((int) event.getX());

            update = true;*/
        }

        return update;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean update = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                update = moveTrackersIfNeeded(event);
                break;
            case MotionEvent.ACTION_MOVE:
                update = moveTrackersIfNeeded(event);
                break;
            case MotionEvent.ACTION_UP:
                update = moveTrackersIfNeeded(event);
                break;
        }
        if (update) {

            /*if (mListener != null) {
                mListener.onColorChanged(Color.HSVToColor(mAlpha, new float[]{mHue, mSat, mVal}));
            }*/
            Log.e("SAT",""+Float.toString(mSat));
            Log.e("Val",""+Float.toString(1f-mVal));
            mCurrentColor=Color.HSVToColor(mAlpha,new float[]{mCurrentHue, mSat, 1f-mVal});

            invalidate();
            return true;
        }


        return super.onTouchEvent(event);
    }

    public int getCurrentColor() {
        return mCurrentColor;
    }

    public void setCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
        float[] hsv = new float[3];
        Color.colorToHSV(mCurrentColor, hsv);
        mCurrentHue = hsv[0];
        mSat = hsv[1];
        mVal = 1f - hsv[2];
        invalidate();
    }
}