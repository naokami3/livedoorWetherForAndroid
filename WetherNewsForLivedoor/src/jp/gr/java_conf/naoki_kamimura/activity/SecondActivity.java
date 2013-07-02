/*
 * @(#)Http.java        1.00 13/06/25
 * Copyright(c) 2012-2013 NaokiKamimura,All rights reserved
 */
package jp.gr.java_conf.naoki_kamimura.activity;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.naoki_kamimura.http.ConnectionThread;
import jp.gr.java_conf.naoki_kamimura.json.Json;
import jp.gr.java_conf.naoki_kamimura.util.ToastUtil;
import jp.gr.java_conf.naoki_kamimura.wethernewsforlivedoor.R;
import jp.gr.java_conf.naoki_kamimura.wethernewsforlivedoor.WetherView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SecondActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		Json json = new Json();
		Context context = getApplicationContext();
		TextView header = (TextView) findViewById(R.id.secondActivity_TextView);

		Button restart = (Button) findViewById(R.id.restart_button);
		json.readJsonNameList(context);// JSON解析
		listView();// ListViewを表示

		// 更新ボタンの処理
		restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// コネクションの開始
				Thread thread = new Thread(new ConnectionThread());
				thread.start();
			}
		});
	}

	/**
	 * @version 1.00 27 June 2013
	 * @author NaokiKamimura リストを表示
	 */
	private void listView() {
		// 画面表示用ListView
		Json json = new Json();
		ListView listView = (ListView) findViewById(R.id.listview);
		final ToastUtil toast = new ToastUtil();
		final Context con = getApplicationContext();
		ArrayAdapter<String> nameListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		nameListAdapter = json.getJSONNameListAdapter(con);// JSON名前データの格納されたadapterを貰う
		listView.setAdapter(nameListAdapter);// adapterのセット
		// ListViewの処理
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Json json = new Json();
				Context context = getApplicationContext();
				Intent intent = new Intent(SecondActivity.this,
						WetherView.class);
				ListView list = (ListView) parent;
				String msg = "index:" + position;
				toast.makeToast(con, msg);
				// (テスト用)タッチされた場所のアイテムを取得する
				String item = (String) list.getItemAtPosition(position);
				toast.makeToast(con, item);
				// リストの押した項目と位置を送る
				List<String> linkList = new ArrayList<String>();// リンクアドレスを格納するArrayList
				linkList = json.readJsonLinkList(context);// リンクアドレスを格納
				String linkPosition = linkList.get(position);// 押したindexに対応するアドレス
				intent.putExtra("name", item);
				intent.putExtra("index", linkPosition);
				startActivity(intent);
				finish();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

	/**
	 * @version 1.00 27 June 2013
	 * @author NaokiKamimura リストを更新するボタンの処理
	 */
	private void update() {
		// TODO　リスト内容を更新する処理
	}

}
