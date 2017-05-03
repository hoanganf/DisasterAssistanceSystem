package an.it.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import an.it.R;


public class Convert {
    //convert DB to pixel
    public static int PixelFromDp(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int DpFromPixel(Context context, int pixel) {
        return (int) (pixel / context.getResources().getDisplayMetrics().density);
    }

    public static int SpFromPixel(Context context, int pixel) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pixel, context.getResources().getDisplayMetrics());
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Drawable decodeSampledBitmapFromResource(Resources res, int resId,
                                                           int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return new BitmapDrawable(res,  BitmapFactory.decodeResource(res, resId, options));
    }

    public static Drawable getDrawable(Context ctx,int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return ctx.getResources().getDrawable(id, ctx.getTheme());
        } else return ctx.getResources().getDrawable(id);
    }
    public static void setBackGround(Context context, View view, int id){
        if (Build.VERSION.SDK_INT >= 21) {
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_white_pressed, context.getTheme()));
        } else if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_white_pressed));
        } else view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_white_pressed));
    }
    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Junly";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

}
