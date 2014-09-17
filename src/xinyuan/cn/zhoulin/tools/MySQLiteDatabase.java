package xinyuan.cn.zhoulin.tools;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * 
 */
public class MySQLiteDatabase {

	private SQLiteDatabase db;
	private Cursor cursor;
	private MySQLiteOpenHelper mHelper;
	/**
	 * 
	 */
	public MySQLiteDatabase(Context mContext) {
		mHelper = new MySQLiteOpenHelper(mContext);
		db = mHelper.getReadableDatabase();
	}
	/**
	 * 
	 */
	public void getAerasList(String _id, List<String> areasList) {
		// 查询相应_id的信息
		cursor = db.rawQuery("SELECT * FROM Province WHERE _id=?",
				new String[] { _id });
		// 清空List
		areasList.clear();
		// 为List添加数据
		if (cursor.moveToFirst()) {
			String areaStr = cursor.getString(cursor.getColumnIndex("area"));
			String[] areasStr = areaStr.split(",");
			for (String area : areasStr) {
				areasList.add(area);
			}
		}
	}

	/** close DataBase */
	public void closeDB() {
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
	}

	/** close DataBase，Cursor */
	public void closeDB_Cursor() {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		if (mHelper != null) {
			mHelper.close();
			mHelper = null;
		}
	}

}
