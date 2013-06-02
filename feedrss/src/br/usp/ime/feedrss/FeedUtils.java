package br.usp.ime.feedrss;

public class FeedUtils {

	public static String geraLinhaInsertFeed(Feed feed) {
		return "( \'" + feed.getTitulo() + "\' , \'" + feed.getDescricao()
				+ "\', \'" + feed.getCategoria() + "\' );";
	}
}
