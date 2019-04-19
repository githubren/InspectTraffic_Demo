package com.example.yfsl.inspecttraffic_demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 自定义折线图
 */
public class ChartView extends View {
    private int mViewHeight;
    private int mViewWidth;

    private Paint mPaintCdt;
    private Paint mPaintSysPoint;
    private Paint mPaintLinePoint;
    private Paint mPaintText;
    private Paint mPaintLine;
    private Paint mPaintDash;
    private Paint mPaintSys;

    private Rect mXBound;
    private Rect mYBound;

    private ArrayList<Point> pointList = null;
    private int X_MAX;
    private int Y_MAX;
    private float mScreenXdistance;
    private float mScreenYdistance;

    private int Margin = 80;
    private int coordinateSystemColor;
    private float coordinateSystemSize;
    private int lineColor;
    private float lineSize;
    private int lineColorPoint;
    private float linrColorPointRadius;
    private int scalePointColor;
    private float scalePointRadius;
    private boolean isShowDash;
    private int xScale;
    private int yScale;
    private float dashSize;
    private int dashColor;

    public ChartView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        mPaintCdt = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCdt.setStyle(Paint.Style.STROKE);
        mPaintCdt.setStrokeWidth(lineSize);
        mPaintCdt.setColor(lineColor);
        mPaintSysPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSysPoint.setStyle(Paint.Style.FILL);
        mPaintSysPoint.setColor(scalePointColor);
        mPaintLinePoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLinePoint.setStyle(Paint.Style.FILL);
        mPaintLinePoint.setColor(lineColorPoint);
        mPaintSys = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSys.setStyle(Paint.Style.STROKE);
        mPaintSys.setStrokeWidth(coordinateSystemSize);
        mPaintSys.setColor(coordinateSystemColor);
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(30);
        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(lineSize);
        mPaintLine.setColor(lineColor);
        mPaintDash = new Paint();
        mPaintDash.setStyle(Paint.Style.STROKE);
        mPaintDash.setStrokeWidth(dashSize);
        mPaintDash.setColor(dashColor);
        mPaintDash.setPathEffect(new DashPathEffect(new float[]{10,10},0));
        mXBound = new Rect();
        mYBound = new Rect();
        invalidate();
    }

    public void setPoint(ArrayList<Point> points){
        pointList = new ArrayList<>();
        pointList = points;

        int[] xPointArray = new int[100];
        int[] yPointArray = new int[100];

        for (int i = 0;i<pointList.size();i++){
            Point point = pointList.get(i);
            xPointArray[i] = point.x;
            yPointArray[i] = point.y;
        }
        Arrays.sort(xPointArray);
        Arrays.sort(yPointArray);

        X_MAX = xPointArray[xPointArray.length-1];
        Y_MAX = yPointArray[yPointArray.length-1];

        invalidate();
    }

    public ChartView(Context context,  AttributeSet attrs) {
        super(context, attrs);

        getAttrs(context,attrs);
        initPaint();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.ChartView);

        coordinateSystemColor = ta.getColor(R.styleable.ChartView_coordinateSystemColor,Color.RED);
        coordinateSystemSize = ta.getDimension(R.styleable.ChartView_coordinateSystemLinrSize,3f);

        lineColor = ta.getColor(R.styleable.ChartView_lineColor,Color.BLACK);
        lineSize = ta.getDimension(R.styleable.ChartView_lineSize,2f);

        lineColorPoint = ta.getColor(R.styleable.ChartView_linePointColor,Color.RED);
        linrColorPointRadius = ta.getDimension(R.styleable.ChartView_linePointRadius,6f);
        scalePointColor = ta.getColor(R.styleable.ChartView_scalePointColor,Color.RED);
        scalePointRadius = ta.getDimension(R.styleable.ChartView_scalePointRadius,6f);
        isShowDash = ta.getBoolean(R.styleable.ChartView_shoewDash,false);
        dashSize = ta.getDimension(R.styleable.ChartView_setDashSize,2f);
        dashColor = ta.getColor(R.styleable.ChartView_setDashColor,Color.WHITE);

        xScale = ta.getInt(R.styleable.ChartView_setXscale,5);
        yScale = ta.getInt(R.styleable.ChartView_setYscale,5);

        ta.recycle();
    }

    public ChartView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getAttrs(context,attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        else {
            result = 400;
            if (specMode == MeasureSpec.AT_MOST){

            }
        }
        return 0;
    }

    private int measureWidth(int widthMeasureSpec) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewHeight = h;
        mViewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(Margin,mViewHeight-Margin,Margin,5,mPaintSys);
        canvas.drawLine(Margin,mViewHeight-Margin,mViewWidth-5,mViewHeight-Margin,mPaintSys);
        canvas.drawCircle(Margin,mViewHeight-Margin,scalePointRadius,mPaintSysPoint);

        int num_x = X_MAX/xScale;
        mScreenXdistance = (mViewWidth - Margin*2)/(num_x*1f);
        int num_y = Y_MAX/yScale;
        mScreenYdistance = (mViewHeight - Margin*2)/(num_y*1f);
        for (int i = 0; i<pointList.size(); i++){
            canvas.drawCircle(Margin + (i*mScreenXdistance),mViewHeight-Margin,scalePointRadius,mPaintSysPoint);
            canvas.drawCircle(Margin,mViewHeight - Margin - (i*mScreenYdistance),scalePointRadius,mPaintSysPoint);

            String index_x = String.valueOf(i*xScale);
            String index_y = String.valueOf(i*yScale);
            mPaintText.getTextBounds(index_x,0,index_x.length(),mXBound);
            mPaintText.getTextBounds(index_y,0,index_x.length(),mYBound);
            int indexWidth_x = mXBound.width();
            int indexHeight_x = mXBound.height();
            int indexWidth_y = mYBound.width();
            int indexHeight_y = mYBound.height();

            canvas.drawText(index_x,Margin + (i*mScreenXdistance),mViewHeight-Margin+indexHeight_x + indexHeight_x/2,mPaintText);
            if (i!=0){
                canvas.drawText(index_y,Margin - indexHeight_y-indexWidth_y/2,mViewHeight - Margin - (i*mScreenYdistance),mPaintText);
            }
        }
        Point lastPoint = new Point();
        lastPoint.x = Margin;
        lastPoint.y = mViewHeight - Margin;
        for (int i = 0;i<pointList.size();i++){
            float point_x = (pointList.get(i).x/(xScale*1f))* mScreenXdistance;
            float point_y = (pointList.get(i).y/(yScale*1f))* mScreenYdistance;

            float startX = lastPoint.x;
            float startY = lastPoint.y;

            float endX = Margin + point_x;
            float endY = mViewHeight - Margin - point_y;

            canvas.drawLine(startX,startY,endX,endY,mPaintLine);

            lastPoint.x = (int) endX;
            lastPoint.y = (int) endY;

            if (isShowDash){
                canvas.drawLine(Margin,mViewHeight - Margin - point_y - linrColorPointRadius/2,Margin+point_x-linrColorPointRadius/2,mViewHeight- Margin - point_y - linrColorPointRadius/2,mPaintDash);
                canvas.drawLine(lastPoint.x,lastPoint.y,lastPoint.x,mViewHeight - Margin - linrColorPointRadius,mPaintDash);
            }
            canvas.drawCircle(lastPoint.x,lastPoint.y,linrColorPointRadius,mPaintLinePoint);
        }
    }
}
