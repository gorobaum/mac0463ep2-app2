package br.usp.ime.feedrss;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

public class RequestFeeds extends AsyncTask<String, Void, List<Feed>> {

	protected List<Feed> doInBackground(String... urls) {
		try {
			return getFeeds(urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Feed> getFeeds(String[] urls) {
		for (int i = 0; i < urls.length; i++) {
			try {
				URL url = new URL(urls[i]);
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(url.openStream(), "UTF_8");
				List<Feed> feeds = carregaFeedsNoBanco(parser);
				return feeds;
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private List<Feed> carregaFeedsNoBanco(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		int eventType = parser.getEventType();
		List<Feed> feeds = new ArrayList<Feed>();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG
					&& parser.getName().equalsIgnoreCase("item")) {
				feeds.add(processaItem(parser));
			}
			eventType = parser.next();
		}
		return feeds;
	}

	public Feed processaItem(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		int eventType = parser.next();
		Feed feed = new Feed();
		while (!(eventType == XmlPullParser.END_TAG && parser.getName().equals(
				"item"))) {
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equalsIgnoreCase("title")) {
					feed.setTitulo(parser.nextText());
				} else if (parser.getName().equalsIgnoreCase("description")) {
					String descricao = parser.nextText();
					feed.setDescricao(descricao);
					feed.setData(pegaDataDaDescricao(descricao));
				} else if (parser.getName().equalsIgnoreCase("category")) {
					feed.setCategoria(parser.nextText());
				}
			}
			eventType = parser.next();
		}
		return feed;
	}

	private long pegaDataDaDescricao(String descricao) {
		Calendar c = Calendar.getInstance();
		String[] lines = descricao.split("<br\\s*/>");
		for (String string : lines) {
			if (string.contains("Data: ")) {
				String[] tempo = string.subSequence(6, 16).toString()
						.split("\\.");
				c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tempo[0]));
				c.set(Calendar.MONTH, Integer.valueOf(tempo[1]) - 1);
				c.set(Calendar.YEAR, Integer.valueOf(tempo[2]));
			}
			if (string.contains("Hora: ")) {
				String[] tempo = string.subSequence(6, 11).toString()
						.split("\\.");
				c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(tempo[0]));
				c.set(Calendar.MINUTE, Integer.valueOf(tempo[1]));
				c.set(Calendar.SECOND, 0);
			}
		}
		return c.getTime().getTime();
	}
}
