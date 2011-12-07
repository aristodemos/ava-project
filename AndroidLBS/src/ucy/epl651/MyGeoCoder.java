package ucy.epl651;

public class MyGeoCoder {

	  private static final String MY_BASE_URL = "http://10.0.2.2/geoNoteX/service.yahoo.geocode.php?qlo=%1$s&qla=%2$s&msg=%3$s&usr=%4$s";
	  								//http://where.yahooapis.com/geocode?q=%1$s,+%2$s&gflags=R&appid=[yourappidhere]
	  private HttpRetriever httpRetriever = new HttpRetriever();
	  private XmlParser xmlParser = new XmlParser();
	  String msg = "atssss";
	  String user_name = "remoteUser";
	  
	  public GeoCodeResult reverseGeoCode(double latitude, double longitude) {
	         String url = String.format(MY_BASE_URL, String.valueOf(latitude), String.valueOf(longitude), msg, user_name);       
	         String response = httpRetriever.retrieve(url);
	         return xmlParser.parseXmlResponse(response);
	       }
	
}
