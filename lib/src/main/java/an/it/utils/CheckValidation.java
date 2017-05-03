package an.it.utils;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class CheckValidation {

	/*
	 * public methods
	 */
	public static boolean validateEmail(EditText edt, int limit){
		String email = edt.getText().toString().trim();
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		if(email.length() == 0){
			edt.setError(setColor("Please input data"));
			return false;
		}else if(email.length() > limit){
			edt.setError(setColor("The data must less than "+limit+" characters"));
			return false;
		}else if(!pattern.matcher(email).matches()){
	    	edt.setError(setColor("The data entered is invalid email address"));
			return false;
	    }
	    return true;
	}
	public static boolean validateForcedEditText(EditText edt, String pat,int minLimit, int maxLimit){
		String text = edt.getText().toString().trim();
		if(text.length() == 0){
			edt.setError(setColor("Please input data"));
			return false;
		}else if(text.length()<minLimit){
			edt.setError(setColor("The data at least "+minLimit+" characters"));
			return false;
		}else if(text.length() > maxLimit){
			edt.setError(setColor("The data between "+minLimit+" and "+maxLimit+" characters"));
			return false;
		}else if(pat!=null &&!Pattern.compile(pat).matcher(text).matches()){
	    	edt.setError(setColor("Wrong format data"));
			return false;
	    }
	    return true;
	}
	public static boolean validateDate(int years){
		Calendar cal=Calendar.getInstance(Locale.getDefault());

		return (years-1990<=cal.get(Calendar.YEAR));
	}
	public static SpannableStringBuilder setColor(String str){
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(0xff2fb0e0);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
		ssbuilder.setSpan(fgcspan, 0, str.length(), 0);
		return ssbuilder;
	}
}
