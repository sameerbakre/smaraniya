package com.maitrey.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maitrey.subhashit.db.TableList;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	public static final String DATABASE_NAME = "smaraniyam";
	public static final int DATABASE_VERSION = 2;
	
	private Context m_context; 
	private static String m_createTemp = "CREATE TABLE ";
	private static String m_dropTemp = "drop table if exists ";
	
	private SQLiteDatabase m_db = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		m_context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db,TableList.favourite);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db, TableList.favourite);
		onCreate(db);
	}
	
	public long insertData(ContentValues values,RecordEntity record){
		if(m_db == null)
			m_db = getWritableDatabase();
		long id = m_db.insert(record.getTableName(), null, values);
		//m_db.close();
		return id;
	}
	public Cursor selectData(RecordEntity record,ContentValues conditions, String[] columns){
		return selectData(record, conditions, columns, null);
	}
	
	public Cursor selectData(RecordEntity record,ContentValues conditions, String[] columns,String orderBy){
		if(m_db == null)
			m_db = getWritableDatabase();

		String[] selColumns = {};
		
		if(columns == null || columns.length == 0){
			record.getColumnDefine().keySet().toArray(selColumns);
		}else
			selColumns = columns;
		String[] condition = getCondtionStatement(conditions);
		
		Cursor cursor = null;
		if(condition == null)
			cursor = m_db.query(record.getTableName(), selColumns, null,null, null, null, orderBy);
		else
			cursor = m_db.query(record.getTableName(), selColumns, condition[0], condition[1].split(","), null, null, orderBy);
		return cursor;
	}
	
	
	private String[] getCondtionStatement(ContentValues conditions) {
		if(conditions == null || conditions.valueSet() == null)
			return null;
		
		Iterator<Entry<String, Object>> conditionIt = conditions.valueSet().iterator();
		
		StringBuilder colBuff = new StringBuilder();
		StringBuilder valueBuff = new StringBuilder();
		while(conditionIt.hasNext()){
			Entry<String, Object> condition = conditionIt.next();
			
			Object value = condition.getValue();
			
			if( value instanceof String || value instanceof Integer || value instanceof Long){
				if(colBuff.length() != 0){
					colBuff.append(" AND ");
					valueBuff.append(",");
				}
				colBuff.append( condition.getKey() + "=?");
				valueBuff.append(value);
			}
		}
		
		return new String[] {colBuff.toString(),valueBuff.toString()};
	}

	public void dropTable(SQLiteDatabase db, RecordEntity record){
		String tableName = record.getTableName();
		if(tableName == null)
			return;
		
		StringBuilder dropTable = new StringBuilder(m_dropTemp);
		dropTable.append(tableName);
		dropTable.append(";");
		
		try{
			db.execSQL(dropTable.toString());
		}catch(SQLException ex){
		}
		
	}
	
	
	public void createTable(SQLiteDatabase db, RecordEntity record){
		String tableName = record.getTableName();
		if(tableName == null)
			return;
		
		StringBuilder createtable = new StringBuilder(m_createTemp);
		createtable.append(tableName);
		
		HashMap<String, String> columns = record.getColumnDefine();
		if(columns == null || columns.size() == 0)
			return;

		createtable.append(" (");
		Iterator<Entry<String, String>> columnIt = columns.entrySet().iterator();
		boolean isFirst = true;
		while(columnIt.hasNext()){
			Entry<String, String> colDef = columnIt.next();
			if(!isFirst){
				createtable.append(", ");
			}else{
				isFirst = false;
			}
			createtable.append(colDef.getKey());
			createtable.append(" ");
			createtable.append(colDef.getValue());
		}
		
		createtable.append(" );");
		
		try{
			db.execSQL(createtable.toString());
		}catch(SQLException ex){
		}
		
	}
	
	public int updateRow(RecordEntity record,ContentValues values, ContentValues conditions){
		if(m_db == null)
			m_db = getWritableDatabase();
		
		String[] condition = getCondtionStatement(conditions);
		int rows = m_db.update(record.getTableName(), values, condition[0], condition[1].split(","));
		return rows;
	}
	
	public int deleteRow(RecordEntity record,ContentValues conditions){
		if(m_db == null)
			m_db = getWritableDatabase();
		
		String[] condition = getCondtionStatement(conditions);
		int rows = m_db.delete(record.getTableName(), condition[0], condition[1].split(","));
		return rows;
	}

}
