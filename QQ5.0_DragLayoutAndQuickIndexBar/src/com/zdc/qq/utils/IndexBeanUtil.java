package com.zdc.qq.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.zdc.qq.bean.IndexBean;

public class IndexBeanUtil {
	/** 将指定的数组或集合转换成带有拼音首字母的bean对象 **/
	public static ArrayList<IndexBean> convertArray2Bean(String[] arr) {
		ArrayList<IndexBean> beans = new ArrayList<IndexBean>();
		for (String str : arr) {
			String pinYin = PinyinUtil.getPinYin(str);
			IndexBean bean = new IndexBean(str, pinYin.charAt(0));
			beans.add(bean);
		}
		Collections.sort(beans);
		return beans;
	}
}
