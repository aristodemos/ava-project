package com.epl603.ava.classes;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.PointF;

public class XMLParser extends DefaultHandler {

	/*private Context _context;

	public XMLParser(Context context) {
		_context = context;
	} */	
	
	public ArrayList<PointPath> roiPaths;
	PointPath tempPath;
	
	StringBuilder builder;
	PointF tempPoint;
	
	public void startDocument() throws SAXException{
		roiPaths = new ArrayList<PointPath>();
	}	
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException {
		
		if (localName.equalsIgnoreCase("roi")){
			tempPath=new PointPath();
			builder=new StringBuilder();
		}
		else if(localName.toLowerCase().equals("point")){
			   tempPoint=new PointF();
		}
	}
	public void endElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException{
		if(localName.toLowerCase().equals("roi")){
			this.roiPaths.add(tempPath);
		}
		else if(localName.toLowerCase().equals("point")){
			this.tempPath.addPoint(this.tempPoint.x, this.tempPoint.y, 0, 0, 1);
		}
		else if(localName.toLowerCase().equals("x")){
			  tempPoint.x= Float.valueOf(attributes.getValue(0)).floatValue();
		}
		else if(localName.toLowerCase().equals("y")){
			  tempPoint.y= Float.valueOf(attributes.getValue(0)).floatValue();
		}	
	}
	
	public void characters(char[] ch, int start, int length)
		throws SAXException {
		String tempString=new String(ch, start, length);
		builder.append(tempString);
	}
	
	
}
