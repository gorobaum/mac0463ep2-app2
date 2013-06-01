package br.usp.ime.feedrss;

import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

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
			DBAdapter adapter = new DBAdapter(getApplicationContext());
			salvarFeedsNoBanco(feeds, adapter);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void salvarFeedsNoBanco(List<Feed> feeds, DBAdapter adapter) {
		adapter.open(); 
		adapter.insertAllFeeds(feeds);
		adapter.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
