package com.example.viewdamo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

//仪表盘
public class PanelView extends View {
    private static final String TAG = "PanelView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int MARGIN = 100;
    private static final int ANGLE = 160;//圆弧开口角度
    private PathDashPathEffect pathEffect;//虚线特效
    private Path dash = new Path();
    private Path measurePath = new Path();
    private float startAngle = 90 - ANGLE / 2 + 180;//圆弧起始角度，逆时针为正
    private static final int DASH_WIDTH = 8;//刻度条宽度
    private int r;
    private float totalLength;

    public PanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        dash.addRect(0, 0, DASH_WIDTH, 24, Path.Direction.CCW);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measurePath.addArc(MARGIN, MARGIN, getRight() - MARGIN, getBottom() / 2, startAngle, 360 - startAngle + 180 - startAngle);
        PathMeasure pathMeasure = new PathMeasure(measurePath, false);
        totalLength = pathMeasure.getLength();
        float p = (totalLength - DASH_WIDTH) / 20;//分为20段
        pathEffect = new PathDashPathEffect(dash, p, 0, PathDashPathEffect.Style.ROTATE);
        r = getWidth() / 2 - MARGIN;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画圆弧
        drawArc(canvas);
        //画圆弧🌟虚线
        paint.setPathEffect(pathEffect);
        drawArc(canvas);
        paint.setPathEffect(null);

        double angle = (startAngle - 180) + ((ANGLE / 20)) * 10;//要考虑刻度的厚度
        float endY = r + MARGIN - (float) Math.sin(Math.toRadians(angle)) * r;
        float endX = r + MARGIN - (float) Math.cos(Math.toRadians(angle)) * r;
        canvas.drawLine(r + MARGIN, r + MARGIN, endX, endY, paint);
    }

    private void drawArc(Canvas canvas) {
        canvas.drawArc(MARGIN, MARGIN, getWidth() - MARGIN, getHeight() - MARGIN, startAngle, 360 - startAngle + 180 - startAngle, false, paint);
    }
}
