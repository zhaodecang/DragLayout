package com.zdc.qq.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zdc.qq.R;
import com.zdc.qq.bean.IndexBean;
import com.zdc.qq.global.Constant;
import com.zdc.qq.utils.ValueEvaluator;
import com.zdc.qq.view.DragLayout;
import com.zdc.qq.view.DragLayout.OnDragStateChangeListener;
import com.zdc.qq.view.QuickIndexBar;
import com.zdc.qq.view.QuickIndexBar.OnLetterChangedListener;
import com.zdc.qq.view.QuickIndexListView;

public class MainActivity extends Activity implements OnDragStateChangeListener,
		OnItemClickListener, OnLetterChangedListener {

	ListView lvMenu;
	QuickIndexListView lvMain;
	private DragLayout slidMenu;
	private ImageView ivHead, ivMenuHead;
	private ArrayList<IndexBean> indexBeans;
	private QuickIndexBar quickBar;
	private TextView tvIndexHint;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		lvMenu = (ListView) findViewById(R.id.menu_listview);
		lvMain = (QuickIndexListView) findViewById(R.id.main_listview);
		slidMenu = (DragLayout) findViewById(R.id.slideMenu);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		ivMenuHead = (ImageView) findViewById(R.id.iv_menu_head);
		quickBar = (QuickIndexBar) findViewById(R.id.quickbar);
		tvIndexHint = (TextView) findViewById(R.id.tv_index_hint);
	}

	private void initData() {
		initMainData();
		initMenuData();
	}

	private void initMainData() {
		indexBeans = lvMain.setAdapter(Constant.NAMES);
	}

	private void initMenuData() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Constant.sCheeseStrings) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = (TextView) super.getView(position, convertView, parent);
				tv.setTextColor(Color.WHITE);
				return tv;
			}
		};
		lvMenu.setAdapter(adapter);
	}

	private void initListener() {
		slidMenu.setOnDragStateChangeListener(this);
		lvMain.setOnItemClickListener(this);
		lvMenu.setOnItemClickListener(this);
		quickBar.setOnLetterChangedListener(this);
	}

	@Override
	public void onDraging(float fraction) {
		ViewHelper.setAlpha(ivHead, ValueEvaluator.FloatEvaluate(fraction, 1.0f, 0.2f));
		ViewHelper.setAlpha(ivMenuHead,
				ValueEvaluator.FloatEvaluate(fraction, 0.1f, 1.0f));
	}

	@Override
	public void onClose() {
		ViewPropertyAnimator.animate(ivHead).translationXBy(15)
				.setInterpolator(new CycleInterpolator(8)).setDuration(1000).start();
	}

	@Override
	public void OnOpen() {
		ViewPropertyAnimator.animate(ivMenuHead).translationXBy(10)
				.setInterpolator(new CycleInterpolator(8)).setDuration(800).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String msg = "";
		if (parent.getItemAtPosition(position) instanceof IndexBean) {
			msg = ((IndexBean) parent.getItemAtPosition(position)).getName();
		} else {
			msg = (String) parent.getItemAtPosition(position);
		}
		Toast.makeText(this, msg, 0).show();
	}

	@Override
	public void onLetterChanged(char letter) {
		for (int i = 0; i < indexBeans.size(); i++) {
			if (indexBeans.get(i).getFirstLetter() == letter) {
				lvMain.setSelection(i);
				showCurrentWord(String.valueOf(letter));
				break;
			}
		}
	}

	protected void showCurrentWord(String letter) {
		tvIndexHint.setText(letter);
		// tvIndexHint.setVisibility(View.VISIBLE);
		ViewPropertyAnimator.animate(tvIndexHint).scaleX(1f).scaleY(1f).setDuration(350)
				.start();
		// 移除所有的任务
		handler.removeCallbacksAndMessages(null);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// tvIndexHint.setVisibility(View.GONE);
				// 抬起的时候缩小
				ViewPropertyAnimator.animate(tvIndexHint).scaleX(0f).scaleY(0f)
						.setDuration(350).start();
			}
		}, 2000);
	}
}
