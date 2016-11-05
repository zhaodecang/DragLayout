package com.zdc.qq.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zdc.qq.R;
import com.zdc.qq.bean.IndexBean;
import com.zdc.qq.global.Constant;
import com.zdc.qq.utils.IndexBeanUtil;

/**
 * 描述：用于展示通过拼音首字母大写分组的names集合的listview
 * <p>
 * Created by zhaodecang on 2016-10-24下午4:01:32
 * <p>
 * 邮箱：zhaodecang@gmail.com
 */
public class QuickIndexListView extends ListView {

	/** 默认展示的数据 **/
	private String[] listContents = Constant.NAMES;
	private ArrayList<IndexBean> indexBeans;

	public QuickIndexListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public QuickIndexListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QuickIndexListView(Context context) {
		super(context);
	}

	/** 在listview中展示默认的的数据集合,返回的集合中每一个bean对象已经包含拼音 **/
	public ArrayList<IndexBean> setAdapter() {
		return setAdapter(listContents);
	}

	/** 在listview中展示指定的的数据集合,返回的集合中每一个bean对象已经包含拼音 **/
	public ArrayList<IndexBean> setAdapter(ArrayList<String> listContents) {
		String[] contents = (String[]) listContents.toArray();
		return setAdapter(contents);
	}

	/** 在listview中展示指定的的数据集合,返回的集合中每一个bean对象已经包含拼音 **/
	public ArrayList<IndexBean> setAdapter(String[] listContents) {
		this.listContents = listContents;
		indexBeans = IndexBeanUtil.convertArray2Bean(listContents);
		super.setAdapter(new QilvAdapter());
		return indexBeans;
	}

	/** QuickIndexListView的数据适配器 **/
	private class QilvAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return indexBeans.size();
		}

		@Override
		public IndexBean getItem(int position) {
			return indexBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(parent.getContext(), R.layout.item_man, null);
				holder.tvIndex = (TextView) convertView.findViewById(R.id.tv_index);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 显示每一个名字及其姓名的首字母大写
			holder.tvIndex.setText(String.valueOf(getItem(position).getFirstLetter()));
			holder.tvName.setText(getItem(position).getName());
			// 同一组首字母只显示一个
			if (position > 0) {
				char preLetter = getItem(position - 1).getFirstLetter();
				char CurrentLetter = getItem(position).getFirstLetter();
				if (preLetter == CurrentLetter) {
					holder.tvIndex.setVisibility(View.GONE);
				} else {
					holder.tvIndex.setVisibility(View.VISIBLE);
				}
			} else {
				holder.tvIndex.setVisibility(View.VISIBLE);
			}
			return convertView;
		}
	}

	private static class ViewHolder {
		TextView tvIndex, tvName;
	}
}
