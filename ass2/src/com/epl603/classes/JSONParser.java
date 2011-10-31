package com.epl603.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.epl603.assign2.R.raw;

import android.content.Context;
import android.util.Log;

public class JSONParser {

	private Context _context;

	public JSONParser(Context context) {
		_context = context;
	}

	public ArrayList<Publisher> parse() {
		
		ArrayList<Publisher> list = new ArrayList<Publisher>();
		
		try {
			String jString = getRawString(raw.books);
			JSONObject jObject = new JSONObject(jString);
			JSONArray publishersArray = jObject.getJSONArray("publishers");
			
			for (int i=0; i<publishersArray.length(); i++)
			{
				JSONObject publishersObject = publishersArray.getJSONObject(i);
				
				String attributeName = publishersObject.getString("name");
				String attributeURL = publishersObject.getString("url");
				
				Publisher publisher = new Publisher(attributeName, attributeURL);
				
				JSONArray booksArray = publishersObject.getJSONArray("books");
				for (int k=0; k<booksArray.length(); k++)
				{
					JSONObject bookObject = booksArray.getJSONObject(k);
					String attributeTitle = bookObject.getString("title");
					String attributeAuthors = bookObject.getString("authors");
					String attributeISBN = bookObject.getString("isbn");
					
					publisher.bookList.add(new Book(attributeTitle, attributeAuthors, attributeISBN, -1));
				}
				
				list.add(publisher);
			}
			
		} catch (JSONException e) {
			Log.e("JSONParser", "Unable to parse json file");
			return new ArrayList<Publisher>();
		}
	
		return list;
	}

	private String getRawString(int resID) {
		InputStream inputStream = _context.getResources()
				.openRawResource(resID);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		StringBuilder text = new StringBuilder();

		try {
			while ((line = buffreader.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}
}
