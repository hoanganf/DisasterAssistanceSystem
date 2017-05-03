package an.it.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import an.it.R;
import an.it.utils.Convert;

public class ANoScrollListView extends LinearLayout implements View.OnClickListener {
    private Adapter adapter;
    private OnItemClickListener lsn;
    private int mHorizontalHeight;
    private int mListSelecterID = R.drawable.bg_white_pressed;
    private int mColor;

    //	private int lineHeigth;
    public ANoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ANoScrollListView(Context context) {
        super(context);
        init(context,null);
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        notifyDataSetChanged();
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ANoScrollListView);
            mHorizontalHeight = a.getDimensionPixelSize(R.styleable.ANoScrollListView_adividerHeight, Convert.PixelFromDp(getContext(), 5));
            if (a.hasValue(R.styleable.ANoScrollListView_listSelecter)) {
                mListSelecterID = a.getResourceId(R.styleable.ANoScrollListView_listSelecter, R.drawable.bg_white_pressed);
            }
            mColor = a.getColor(R.styleable.ANoScrollListView_adivider, getResources().getColor(android.R.color.transparent));

            a.recycle();
        }
    }

    public void notifyDataSetChanged() {
        if (adapter != null && adapter.getCount() > 0) {
            int pos = 0;
            int i = 0;
            while (pos < adapter.getCount()) {
                View v = null;
                if (getChildCount() > i) {
                    v = adapter.getView(pos, getChildAt(i), this);
                } else {
                    v = adapter.getView(pos, null, this);
                    if (Build.VERSION.SDK_INT >= 21) {
                        v.setBackground(getResources().getDrawable(mListSelecterID, getContext().getTheme()));
                    } else if (Build.VERSION.SDK_INT >= 16) {
                        v.setBackground(getResources().getDrawable(mListSelecterID));
                    } else v.setBackgroundDrawable(getResources().getDrawable(mListSelecterID));
                    addView(v);
                    addView(getHorizontalLine());
                    v.setOnClickListener(this);
                    v.setClickable(true);
                }
                v.setTag(pos);
                pos++;
                i += 2;
            }
            while (i > 0 && getChildCount() > i - 1) {
                removeViewAt(i - 1);
            }
        } else {
            removeAllViews();
        }
    }

    private ImageView getHorizontalLine() {
        ImageView horizontalLine = new ImageView(getContext());
        horizontalLine.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, mHorizontalHeight));
        horizontalLine.setBackgroundColor(mColor);
        return horizontalLine;
    }

    public void setOnItemClickListener(OnItemClickListener lsn) {
        this.lsn = lsn;
    }

    @Override
    public void onClick(View view) {
        if (lsn != null) {
            int pos = Integer.parseInt(view.getTag().toString());
            lsn.onItemClick(null, view, pos, adapter.getItemId(pos));
        }
    }
}
