package com.unitbv.clockapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

public class ClockSurfaceView extends SurfaceView implements Runnable {

    private Context context;
    private float length;
    private Thread animThread = null;
    private SurfaceHolder holder = null;
    private boolean running;

    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int numeralSpacing = 0;
    private int handTruncation, hourHandTruncation = 0;
    private int radius = 0;
    private int clockColor = Color.WHITE;
    private Paint paint;
    private Paint innerPaint;
    private Paint secondPaint;
    private Paint minutePaint;
    private Paint hourPaint;
    private boolean isInit;
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};
    private Rect rect = new Rect();

    public ClockSurfaceView(Context context, float length) {
        super(context);

        this.context = context;
        this.length = length;
        this.animThread = new Thread(this);
        this.animThread.start();
        this.running = true;
        this.holder = getHolder();
    }

    public void run() {
        int sec = 0;

        while(this.running) {
            if(this.holder.getSurface().isValid()) {
                Calendar calendar = Calendar.getInstance();
                sec = calendar.get(Calendar.SECOND);

                Canvas canvas = this.holder.lockCanvas();

                if (!isInit) {
                    initClock();
                }

                canvas.drawColor(Color.BLACK);
                drawCircle(canvas, clockColor);
                drawTicks(canvas);
                drawCenter(canvas);
                drawNumeral(canvas);
                drawHands(canvas, sec);

                postInvalidateDelayed(500);
                invalidate();

                this.holder.unlockCanvasAndPost(canvas);
                try{
                    Thread.sleep(1000);
                    sec++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClockPause() {
        this.running = false;
        boolean reEntry = true;
        while (reEntry) {
            try {
                this.animThread.join();
                reEntry = false;
            } catch (Exception e) {}
        }
    }

    public void onClockResume() {
        this.running = true;
        this.animThread = new Thread(this);
        this.animThread.start();
    }

    public void setClockColor(int color) {

        clockColor = color;
        paint.setColor(color);
    }

    public void setHourHandColor(int color) {
        hourPaint.setColor(color);
    }

    public void setMinuteHandColor(int color) {
        minutePaint.setColor(color);
    }

    public void setSecondHandColor(int color) {
        secondPaint.setColor(color);
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        innerPaint = new Paint();
        initHands();
        isInit = true;
    }

    private void initHands() {
        hourPaint = new Paint();
        minutePaint = new Paint();
        secondPaint = new Paint();

        hourPaint.setColor(Color.WHITE);
        hourPaint.setStrokeWidth(6);
        minutePaint.setColor(Color.BLUE);
        minutePaint.setStrokeWidth(4);
        secondPaint.setColor(Color.RED);
        secondPaint.setStrokeWidth(2f);
    }

    private void drawTicks(Canvas canvas) {
        int tickSec = 10;
        int tick5min = 30;
        int tickQuarters = 50;

        for(int i=1;i<=60;i++) {
            int len = tickSec;
            if(i%15 == 0){
                len = tickQuarters;
            }
            else if(i%5 == 0) {
                len = tick5min;
            }

            double di = (double)i;
            double angleFrom12 = di/60.0*2.0*Math.PI;
            double angleFrom3 = Math.PI/2.0-angleFrom12;

            canvas.drawLine(
                    (float)(getWidth()/2 + Math.cos(angleFrom3)*(radius-30)),
                    (float)(getHeight()/2 - Math.sin(angleFrom3)*(radius-30)),
                    (float)(getWidth()/2 + Math.cos(angleFrom3)*(radius-len-30)),
                    (float)(getHeight()/2 - Math.sin(angleFrom3)*(radius-len-30)),
                    paint
            );
        }
    }

    private void drawHand(Canvas canvas, double loc, boolean isHour, Paint paint) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        canvas.drawLine(
                width/2,
                height/2,
                (float) (width/2 + Math.cos(angle) * handRadius),
                (float)(height/2 + Math.sin(angle) * handRadius),
                paint
        );
    }

    private void drawHands(Canvas canvas, int sec) {
        Calendar c = Calendar.getInstance();
        float hour = c.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas, (hour + c.get(Calendar.MINUTE) / 60f) * 5f, true, hourPaint);
        drawHand(canvas, c.get(Calendar.MINUTE), false, minutePaint);
        drawHand(canvas, sec, false, secondPaint);
    }

    private void drawNumeral(Canvas canvas) {
        paint.setTextSize(fontSize);

        for(int number : numbers) {
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (width/2 + Math.cos(angle) * radius - rect.width()/2);
            int y = (int) (height/2 + Math.sin(angle) * radius + rect.height()/2);
            canvas.drawText(tmp, x, y, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2, height/2, 12, paint);
    }

    private void drawCircle(Canvas canvas, int color) {
        paint.reset();
        paint.setColor(color);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2, height/2, radius + padding - 10, paint);
    }
}
