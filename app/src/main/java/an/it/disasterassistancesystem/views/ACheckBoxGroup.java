package an.it.disasterassistancesystem.views;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import an.it.R;
import an.it.utils.Convert;
import an.it.views.ACheckBox;

public class ACheckBoxGroup extends LinearLayout implements View.OnClickListener {
    private ArrayList<String> mCheckList;
    private boolean[] mCheck;
    private int mPadding;
    private int mHeight;
    private AsyncTask<Void, Void, Boolean> mAST = null;
    private OnAllCheckListener onAllCheckListener;

    public ACheckBoxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
        mPadding = Convert.PixelFromDp(getContext(), 2);
        mHeight = Convert.PixelFromDp(getContext(), 42);
    }

    public void setCheckList(ArrayList<String> checkList) {
        mCheckList = checkList;
        if (mCheckList != null && mCheckList.size() > 0) {
            mCheck = new boolean[checkList.size()];
            int pos = 0;
            int i = 0;
            while (pos < checkList.size()) {
                mCheck[pos] = false;
                View v = null;
                if (getChildCount() > i) {
                    v = getChildView(pos, getChildAt(i));
                } else {
                    v = getChildView(pos, null);
                    addView(v);
                    addView(getHorizontalLine());
                }
                pos++;
                i += 2;
            }
            while (i > 0 && getChildCount() > i - 1) {
                removeViewAt(i - 1);
            }
        } else {
            removeAllViews();
            mCheck = null;
        }
    }

    private View getChildView(final int pos, View v) {
        LinearLayout ln = (LinearLayout) v;
        if (ln == null) {
            ln = new LinearLayout(getContext());
            ln.setOrientation(LinearLayout.HORIZONTAL);
            ln.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ln.setPadding(mPadding, mPadding, mPadding, mPadding);
            ln.setGravity(Gravity.CENTER_VERTICAL);

            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
            tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tv.setSelected(true);
            tv.setSingleLine(true);
            tv.setText(mCheckList.get(pos));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextColor(getContext().getColor(R.color.gray_dark));
            } else tv.setTextColor(getContext().getResources().getColor(R.color.gray_dark));

            ACheckBox acb = new ACheckBox(getContext(), null);
            acb.setLayoutParams(new LayoutParams(mHeight, mHeight));
            int padding = Convert.PixelFromDp(getContext(), 8);
            acb.setPadding(padding, padding, padding, padding);
            acb.setOnCheckedChangeListener(new ACheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(View var1, boolean var2) {
                    mCheck[pos] = var2;
                    checkAllCheck();
                }
            });
            ln.addView(tv);
            ln.addView(acb);
            Convert.setBackGround(getContext(), ln, R.drawable.bg_white_pressed);
            ln.setClickable(true);
            ln.setTag(pos);
            ln.setOnClickListener(this);
        } else {
            ((TextView) ln.getChildAt(0)).setText(mCheckList.get(pos));
        }
        return ln;
    }


    private ImageView getHorizontalLine() {
        ImageView horizontalLine = new ImageView(getContext());
        horizontalLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Convert.PixelFromDp(getContext(), 1)));
        horizontalLine.setBackgroundColor(Color.parseColor("#cfcfd6"));
        return horizontalLine;
    }


    @Override
    public void onClick(View view) {
        int pos = Integer.parseInt(view.getTag().toString());
        ACheckBox checkBox = (ACheckBox) ((LinearLayout) getChildAt(pos * 2)).getChildAt(1);
        checkBox.setChecked(!checkBox.isChecked());
        mCheck[pos] = checkBox.isChecked();
        checkAllCheck();
    }

    public void setCheckAll(boolean check) {
        if (getChildCount() > 2) {
            int j = 0;
            for (int i = 0; i < getChildCount(); i += 2) {
                ACheckBox checkBox = (ACheckBox) ((LinearLayout) getChildAt(i)).getChildAt(1);
                checkBox.setChecked(check);
                mCheck[j] = check;
                j++;
            }
        }
    }

    public void setOnAllCheckListener(OnAllCheckListener onAllCheckListener) {
        this.onAllCheckListener = onAllCheckListener;
    }

    public interface OnAllCheckListener {
        public void onAllCheck();

        public void onAllUnCheck();
    }

    public ArrayList<String> getCheckedList() {
        if (mCheck != null && mCheck.length > 0) {
            ArrayList<String> titleChild = new ArrayList<String>();
            for (int i = 0; i < mCheck.length; i++) {
                if (mCheck[i]) titleChild.add(mCheckList.get(i));
            }
            return titleChild;
        }else return null;
    }

    public void checkAllCheck() {
        if (mAST == null || mAST.getStatus() == AsyncTask.Status.FINISHED) {
            mAST = new AsyncTask<Void, Void, Boolean>() {


                @Override
                protected Boolean doInBackground(Void... voids) {
                    boolean check = true;
                    for (int i = 0; i < mCheck.length; i++) {
                        if (!mCheck[i]) {
                            check = false;
                            break;
                        }
                    }
                    return check;
                }

                @Override
                protected void onPostExecute(Boolean check) {
                    if (check) {
                        if (onAllCheckListener != null)
                            onAllCheckListener.onAllCheck();
                    } else if (onAllCheckListener != null)
                        onAllCheckListener.onAllUnCheck();
                }
            };
            mAST.execute();
        }
    }
}
