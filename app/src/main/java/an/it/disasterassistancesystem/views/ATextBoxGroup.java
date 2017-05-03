package an.it.disasterassistancesystem.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.utils.Convert;
import an.it.views.ATextBox;

public class ATextBoxGroup extends LinearLayout {
    private ArrayList<String> mTBList;
    private int childLeftImage= R.drawable.ic_license;
    public ATextBoxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setTextBoxList(ArrayList<String> mTBList) {
        this.mTBList = mTBList;
        if (mTBList != null && mTBList.size() > 0) {
            int pos = 0;
            while (pos < mTBList.size()) {
                View v = null;
                if (getChildCount() > pos) {
                    v = getChildView(pos, getChildAt(pos));
                } else {
                    v = getChildView(pos, null);
                    addView(v);
                }
                pos++;
            }
            while (pos > 0 && getChildCount() > pos) {
                removeViewAt(pos);
            }
        } else {
            removeAllViews();
        }
    }

    protected View getChildView(int pos, View v) {
        ATextBox atb = (ATextBox) v;
        if (atb == null) {
            atb = new ATextBox(getContext(),null);
            LayoutParams ly = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            ly.topMargin = Convert.PixelFromDp(getContext(), 2);
            atb.setLayoutParams(ly);
            atb.setHint(mTBList.get(pos));
            atb.setLeftImage(R.drawable.ic_license);
        } else {
            atb.setHint(mTBList.get(pos));
        }
        return atb;
    }
    public View getChildView(int pos){
        if(this.getChildCount()>pos){
            return this.getChildAt(pos);
        }else return null;
    }

    public void setChildLeftImage(int childLeftImage) {
        this.childLeftImage = childLeftImage;
    }
}
