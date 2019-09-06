package com.kk.tongfu.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kk.tongfu.myapplication.R;


/**
 * Created by tongfu
 * on 2019-09-04
 * Desc:
 */

public class ProgressButton extends View {

    Paint mPaint;
    Paint mProgressPaint;
    Paint mTextPaint;
    private int mBgColor;
    private int mProgressColor;
    private String mText;
    private int mTextSize;
    private int mTextColor;
    private int mCornerRadius;
    private int mTotalWidth,mTotalHeight;
    private float mProgress;
    private boolean mIsDownloading;

    public ProgressButton(Context context) {
        this(context,null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton);
        if(typedArray!=null){
            mBgColor=typedArray.getColor(R.styleable.ProgressButton_bgColor,getResources().getColor(R.color.colorAccent));
            mProgressColor=typedArray.getColor(R.styleable.ProgressButton_progressColor,getResources().getColor(R.color.colorPrimary));
            mText=typedArray.getString(R.styleable.ProgressButton_text);
            mTextSize= (int) typedArray.getDimension(R.styleable.ProgressButton_textSize,16);
            mTextColor=typedArray.getColor(R.styleable.ProgressButton_textColor,getResources().getColor(R.color.text_black));
            //mCornerRadius= dipToPx((int) typedArray.getDimension(R.styleable.ProgressButton_cornerRadius,50));
        }
        typedArray.recycle();

        mPaint=new Paint();
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mProgressPaint=new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);

        mTextPaint=new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth=w;
        mTotalHeight=h;
        mCornerRadius=mTotalHeight/2;
    }

    /**
     * 设置进度 0-100
     * @param progress
     */
    public void setProgress(float progress){
        if(progress>100){
            return;
        }
        this.mProgress=progress;
        invalidate();
    }

    public int dipToPx(int dip){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    /**
     * 设置字体
     * @param text 要设置的文字
     * @param isDownloading  判断是否在下载中
     */
    public void setText(String text,boolean isDownloading){
        this.mText=text;
        if(!this.mIsDownloading==isDownloading){
            this.mIsDownloading=isDownloading;
            invalidate();
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {

        RectF rect=new RectF(0,0,mTotalWidth,mTotalHeight);
        canvas.drawRoundRect(rect,mCornerRadius,mCornerRadius,mPaint);
        if(mTotalWidth*mProgress/100<=mCornerRadius){
            //如果进度宽度没有超过左边半圆的半径，画左边的半圆
            RectF rectF=new RectF(0,0,mTotalHeight,mTotalHeight);
            float progress = (mTotalWidth * mProgress/100)/mCornerRadius * 180;
            canvas.drawArc(rectF,180-progress/2,progress,false,mProgressPaint);
        }else if(mTotalWidth*mProgress/100<(mTotalWidth-mCornerRadius)){
            //如果进度宽度超过了左边半圆的半径，画左边的半圆和中间的长方形
            RectF rectF=new RectF(0,0,mTotalHeight,mTotalHeight);
            canvas.drawArc(rectF,90,180,false,mProgressPaint);

            RectF rectF1=new RectF(mCornerRadius,0,mTotalWidth*mProgress/100,mTotalHeight);
            canvas.drawRect(rectF1,mProgressPaint);
        }else{
            //如果进度宽度超过了长方形和左边半圆的半径和，开始画左边的半圆，中间的长方形，右边的半圆
            RectF rectF=new RectF(0,0,mTotalHeight,mTotalHeight);
            canvas.drawArc(rectF,90,180,false,mProgressPaint);
            RectF rectF1=new RectF(mCornerRadius,0,mTotalWidth-mCornerRadius,mTotalHeight);
            canvas.drawRect(rectF1,mProgressPaint);

            float percent = (mTotalWidth * mProgress/100 - mTotalWidth + mCornerRadius) / mCornerRadius;
            float startAngle=90-percent*180/2;
            RectF rectF2=new RectF(mTotalWidth-2*mCornerRadius,0,mTotalWidth,mTotalHeight);
            canvas.drawArc(rectF2,startAngle,360-startAngle*2,false,mProgressPaint);
        }

        //画文字
        String text=mIsDownloading?mText+" "+mProgress+"%":mText;
        Rect rectF=new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),rectF);
        int width = rectF.width();
        int height = rectF.height();
        int left=(mTotalWidth-width)/2;
        int buttom=(mTotalHeight+height)/2;
        canvas.drawText(text,left,buttom,mTextPaint);


    }
}
