package com.androiddev101.inksketch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDraw extends View {
	
	static final public int PAINT_BRUSH_WIDTH_LARGE = 20; 
	static final public int PAINT_BRUSH_WIDTH_MEDIUM = 10; 
	static final public int PAINT_BRUSH_WIDTH_SMALL = 5; 
	
	Path previouslyDrawn;
	Paint brush;

	public TouchDraw(Context context, 
			AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public TouchDraw(Context context, 
			AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TouchDraw(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		previouslyDrawn = new Path();
		brush = new Paint();
		//set paint brush details
		brush.setAntiAlias(true);
		brush.setStrokeWidth(PAINT_BRUSH_WIDTH_LARGE);
		brush.setColor(Color.BLACK);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawPath(previouslyDrawn, brush);
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			previouslyDrawn.moveTo(eventX, eventY);
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			previouslyDrawn.lineTo(eventX, eventY);
			previouslyDrawn.moveTo(eventX, eventY);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			performClick();
			break;
		default:
			return false;
		}
		return false;
	}
	
	public Path getPath(){
		return previouslyDrawn;
	}
	
	public void setPreviousPath(Path previous){
		previouslyDrawn = previous;
	}
	
	public Bitmap getBitmapFromView() {
	    Bitmap returnedBitmap = Bitmap.createBitmap(600, 800,Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(returnedBitmap);
	    canvas.drawColor(Color.WHITE);
	    this.draw(canvas);
	    return returnedBitmap;
	}
	
	public void clear(){
		previouslyDrawn.rewind();
		invalidate();
	}
	
	public void changeStroke(int newWidth){
		brush.setStrokeWidth(newWidth);
	}
}
