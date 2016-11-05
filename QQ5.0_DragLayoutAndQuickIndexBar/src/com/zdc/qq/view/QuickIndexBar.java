package com.zdc.qq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {

	/** 默认检索条显示文本 **/
	public char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	/** 索引条的宽度 **/
	private static int mBarWidth = 20;
	/** 每一个索引块的高度 **/
	private int mEachIndexHeight;
	/** 当前手指触摸到的字符索引 **/
	private int currentIndex = -1;
	private Paint paint;
	private Rect bounds;
	/** 第一个字符的Y轴坐标 **/
	private float startY;
	private OnLetterChangedListener onLetterChangedListener;

	public QuickIndexBar(Context context) {
		super(context);
		initView();
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		// 初始化画笔
		paint = new Paint();
		paint.setTextSize(17);
		paint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(mBarWidth, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mEachIndexHeight = h / LETTERS.length;
		// 初始化字符高度
		initCharHeight();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/** 初始化字符高度 **/
	protected void initCharHeight() {
		bounds = new Rect();
		String letter = String.valueOf(LETTERS[0]);
		paint.getTextBounds(letter, 0, letter.length(), bounds);
		// 第一个字符的Y轴坐标
		startY = (mEachIndexHeight + bounds.height()) / 2;
		startY += 5;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < LETTERS.length; i++) {
			String letter = String.valueOf(LETTERS[i]);
			// 获取每一个字符的宽度
			paint.getTextBounds(letter, 0, letter.length(), bounds);
			// 计算每一个字符的坐标
			float drawX = (mBarWidth - bounds.width()) / 2;
			float drawY = startY + mEachIndexHeight * i;
			// 改变字体颜色并绘制每一个字符
			if (i == currentIndex) {
				paint.setColor(Color.GREEN);
			} else {
				paint.setColor(Color.BLACK);
			}
			canvas.drawText(letter, drawX, drawY, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// 手指按下和手指移动时改变重新绘制
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			int index = currentIndex;
			currentIndex = fixCurrentIndex((int) (ev.getY() / mEachIndexHeight));
			// 如果始终按下的是同一个 就不做任何处理
			if (index == currentIndex) {
				break;
			}
			// 执行回调方法
			if (onLetterChangedListener != null) {
				onLetterChangedListener.onLetterChanged(LETTERS[currentIndex]);
			}
			break;
		case MotionEvent.ACTION_UP:
			currentIndex = -1;
			break;
		}
		invalidate();
		return true;
	}

	/** 手指滑动时做容错处理 **/
	private int fixCurrentIndex(int index) {
		if (index < 0)
			return 0;
		if (index > LETTERS.length - 1)
			return LETTERS.length - 1;
		return index;
	}

	// --------------------------回调接口-----------------------------

	public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
		this.onLetterChangedListener = onLetterChangedListener;
	}

	public interface OnLetterChangedListener {
		void onLetterChanged(char letter);
	}

	// ----------------------暴露修改参数的方法----------------------
	/** 修改分隔条上字体颜色 **/
	public void setBarTestColor(int color) {
		paint.setColor(color);
		invalidate();
	}

	/** 修改分隔条上字体大小 **/
	public void setBarTestSize(float textSize) {
		paint.setTextSize(textSize);
		invalidate();
	}
}
