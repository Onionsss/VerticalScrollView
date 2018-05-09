package com.onion.vertical;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqi on 2018/1/21 0021.
 */

public class VerticalScrollView extends View {

    private static final String TAG = "zhangqi";

    //画笔
    private Paint mTextPaint;
    //默认间隔
    private int mInterval = 2000;
    //数据源
    private List<String> mList = new ArrayList<>();

    private Rect mTextRect = new Rect();
    //默认的字体颜色
    private int mDefaultColor = Color.WHITE;
    /**
     * 当前的文字坐标
     */
    private int mIndex = 0;
    private int mDefaultTextSize = DensityUtil.sp2px(14);
    /**
     * 可移动的Y
     */
    private float mY = 0;
    /**
     * 是否初始化
     */
    private boolean mInit = false;
    /**
     * 速度
     */
    private float mYSpeed = 1;
    /**
     * 是否暂停
     */
    private boolean mPause = false;
    /**
     * 是否可以移动
     */
    private boolean move = true;

    //X的移动轴
    private float mX;
    //最大的X移动
    private int mMaxX;
    //是否可以移动X
    private boolean isMoveX;
    //X移动速度
    private float mXSpeed = 1;

    private VerticalHandler mVerticalHandler;
    private final static class VerticalHandler extends Handler{
        private WeakReference<VerticalScrollView> mViewWeakReference;

        private VerticalScrollView mVerticalScrollView;

        public VerticalHandler(VerticalScrollView verticalScrollView){
            mViewWeakReference = new WeakReference<>(verticalScrollView);
            mVerticalScrollView = mViewWeakReference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    mVerticalScrollView.postInvalidate();
                    break;
                case 0:
                    mVerticalScrollView.postInvalidate();
                    break;
                case 2:
                    mVerticalScrollView.invalidate();
                    break;
            }
        }
    }

    public VerticalScrollView(Context context) {
        this(context,null);
    }

    public VerticalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollView);

        mYSpeed = a.getFloat(R.styleable.VerticalScrollView_v_yspeed,1);
        mXSpeed = a.getFloat(R.styleable.VerticalScrollView_v_xspeed,1);
        mInterval = a.getInteger(R.styleable.VerticalScrollView_v_interval,2000);
        mDefaultTextSize = (int) a.getDimension(R.styleable.VerticalScrollView_v_textSize,DensityUtil.sp2px(16));
        mDefaultColor = a.getColor(R.styleable.VerticalScrollView_v_textColor,Color.WHITE);

        initV();

        a.recycle();

        mVerticalHandler = new VerticalHandler(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = geHeight(heightMeasureSpec);
        int width = getWidth(widthMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int geHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if(MeasureSpec.AT_MOST == mode){
            //字的两倍大
            return mDefaultTextSize * 2;
        }
        //其他模式
        return size;
    }

    //数据源
    public void setData(List<String> list){
        mList.clear();
        mList.addAll(list);
        mIndex = 0;
    }

    public void start(){
        requestLayout();
        invalidate();
    }

    private int getWidth(int widthMeasureSpec){
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if(MeasureSpec.EXACTLY  == mode){
            return size;
        }

        if(mList != null && mList.size() > 0){

            int width = 0;
            for (int i = 0; i < mList.size(); i++) {
                String temp = mList.get(i);
                int v = (int) mTextPaint.measureText(temp);
                if(width < v){
                    width = v;
                }
            }

            size = Math.min(size,width);
            return size;
        }

        return 0;
    }

    private void initV() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mDefaultTextSize);
        mTextPaint.setColor(mDefaultColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mList.size() == 0){
            return;
        }

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        if (mIndex == mList.size()) {
            /**
             * 重新循环
             */
            mIndex = 0;
        }

        String temp = mList.get(mIndex);
        int textWidth = (int) mTextPaint.measureText(temp);
        mTextPaint.getTextBounds(temp,0,temp.length(),mTextRect);

        //初始化
        if(!mInit){
            mY = measuredHeight - mTextRect.top;
            mInit = true;
        }
        //如果滑到最上面 则重置
        if (mY <= 0 - (mTextRect.bottom-mTextRect.top)) {
            mY = measuredHeight - mTextRect.top;
            mIndex ++;
            mPause = false;
            mMaxX = 0;
            mX = 0;
            isMoveX = false;
        }

        //移动到中间暂停
        if(!mPause && mY < measuredHeight - (measuredHeight/2 - (mTextRect.bottom-mTextRect.top) / 2 )){
            mPause = true;
            move = false;
            if(textWidth > measuredWidth && !isMoveX){
                isMoveX = true;
                mMaxX = textWidth - measuredWidth;
            }
        }

        //横向滑动
        if(mPause && isMoveX && mX<mMaxX){
            canvas.drawText(temp,0,temp.length(),getPaddingLeft()-mX,mY,mTextPaint);
            mX+=mXSpeed;
            mVerticalHandler.sendEmptyMessage(2);
            return ;
        }

        canvas.drawText(temp,0,temp.length(),getPaddingLeft()-mMaxX,mY,mTextPaint);

        mY -= mYSpeed;

        if(mPause && !move){
            move = true;
            mVerticalHandler.sendEmptyMessageDelayed(1,mInterval);
        }else{
            mVerticalHandler.sendEmptyMessage(0);
        }
    }

}