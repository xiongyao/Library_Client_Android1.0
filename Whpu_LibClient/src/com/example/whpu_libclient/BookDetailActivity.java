package com.example.whpu_libclient;

import java.util.List;

import com.example.whpu_libclient.R;
import manager.PostMsg;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;

public class BookDetailActivity extends  ListActivity implements OnScrollListener{
	private SimpleAdapter adapter; 
	private List data;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*显示ProgressDialog*/
		pd=ProgressDialog.show(BookDetailActivity.this, "", "加载中，请稍后...");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//spandTimeMethod();
				data=new PostMsg().getDetail(getIntent().getExtras().getString("HREF"));
				handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler  
			}
		}).start();
//		data=new PostMsg().getDetail(getIntent().getExtras().getString("HREF"));
//		int resource=R.layout.activity_book_detail;
//		String[] from={"book_barcode","book_lib","book_type","book_state","book_backTime","book_explain"};
//		int[] to={R.id.book_barcode,R.id.book_lib,R.id.book_type,R.id.book_state,R.id.book_backTime,R.id.book_explain};
//		adapter=new SimpleAdapter(this, data, resource, from, to);
//		setListAdapter(adapter);
//		getListView().setOnScrollListener(this);  
	}
	private Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法  
            pd.dismiss();// 关闭ProgressDialog  
    		int resource=R.layout.activity_book_detail;
    		String[] from={"book_barcode","book_lib","book_type","book_state","book_backTime","book_explain"};
    		int[] to={R.id.book_barcode,R.id.book_lib,R.id.book_type,R.id.book_state,R.id.book_backTime,R.id.book_explain};
    		adapter=new SimpleAdapter(BookDetailActivity.this, data, resource, from, to);
    		setListAdapter(adapter);
    		getListView().setOnScrollListener(BookDetailActivity.this);  
        }  
    };
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_detail1, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,MyBookStore.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
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

}
