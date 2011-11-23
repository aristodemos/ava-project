package com.epl603.ava.classes;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.PointF;
import android.util.Log;

public class XMLParser extends DefaultHandler {

	/*private Context _context;

	public XMLParser(Context context) {
		_context = context;
	} */	
	
	public ArrayList<PointPath> roiPaths;
	public ArrayList<FlagPair> flags;
	PointPath tempPath;
	FlagPair myFlagPair;
	
	StringBuilder builder;
	PointF tempPoint;
	PointF tempFlagPoint;
	
	
	public void startDocument() throws SAXException{
		roiPaths = new ArrayList<PointPath>();
	}	
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException {
		
		if (localName.equalsIgnoreCase("ROIs")){
			builder=new StringBuilder();
		}
		if (localName.equalsIgnoreCase("roi")){
			tempPath=new PointPath();
			
		}
		else if(localName.toLowerCase().equals("point")){
			//builder=new StringBuilder();
				tempPoint=new PointF();
			   //String attr = attributes.getValue("xxx");
			   tempPoint.x = Float.valueOf(attributes.getValue("xxx")).floatValue();
			   tempPoint.y = Float.valueOf(attributes.getValue("yy")).floatValue();
			   //builder=new StringBuilder();
		}
		else if (localName.toLowerCase().equalsIgnoreCase("isClosed")){
			if (attributes.getValue("isClosed").equalsIgnoreCase("-1")){
				this.tempPath.close();
			}
		}
		else if (localName.toLowerCase().equalsIgnoreCase("flagPairs")){
			flags = new ArrayList<FlagPair>();
		}
		else if (localName.equals("pair")){
		   myFlagPair = new FlagPair();
			//tempFlagPoint = new PointF();
		}
		else if (localName.equals("start")){
			   myFlagPair.setStart(Float.valueOf(attributes.getValue("sx")).floatValue(), Float.valueOf(attributes.getValue("sy")).floatValue()); 
		}
		else if (localName.equals("finish")){
			   myFlagPair.setFinish(Float.valueOf(attributes.getValue("fx")).floatValue(), Float.valueOf(attributes.getValue("fy")).floatValue());
			   myFlagPair.Pair();
		}
		
		else if (localName.equals("color")){
			 myFlagPair.setColor(Integer.valueOf(attributes.getValue("color")));
		}
		else if (localName.equalsIgnoreCase("distance")){
			myFlagPair.setDistanceInPixels(Double.valueOf(attributes.getValue("distance")));
		}

	}
	public void endElement(String uri, String localName, String qName)
		throws SAXException{
		if(localName.equals("roi")){
			this.roiPaths.add(tempPath);
		}
		else if(localName.toLowerCase().equals("point")){
			this.tempPath.addPoint(this.tempPoint.x, this.tempPoint.y, 0, 0, 1);
		}
		else if(localName.toLowerCase().equals("pair")){
			this.flags.add(myFlagPair);
		}
		
		/*else if(localName.toLowerCase().equals("x")){
			  tempPoint.x= Float.valueOf(builder.toString()).floatValue();
		}
		else if(localName.toLowerCase().equals("y")){
			  tempPoint.y= Float.valueOf(builder.toString()).floatValue();;
		}*/	
	}
	
	public void characters(char[] ch, int start, int length)
		throws SAXException {
		try {
		String tempString=new String(ch, start, length);
		builder.append(tempString); } catch (Exception x) {
			Log.d("", "yiuhio");
		}
	}
	
	
}
