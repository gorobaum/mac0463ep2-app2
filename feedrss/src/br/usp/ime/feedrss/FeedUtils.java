package br.usp.ime.feedrss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class FeedUtils {

	public static String geraLinhaInsertFeed(Feed feed) {
		return "( " + feed.getTitulo() + ", " + feed.getLink() + ", "
				+ feed.getDescricao() + ", " + feed.getCategoria() + ")";
	}

	public List<Feed> montaFeeds(List<JSONArray> jsons) {
		List<Feed> feeds = new ArrayList<Feed>();
		for (JSONArray jsonArray : jsons) {
			feeds.addAll(montaFeedCategoria(jsonArray));
		}
		return feeds;
	}

	private List<Feed> montaFeedCategoria(JSONArray jsonArray) {
		List<Feed> feeds = new ArrayList<Feed>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				feeds.add(montaFeed(jsonArray.get(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return feeds;
	}
	
	private Feed montaFeed(Object object) {
		Feed feed = new Feed(null, null, null, null);
		return feed;
	}
}
