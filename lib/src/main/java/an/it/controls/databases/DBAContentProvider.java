package an.it.controls.databases;/*
package it.ha.halib.controls.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.a.autoclick.objects.GeoCountry;
import com.a.autoclick.objects.History;
import com.a.autoclick.objects.Link;
import com.a.autoclick.objects.Model;

import java.util.ArrayList;

public class DBAContentProvider {
	public static final String AUTHORITY = "halib";//specific for our our app, will be specified in maninfed
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	public static final String prefix="halib_";
	private String TAG = "Karaoke";
	public static final int LIMIT=50;
	
	// Contacts table name
    private static final String TABLE_COUNTRIES = prefix+"countries";
    private static final String TABLE_LINKS = prefix+"links";
    private static final String TABLE_USERS = prefix+"users";
    private static final String TABLE_HISTORIES = prefix+"histories";
    private static final String TABLE_MODELS = prefix+"models";
    private static final String TABLE_IPS= prefix+"ips";
    
	 // Contacts Table Columns names
    public static final String KEY_ID = prefix+"id";
    public static final String KEY_NAME = prefix+"name";
    public static final String KEY_IP_FROM = prefix+"ip_from";
    public static final String KEY_IP_TO = prefix+"ip_to";
    public static final String KEY_DECIMAL_FROM = prefix+"decimal_from";
    public static final String KEY_DECIMAL_TO = prefix+"decimal_to";
    public static final String KEY_COUNTRY_ID = prefix+"country_id";
    
    public static final String KEY_USER_NAME = prefix+"user_name";
    public static final String KEY_PASSWORD = prefix+"password";
    public static final String KEY_PRIORITY = prefix+"priority";
    
    public static final String KEY_IMG = prefix+"img";
    public static final String KEY_URL = prefix+"url";
    public static final String KEY_COUNTRIES = prefix+"countries";
    public static final String KEY_LOOP = prefix+"loop";
    public static final String KEY_DELAY_TIME = prefix+"delay_time";
    public static final String KEY_DELAY_LOOP = prefix+"delay_loop";
    
    public static final String KEY_DECIMAL_IP = prefix+"decimal_ip";
    public static final String KEY_IP = prefix+"ip";
    public static final String KEY_TIME = prefix+"time";
    public static final String KEY_USER = prefix+"user";
    
    public static final String KEY_DISPLAY = prefix+"display";
    public static final String KEY_VERSION = prefix+"version";
    public static final String KEY_MODEL = prefix+"model";
    public static final String KEY_BRAND = prefix+"brand";
    public static final String KEY_DEVICE = prefix+"device";
    public static final String KEY_BOARD = prefix+"board";
    public static final String KEY_MANUFACTURER = prefix+"manufacturer";
    
    

	private Context mContext;
	
	// Database creation SQL statement
	*/
/*public static final String CREATE_TABLE_COUNTRIES = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_COUNTRIES + " (" + KEY_ID+ " text primary key, "
			+ KEY_NAME	+ " text not null, "
			+ KEY_PRIORITY+" integer not null)";
	public static final String CREATE_TABLE_LINKS = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_LINKS + " (" + KEY_ID+ " integer primary key, " 
			+ KEY_NAME	+ " text not null, " 
			+ KEY_IMG+ " text not null, "
			+ KEY_URL + " text not null, "
			+ KEY_LOOP + " integer not null default 3, "
			+ KEY_DELAY_LOOP + " integer not null default 3, "
			+ KEY_DELAY_TIME + " integer not null default 30, "
			+ KEY_COUNTRIES + " text not null)";
	public static final String CREATE_TABLE_HISTORIES= "CREATE TABLE IF NOT EXISTS "
			+ TABLE_HISTORIES + " (" + KEY_ID+ " integer primary key autoincrement, " 
			+ KEY_IP + " text not null, "
			+ KEY_TIME + " integer not null, "
			+ KEY_USER + " text)";
	public static final String CREATE_TABLE_IPS="CREATE TABLE IF NOT EXISTS "
			+ TABLE_IPS+ " (" + KEY_ID+ " integer primary key autoincrement, "
			+ KEY_COUNTRY_ID + " text not null, "
			+ KEY_IP_FROM + " text not null, "
			+ KEY_IP_TO + " text not null, "
			+ KEY_DECIMAL_FROM + " integer not null, "
			+ KEY_DECIMAL_TO + " integer not null)";*//*
*/
/*
	public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_USERS+ " (" + KEY_USER_NAME+ " text primary key, "
			+ KEY_PASSWORD	+ " text not null,"
			+ KEY_PRIORITY	+ " integer not null default 1)";*//*
*/
/*
	public static final String CREATE_TABLE_MODELS="CREATE TABLE IF NOT EXISTS "
			+ TABLE_MODELS+ " (" + KEY_DISPLAY + " text not null, "
			+ KEY_VERSION + " text not null, "
			+ KEY_MODEL + " text primary key, "
			+ KEY_NAME + " text, "
			+ KEY_BRAND + " text not null, "
			+ KEY_DEVICE + " text not null, "
			+ KEY_BOARD + " text not null, "
			+ KEY_MANUFACTURER + " text not null)";*//*


	public DBAContentProvider(Context ctx) {
		this.mContext = ctx;
	}
	
	public boolean recreateTable(String tableName)
	{
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, tableName);
		int value=mContext.getContentResolver().delete(contentUri, null, null);
		return value>0;
	}
	// create a form
	public long addLink(long id,String name,String img,String url,String countries,long timeDelay,int delayLoop,int loop) {
		long returnID = -1;
		ContentValues values = new ContentValues();
		values.put(KEY_COUNTRIES, countries);
		values.put(KEY_NAME, name);
		values.put(KEY_IMG, img);
		values.put(KEY_URL, url);
		values.put(KEY_LOOP, loop);
		values.put(KEY_DELAY_TIME, timeDelay);
		values.put(KEY_DELAY_LOOP, delayLoop);
		values.put(KEY_ID, id);
		// check if the form exists
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_LINKS);
		Uri resultUri=mContext.getContentResolver().insert(contentUri, values);
		returnID =Long.parseLong(resultUri.getLastPathSegment());
		return returnID;
	}
	public boolean deleteLink(long id)
	{
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_LINKS);
		int value=mContext.getContentResolver().delete(contentUri, KEY_ID+"=?", new String[]{id+""});
		return value>0;
	}
	public boolean deleteLinks()
	{
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_LINKS);
		int value=mContext.getContentResolver().delete(contentUri,null,null);
		return value>0;
	}
	// create a form
	public boolean updateLink(Link link) {
		ContentValues values = new ContentValues();
		if(link.getCountries()!=null && link.getCountries().size()>0){
			String countries="";
			for (GeoCountry iterable_element : link.getCountries()) {
				countries+=(iterable_element.getID()+"#");
			}
			countries=countries.substring(0,countries.length()-1);
			values.put(KEY_COUNTRIES, countries);
		}
		
		values.put(KEY_NAME, link.getName());
		values.put(KEY_IMG, "");
		values.put(KEY_URL, link.getUrl());
		values.put(KEY_LOOP, link.getLoop());
		values.put(KEY_DELAY_TIME, link.getDelayTime());
		values.put(KEY_DELAY_LOOP, link.getDelayLoop());
		
		// check if the form exists
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_LINKS);		
		return mContext.getContentResolver().update(contentUri, values, KEY_ID+"="+link.getID(), null)>0;
	}
	// getting all forms for a user
	public ArrayList<Link> getLinks() {
		ArrayList<Link> results=new ArrayList<Link>();
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_LINKS);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_NAME,KEY_IMG,KEY_URL,KEY_COUNTRIES,KEY_LOOP,KEY_DELAY_TIME,KEY_DELAY_LOOP},null, null,null);
		if(cursor.moveToFirst()){
			do{
				
				Link item = new Link();
				item.setID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
				item.setUrl(cursor.getString(cursor.getColumnIndex(KEY_URL)));
				item.setLoop(cursor.getInt(cursor.getColumnIndex(KEY_LOOP)));
				item.setDelayLoop(cursor.getInt(cursor.getColumnIndex(KEY_DELAY_LOOP)));
				item.setDelayTime(cursor.getLong(cursor.getColumnIndex(KEY_DELAY_TIME)));
				
				ArrayList<GeoCountry> countries=new ArrayList<GeoCountry>();
				String countri=cursor.getString(cursor.getColumnIndex(KEY_COUNTRIES));
				if(countri!=null && countri.length()>0){
					for (String string : countri.split("#")) {
						GeoCountry c=getCountry(string);
						if(c!=null)	countries.add(c);
					}
					item.setCountries(countries);
				}
				
				results.add(item);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return results;
	}
	public long addModel(String display,String version,String model,String brand,String name,String device, String board, String manufacturer) {
		long returnID = -1;
		ContentValues values = new ContentValues();
		values.put(KEY_DISPLAY, display);
		values.put(KEY_VERSION, version);
		values.put(KEY_MODEL, model);
		values.put(KEY_BRAND, brand);
		values.put(KEY_NAME, name);
		values.put(KEY_DEVICE, device);
		values.put(KEY_BOARD, board);
		values.put(KEY_MANUFACTURER, manufacturer);
		// check if the form exists
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_MODELS);		
		mContext.getContentResolver().insert(contentUri, values);
		return returnID;
	}
	public boolean deleteModel(String model)
	{
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_MODELS);
		int value=mContext.getContentResolver().delete(contentUri, KEY_MODEL+"=?", new String[]{model});
		return value>0;
	}
	// create a form
	public boolean updateModel(Model md) {
		ContentValues values = new ContentValues();
		values.put(KEY_DISPLAY, md.getDisplay());
		values.put(KEY_VERSION, md.getVersion());
		values.put(KEY_MODEL, md.getModel());
		values.put(KEY_BRAND, md.getBrand());
		values.put(KEY_NAME, md.getName());
		values.put(KEY_DEVICE, md.getDevice());
		values.put(KEY_BOARD, md.getBoard());
		values.put(KEY_MANUFACTURER, md.getManufacturer());
		
		// check if the form exists
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_MODELS);		
		return mContext.getContentResolver().update(contentUri, values, KEY_MODEL+"=?",new String[]{md.getModel()})>0;
	}
	// getting all forms for a user
	public ArrayList<Model> getModels() {
		ArrayList<Model> results=new ArrayList<Model>();
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_MODELS);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_DISPLAY,KEY_NAME,KEY_VERSION,KEY_MODEL,KEY_BRAND,KEY_DEVICE,KEY_BOARD,KEY_MANUFACTURER},null, null,null);
		if(cursor.moveToFirst()){
			do{
				
				Model item = new Model();
				item.setDisplay(cursor.getString(cursor.getColumnIndex(KEY_DISPLAY)));
				item.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
				item.setVersion(cursor.getString(cursor.getColumnIndex(KEY_VERSION)));
				item.setModel(cursor.getString(cursor.getColumnIndex(KEY_MODEL)));
				item.setManufacturer(cursor.getString(cursor.getColumnIndex(KEY_MANUFACTURER)));
				item.setDevice(cursor.getString(cursor.getColumnIndex(KEY_DEVICE)));
				item.setBrand(cursor.getString(cursor.getColumnIndex(KEY_BRAND)));
				item.setBoard(cursor.getString(cursor.getColumnIndex(KEY_BOARD)));
				
				results.add(item);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return results;
	}

	// getting all forms for a user
	public ArrayList<GeoCountry> getCountries() {
		ArrayList<GeoCountry> results=new ArrayList<GeoCountry>();
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_COUNTRIES);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_NAME},null, null,KEY_PRIORITY+" DESC");
		if(cursor.moveToFirst()){
			do{
				
				GeoCountry item = new GeoCountry();
				item.setID(cursor.getString(cursor.getColumnIndex(KEY_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
				results.add(item);
			}while(cursor.moveToNext());
		}
		cursor.close();
		return results;
	}
	public GeoCountry getCountry(String id) {
		GeoCountry item=null;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_COUNTRIES);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_NAME},KEY_ID+"=?",new String[]{id},null);
		if(cursor.moveToFirst()){
			item = new GeoCountry();
			item.setID(cursor.getString(cursor.getColumnIndex(KEY_ID)));
			item.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
		}
		cursor.close();
		return item;
	}
	public GeoCountry getCountryByID(String decimalIP) {
		GeoCountry item=null;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_IPS);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_COUNTRY_ID},KEY_DECIMAL_FROM+"<=? AND "+KEY_DECIMAL_TO+">=?",new String[]{decimalIP,decimalIP},null);
		if(cursor.moveToFirst()){
			item=getCountry(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_ID)));
			
		}
		cursor.close();
		return item;
	}
	public boolean updatePriority(GeoCountry geo) {
		boolean result=false;
		if(geo==null) return result;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_COUNTRIES);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_NAME,KEY_PRIORITY},KEY_ID+"=?",new String[]{geo.getID()},null);
		if(cursor.moveToFirst()){
			int pre=cursor.getInt(cursor.getColumnIndex(KEY_PRIORITY));
			ContentValues values = new ContentValues();
			values.put(KEY_PRIORITY, ++pre);
			Log.e("create Country", ""+pre);
			result=mContext.getContentResolver().update(contentUri, values,KEY_ID+"=?",new String[]{geo.getID()})>0;
		}
		cursor.close();
		return result;
	}
	public long createCountry(GeoCountry geo) {
		long idAdded=0;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_COUNTRIES);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_NAME},KEY_ID+"=?",new String[]{geo.getID()},null);
		if(!cursor.moveToFirst()){
			Log.e("create Country", geo.getName());
			ContentValues values = new ContentValues();
			values.put(KEY_ID, geo.getID());
			values.put(KEY_NAME, geo.getName());
			values.put(KEY_PRIORITY, 0);
			Uri resultUri=mContext.getContentResolver().insert(contentUri, values);
			idAdded=Long.parseLong(resultUri.getLastPathSegment());
		}
		cursor.close();
		return idAdded;
	}
	*/
/*public long createHistory(History his) {
		long idAdded=0;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_HISTORIES);
		Log.e("create history", his.getiP());
		ContentValues values = new ContentValues();
		values.put(KEY_IP, his.getID());
		values.put(KEY_TIME, his.getTime());
		values.put(KEY_USER, his.getUserName());
		Uri resultUri=mContext.getContentResolver().insert(contentUri, values);
		idAdded=Long.parseLong(resultUri.getLastPathSegment());
		return idAdded;
	}*//*

	public boolean prepareHistory() {
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_HISTORIES);
		int value=mContext.getContentResolver().delete(contentUri,System.currentTimeMillis()+"-"+ KEY_TIME+">=86400000", null);
		return value>0;
	}
	public History getHistoryByIP(String ip){
		History his=null;
		Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_HISTORIES);
		Cursor cursor =  mContext.getContentResolver().query(contentUri, new String[] {KEY_ID,KEY_IP,KEY_TIME,KEY_USER},KEY_IP+"=?",new String[]{ip},null);
		if(cursor.moveToFirst()){
			his=new History();
			his.setID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
			his.setIP(cursor.getString(cursor.getColumnIndex(KEY_IP)));
			his.setTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));
			his.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER)));
		}
		cursor.close();
		return his;
	}
	*/
/*public long createIP(IP ip) {
		long idAdded=0;
		if(idAdded<1){
			Log.e("create IP", ip.getCountryID());
			ContentValues values = new ContentValues();
			values.put(KEY_COUNTRY_ID, ip.getCountryID());
			values.put(KEY_IP_FROM,ip.getIpFrom());
			values.put(KEY_IP_TO,ip.getIpTo());
			values.put(KEY_DECIMAL_FROM,ip.getDecimalFrom());
			values.put(KEY_DECIMAL_TO,ip.getDecimalTo());
			Uri contentUri = Uri.withAppendedPath(CONTENT_URI, TABLE_IPS);
			Uri resultUri=mContext.getContentResolver().insert(contentUri, values);
			idAdded=Long.parseLong(resultUri.getLastPathSegment());
		}
		return idAdded;
	}*//*

	
}
*/
