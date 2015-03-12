package com.maitrey.database.helper;

import android.database.Cursor;


public class EZCursorHelper{

	public static int getInt(Cursor cursor, String column){
		int index = cursor.getColumnIndex(column);
		return cursor.getInt(index);
	}
	
	public static long getLong(Cursor cursor, String column){
		int index = cursor.getColumnIndex(column);
		return cursor.getLong(index);
	}
	
	public static String getString(Cursor cursor, String column){
		int index = cursor.getColumnIndex(column);
		return cursor.getString(index);
	}
}
