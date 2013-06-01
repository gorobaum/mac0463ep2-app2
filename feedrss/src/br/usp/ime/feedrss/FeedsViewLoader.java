package br.usp.ime.feedrss;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

@SuppressLint("NewApi")
public class FeedsViewLoader extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	SimpleCursorAdapter mAdapter;

	static final String[] PROJECTION = new String[] {
			ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME };

	static final String SELECTION = "((" + ContactsContract.Data.DISPLAY_NAME
			+ " NOTNULL) AND (" + ContactsContract.Data.DISPLAY_NAME
			+ " != '' ))";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);

		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);

		String[] fromColumns = { ContactsContract.Data.DISPLAY_NAME };
		int[] toViews = { android.R.id.text1 }; 

		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, null, fromColumns,
				toViews, 0);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
				PROJECTION, SELECTION, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Do something when a list item is clicked
	}
}