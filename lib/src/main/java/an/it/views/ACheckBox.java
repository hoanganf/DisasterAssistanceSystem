package an.it.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import an.it.R;


public class ACheckBox extends ImageView implements View.OnClickListener{
	private boolean checked=false;
	private OnCheckedChangeListener lsn;
	private int mActiveID=R.drawable.ic_cb_active,mInActiveID=R.drawable.ic_cb_inactive;
	public ACheckBox(Context context) {
		super(context);
		init(context,null);
	}
	public ACheckBox(Context context,AttributeSet tr) {
		super(context, tr);
		init(context,tr);
	}
	public ACheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init(context,attrs);
	}
	private void init(Context context, AttributeSet attrs) {
		this.checked=false;
		this.setOnClickListener(this);
		this.setClickable(true);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ACheckBox);
			checked = a.getBoolean(R.styleable.ACheckBox_isChecked,false);
			if (a.hasValue(R.styleable.ACheckBox_activeImage)) {
				mActiveID = a.getResourceId(R.styleable.ACheckBox_activeImage, R.drawable.ic_cb_active);
			}
			if (a.hasValue(R.styleable.ACheckBox_inActiveImage)) {
				mInActiveID = a.getResourceId(R.styleable.ACheckBox_activeImage, R.drawable.ic_cb_active);
			}
			a.recycle();
		}
		setImageResource(mInActiveID);
	}

	public void setChecked(boolean value){
		this.checked=value;
		setValue();
	}
	public boolean isChecked() {
		return checked;
	}
	@Override
	public void onClick(View v) {
		checked=!checked;
		setValue();
		
		if(lsn!=null) lsn.onCheckedChanged(v,checked);
		
	}
	private void setValue(){
		if(checked){
			this.setImageResource(mActiveID);
		}else{
			this.setImageResource(mInActiveID);
		}
	}
	public void setOnCheckedChangeListener(OnCheckedChangeListener lsn){
		this.lsn=lsn;
	}
	public interface OnCheckedChangeListener {
		void onCheckedChanged(View var1, boolean var2);
	}
	
}
