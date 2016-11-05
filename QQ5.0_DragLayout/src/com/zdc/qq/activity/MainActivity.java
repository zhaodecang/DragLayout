package com.zdc.qq.activity;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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
import com.zdc.qq.global.Constant;
import com.zdc.qq.utils.ValueEvaluator;
import com.zdc.qq.view.DragLayout;
import com.zdc.qq.view.DragLayout.OnDragStateChangeListener;

public class MainActivity extends Activity implements OnDragStateChangeListener,
		OnItemClickListener {

	ListView lvMenu;
	ListView lvMain;
	private DragLayout slidMenu;
	private ImageView ivHead, ivMenuHead;
	Random random = new Random();

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
		lvMain = (ListView) findViewById(R.id.main_listview);
		slidMenu = (DragLayout) findViewById(R.id.slideMenu);
		ivHead = (ImageView) findViewById(R.id.iv_head);
		ivMenuHead = (ImageView) findViewById(R.id.iv_menu_head);
	}

	private void initData() {
		initMainData();
		initMenuData();
	}

	private void initMainData() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Constant.NAMES) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView == null ? super.getView(position, convertView,
						parent) : convertView;
				// 先缩小
				ViewHelper.setScaleX(view, 0.5f);
				ViewHelper.setScaleY(view, 0.5f);
				// 再放大
				ViewPropertyAnimator.animate(view).scaleX(1).setDuration(350).start();
				ViewPropertyAnimator.animate(view).scaleY(1).setDuration(350).start();
				return view;
			}
		};
		lvMain.setAdapter(adapter);
		slidMenu.setOnDragStateChangeListener(this);
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
		lvMain.setOnItemClickListener(this);
		lvMenu.setOnItemClickListener(this);
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
		Toast.makeText(this, (CharSequence) parent.getItemAtPosition(position), 0).show();
	}
}
