package com.epl603.classes;

import java.util.ArrayList;

public class Publisher {
	private final String name;
	private final String url;
	public ArrayList<Book> bookList = new ArrayList<Book>();
	
	public Publisher(final String name, final String url)
	{
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
}
