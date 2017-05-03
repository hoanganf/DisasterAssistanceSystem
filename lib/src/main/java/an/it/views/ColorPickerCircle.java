package an.it.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import an.it.R;
import an.it.utils.Convert;


public class ColorPickerCircle extends View {
    private Paint mPaint;
    private Paint mCenterPaint;
    private final int[] mColors;
    private int mWidth;
    private float mChooseStroke;
    private float mSpace;
    private float mCenterRadius;
    private float mPadding;

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }
    private OnColorChangedListener mListener;

    public ColorPickerCircle(Context c, AttributeSet attrs) {
        super(c);


        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPicker);
            mChooseStroke = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerChooseStroke, Convert.PixelFromDp(c,32));
            mCenterRadius = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerCircleRadius, Convert.PixelFromDp(c,22));
            mPadding = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerPadding, Convert.PixelFromDp(c,5));
            mSpace = a.getDimensionPixelSize(R.styleable.ColorPicker_pickerSpace, Convert.PixelFromDp(c,10));
            mCenterPaint.setColor(a.getColor(R.styleable.ColorPicker_pickerColor, Color.BLUE));
            a.recycle();
        }
        mColors = new int[] {
                0x00000000,0xFFFFFFFF,0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000,0xFFFFFFFF,0x00000000
        };

        Shader s = new SweepGradient(0, 0, mColors, null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mChooseStroke);


        mWidth=(int)(mCenterRadius+mChooseStroke+mSpace+mPadding);

    }

    private boolean mTrackingCenter;
    private boolean mHighlightCenter;

    @Override
    protected void onDraw(Canvas canvas) {
        float r =mCenterRadius+mChooseStroke-mPadding;
        Log.e("WIDTH",getWidth()+"");
        Log.e("WIDTH",getHeight()+"");
        canvas.translate(mWidth,mWidth);

        canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
        canvas.drawCircle(0, 0, mCenterRadius, mCenterPaint);

        if (mTrackingCenter) {
            int c = mCenterPaint.getColor();
            mCenterPaint.setStyle(Paint.Style.STROKE);

            if (mHighlightCenter) {
                mCenterPaint.setAlpha(0xFF);
            } else {
                mCenterPaint.setAlpha(0x80);
            }
            canvas.drawCircle(0, 0,
                    mCenterRadius + mCenterPaint.getStrokeWidth(),
                    mCenterPaint);

            mCenterPaint.setStyle(Paint.Style.FILL);
            mCenterPaint.setColor(c);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth*2, mWidth*2);
    }


    private int floatToByte(float x) {
        int n = Math.round(x);
        return n;
    }
    private int pinToByte(int n) {
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        return n;
    }

    private int ave(int s, int d, float p) {
        return s + Math.round(p * (d - s));
    }

    private int interpColor(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int)p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i+1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    private int rotateColor(int color, float rad) {
        float deg = rad * 180 / 3.1415927f;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        ColorMatrix cm = new ColorMatrix();
        ColorMatrix tmp = new ColorMatrix();

        cm.setRGB2YUV();
        tmp.setRotate(0, deg);
        cm.postConcat(tmp);
        tmp.setYUV2RGB();
        cm.postConcat(tmp);

        final float[] a = cm.getArray();

        int ir = floatToByte(a[0] * r +  a[1] * g +  a[2] * b);
        int ig = floatToByte(a[5] * r +  a[6] * g +  a[7] * b);
        int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);

        return Color.argb(Color.alpha(color), pinToByte(ir),
                pinToByte(ig), pinToByte(ib));
    }

    private static final float PI = 3.1415926f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - mWidth;
        float y = event.getY() - mWidth;
        boolean inCenter = Math.sqrt(x*x + y*y) <= mCenterRadius;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTrackingCenter = inCenter;
                if (inCenter) {
                    mHighlightCenter = true;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter;
                        invalidate();
                    }
                } else {
                    float angle = (float) Math.atan2(y, x);
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    float unit = angle/(2*PI);
                    if (unit < 0) {
                        unit += 1;
                    }
                    mCenterPaint.setColor(interpColor(mColors, unit));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTrackingCenter) {
                    if (inCenter) {
                        if(mListener!=null)mListener.colorChanged(mCenterPaint.getColor());
                    }
                    mTrackingCenter = false;    // so we draw w/o halo
                    invalidate();
                }
                break;
        }
        return true;
    }
}