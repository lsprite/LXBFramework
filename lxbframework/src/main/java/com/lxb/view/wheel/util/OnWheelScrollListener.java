package com.lxb.view.wheel.util;

public interface OnWheelScrollListener
{
	/**
	 * Callback method to be invoked when scrolling started.
	 * 
	 * @param wheel
	 *            the wheel fragmentView whose state has changed.
	 */
	void onScrollingStarted(WheelView wheel);

	/**
	 * Callback method to be invoked when scrolling ended.
	 * 
	 * @param wheel
	 *            the wheel fragmentView whose state has changed.
	 */
	void onScrollingFinished(WheelView wheel);
}
