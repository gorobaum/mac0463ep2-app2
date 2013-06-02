package br.usp.ime.feedrss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class DescricaoActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.descricaolayout);

		TextView txtTitulo = (TextView) findViewById(R.id.titulo);
		WebView descricao = (WebView) findViewById(R.id.descricao);
		TextView categoria = (TextView) findViewById(R.id.categoria);

		Bundle bundle = this.getIntent().getExtras();

		txtTitulo.setText(bundle.getString("TITULO"));
		descricao.loadData(bundle.getString("DESCRICAO"),
				"text/html; charset=UTF-8", null);
		categoria.setText(bundle.getString("CATEGORIA"));

		final long data = bundle.getLong("DATA");
		final String titulo = bundle.getString("TITULO");

		Button voltar = (Button) findViewById(R.id.voltar);

		voltar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}

		});

		Button calendario = (Button) findViewById(R.id.calendario);

		calendario.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime", data);
				intent.putExtra("allDay", false);
				intent.putExtra("endTime", data + 2 * 60 * 60 * 1000);
				intent.putExtra("title", titulo);
				startActivity(intent);
			}

		});
	}

}
