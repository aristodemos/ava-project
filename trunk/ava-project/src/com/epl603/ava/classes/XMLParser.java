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
		
		if (localName.equalsIgnoreCase(AppConstants.ROIS)){
			builder=new StringBuilder();
		}
		if (localName.equalsIgnoreCase(AppConstants.ROI)){
			tempPath=new PointPath();
			
		}
		else if(localName.toLowerCase().equals(AppConstants.POINT)){
			//builder=new StringBuilder();
				tempPoint=new PointF();
			   //String attr = attributes.getValue("xxx");
			   tempPoint.x = Float.valueOf(attributes.getValue(AppConstants.POINT_X)).floatValue();
			   tempPoint.y = Float.valueOf(attributes.getValue(AppConstants.POINT_Y)).floatValue();
			   //builder=new StringBuilder();
		}
		else if (localName.toLowerCase().equalsIgnoreCase(AppConstants.IS_CLOSED)){
			if (attributes.getValue(AppConstants.IS_CLOSED).equalsIgnoreCase("-1")){
				this.tempPath.close();
			}
		}
		else if (localName.toLowerCase().equalsIgnoreCase(AppConstants.FLAG_PAIRS)){
			flags = new ArrayList<FlagPair>();
		}
		else if (localName.equals(AppConstants.PAIR)){
		   myFlagPair = new FlagPair();
			//tempFlagPoint = new PointF();
		}
		else if (localName.equals(AppConstants.START)){
			   myFlagPair.setStart(Float.valueOf(attributes.getValue(AppConstants.START_X)).floatValue(), Float.valueOf(attributes.getValue(AppConstants.START_Y)).floatValue()); 
		}
		else if (localName.equals(AppConstants.FINISH)){
			   myFlagPair.setFinish(Float.valueOf(attributes.getValue(AppConstants.FINISH_X)).floatValue(), Float.valueOf(attributes.getValue(AppConstants.FINISH_Y)).floatValue());
			   myFlagPair.Pair();
		}
		
		else if (localName.equals(AppConstants.COLOR)){
			 myFlagPair.setColor(Integer.valueOf(attributes.getValue(AppConstants.COLOR)));
		}
		else if (localName.equalsIgnoreCase(AppConstants.DISTANCE)){
			myFlagPair.setDistanceInPixels(Double.valueOf(attributes.getValue(AppConstants.DISTANCE)));
		}

	}
	public void endElement(String uri, String localName, String qName)
		throws SAXException{
		if(localName.equals(AppConstants.ROI)){
			this.roiPaths.add(tempPath);
		}
		else if(localName.toLowerCase().equals(AppConstants.POINT)){
			this.tempPath.addPoint(this.tempPoint.x, this.tempPoint.y, 0, 0, 1);
		}
		else if(localName.toLowerCase().equals(AppConstants.PAIR)){
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
