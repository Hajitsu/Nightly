package ir.mohammadi.android.nightly.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_USER_INFO = "userinfo";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_USER_EMAIL = "user_email";
	public static final String COLUMN_USER_PASSWORD = "user_password";

	private static final String DATABASE_NAME = "nightly.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_USER_INFO + " (" + COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_ID
			+ " TEXT(5) NOT NULL, " + COLUMN_USER_EMAIL
			+ " TEXT(50) NOT NULL, " + COLUMN_USER_PASSWORD
			+ " TEXT(50) NOT NULL );";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
		onCreate(db);
	}

}
