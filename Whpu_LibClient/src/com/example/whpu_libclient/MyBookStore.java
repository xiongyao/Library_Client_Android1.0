package com.example.whpu_libclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.BookDbAdapter;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyBookStore extends ListActivity implements OnScrollListener{
	int resource=R.layout.activity_book_list;
	private SimpleAdapter adapter;
	private BookDbAdapter bDbAdapter;
	private Cursor cursor;
	private List data=new ArrayList();
	private BookDbAdapter bookDbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float fv=Float.valueOf(android.os.Build.VERSION.RELEASE.substring(0,3).trim());
		if(fv>2.3){			
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads()
			.detectDiskWrites()
			.detectNetwork()   // or .detectAll() for all detectable problems
			.penaltyLog()
			.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
			.penaltyLog() //打印logcat
			.penaltyDeath()
			.build());
		}
		setAdapter();
		registerForContextMenu(getListView());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_book_store, menu);
		return true;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) { 
		openOptionDialog(position);
    }
	private void setAdapter(){
		bDbAdapter=new BookDbAdapter(this);
		bDbAdapter.open();
		fillData();
	}
	@SuppressWarnings("unchecked")
	private void fillData(){
		data=bDbAdapter.getAll();
		Log.i("data.size", data.size()+"");
		String[] from={"book_title","book_author","book_press","book_pages","book_price","_id","book_detail"};
		int[] to={R.id.book_title,R.id.book_author,R.id.book_press,R.id.book_pages,R.id.book_price,R.id.book_noFor,R.id.book_detail};
		adapter=new SimpleAdapter(this, data, resource, from, to);
		setListAdapter(adapter);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	private void openOptionDialog(final int position){
		new AlertDialog.Builder(MyBookStore.this)
		.setTitle("")
		.setItems(new String[]{"馆藏信息","删除"},new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					getListView().getItemAtPosition(position);
			        Map map=(Map)data.get(position);
			        String href= map.get("book_detail").toString();
			        Bundle bundle=new Bundle();
			        bundle.putString("HREF", href);
			        Intent intent=new Intent();
			        intent.putExtras(bundle);
					intent.setClass(MyBookStore.this, BookDetailActivity.class);
					startActivity(intent);
					break;
				case 1:
					getListView().getItemAtPosition(position);
			        Map book=(Map)data.get(position);
			        bDbAdapter.open();
			        bDbAdapter.delete(book.get("book_noFor").toString());
			        data.remove(position);
			        adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
		})		
		.show();
	}

}
