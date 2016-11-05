package com.zdc.qq.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout implements OnClickListener {

	private ViewDragHelper mHelper;
	private View menuView, mainView;
	private OnDragStateChangeListener mListener;
	private DragState currentState = DragState.state_close;
	private int layoutWidth, mainWidth, mainHeight, menuWidth, menuHeight;
	private float mRange;

	public DragLayout(Context context) {
		super(context);
		init();
	}

	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mHelper = ViewDragHelper.create(this, drawCallback);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 2) {
			throw new IllegalArgumentException("DrawLayout only can has 2 child");
		}
		menuView = getChildAt(0);
		mainView = getChildAt(1);
		// 设置打开时点击正文关闭侧边栏
		mainView.setOnClickListener(this);
		super.onFinishInflate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		layoutWidth = getMeasuredWidth();
		mRange = layoutWidth * 0.4f;
		// 初始化正文布局的宽高
		mainWidth = mainView.getMeasuredWidth();
		mainHeight = mainView.getMeasuredHeight();
		// 初始化侧菜单栏的宽高
		menuWidth = menuView.getMeasuredWidth();
		menuHeight = menuView.getMeasuredHeight();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 是否拦截的判断交给ViewDragHelper
		return mHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 将TouchEvent交给ViewDragHelper
		mHelper.processTouchEvent(event);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (currentState == DragState.state_open) {
			closeMain();// 关闭主界面
		}
	}

	// -------------------------ViewDragHelper的回调-------------------------
	private ViewDragHelper.Callback drawCallback = new ViewDragHelper.Callback() {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == menuView || child == mainView;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return (int) mRange;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (child == mainView)
				left = fixContentLeft(left);
			return left;
		}

		@Override
		public void onViewPositionChanged(View changedView, int l, int t, int dx, int dy) {
			if (changedView == menuView) {
				// 如果移动的是菜单项 就复原
				menuView.layout(0, 0, menuWidth, menuHeight);
				// 移动主页项
				int newLeft = fixContentLeft(mainView.getLeft() + dx);
				mainView.layout(newLeft, t, newLeft + mainWidth, t + mainHeight);
			}
			// 1、计算滑动的百分比
			float fraction = mainView.getLeft() / mRange;
			// 2、执行伴随动画
			executeAnimation(fraction);
			// 3、更改状态，执行回调方法
			executeCallback(fraction);
		}

		@Override
		/** 当child的位置改变的时候执行,一般用来做其他子View的伴随移动 **/
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			// 如果需要根据手指滑动的加速度决定是否打开或者关闭，可以判断加速度
			// boolean stopedAndNearLeft =
			// xvel == 0 && mainView.getLeft() > mRange / 2;
			// if (stopedAndNearLeft || xvel > 0) {
			// open();
			// } else {
			// close();
			// }
			boolean mainNearLeft = mainView.getLeft() < mRange / 2;
			if (mainNearLeft) {
				closeMain();
			} else {
				openMain();
			}
		}
	};

	/**
	 * 修正正文的左边坐标
	 * 
	 * @param left 需要修正的左边坐标值
	 * @return 修正之后的左边坐标值
	 */
	protected int fixContentLeft(int left) {
		if (left < 0)
			left = 0;
		if (left > mRange)
			left = (int) mRange;
		return left;
	}

	/** 更改状态，执行回调方法 **/
	protected void executeCallback(float fraction) {
		if (mListener == null) {
			return;
		}
		if (fraction == 0 && currentState != DragState.state_close) {
			currentState = DragState.state_close;
			mListener.onClose();
		} else if (fraction == 1 && currentState != DragState.state_open) {
			currentState = DragState.state_open;
			mListener.OnOpen();
		}
		mListener.onDraging(fraction);
	}

	/** 执行一些列动画 **/
	protected void executeAnimation(float fraction) {
		// ------执行缩放动画
		// 缩小main
		ViewHelper.setScaleX(mainView, evaluateFloat(fraction, 1f, 0.8f));
		ViewHelper.setScaleY(mainView, evaluateFloat(fraction, 1f, 0.8f));
		// 放大menu
		ViewHelper.setScaleX(menuView, evaluateFloat(fraction, 0.6f, 1f));
		ViewHelper.setScaleY(menuView, evaluateFloat(fraction, 0.6f, 1f));
		// ------执行平移动画
		// menu移出来
		ViewHelper.setTranslationX(menuView, evaluateFloat(fraction, -menuWidth / 3, 0));
		// 让main同时向下平移一部分
		ViewHelper.setTranslationY(mainView,
				evaluateFloat(fraction, 0, mainHeight * 0.1f));
		// ------执行渐变动画
		// menu逐渐显示
		ViewHelper.setAlpha(menuView, evaluateFloat(fraction, 0.5f, 1f));
		// ------给背景添加黑暗轮罩的效果
		Integer useColor = (Integer) evaluateArgb(fraction, Color.BLACK, 0);
		getBackground().setColorFilter(useColor, Mode.SRC_OVER);
	}

	/** 关闭主界面 **/
	protected void closeMain() {
		closeOrOpenContent(0);
		setMainChildEnable(true);
	}

	/** 打开主界面 **/
	private void openMain() {
		closeOrOpenContent((int) mRange);
		setMainChildEnable(false);
	}

	/** 执行打开或关闭正文的动画 **/
	private void closeOrOpenContent(int finalLeft) {
		mHelper.smoothSlideViewTo(mainView, finalLeft, 0);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	@Override
	public void computeScroll() {
		if (mHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	// ----------------------禁止打开时正文的滑动事件----------------------
	/** 设置正文或正文子控件是否可使用 **/
	private void setMainChildEnable(boolean can) {
		if (mainView instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) mainView;
			for (int i = 0; i < vp.getChildCount(); i++) {
				changeViewEnable(vp.getChildAt(i), can);
			}
		} else {
			changeViewEnable(mainView, can);
		}
	}

	/** 改变指定控件是否可使用 **/
	private void changeViewEnable(View view, boolean can) {
		view.setEnabled(can);
		view.setClickable(can);
	}

	// ----------------------定义回调接口----------------------
	public void setOnDragStateChangeListener(OnDragStateChangeListener changeListener) {
		this.mListener = changeListener;
	}

	public interface OnDragStateChangeListener {
		void onDraging(float fraction);

		void onClose();

		void OnOpen();
	}

	/** 表示侧边菜单栏的打开状态 **/
	public enum DragState {
		state_open, state_close;
	}

	// ----------------------计算过渡值---------------------
	private float evaluateFloat(float fraction, float start, float end) {
		return start - (start - end) * fraction;
	}

	private Object evaluateArgb(float fraction, Object startValue, Object endValue) {
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
