package com.zdc.qq.utils;

/**
 * 描述：根据百分比，在起始值和最终值间计算出过渡值
 * <p>
 * Created by zhaodecang on 2016-10-23下午9:57:33
 * <p>
 * 邮箱：zhaodecang@gmail.com
 */
public class ValueEvaluator {

	// 根据百分比，在起始值和最终值间计算出过渡Integer值
	public static Integer IntEvaluate(float fraction, Integer startValue, Integer endValue) {
		int startInt = startValue;
		return (int) (startInt + fraction * (endValue - startInt));
	}

	// 根据百分比，在起始值和最终值间计算出过渡Float值
	public static Float FloatEvaluate(float fraction, Float startValue, Float endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	// 根据百分比，在起始颜色和最终颜色之间计算一个过渡颜色
	public static Object ArgbEvaluate(float fraction, Object startValue, Object endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}
}
