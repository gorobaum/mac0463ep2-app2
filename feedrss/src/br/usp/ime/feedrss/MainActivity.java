package br.usp.ime.feedrss;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	List<Feed> feeds = new ArrayList<Feed>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DBAdapter dbadapter = new DBAdapter(getApplicationContext());
		dbadapter.open();
		if (isOnline()) {
			Toast.makeText(getApplicationContext(), "Atualizando dados...",
					Toast.LENGTH_SHORT).show();
			renovarFeedsNoBanco(dbadapter);
			Toast.makeText(getApplicationContext(),
					"Dados atualizados com sucesso.", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (dbadapter.isEmpty()) {
				mostraToastEFecha();
			} else {
				Toast.makeText(
						getApplicationContext(),
						"Mostrando dados antigos... Conectar a internet para atualizar.",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (!dbadapter.isEmpty()) {
			Cursor cursor = dbadapter.getAllFeeds();
			setarAdaptador(cursor);
		}
		dbadapter.close();
	}

	private void mostraToastEFecha() {
		Toast.makeText(
				getApplicationContext(),
				"Você precisa de acesso a internet para baixar os dados pela primeira vez!",
				Toast.LENGTH_SHORT).show();
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3500); // As I am using LENGTH_LONG in
										// Toast
					MainActivity.this.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null) && netInfo.isConnectedOrConnecting();
	}

	private List<Feed> carregaFeeds() {
		List<Integer> tipoSeminarios = new ArrayList<Integer>();
		tipoSeminarios.add(62);
		tipoSeminarios.add(37);
		tipoSeminarios.add(21);
		tipoSeminarios.add(29);
		for (Integer tipoSeminario : tipoSeminarios) {
			RequestFeeds requestFeeds = new RequestFeeds();
			requestFeeds
					.execute("http://www.ime.usp.br/index.php?option=com_eventlist&view=categoryevents&format=feed&id="
							+ tipoSeminario + "&type=rss");
			try {
				feeds.addAll(requestFeeds.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return feeds;
	}

	private void setarAdaptador(Cursor cursor) {
		String[] fromColumns = new String[] { DBAdapter.TITULO,
				DBAdapter.CATEGORIA };
		int[] toViews = new int[] { R.id.titulo, R.id.categoria };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.feeds, cursor, fromColumns, toViews, 0);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				Intent myIntent = new Intent(view.getContext(),
						DescricaoActivity.class);
				Feed feed = feeds.get(position);
				myIntent.putExtra("TITULO", feed.getTitulo());
				myIntent.putExtra("DESCRICAO", feed.getDescricao());
				myIntent.putExtra("CATEGORIA", feed.getCategoria());
				myIntent.putExtra("DATA", feed.getData());
				startActivityForResult(myIntent, 0);
			}
		});
	}

	private void renovarFeedsNoBanco(DBAdapter adapter) {
		adapter.deleteAllFeeds();
		List<Feed> feeds = carregaFeeds();
		adapter.insertNewFeeds(feeds);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
