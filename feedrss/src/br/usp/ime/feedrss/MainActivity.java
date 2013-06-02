package br.usp.ime.feedrss;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		RequestFeeds requestFeeds = new RequestFeeds();
		requestFeeds
				.execute("http://www.ime.usp.br/index.php?option=com_eventlist&view=categoryevents&format=feed&id=62&type=rss");
		try {
			List<Feed> feeds = requestFeeds.get();
			DBAdapter dbadapter = new DBAdapter(getApplicationContext());
			renovarFeedsNoBanco(feeds, dbadapter);
			Cursor cursor = dbadapter.getAllFeeds();
			setarAdaptador(cursor);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void setarAdaptador(Cursor cursor) {
		String[] fromColumns = new String[] { DBAdapter.TITULO, DBAdapter.CATEGORIA };
		int[] toViews = new int[] { R.id.titulo, R.id.categoria };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.feeds, cursor, fromColumns, toViews, 0);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				// TODO: Abrir descrição do role.
			}
		});
	}

	private void renovarFeedsNoBanco(List<Feed> feeds, DBAdapter adapter) {
		adapter.open();
		adapter.deleteAllFeeds();
		adapter.insertNewFeeds(feeds);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
