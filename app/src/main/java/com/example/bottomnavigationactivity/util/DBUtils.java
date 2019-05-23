package com.example.bottomnavigationactivity.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils {
	private DatabaseHelper dbHelper;
	
	public DBUtils(Context context) {
		super();
		dbHelper = new DatabaseHelper(context);
	}
	
	/**
	 * 根据键获取设置表中 boolean 类型的值
	 * @param key
	 * @return
	 */
	public boolean getSettingByKey(String key) {
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try {
			sqLiteDatabase = dbHelper.getReadableDatabase();
			cursor = sqLiteDatabase.query(DatabaseHelper.SETTINGS_TABLE_NAME,
					null, null, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int n = cursor.getInt(cursor.getColumnIndex(key));
				return n == 0;
			}
		} catch (SQLiteException e) {
		} finally {
			release(sqLiteDatabase, cursor);
		}
		return true;
	}
	
	public void putSettingsValue(String key, boolean value) {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(key, value ? 0 : 1);
			sqLiteDatabase.update(DatabaseHelper.SETTINGS_TABLE_NAME, values, null, null);
		}catch(SQLiteException e) {
		}finally {
			release(sqLiteDatabase, null);
		}
	}
	
	public void addUserAndGesture(String userName, String gesture) {
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try{
			sqLiteDatabase = dbHelper.getWritableDatabase();
			final String tableName = DatabaseHelper.GESTURE_TABLE_NAME;
			cursor = sqLiteDatabase.query(tableName, null,
					DatabaseHelper.COLUMN_USER_NAME + "=?",
					new String[] { userName }, null, null, null);
			if (cursor.getCount() > 0) {
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.COLUMN_GESTURE_MD5, gesture);
				sqLiteDatabase.update(tableName, values,
						DatabaseHelper.COLUMN_USER_NAME + "=?",
						new String[] { userName });
			} else {
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.COLUMN_USER_NAME, userName);
				values.put(DatabaseHelper.COLUMN_GESTURE_MD5, gesture);
				sqLiteDatabase.insert(tableName, null, values);
			}
		}catch(SQLiteException e) {
		}finally {
			release(sqLiteDatabase, cursor);
		}
	}
	
	public String getGestureByUser(String userName) {
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try {
			sqLiteDatabase = dbHelper.getReadableDatabase();
			cursor = sqLiteDatabase.query(DatabaseHelper.GESTURE_TABLE_NAME, null,
					DatabaseHelper.COLUMN_USER_NAME + "=?",
					new String[] { userName }, null, null, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				String gesture = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_GESTURE_MD5));
				return gesture;
			}
		}catch(SQLiteException e) {
		}finally {
			release(sqLiteDatabase, cursor);
		}
		return null;
	}
	
	private void release(SQLiteDatabase sqLiteDatabase, Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
			sqLiteDatabase.close();
		}
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "mytextDb";
		private static final String SETTINGS_TABLE_NAME = "settingsTable";   // 存储设置里面的数据
		private static final String COLUMN_GESTURE = Constant.GESTURE_SWITCH_KEY;
		private static final String COLUMN_PUSH = Constant.PUSH_SWITCH_KEY;
		private static final String GESTURE_TABLE_NAME = "gestureTableName";  // 存储用户名和用户对应的手势密码
		private static final String COLUMN_USER_NAME = "userName";
		private static final String COLUMN_GESTURE_MD5 = "gesture";

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table if not exists " + SETTINGS_TABLE_NAME
					+ "(_id INTEGER primary key autoincrement, "
					+ COLUMN_GESTURE + " INTEGER, " + COLUMN_PUSH
					+ " INTEGER);");
			db.execSQL("create table if not exists " + GESTURE_TABLE_NAME
					+ "(_id INTEGER primary key autoincrement, "
					+ COLUMN_USER_NAME + " TEXT, " + COLUMN_GESTURE_MD5
					+ " TEXT);");
			db.execSQL("insert into " + SETTINGS_TABLE_NAME + "("
					+ COLUMN_GESTURE + ", " + COLUMN_PUSH
					+ ") values(0, 0)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
