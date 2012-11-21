package ir.mohammadi.android.nightly.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserInfoDataSource {

	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	private String[] allColumns = { SQLiteHelper.COLUMN_USER_ID,
			SQLiteHelper.COLUMN_USER_EMAIL, SQLiteHelper.COLUMN_USER_PASSWORD };

	public UserInfoDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException {

		Log.i("UserInfoDataSource >> open >>", "...");
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public UserInfo createUserInfo(String user_id, String user_email,
			String user_password) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_USER_ID, user_id);
		values.put(SQLiteHelper.COLUMN_USER_EMAIL, user_email);
		values.put(SQLiteHelper.COLUMN_USER_PASSWORD, user_password);

		long insertId = database.insert(SQLiteHelper.TABLE_USER_INFO, null,
				values);

		Cursor cursor = database.query(SQLiteHelper.TABLE_USER_INFO,
				allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		UserInfo newUserInfo = cursorToUserInfo(cursor);
		cursor.close();

		Log.i("UserInfoDataSource >> createUserInfo >>", newUserInfo.toString());

		return newUserInfo;
	}

	public int deleteUserInfo() {
		return database.delete(SQLiteHelper.TABLE_USER_INFO, null, null);
	}

	public UserInfo getUserInfo() {

		UserInfo userInfo = null;
		Cursor cursor = database.query(SQLiteHelper.TABLE_USER_INFO,
				allColumns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			Log.i("UserInfoDataSource >> cursor >>", cursor.toString());
			userInfo = cursorToUserInfo(cursor);
			cursor.close();
		}

		return userInfo;
	}

	private UserInfo cursorToUserInfo(Cursor cursor) {

		Log.i("UserInfoDataSource >> cursorToUserInfo >>", cursor.toString());

		UserInfo userInfo = new UserInfo();

		userInfo.setUserId(cursor.getString(0));

		userInfo.setUser_email(cursor.getString(1));

		userInfo.setUserPassword(cursor.getString(2));

		return userInfo;

	}

}
