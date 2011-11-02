package com.epl603.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.epl603.classes.Book;
import com.epl603.classes.Publisher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseHelper {

	private final DataBaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase sqLiteDatabase = null;

	private static Map<Context,DatabaseHelper> databaseHelpers = new HashMap<Context,DatabaseHelper>();

	public static DatabaseHelper getDatabaseHelper(final Context context)
	{
		if(!databaseHelpers.containsKey(context.getApplicationContext()))
		{
			databaseHelpers.put(context, new DatabaseHelper(context));
		}
		return databaseHelpers.get(context.getApplicationContext());
	}

	private DatabaseHelper(final Context context)
	{
		this.databaseOpenHelper = new DataBaseOpenHelper(context, DatabaseMetaData.DATABASE_NAME, DatabaseMetaData.DATABASE_VERSION);
	}

	private SQLiteDatabase getDatabase()
	{
		if(sqLiteDatabase == null)
		{
			try {
			sqLiteDatabase = databaseOpenHelper.getWritableDatabase();
			}
			catch(Exception ex)
			{
				Log.d("EX","catch the exception");
			}
		}
		Log.d("", "sqlite loaded");
		return sqLiteDatabase;
	}

	public void cleanup()
	{
		if(this.sqLiteDatabase != null)
		{
			this.sqLiteDatabase.close();
			this.sqLiteDatabase = null;
		}
	}
	
	public void insertPublisher(final Publisher publisher)
	{
		this.insertPublisher(publisher.getName(), publisher.getUrl());
	}
	
	public long insertPublisher(String name, String url)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(DatabaseMetaData.PublishersTableMetadata.NAME, name);
		contentValues.put(DatabaseMetaData.PublishersTableMetadata.URL, url);
		
		return getDatabase().insert(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, null, contentValues);
	}
	
	public void insertBook(final Book book)
	{
		this.insertBook(book.getTitle(), book.getAuthors(), book.getISBN(), book.getPublisher_id());
	}
	
	public void insertBook(String title, String authors, String isbn, long publisher_id)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(DatabaseMetaData.BooksTableMetadata.TITLE, title);
		contentValues.put(DatabaseMetaData.BooksTableMetadata.AUTHORS, authors);
		contentValues.put(DatabaseMetaData.BooksTableMetadata.ISBN, isbn);
		contentValues.put(DatabaseMetaData.BooksTableMetadata.PUBLISHER_ID, publisher_id);
		
		getDatabase().insert(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, null, contentValues);
	}
	
	public void deleteBookByTitle(String title)
	{
		getDatabase().delete(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, 
				DatabaseMetaData.BooksTableMetadata.TITLE + "='" + title + "'", null);
	}
	
	public void deleteBookByISBN(String isbn) {
		// TODO Auto-generated method stub
		getDatabase().delete(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, 
				DatabaseMetaData.BooksTableMetadata.ISBN + "='" + isbn + "'", null);		
	}
	
	public int deleteAllBooks()
	{
		return getDatabase().delete(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, "1", null);
	}
	
	public void deletePublisherByName(String name)
	{
		getDatabase().delete(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, 
				DatabaseMetaData.PublishersTableMetadata.NAME + "='" + name + "'", null);
	}
	
	public int deleteAllPublishers()
	{
		return getDatabase().delete(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, "1", null);
	}
	
	public Vector<Book> getAllBooks()
	{
		final Vector<Book> allBooks = new Vector<Book>();
		
		final Cursor cursor = getDatabase().query(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, // table name
				DatabaseMetaData.BooksTableMetadata.ALL_COLUMNS, // select columns
				null, // selection
				null, // selection arguments
				null, // group by
				null, // having
				DatabaseMetaData.BooksTableMetadata.DEFAULT_SORT_ORDER); // order by
		
		final int TITLE_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.TITLE);
		final int AUTHORS_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.AUTHORS);
		final int ISBN_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.ISBN);
		final int PUBLISHER_ID_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.PUBLISHER_ID);
		
		cursor.moveToFirst();
		for (int i=0; i<cursor.getCount(); i++)
		{
			final String title = cursor.getString(TITLE_COLUMN_INDEX);
			final String authors = cursor.getString(AUTHORS_COLUMN_INDEX);
			final String isbn = cursor.getString(ISBN_COLUMN_INDEX);
			final int publisher_id = cursor.getInt(PUBLISHER_ID_COLUMN_INDEX);
			allBooks.add(new Book(title, authors, isbn, publisher_id));
			cursor.moveToNext();
		}
		return allBooks;
	}
	
	public Vector<Publisher> getAllPublishers()
	{
		final Vector<Publisher> allPublishers = new Vector<Publisher>();
		
		final Cursor cursor = getDatabase().query(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, 
				DatabaseMetaData.PublishersTableMetadata.ALL_COLUMNS,
				"", null, "", "", DatabaseMetaData.PublishersTableMetadata.DEFAULT_SORT_ORDER);
		
		final int NAME_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.NAME);
		final int URL_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.URL);
		
		cursor.moveToFirst();
		for (int i=0; i<cursor.getCount(); i++)
		{
			final String name = cursor.getString(NAME_COLUMN_INDEX);
			final String url = cursor.getString(URL_COLUMN_INDEX);
			allPublishers.add(new Publisher(i, name, url));
			cursor.moveToNext();
		}
		return allPublishers;
	}
	
	public ArrayList<String> getAllPublishersNames()
	{
		final ArrayList<String> allPublishersNames = new ArrayList<String>();
		
		final Cursor cursor = getDatabase().query(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, 
				DatabaseMetaData.PublishersTableMetadata.ALL_COLUMNS,
				"", null, "", "", DatabaseMetaData.PublishersTableMetadata.DEFAULT_SORT_ORDER);
		
		final int NAME_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.NAME);
		final int URL_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.URL);
		
		cursor.moveToFirst();
		for (int i=0; i<cursor.getCount(); i++)
		{
			final String name = cursor.getString(NAME_COLUMN_INDEX);
			final String url = cursor.getString(URL_COLUMN_INDEX);
			allPublishersNames.add(name);
			cursor.moveToNext();
		}
		return allPublishersNames;
	}
	
	public Book getBookByTitle(final String titleQuery)
	{
		final Cursor cursor = getDatabase().query(DatabaseMetaData.BooksTableMetadata.TABLE_NAME, 
				DatabaseMetaData.BooksTableMetadata.ALL_COLUMNS,
				DatabaseMetaData.BooksTableMetadata.TITLE + "='" + titleQuery + "'", null, "", "", 
				DatabaseMetaData.BooksTableMetadata.DEFAULT_SORT_ORDER);
		
		final int TITLE_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.TITLE);
		final int AUTHORS_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.AUTHORS);
		final int ISBN_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.ISBN);
		final int PUBLISHER_ID_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.PUBLISHER_ID);
		
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			final String title = cursor.getString(TITLE_COLUMN_INDEX);
			final String authors = cursor.getString(AUTHORS_COLUMN_INDEX);
			final String isbn = cursor.getString(ISBN_COLUMN_INDEX);
			final int publisher_id = cursor.getInt(PUBLISHER_ID_COLUMN_INDEX);
				
			return new Book(title, authors, isbn, publisher_id);
		}
		else 
			return null;
	}
	
	public Publisher getPublisherByName(final String nameQuery)
	{
		final Cursor cursor = getDatabase().query(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, 
				DatabaseMetaData.PublishersTableMetadata.ALL_COLUMNS,
				DatabaseMetaData.PublishersTableMetadata.NAME + "='" + nameQuery + "'",
				null, "", "", DatabaseMetaData.PublishersTableMetadata.DEFAULT_SORT_ORDER);
		
		final int NAME_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.NAME);
		final int URL_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.URL);
		
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			final String name = cursor.getString(NAME_COLUMN_INDEX);
			final String url = cursor.getString(URL_COLUMN_INDEX);
			return new Publisher(cursor.getPosition(), name, url);
		}
		else
			return null;
	}
	public int getPublisherByIdName(final String nameQuery)
	{
		final Cursor cursor = getDatabase().query(DatabaseMetaData.PublishersTableMetadata.TABLE_NAME, 
				DatabaseMetaData.PublishersTableMetadata.ALL_COLUMNS,
				DatabaseMetaData.PublishersTableMetadata.NAME + "='" + nameQuery + "'",
				null, "", "", DatabaseMetaData.PublishersTableMetadata.DEFAULT_SORT_ORDER);
		
		final int NAME_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.NAME);
		final int URL_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.PublishersTableMetadata.URL);
		
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			final String name = cursor.getString(NAME_COLUMN_INDEX);
			final String url = cursor.getString(URL_COLUMN_INDEX);
			return cursor.getPosition();
		}
		else
			return (Integer) null;
	}

	public String findBookTitleById(long id) {
		final Cursor cursor = getDatabase().query(DatabaseMetaData.BooksTableMetadata.TABLE_NAME,
				DatabaseMetaData.BooksTableMetadata.ALL_COLUMNS, 
				DatabaseMetaData.BooksTableMetadata._ID+"='"+id+"'", 
				null, "", "", DatabaseMetaData.BooksTableMetadata.DEFAULT_SORT_ORDER);
		// TODO Auto-generated method stub
		final int TITLE_COLUMN_INDEX = cursor.getColumnIndex(DatabaseMetaData.BooksTableMetadata.TITLE);
		
		
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			final String name = cursor.getString(TITLE_COLUMN_INDEX);
			
			return name;
		}
		else
			return null;
	}


	
}
