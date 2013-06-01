package br.usp.ime.feedrss;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final String ID = "_id";
	private static final String TITULO = "titulo";
	private static final String LINK = "link";
	private static final String DESCRICAO = "descricao";
	private static final String CATEGORIA = "categoria";
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "ep2";
	private static final String DATABASE_TABLE = "feeds";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + TITULO
			+ " TEXT NOT NULL, " + LINK + " TEXT NOT NULL, " + DESCRICAO
			+ " TEXT NOT NULL, " + CATEGORIA + " TEXT NOT NULL);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public List<Long> insertAllFeeds(List<Feed> feeds) {
		List<Long> ids = new ArrayList<Long>();
		for (Feed feed : feeds) {
			ids.add(insertFeed(feed));
		}
		return ids;
	}

	public long insertFeed(Feed feed) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(TITULO, feed.getTitulo());
		initialValues.put(LINK, feed.getLink());
		initialValues.put(DESCRICAO, feed.getDescricao());
		initialValues.put(CATEGORIA, feed.getCategoria());
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteTitle(long rowId) {
		return db.delete(DATABASE_TABLE, ID + "=" + rowId, null) > 0;
	}

	public Cursor getAllFeeds() {
		return db.query(DATABASE_TABLE, new String[] { ID, TITULO, LINK,
				DESCRICAO, CATEGORIA }, null, null, null, null, null);
	}

	public Cursor getFeed(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { ID,
				TITULO, LINK, DESCRICAO, CATEGORIA }, ID + "=" + rowId, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getFeedPorTitulo(String titulo) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { ID,
				TITULO, LINK, DESCRICAO, CATEGORIA }, TITULO + "LIKE"
				+ "%titulo", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateTitle(long rowId, Feed feed) {
		ContentValues args = new ContentValues();
		args.put(TITULO, feed.getTitulo());
		args.put(LINK, feed.getLink());
		args.put(DESCRICAO, feed.getDescricao());
		args.put(CATEGORIA, feed.getCategoria());
		return db.update(DATABASE_TABLE, args, ID + "=" + rowId, null) > 0;
	}
}