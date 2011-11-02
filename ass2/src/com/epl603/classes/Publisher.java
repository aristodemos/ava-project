package com.epl603.classes;

import java.util.ArrayList;

public class Publisher {
	private final int id;
	private final String name;
	private final String url;
	public ArrayList<Book> bookList = new ArrayList<Book>();
	
	public Publisher(final int id, final String name, final String url)
	{
		this.id = id;
		this.name = name;
		this.url = url;
	}
	
	public int getId() {return id;}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
}
