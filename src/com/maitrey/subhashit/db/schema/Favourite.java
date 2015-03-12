package com.maitrey.subhashit.db.schema;

import java.util.HashMap;

import com.maitrey.database.RecordEntity;

public class Favourite implements RecordEntity{

	public static final String TABLE_NAME = "FAVOURITE";
	
	public static final String UID = "_id";
	public static final String NAME = "NAME";
	public static final String TYPE = "TYPE";
	public static final String REC_NUMBER = "RECNUMBER";
	public static final String INDEX_NUMBER = "INDEXNUMBER";
	
	
	public static HashMap<String, String> m_columns = new HashMap<String,String>();
	
	static{
		m_columns.put(UID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		m_columns.put(NAME, "VARCHAR(255)");
		m_columns.put(TYPE, "INTEGER");
		m_columns.put(REC_NUMBER, "INTEGER");
		m_columns.put(INDEX_NUMBER, "INTEGER");
	}
	
	@Override
	public HashMap<String, String> getColumnDefine() {
		return m_columns;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

}
