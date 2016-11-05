package com.zdc.tencentqq.swipe;

import com.zdc.tencentqq.swipe.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
