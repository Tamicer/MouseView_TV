/*
 * TVMouseController.java
 * 
 * Version:
 *
 * Date: 2014�?�?�?
 *
 * Changes: guojing04@2014�?�?�?修改日志模板
 * 
 * Copyright 2012-2014 Tamic. All Rights Reserved
 */
package com.tamic.tvmouse;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author liuyongkui.
 *
 */
public class TcMouseManager implements TcMouseView.OnMouseListener {
	
	public static final int KEYCODE_UP = KeyEvent.KEYCODE_DPAD_UP;
	
	public static final int KEYCODE_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
	
	public static final int KEYCODE_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
	
	public static final int KEYCODE_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
	
	public static final int KEYCODE_CENTER = KeyEvent.KEYCODE_DPAD_CENTER; 
	
	public static final int MOUSE_STARTX = 250;
	
	public static final int MOUSE_STARY = 350;
	
	public static final int MOUSE_MOVE_STEP = 15;

	public static final int MOUSE_TYPE = 0;
	
	private int mCurrentType;
	
	private ViewGroup mParentView;
	
	private TcMouseView mMouseView;
	
	private boolean isShowMouse = true;
	
	private boolean isKeyEventCousumed = false;
	
	private int mSpeed = 1;
	
	private Context mContext;
	
	private int  defTimes = 400;
	
	private int defMaxSpeed = 7;
	
	/**
	 * @param parent
	 * @param type
	 */
	public void init(ViewGroup parentView, int type) {
		mParentView = parentView;
		mContext = parentView.getContext();
		mMouseView = new TcMouseView(mContext, this);
		mMouseView.setOnMouseListener(this);
		mCurrentType = type;
	}
	
	/**
	 * @return
	 */
	public boolean isShowMouse() {
		
		return isShowMouse;
	}

	public void setShowMouse(boolean isMouse) {
		if(this.isShowMouse != isMouse) {
			this.isShowMouse = isMouse;
			if(isMouse) {
				mMouseView.setVisibility(View.VISIBLE);
			} else {
				mMouseView.setVisibility(View.GONE);
			}
			mMouseView.requestLayout();
		}
	}
	
	/**
	 * @return
	 */
	public int getCurrentActivityType() {
		return mCurrentType;
	}

	/**
	 * showmouse
	 */
	public void showMouseView() {
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		
		
		if(mMouseView != null) {
			mParentView.addView(mMouseView, lp);
		}
	}

	private long mLastEventTime;

	
	
	public boolean onDpadClicked(KeyEvent event) {
		
		if(!isShowMouse) {
			return false;
		}
		
		if(event.getKeyCode() == KEYCODE_CENTER) {
			dispatchKeyEventToMouse(event);
		} else {
			if(event.getAction() == KeyEvent.ACTION_DOWN) {
				if(!isKeyEventCousumed) {
					
					if(event.getDownTime() - mLastEventTime < defTimes) {
						
						if(mSpeed < defMaxSpeed) {
							mSpeed ++;
						}
					} else {
						mSpeed = 1;
					}
				}
				mLastEventTime = event.getDownTime();
				dispatchKeyEventToMouse(event);
				isKeyEventCousumed = true;
			} else if(event.getAction() == KeyEvent.ACTION_UP) {
				if(!isKeyEventCousumed){
					dispatchKeyEventToMouse(event);
				}
				isKeyEventCousumed = false;
			}
		}
		return true;
	}

	private void dispatchKeyEventToMouse(KeyEvent event) {
		if(event.getKeyCode() == KEYCODE_CENTER) {
			mMouseView.onCenterButtonClicked(event);
		} else {
			mMouseView.moveMouse(event, mSpeed);
		}
	}
	
	public void sendCenterClickEvent(int x, int y, int action) {
		sendMotionEvent(x, y, action);
	}
	
	@SuppressLint("InlinedApi")
	public void sendMouseHoverEvent(int downx, int downy) {
		sendMotionEvent(downx, downy, MotionEvent.ACTION_HOVER_MOVE);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@SuppressLint("NewApi")
	private void sendMotionEvent(int x, int y, int action) {
		
		
		MotionEvent motionEvent = getMotionEvent( x, y ,action) ;
		
		
		if(action == MotionEvent.ACTION_HOVER_MOVE) {
			motionEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);
			mMouseView.dispatchGenericMotionEvent(motionEvent);
			//mParentView.dispatchGenericMotionEvent(motionEvent);
		} else {
			//mParentView.dispatchTouchEvent(motionEvent);
			mMouseView.dispatchTouchEvent(motionEvent);
		}
	}

	private  MotionEvent getMotionEvent(int x, int y, int action) {
		// TODO Auto-generated method stub
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis();
		int metaState = 0;
		return MotionEvent.obtain(
				downTime, 
				eventTime, 
				action, 
				x, 
				y, 
				metaState
				);
	}

	@Override
	public boolean onclick(View v, KeyEvent et) {
		if (isShowMouse()) {
			
			return onDpadClicked(et);
		}
		return mParentView.dispatchKeyEvent(et);
	}

	
	
}
