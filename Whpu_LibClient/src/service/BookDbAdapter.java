package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *  ’≤ÿº– ˝æ›ø‚  ≈‰∆˜
 * @author Administrator
 *
 */
public class BookDbAdapter {
	private static final String DATABASE_NAME="mystore.db";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE="myStore";
	
	private static final String DATABASE_CREATE=
			"create table myStore("+
			" book_title varchar(50),"+
			" book_author varchar(50),"+
			" book_press varchar(50),"+
			" book_pages varchar(50),"+
			" book_price varchar(50),"+
			" book_noFor varchar(50) PRIMARY KEY,"+
			" book_detail varchar(50))";
	private Context mCtx=null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.i("sql", DATABASE_CREATE);
			Log.i("***********onCreate*********", "onCreate");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	public BookDbAdapter(Context ctx){
		this.mCtx=ctx;
	}
	public BookDbAdapter open() throws SQLException{
		dbHelper=new DatabaseHelper(mCtx);
		db=dbHelper.getWritableDatabase();
		return this;
	}
	public void close(){
		dbHelper.close();
	}
	public List getAll(){
		 List data=new ArrayList();
		 Cursor cursor=db.rawQuery("select book_noFor as _id,book_pages,book_price,book_press,book_author,book_title,book_detail from myStore", null);
		 while(cursor.moveToNext()){
				Map map=new HashMap();
				map.put("book_title", cursor.getString(cursor.getColumnIndex("book_title")));
				map.put("book_author", cursor.getString(cursor.getColumnIndex("book_author")));
				map.put("book_press", cursor.getString(cursor.getColumnIndex("book_press")));
				map.put("book_pages", cursor.getString(cursor.getColumnIndex("book_pages")));
				map.put("book_price", cursor.getString(cursor.getColumnIndex("book_price")));
				map.put("book_noFor", cursor.getString(cursor.getColumnIndex("_id")));
				map.put("book_detail", cursor.getString(cursor.getColumnIndex("book_detail")));
				data.add(map);
			}
		 cursor.close();
		 return data;
	}
	public void create(Map map){
		ContentValues args=new ContentValues();
		Set set=map.keySet();
		Iterator<String> iterator=set.iterator();
		while(iterator.hasNext()){
			String key=iterator.next();
			Log.i(key, map.get(key).toString());
			args.put(key,map.get(key).toString());
		}
		db.insert(DATABASE_TABLE, null, args);
		db.close();
	}
	public void delete(String id){
		boolean flag=db.delete(DATABASE_TABLE, "book_noFor='"+id+"'", null)>0;
		db.close();
	}
}
