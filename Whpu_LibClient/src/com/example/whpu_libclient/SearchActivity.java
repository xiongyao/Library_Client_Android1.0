package com.example.whpu_libclient;


import com.example.whpu_libclient.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends Activity{
	private EditText v_value;
	private Spinner v_index;
	private Spinner v_pagenum;
	private Spinner v_seldatabase;
	private Spinner v_LogicSrch;
	private String[] index_text={"题名(刊名)","责任者","主题词","分类号","国际标准书/刊号","索取号"};
	private String[] index_value={"TITLE","AUTHOR","SUBJECT","CLASSNO","ISBN","CALLNO"};
	private String[] pagenum_text={"10条","15条","20条"};
	private String[] pagenum_value={"10","15","20"};
	private String[] seldatabase_text={"书和刊 ","图书","期刊"};
	private String[] seldatabase_value={"0","1","2"};
	private String[] logicSrch_text={"前方一致 ","模糊检索"};
	private String[] logicSrch_value={"0","1"};
	private Bundle bundle=new Bundle();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		Button submit=(Button)findViewById(R.id.submit);
		v_value=(EditText) findViewById(R.id.v_value);
		v_index=(Spinner) findViewById(R.id.v_index);
		v_pagenum=(Spinner) findViewById(R.id.v_pagenum);
		v_seldatabase=(Spinner) findViewById(R.id.v_seldatabase);
		v_LogicSrch=(Spinner) findViewById(R.id.v_LogicSrch);
		ArrayAdapter<String> adapter_Spinner_Index=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,index_text);
		adapter_Spinner_Index.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		v_index.setAdapter(adapter_Spinner_Index);
		v_index.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				bundle.putString("v_index", index_value[arg2]);
				Log.i("index_value", index_value[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ArrayAdapter<String> adapter_Spinner_pagenum=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,pagenum_text);
		adapter_Spinner_pagenum.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		v_pagenum.setAdapter(adapter_Spinner_pagenum);
		v_pagenum.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				bundle.putString("v_pagenum", pagenum_value[arg2]);
				Log.i("pagenum_value", pagenum_value[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ArrayAdapter<String> adapter_Spinner_seldatabase=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,seldatabase_text);
		adapter_Spinner_seldatabase.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		v_seldatabase.setAdapter(adapter_Spinner_seldatabase);
		v_seldatabase.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				bundle.putString("v_seldatabase", seldatabase_value[arg2]);
				Log.i("seldatabase_value", seldatabase_value[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		ArrayAdapter<String> adapter_Spinner_logicSrch=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,logicSrch_text);
		adapter_Spinner_logicSrch.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		v_LogicSrch.setAdapter(adapter_Spinner_logicSrch);
		v_LogicSrch.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				bundle.putString("v_LogicSrch", logicSrch_value[arg2]);
				Log.i("logicSrch_value", logicSrch_value[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		submit.setOnClickListener(search);
		
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
		Intent intent=new Intent(this,MyBookStore.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener search=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(SearchActivity.this, BookListActity.class);
			bundle.putString("v_value",v_value.getText().toString());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
}
