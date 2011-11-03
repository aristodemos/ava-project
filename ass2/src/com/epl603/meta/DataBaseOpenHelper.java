package com.epl603.meta;

import java.util.ArrayList;

import com.epl603.classes.Book;
import com.epl603.classes.JSONParser;
import com.epl603.classes.Publisher;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	Context _context;

	public DataBaseOpenHelper(Context context, String databaseName, int version) {
		super(context, databaseName, null, version);
		_context = context;
	}

	public static final String PUBLISHERS_TABLE_CREATE = "CREATE TABLE "
			+ DatabaseMetaData.PublishersTableMetadata.TABLE_NAME + " ("
			+ DatabaseMetaData.PublishersTableMetadata._ID
			+ " INTEGER PRIMARY KEY, "
			+ DatabaseMetaData.PublishersTableMetadata.NAME
			+ " TEXT UNIQUE NOT NULL, "
			+ DatabaseMetaData.PublishersTableMetadata.URL + " TEXT);";

	public static final String BOOK_TABLE_CREATE = "CREATE TABLE "
			+ DatabaseMetaData.BooksTableMetadata.TABLE_NAME + " ("
			+ DatabaseMetaData.BooksTableMetadata._ID
			+ " INTEGER PRIMARY KEY, "
			+ DatabaseMetaData.BooksTableMetadata.TITLE
			+ " TEXT UNIQUE NOT NULL, "
			+ DatabaseMetaData.BooksTableMetadata.AUTHORS + " TEXT, "
			+ DatabaseMetaData.BooksTableMetadata.ISBN
			+ " TEXT UNIQUE NOT NULL, "
			+ DatabaseMetaData.BooksTableMetadata.PUBLISHER_ID
			+ " INTEGER NOT NULL);";

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(PUBLISHERS_TABLE_CREATE);
			db.execSQL(BOOK_TABLE_CREATE);

			JSONParser parser = new JSONParser(_context);
			ArrayList<Publisher> publishers = parser.parse();

			for (int i = 0; i < publishers.size(); i++) {
				Publisher p = publishers.get(i);
				ContentValues contentValues = new ContentValues();
				contentValues.put(
						DatabaseMetaData.PublishersTableMetadata.NAME,
						p.getName());
				contentValues.put(DatabaseMetaData.PublishersTableMetadata.URL,
						p.getUrl());

				long id = db.insert(
						DatabaseMetaData.PublishersTableMetadata.TABLE_NAME,
						null, contentValues);

				for (int k = 0; k < p.bookList.size(); k++) {
					Book b = p.bookList.get(k);

					contentValues = new ContentValues();
					contentValues.put(
							DatabaseMetaData.BooksTableMetadata.TITLE,
							b.getTitle());
					contentValues.put(
							DatabaseMetaData.BooksTableMetadata.AUTHORS,
							b.getAuthors());
					contentValues.put(DatabaseMetaData.BooksTableMetadata.ISBN,
							b.getISBN());
					contentValues.put(
							DatabaseMetaData.BooksTableMetadata.PUBLISHER_ID,
							id);

					db.insert(DatabaseMetaData.BooksTableMetadata.TABLE_NAME,
							null, contentValues);
				}
			}
		} catch (SQLException sqle) {
			Log.e("DATABASE", "Error in tables creation");
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "
				+ DatabaseMetaData.BooksTableMetadata.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "
				+ DatabaseMetaData.PublishersTableMetadata.TABLE_NAME);
		this.onCreate(db);
	}
}
