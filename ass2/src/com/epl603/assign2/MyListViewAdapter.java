package com.epl603.assign2;

import java.util.Vector;

import com.epl603.classes.Book;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter {

	private Vector<Book> books;
	Activity activity;

	public MyListViewAdapter(Activity activityfrom, Vector<Book> booklist) {
		super();
		activity = activityfrom;
		books = booklist;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return books.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return books.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {
		TextView txtTitle;
		TextView txtAuthors;
		TextView txtISBN;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();
		
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_view_cell, null);
			holder = new ViewHolder();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txt1);
			holder.txtAuthors = (TextView) convertView.findViewById(R.id.txt2);
			holder.txtISBN = (TextView) convertView.findViewById(R.id.txt3);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Book b = books.get(position);
		holder.txtTitle.setText(b.getTitle());
		holder.txtAuthors.setText(b.getAuthors());
		holder.txtISBN.setText(b.getISBN());
		
		return convertView;
	}

}
