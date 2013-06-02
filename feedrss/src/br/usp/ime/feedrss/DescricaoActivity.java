package br.usp.ime.feedrss;

import android.app.Activity;
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

		TextView titulo = (TextView) findViewById(R.id.titulo);
		WebView descricao = (WebView) findViewById(R.id.descricao);
		TextView categoria = (TextView) findViewById(R.id.categoria);

		Bundle bundle = this.getIntent().getExtras();

		titulo.setText(bundle.getString("TITULO"));
		descricao.loadData(bundle.getString("DESCRICAO"),
				"text/html; charset=UTF-8", null);
		categoria.setText(bundle.getString("CATEGORIA"));

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
				finish();
			}

		});
	}

}
