package com.zdc.qq.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
	// 将 str 转换为拼音字符串
	public static String getPinYin(String str) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < str.length(); i++) {
				char charAt = str.charAt(i);
				// 如果是空白字符，则不转换
				if (Character.isWhitespace(charAt)) {
					continue;
				}
				// 如果是英文字符，则直接添加到拼音里
				if (charAt >= -128 && charAt <= 127) {
					sb.append(charAt);
					continue;
				}
				// 转换字母为拼音
				String[] arr = PinyinHelper.toHanyuPinyinStringArray(charAt, format);
				String pinyin = arr[0];
				sb.append(pinyin);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return sb.toString().toUpperCase();
	}
}
