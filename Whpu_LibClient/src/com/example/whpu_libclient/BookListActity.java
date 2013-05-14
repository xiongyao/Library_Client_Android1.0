package com.example.whpu_libclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.BookDbAdapter;

import manager.PostMsg;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookListActity extends ListActivity implements OnScrollListener {
	private boolean isLastRow = false;
	private List data = new ArrayList();
	private SimpleAdapter adapter;
	private BookDbAdapter bookDbAdapter;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		float fv = Float.valueOf(android.os.Build.VERSION.RELEASE.substring(0,
				3).trim());
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // or
																			// .detectAll()
																			// for
																			// all
																			// detectable
																			// problems
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
					.penaltyLog() // ��ӡlogcat
					.penaltyDeath().build());
		}
		/* ��ʾProgressDialog */
		pd = ProgressDialog.show(BookListActity.this, "", "�����У����Ժ�...");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// spandTimeMethod();
				data = new PostMsg().getList(getIntent().getExtras());
				handler.sendEmptyMessage(0);// ִ�к�ʱ�ķ���֮��������handler
			}
		}).start();
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
			pd.dismiss();// �ر�ProgressDialog
			int resource = R.layout.activity_book_list;
			String[] from = { "book_title", "book_author", "book_press",
					"book_pages", "book_price", "book_noFor", "book_detail" };
			int[] to = { R.id.book_title, R.id.book_author, R.id.book_press,
					R.id.book_pages, R.id.book_price, R.id.book_noFor,
					R.id.book_detail };
			adapter = new SimpleAdapter(BookListActity.this, data, resource,
					from, to);
			setListAdapter(adapter);
			getListView().setOnScrollListener(BookListActity.this);
			registerForContextMenu(getListView());
		}
	};
	private Handler handler_refresh = new Handler() {
		@Override
		public void handleMessage(Message msg) {// handler���յ���Ϣ��ͻ�ִ�д˷���
			pd.dismiss();// �ر�ProgressDialog
			adapter.notifyDataSetChanged();
			isLastRow = false;
		}
	};

	private void openOptionDialog(final int position) {
		new AlertDialog.Builder(BookListActity.this).setItems(
				new String[] { "�ݲ���Ϣ", "�����ղؼ�" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							Map map = (Map) data.get(position);
							String href = map.get("book_detail").toString();
							Bundle bundle = new Bundle();
							bundle.putString("HREF", href);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(BookListActity.this,
									BookDetailActivity.class);
							startActivity(intent);
							break;
						case 1:
							Map book = (Map) data.get(position);
							bookDbAdapter = new BookDbAdapter(
									BookListActity.this);
							bookDbAdapter.open();
							bookDbAdapter.create(book);
							break;
						default:
							break;
						}
					}
				}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MyBookStore.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// �ж��Ƿ�������һ��
		Log.i("firstVisibleItem", firstVisibleItem + "");
		Log.i("visibleItemCount", visibleItemCount + "");
		Log.i("totalItemCount", totalItemCount + "");
		if (firstVisibleItem + visibleItemCount == totalItemCount
				&& totalItemCount > 0) {
			isLastRow = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// ���������һ����ֹͣ����ʱ��ִ�м���
		if (isLastRow
				&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			// Toast.makeText(BookListActity.this, "������һҳ",10).show();
			// ����Ԫ��
			// bundle1.putString(key, value);
			// data = new PostMsg().nextPage();
			pd = ProgressDialog.show(BookListActity.this, "", "���ظ���...");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List nextList = new PostMsg().nextPage();
					for (int i = 0; i < nextList.size(); i++) {
						data.add(nextList.get(i));
					}
					handler_refresh.sendEmptyMessage(0);
				}
			}).start();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		openOptionDialog(position);
	}

}
