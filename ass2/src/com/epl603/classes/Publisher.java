package com.epl603.classes;

public class Publisher {
	private final String name;
	private final String url;
	
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
