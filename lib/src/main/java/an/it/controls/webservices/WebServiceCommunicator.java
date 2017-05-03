package an.it.controls.webservices;/*
package it.ha.halib.controls.webservices;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceCommunicator extends AsyncTask<Void, Integer, String> {
	
	public static final int TIME_OUT = 15000;
	public static final int SOCKET_TIME_OUT = 30000;
	
	private WebServiceCommunicatorListener listener;
	private String data;
	private WebServiceFlag flag;
	private Context cxt;

	public WebServiceCommunicator(Context cxt) {
		this.cxt = cxt;
	}
	public void fetch(WebServiceFlag flag,String data) {
		this.data = data;
		this.flag = flag;
		this.execute();
	}

	@Override
	protected void onPreExecute() {
	//	Log.i("AAAAAA", "pre");
		if (listener != null) {
			listener.onConnectionOpen(flag);
		}
	}
	
	public void setWebServiceCommuncatorListener(WebServiceCommunicatorListener listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(Void... params) {
//		Log.i("AAAAAA", "0");
		String url = getURL(flag)+data;
		Log.i("URL", url);
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIME_OUT);
		HttpClient client = new DefaultHttpClient(httpParameters);
		HttpGet method = new HttpGet(url);
		try {			
			HttpResponse response = client.execute(method);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Unknown Protocol Used.";
		} catch (IOException e) {
			e.printStackTrace();
			return "No connection to the server.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Unknown exception.";
		}
	
	}

	@Override
	protected void onPostExecute(String result) {
		if(result!=null){
			if(listener!=null) listener.onConnectionDone(flag, result);
		}else if(listener!=null){
			listener.onConnectionError(flag,"Cannot connect to server. Please check your connection.");
		}
	}
	public static String getURL(WebServiceFlag fl){
		if(fl==WebServiceFlag.GET_LINK){
			return Configs.getApiUrl()+"getlink.html";
		}else if(fl==WebServiceFlag.POST_CLICK){
			return Configs.getApiUrl()+"postclick.html";
		}else if(fl==WebServiceFlag.GET_GEO){
			return "http://www.ozemobi.net/ucp/geoip";
		}else return null;
	}
	public enum WebServiceFlag{
		GET_LINK,
		POST_CLICK,
		GET_GEO
		
	}
}
*/
