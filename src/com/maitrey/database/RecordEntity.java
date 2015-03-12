package com.maitrey.database;

import java.util.HashMap;

public interface RecordEntity {
	public HashMap<String,String> getColumnDefine();
	
	public String getTableName();
}
