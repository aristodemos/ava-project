package com.epl603.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.epl603.assign2.AddBookActivity;

public class IncomingReceiver extends BroadcastReceiver {

	private static final String CUSTOM_INTENT = "epl603.assignment2.ACTION_BOOK_LOOKUP_RESULT";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(CUSTOM_INTENT)) {

			String publisher = "";
			String authors = "";
			String ISBN = "";
			String title = "";
			String msg = "";

			String result = intent
					.getStringExtra("epl603.assignment2.EXTRA_LOOKUP_RESULT");
			if (result
					.compareTo("epl603.assignment2.EXTRA_LOOKUP_RESULT_SUCCEEDED") == 0) {
				publisher = intent
						.getStringExtra("epl603.assignment2.EXTRA_BOOK_PUBLISHER");
				authors = intent
						.getStringExtra("epl603.assignment2.EXTRA_BOOK_AUTHORS");
				ISBN = intent
						.getStringExtra("epl603.assignment2.EXTRA_BOOK_ISBN");
				title = intent
						.getStringExtra("epl603.assignment2.EXTRA_BOOK_TITLE");
			} else if (result
					.compareTo("epl603.assignment2.EXTRA_LOOKUP_RESULT_INVALID_QUERY") == 0) {
				msg = "invalid_query";
			} else if (result
					.compareTo("epl603.assignment2.EXTRA_LOOKUP_RESULT_IO_FAIL") == 0) {
				msg = "io_fail";
			}

			Intent myIntent = new Intent(context, AddBookActivity.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			myIntent.putExtra("publisher", publisher);
			myIntent.putExtra("authors", authors);
			myIntent.putExtra("ISBN", ISBN);
			myIntent.putExtra("title", title);
			myIntent.putExtra("msg", msg);
			context.startActivity(myIntent);

			Log.d("BOOOOOOOK", publisher + " " + authors + " " + ISBN + " "
					+ title);
			Log.d("RESUUUUULT", intent
					.getStringExtra("epl603.assignment2.EXTRA_LOOKUP_RESULT"));
			/*
			 * Bundle extras = intent.getExtras(); Set<String> ks =
			 * extras.keySet(); Iterator<String> iterator = ks.iterator(); while
			 * (iterator.hasNext()) { Log.d("KEY", iterator.next()); }
			 */
		}
	}
}
