package com.epl603.meta;

public class DatabaseMetaData {
	public static final String DATABASE_NAME = "assign2.db";
	public static final int DATABASE_VERSION = 1;
	
	public static final class PublishersTableMetadata implements android.provider.BaseColumns {
		private PublishersTableMetadata() {}
		
		public static final String TABLE_NAME = "publishers";
		public static final String DEFAULT_SORT_ORDER = "name ASC";
		
		// column names
		public static final String _ID = "id";
		public static final String NAME = "name";
		public static final String URL = "url";
		
		public static final String [] ALL_COLUMNS = { _ID, NAME, URL };
	}
	
	public static final class BooksTableMetadata implements android.provider.BaseColumns {
		private BooksTableMetadata() {}
		
		public static final String TABLE_NAME = "books";
		public static final String DEFAULT_SORT_ORDER = "title ASC";
		
		// column names
		public static final String _ID = "id";
		public static final String TITLE = "title";
		public static final String AUTHORS = "authors";
		public static final String ISBN = "isbn";
		public static final String PUBLISHER_ID = "publisher_id";
		
		public static final String [] ALL_COLUMNS = { TITLE, AUTHORS, ISBN, PUBLISHER_ID };
	}
}
