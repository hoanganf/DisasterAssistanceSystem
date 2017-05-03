package an.it.controls.webservices;

import android.os.Environment;

public class Configs {
	//public static String HOST="http://click.faddriect.net";
	public static String HOST="http://www.ozemobi.net";
	public static String GEOIP_LINK="http://www.telize.com/geoip";
	public static String TAG_COUNTRY_CODE="country_code";
	public static String TAG_IP="ip";
	public static String IMAGE_UPLOAD_STORE=Environment.getExternalStorageDirectory()+"/ispye/uploads/";
	public static String getApiUrl(){
		return HOST+"/ucp/";
	}
}
