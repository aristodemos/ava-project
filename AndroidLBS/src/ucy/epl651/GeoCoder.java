package ucy.epl651;

import ucy.epl651.GeoCodeResult;

public class GeoCoder {

	  private static final String YAHOO_API_BASE_URL = "http://where.yahooapis.com/geocode?q=%1$s,+%2$s&gflags=R&appid=[yourappidhere]";
	  private HttpRetriever httpRetriever = new HttpRetriever();
	  private XmlParser xmlParser = new XmlParser();
	  
	  public GeoCodeResult reverseGeoCode(double latitude, double longitude) {
	         String url = String.format(YAHOO_API_BASE_URL, String.valueOf(latitude), String.valueOf(longitude));       
	         String response = httpRetriever.retrieve(url);
	         return xmlParser.parseXmlResponse(response);
	       }
	
}
