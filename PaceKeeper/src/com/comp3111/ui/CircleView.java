package com.comp3111.ui;

import com.comp3111.pacekeeper.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CircleView extends View {

	Paint mPaint = new Paint();

	int size = 180;
	int radius = 150;
	int delta = size - radius;
	int arcSize = (size - (delta / 2)) * 2;
	int percent = 66;

    Paint paint;
    Paint bgpaint;
    RectF rect;
    float percentage = 0;
    int arcWidth = 80;
    int startOffset = 0;
    
    // colors
    public static final int LIGHT_BLUE = 0;
    public static final int GREEN = 1;
    public static final int LIGHT_GREY = 2;
    public static final int RED = 3;
    

	public CircleView(Context context) {
		super(context);
	}
	
	public CircleView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init();
	}

	public CircleView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init();
	}

	public void setParameter(int persentage) {	//set persentage
		this.percent = persentage;
	}
	
    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#a8dff4"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(arcWidth);
        rect = new RectF();
        setPercentage(0);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background circle anyway
        int left = 0;
        int width = getWidth();
        int top = 0;
        rect.set(left + arcWidth/2, top + arcWidth/2, left+width - arcWidth/2, top + width - arcWidth/2); 
        if(percentage!=0) {
            canvas.drawArc(rect, 270-(360*startOffset / 100), -(360*percentage), false, paint);
        }
    }
    
    public void setColor(int colorTag){
    	switch(colorTag){
    	case 0:
            paint.setColor(Color.parseColor("#a8dff4"));
            break;
    	case 1:
            paint.setColor(Color.parseColor("#d3e992"));
            break;
    	case 2:
            paint.setColor(Color.parseColor("#DEDEDE"));
            break;
    	case 3:
            paint.setColor(Color.parseColor("#ffafaf"));
            break;    		
    	}
    }
    
    public void setStartOffset(int startPercentage){
    	startOffset = startPercentage;
    }
    
    public void setPercentage(float percentage) {
        this.percentage = percentage / 100;
        invalidate();
    }

}
