package com.epl603.classes;

public class Book {
	private final String Title;
	private final String Authors;
	private final String ISBN;
	private final long Publisher_id;
	
	public Book(final String title, final String authors, final String ISBN, final int publisher_id)
	{
		this.Title = title;
		this.Authors = authors;
		this.ISBN = ISBN;
		this.Publisher_id = publisher_id;
	}

	public String getTitle() {
		return Title;
	}

	public String getAuthors() {
		return Authors;
	}

	public String getISBN() {
		return ISBN;
	}

	public long getPublisher_id() {
		return Publisher_id;
	}
}
