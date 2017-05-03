package an.it.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import an.it.R;
import an.it.utils.Convert;

public class ATextBox extends LinearLayout implements TextWatcher,OnClickListener,OnFocusChangeListener{
	private ImageView imvdes;
	private ImageView imvclose;
	private ImageView imvVerticalLine;
	private EditText edt;
	public ATextBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}


	private void init(Context context,AttributeSet attrs){

		int imageSize=Convert.PixelFromDp(context,42);
		int verlineWidth=Convert.PixelFromDp(context,1);
		int imvPadding=Convert.PixelFromDp(context,8);
		int closeImagePadding= Convert.PixelFromDp(context,10);
		int textSize=Convert.SpFromPixel(context,16);
		int inputType=0;
		Drawable leftBackground=null;
		String hint=null;
		String text=null;
		if(attrs != null){
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ATexBox);
			imvPadding = a.getDimensionPixelSize( R.styleable.ATexBox_leftImagePadding, imvPadding);
			closeImagePadding = a.getDimensionPixelSize( R.styleable.ATexBox_closeImagePadding,closeImagePadding);
			verlineWidth = a.getDimensionPixelSize( R.styleable.ATexBox_verticalLineWidth, verlineWidth);
			imageSize=a.getDimensionPixelSize( R.styleable.ATexBox_imageSize, imageSize );
			textSize=a.getDimensionPixelSize(R.styleable.ATexBox_textSize,textSize);
			hint=a.getString(R.styleable.ATexBox_hint);
			text=a.getString(R.styleable.ATexBox_text);
			if(a.hasValue(R.styleable.ATexBox_leftImageBackground)){
				leftBackground = a.getDrawable(R.styleable.ATexBox_leftImageBackground);

			}
			if(a.hasValue(R.styleable.ATexBox_inputType)){
				inputType=a.getInt(R.styleable.ATexBox_inputType, 0);
			}

			a.recycle();

		}

		this.setBackgroundResource(R.drawable.bg_white_pressed);
		this.edt=new  EditText(context);
		this.edt.setLayoutParams(new LayoutParams(0,LayoutParams.MATCH_PARENT,1.0f));
		this.edt.setBackgroundDrawable(null);
		this.edt.setInputType(inputType);

		this.imvdes=new ImageView(context);
		this.imvdes.setImageResource(R.drawable.ic_close);
		this.imvdes.setLayoutParams(new LayoutParams(imageSize,imageSize));
		this.imvdes.setPadding(imvPadding, imvPadding, imvPadding, imvPadding);
		if(leftBackground != null){
			this.imvdes.setImageDrawable(leftBackground);
		}
		this.imvVerticalLine=new ImageView(context);
		this.imvVerticalLine.setBackgroundColor(Color.parseColor("#cfcfd6"));
		this.imvVerticalLine.setLayoutParams(new LayoutParams(verlineWidth,LayoutParams.MATCH_PARENT));
		this.addView(imvdes);
		this.addView(imvVerticalLine);

		this.imvclose=new ImageView(context);
		this.imvclose.setImageResource(R.drawable.ic_close);
		this.imvclose.setBackgroundResource(R.drawable.pressed_action);
		this.imvclose.setLayoutParams(new LayoutParams(imageSize,imageSize));
		this.imvclose.setPadding(closeImagePadding, closeImagePadding, closeImagePadding, closeImagePadding);
		this.imvclose.setOnClickListener(this);

		this.edt.setHint(hint);
		this.edt.setPadding(imvPadding, 0, 0, 0);
		this.edt.setTextSize(textSize / context.getResources().getDisplayMetrics().scaledDensity);
		this.edt.addTextChangedListener(this);
		this.edt.setOnFocusChangeListener(this);
		this.edt.setHintTextColor(Color.parseColor("#a2a1ae"));
		this.edt.setText(text);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			this.edt.setTextColor(getContext().getColor(R.color.gray_dark));
		}else		this.edt.setTextColor(getContext().getResources().getColor(R.color.gray_dark));


		this.edt.setSingleLine();
		this.addView(edt);
		this.addView(imvclose);



	}
	@Override
	public void afterTextChanged(Editable s) {}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length()>0){
			imvclose.setVisibility(View.VISIBLE);
		}else{
			imvclose.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		if(v==imvclose){
			this.edt.setText(null);
		}
		
	}
	public CharSequence getText(){
		return edt.getText();
	}
	public void setText(String value){
		edt.setText(value);
	}
	public void setError(SpannableStringBuilder abc){
		edt.setError(abc);
	}
	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if(arg1){
			this.setBackgroundResource(R.drawable.bg_red_stroke);
		}else{
			this.setBackgroundResource(R.drawable.bg_white_pressed);
		}
	}
	public EditText getEditText(){
		return edt;
	}
	public void setLeftImage(int ID){
		if(this.imvdes!=null){
			this.imvdes.setImageResource(ID);
		}
	}
	public void setHint(String text){
		if(this.edt!=null){
			this.edt.setHint(text);
		}
	}
	public void setInputType(int inputType){
		if(this.edt!=null) {
			switch (inputType) {
				case (1):
					this.edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					break;
				case (2):
					this.edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
					break;
				case (3):
					this.edt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					break;
			}
		}
	}
}
