package an.it.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginSaveAdapter {
	public final String KEY_USER_NAME="_name";
	public final String KEY_USER_PASSWORD="_password";
	private Context mContext;
	private Editor edt;
	private SharedPreferences prefs;
	public String getUserName() {
		return this.prefs.getString(KEY_USER_NAME,null);
	}

	public void setUserName(String username) {
		edt.putString(KEY_USER_NAME, username);
		edt.commit();
	}

	public String getPassword() {
		return this.prefs.getString(KEY_USER_PASSWORD,null);
	}

	public void setPassword(String password) {
		edt.putString(KEY_USER_PASSWORD,password);
		edt.commit();
	}



}
