/*
 * TVMouseView.java
 * 
 * Version:1.0.0
 *
 * Date: 2014�?�?�?
 *
 * Copyright 2012-2014 Tamic. All Rights Reserved
 */
package com.tamic.tvmouse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author liuyongkui
 *
 */
public class TcMouseView extends FrameLayout {
	
	private int mOffsetX;
	private int mOffsetY;
	
	private ImageView mMouseView;
	
	private Bitmap mMouseBitmap;
	
	private TcMouseManager mMouseManager ;
	
	private int mMouseX = TcMouseManager.MOUSE_STARTX;
	
	private int mMouseY = TcMouseManager.MOUSE_STARY;
	
    private int mLastMouseX = mMouseX;
	
	private int mLastMouseY = mMouseY;
	
	private int mMoveDis =  TcMouseManager.MOUSE_MOVE_STEP;
	
	
	private OnMouseListener mOnMouseListener;

	public TcMouseView(Context context) {
		super(context);
	}
	
	public TcMouseView(Context context, TcMouseManager mMouseMrg) {
		super(context);
		init( mMouseMrg);
	}
	
	public OnMouseListener getOnMouseListener() {
		return mOnMouseListener;
	}

	public void setOnMouseListener(OnMouseListener mOnMouseListener) {
		this.mOnMouseListener = mOnMouseListener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mMouseView != null && mMouseBitmap != null) {
			mMouseView.measure(MeasureSpec.makeMeasureSpec(mMouseBitmap.getWidth(), MeasureSpec.EXACTLY), 
					MeasureSpec.makeMeasureSpec(mMouseBitmap.getHeight(), MeasureSpec.EXACTLY));
		}
	}
	
	private void init(TcMouseManager manager) {
		mMouseManager  = manager;
		Drawable drawable =	getResources().getDrawable(
				R.mipmap.shubiao);
		mMouseBitmap = drawableToBitamp(drawable);
		mMouseView = new ImageView(getContext());
		mMouseView.setImageBitmap(mMouseBitmap);
		addView(mMouseView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mOffsetX = (int)((mMouseBitmap.getWidth()));
		mOffsetY = (int)((mMouseBitmap.getHeight()));
		mOffsetX = (int)((mMouseBitmap.getWidth())*30/84);
		mOffsetY = (int)((mMouseBitmap.getHeight())*20/97);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if(mMouseView != null) {
			mMouseView.layout(mMouseX, mMouseY, mMouseX + mMouseView.getMeasuredWidth(), mMouseY + mMouseView.getMeasuredHeight());
		}
	}

	

	private void scrollView(KeyEvent event) {
		if(mMouseManager .getCurrentActivityType() == TcMouseManager.MOUSE_TYPE) {
		
				int pageScrollBy = 0;
				if(event.getKeyCode() == TcMouseManager.KEYCODE_UP) {
					pageScrollBy = - mMoveDis;
				} else if (event.getKeyCode() == TcMouseManager.KEYCODE_DOWN) {
					pageScrollBy = mMoveDis;
				}
				this.dispatchKeyEvent(event)	;	
		/* MainActivity.contentView.dispatchKeyEvent(event);*/
			
		}
	}

	public void onCenterButtonClicked(KeyEvent event) {
		mMouseManager .sendCenterClickEvent(mMouseX + mOffsetX, mMouseY + mOffsetY, event.getAction());//加一点偏移补�?
	}
	
	 
	 
	 private Bitmap drawableToBitamp(Drawable drawable) {
		 
		 BitmapDrawable bd = (BitmapDrawable) drawable;
		 Bitmap bitmap = bd.getBitmap();
		 return Bitmap.createScaledBitmap(bitmap, 50, 50 ,true);
	 }
	 
	 
	 
	 @Override	
	 public boolean dispatchKeyEvent(KeyEvent event) {
			Log.i("dispatchKeyEvent",
					"dispatchKeyEvent(), action=" + event.getAction() + " keycode="
							+ event.getKeyCode());
			switch (event.getKeyCode()) {
			case TcMouseManager.KEYCODE_UP:
			
			case TcMouseManager.KEYCODE_DOWN:
				
			case TcMouseManager.KEYCODE_LEFT:
				
			case TcMouseManager.KEYCODE_RIGHT:
				
			case TcMouseManager.KEYCODE_CENTER:
				
				if (mOnMouseListener != null) {
					
					return mOnMouseListener.onclick(TcMouseView.this, event);
				}
				
			default:
				break;
			}
			return super.dispatchKeyEvent(event);
			
		}
	 
	 public void moveMouse(KeyEvent event, int times) {
			Log.d("BdMainView", "wrapper moveMouse() ENTER");
			mMoveDis = times * TcMouseManager.MOUSE_MOVE_STEP;
			switch(event.getKeyCode()) {
			case TcMouseManager.KEYCODE_UP:
				if(mMouseY - mMoveDis >= 0) {
					mMouseY = mMouseY - mMoveDis;
				} else {
					mMouseY = 0;
					scrollView(event);
				}
				break;
			case TcMouseManager.KEYCODE_LEFT:
				mMouseX = (mMouseX - mMoveDis > 0) ? mMouseX - mMoveDis : 0;
				break;
			case TcMouseManager.KEYCODE_DOWN:
				if(mMouseY + mMoveDis < getMeasuredHeight() - mMoveDis) {
					mMouseY = mMouseY + mMoveDis;
				} else {
					mMouseY = getMeasuredHeight() - mOffsetY;
					scrollView(event);
				}
				break;
			case TcMouseManager.KEYCODE_RIGHT:
				mMouseX = (mMouseX + mMoveDis < getMeasuredWidth() - mOffsetX) ? mMouseX + mMoveDis : getMeasuredWidth() - mOffsetX;
				break;
			}
			if(mLastMouseX == mMouseX && mLastMouseY == mMouseY) {
				return;
			}
			
			mLastMouseX = mMouseX;
			mLastMouseY = mMouseY;
			
			requestLayout();
			mMouseManager .sendMouseHoverEvent(mMouseX + mOffsetX, mMouseY + mOffsetY);
			
		}
	 
	 /**
		 * @author liuyongkui
		 *
		 */
	 public interface OnMouseListener {
		 
		 boolean onclick(View v, KeyEvent event);
		 
		
	 }
	 
	 @Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
		
	
	
}