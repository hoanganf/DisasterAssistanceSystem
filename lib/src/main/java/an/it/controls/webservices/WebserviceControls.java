package an.it.controls.webservices;/*
package it.ha.halib.controls.webservices;

import android.content.Context;

import com.bisengogeo.listeners.WebServiceCommunicatorListener;
import com.bisengogeo.utils.WebServiceCommunicator;
import com.bisengogeo.utils.WebServiceCommunicator.WebServiceFlag;

*/
/**
 * 
 * @author AN
 * 
 *//*

public class WebserviceControls {
	*/
/*
	 * fields
	 *//*


	//
	private Context mContext;
	private WebServiceCommunicatorListener lsn;
	private WebServiceCommunicator getCities;
	private WebServiceCommunicator getThemes;
	private WebServiceCommunicator searchAgenda;
	private WebServiceCommunicator getPlaces;
	private WebServiceCommunicator getAgenda;
	private WebServiceCommunicator getCoupon;
	private WebServiceCommunicator getTraditions;

	*/
/*
	 * 
	 * constructor
	 *//*

	public WebserviceControls(Context cxt, WebServiceCommunicatorListener lsn) {
		this.mContext = cxt;
		this.lsn = lsn;
	}

	public boolean searchPlace(String cityCode,String language, String key) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GeoAppGetPlaceSearch xmlns=\"http://tempuri.org/\">"
				+ "<thecode>" + cityCode + "</thecode>" 
				+ "<lalangue>"+ language + "</lalangue>"
				+ "<keyword>"+key+"</keyword>"
				+ "</GeoAppGetPlaceSearch>"
				+ "</soap:Body>" 
				+ "</soap:Envelope>";

		if (searchAgenda != null) searchAgenda.cancel(true);
		searchAgenda = new WebServiceCommunicator(mContext);
		searchAgenda.setWebServiceCommuncatorListener(lsn);
		searchAgenda.fetch(WebServiceFlag.SEARCH_PLACE, content);
		return true;
	}

	public boolean getCities() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GeoAppGetApplications xmlns=\"http://tempuri.org/\">"
				+ "<thecode>"
				+ Configs.APP_CODE
				+ "</thecode>"
				+ "</GeoAppGetApplications>"
				+ "</soap:Body>"
				+ "</soap:Envelope>";

		if (getCities != null)
			getCities.cancel(true);
		getCities = new WebServiceCommunicator(mContext);
		getCities.setWebServiceCommuncatorListener(lsn);
		getCities.fetch(WebServiceFlag.GET_CITIES, content);
		return true;
	}

	public boolean getThemes(String cityCode, String language) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GeoAppGetTheme xmlns=\"http://tempuri.org/\">"
				+ "<citycode>" + cityCode + "</citycode>" + "<lalangue>"
				+ language + "</lalangue>" + "</GeoAppGetTheme>"
				+ "</soap:Body>" + "</soap:Envelope>";

		if (getThemes != null)
			getThemes.cancel(true);
		getThemes = new WebServiceCommunicator(mContext);
		getThemes.setWebServiceCommuncatorListener(lsn);
		getThemes.fetch(WebServiceFlag.GET_THEMES, content);
		return true;
	}

	public boolean getPlaces(String cityCode, String theme) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GeoAppGetPlace xmlns=\"http://tempuri.org/\">"
				+ "<thecode>" + cityCode + "</thecode>" + "<lalangue>"
				+ Configs.LANGUAGE + "</lalangue>" + "<thetheme>" + theme
				+ "</thetheme>" + "</GeoAppGetPlace>" + "</soap:Body>"
				+ "</soap:Envelope>";

		if (getPlaces != null)
			getPlaces.cancel(true);
		getPlaces = new WebServiceCommunicator(mContext);
		getPlaces.setWebServiceCommuncatorListener(lsn);
		getPlaces.fetch(WebServiceFlag.GET_PLACE, content);
		return true;
	}

	public boolean getAgenda(String articeID) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GetPlaceEvent xmlns=\"http://tempuri.org/\">"
				+ "<theFunspotId>" + articeID + "</theFunspotId>"
				+ "</GetPlaceEvent>" + "</soap:Body>" + "</soap:Envelope>";
		if (getAgenda != null)
			getAgenda.cancel(true);
		getAgenda = new WebServiceCommunicator(mContext);
		getAgenda.setWebServiceCommuncatorListener(lsn);
		getAgenda.fetch(WebServiceFlag.GET_AGENDA, content);
		return true;
	}

	public boolean getCoupon(String articeID) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GetPlaceCoupon xmlns=\"http://tempuri.org/\">"
				+ "<theFunspotId>" + articeID + "</theFunspotId>"
				+ "</GetPlaceCoupon>" + "</soap:Body>" + "</soap:Envelope>";
		if (getCoupon != null)
			getCoupon.cancel(true);
		getCoupon = new WebServiceCommunicator(mContext);
		getCoupon.setWebServiceCommuncatorListener(lsn);
		getCoupon.fetch(WebServiceFlag.GET_COUPON, content);
		return true;
	}

	public boolean getTradition(String language) {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<GetTraductionFile xmlns=\"http://tempuri.org/\">"
				+ "<thelanguage>" + language + "</thelanguage>"
				+ "</GetTraductionFile>" + "</soap:Body>" + "</soap:Envelope>";
		if (getTraditions != null)
			getTraditions.cancel(true);
		getTraditions = new WebServiceCommunicator(mContext);
		getTraditions.setWebServiceCommuncatorListener(lsn);
		getTraditions.fetch(WebServiceFlag.GET_TRANDITIONS, content);
		return true;
	}

	*/
/*
	 * public ArrayList<NameValuePair> getAuthenParams(){
	 * ArrayList<NameValuePair> lst=new ArrayList<NameValuePair>();
	 * if(save.getLoginType()==SaveControl.LOGIN_EMAIL){
	 * lst.add(getProperty("email", save.getEmail()));
	 * lst.add(getProperty("password", save.getPassword())); }else{
	 * lst.add(getProperty("fb_token", save.getFbToken())); } return lst; }
	 *//*

	public void cancelAll() {
		if (getCities != null)
			getCities.cancel(true);
		if (getThemes != null)
			getThemes.cancel(true);
		if (getPlaces != null)
			getPlaces.cancel(true);
		if (getAgenda != null)
			getAgenda.cancel(true);
		if (getCoupon != null)
			getCoupon.cancel(true);
		if (searchAgenda != null)
			searchAgenda.cancel(true);
	}

}
*/
