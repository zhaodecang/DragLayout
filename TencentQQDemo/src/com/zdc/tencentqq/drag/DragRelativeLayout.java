package com.zdc.tencentqq.drag;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.zdc.tencentqq.drag.DragLayout.Status;

public class DragRelativeLayout extends RelativeLayout {

    private DragLayout dl;

	public DragRelativeLayout(Context context) {
        super(context);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setDragLayout(DragLayout dl) {
        this.dl = dl;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dl.getStatus() != Status.Close) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dl.close(true);
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
}
