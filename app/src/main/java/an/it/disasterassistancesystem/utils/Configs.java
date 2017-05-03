package an.it.disasterassistancesystem.utils;

import android.os.Environment;


public class Configs {
	
	/*
	 * static Field
	 */

	public static String HOST="192.168.0.5";
	public static String HOST_PORT="80";
	public static String WEB_UPLOAD = "http://"+HOST+":"+HOST_PORT+"/housesale/uploadfile.php";
	public static String WSLINK="/disasterassistancesystem/service.php";
	public static String FOLDER_LINK=Environment.getExternalStorageDirectory().getAbsolutePath()+"/ChuTai/";
}
