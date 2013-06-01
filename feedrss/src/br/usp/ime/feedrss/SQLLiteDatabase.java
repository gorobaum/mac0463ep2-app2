package br.usp.ime.feedrss;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteDatabase extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ep2";
	private static final String DATABASE_TABLE = "feeds";
	private static final String TITULO = "titulo";
	private static final String LINK = "link";
	private static final String DESCRICAO = "descricao";
	private static final String CATEGORIA = "categoria";
	private static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE + " (_id INTEGER PRIMERY KEY AUTOINCREMENT, " + TITULO
			+ " TEXT NOT NULL, " + LINK + " TEXT NOT NULL," + DESCRICAO
			+ " TEXT NOT NULL, " + CATEGORIA + " TEXT NOT NULL);";

	public SQLLiteDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DICTIONARY_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		return;
	}

	public void saveAllFeeds(List<Feed> feeds) {
		for (Feed feed : feeds) {
			saveFeed(feed);
		}
	}

	public void saveFeed(Feed feed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TITULO, feed.getTitulo());
		initialValues.put(LINK, feed.getLink());
		initialValues.put(DESCRICAO, feed.getDescricao());
		initialValues.put(CATEGORIA, feed.getCategoria());
		this.getWritableDatabase().insert(DATABASE_TABLE, null, initialValues);
		this.close();
	}
}
