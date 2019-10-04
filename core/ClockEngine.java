package com.liner.linerlauncher.ClockEngine.core;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import com.liner.linerlauncher.ClockEngine.util.ClockEngineData;
import com.liner.linerlauncher.LauncherUtil.Launcher;
import com.liner.linerlauncher.LauncherUtil.Other.Config;
import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;
import com.liner.linerlauncher.LauncherUtil.Other.UserManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public abstract class ClockEngine implements Serializable {
    String mPreview;
    public int position;
    int mClockWidth = Config.SCREEN_WIDTH;
    int mClockHeight = Config.SCREEN_HEIGHT;
    private int scaleWidth = 400;
    private UserManager userManager;

    int getScaleWidth() {
        return scaleWidth;
    }

    void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    private int getResolution(int paramInt) {
        return paramInt * mClockWidth / scaleWidth;
    }

    float getResolutionFloat(float paramInt) {
        return paramInt * mClockWidth / scaleWidth;
    }

    void drawBatteryPicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int batteryLevel) {
        int b = batteryLevel / 100;
        int t = (batteryLevel / 10) % 10;
        int n = batteryLevel % 10;
        if (batteryLevel < 10) {
            t = 10;
            b = 10;
        }
        Drawable drawable1 = paramList.get(b);
        Drawable drawable2 = paramList.get(t);
        Drawable drawable3 = paramList.get(n);
        Drawable drawable4 = paramList.size() > 11 ? paramList.get(11) : null;
        int width, width2, width3, width4, height;
        width = getResolution(drawable1.getIntrinsicWidth());
        width2 = getResolution(drawable2.getIntrinsicWidth());
        width3 = getResolution(drawable3.getIntrinsicWidth());
        if(drawable4 != null){
            width4 = getResolution(drawable4.getIntrinsicWidth());
        } else {
            width4 = 0;
        }
        height = getResolution(drawable3.getIntrinsicHeight());
        if (b == 10 || b == 0) {
            width = 0;
        }
        centerX -= (width + width2 + width3 + width4) / 2;
        centerY -= height / 2;
        if (b > 0) {
            drawable1.setBounds(centerX, centerY, centerX + width, centerY + height);
            drawable1.draw(paramCanvas);
            centerX += width;
        }
        drawable2.setBounds(centerX, centerY, centerX + width2, centerY + height);
        drawable2.draw(paramCanvas);
        centerX += width2;
        drawable3.setBounds(centerX, centerY, centerX + width3, centerY + height);
        drawable3.draw(paramCanvas);
        if (drawable4 != null) {
            centerX += width3;
            drawable4.setBounds(centerX, centerY, centerX + width4, centerY + height);
            drawable4.draw(paramCanvas);
        }
    }

    void drawBatteryPictureWithCirclePic(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int batteryLevel) {
        Drawable batteryPic;
        int index = 0;
        switch (batteryLevel){
            case 100:
                index = 10;
                break;
            case 90:
                index = 9;
                break;
            case 80:
                index = 8;
                break;
            case 70:
                index = 7;
                break;
            case 60:
                index = 6;
                break;
            case 50:
                index = 5;
                break;
            case 40:
                index = 4;
                break;
            case 30:
                index = 3;
                break;
            case 20:
                index = 2;
                break;
            case 10:
                index = 1;
                break;
            case 0:
                index = 0;
                break;
        }
        batteryPic = paramList.get(index);
        int width = getResolution(batteryPic.getIntrinsicWidth());
        int height = getResolution(batteryPic.getIntrinsicHeight());
        centerX -= width / 2;
        centerY -= height / 2;
        batteryPic.setBounds(centerX, centerY, centerX + width, centerY + height);
        batteryPic.draw(paramCanvas);
    }





    void drawBatteryPictureWithCircleNew(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int batteryLevel, String colorsInfo, int radius) {

        batteryLevel = Math.min(100, batteryLevel);
        batteryLevel = Math.max(0, batteryLevel);
        int h = batteryLevel / 100;
        int t = batteryLevel / 10 % 10;
        int g = batteryLevel % 10;
        int center = centerX;
        int highColor;
        int normalColor;
        Paint paint;
        if (batteryLevel < 10) {
            h = 10;
            t = 10;
        }
        Drawable drawable1 = paramList.get(h);
        Drawable drawable2 = paramList.get(t);
        Drawable drawable3 = paramList.get(g);
        Drawable drawable4 = paramList.size() > 11 ? paramList.get(11) : null;
        int width, width2, width3, width4, height;
        width = getResolution(drawable1.getIntrinsicWidth());
        width2 = getResolution(drawable2.getIntrinsicWidth());
        width3 = getResolution(drawable3.getIntrinsicWidth());
        if (drawable4 != null) {
            width4 = getResolution(drawable4.getIntrinsicWidth());
        } else {
            width4 = 0;
        }
        height = getResolution(drawable3.getIntrinsicHeight());
        if (h == 0) {
            width = 0;
        }
        centerX -= (width + width2 + width3 + width4) / 2;
        centerY -= height / 2;
        if (h > 0) {
            drawable1.setBounds(centerX, centerY, centerX + width, centerY + height);
            drawable1.draw(paramCanvas);
        }
        centerX += width;
        drawable2.setBounds(centerX, centerY, centerX + width2, centerY + height);
        drawable2.draw(paramCanvas);
        centerX += width2;
        drawable3.setBounds(centerX, centerY, centerX + width3, centerY + height);
        drawable3.draw(paramCanvas);

        if (drawable4 != null) {
            centerX += width3;
            drawable4.setBounds(centerX, centerY, centerX + width4, centerY + height);
            drawable4.draw(paramCanvas);
        }

        if (colorsInfo.contains(",")) {
            normalColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[0], 16);
            highColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[1], 16);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(getResolution(15));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha(255);
            paramCanvas.save();

            if (radius == 0) {
                centerY += height / 2;
                paramCanvas.translate(center, centerY);
                paramCanvas.scale(getResolutionFloat(0.28f), getResolutionFloat(0.28f));
                paramCanvas.rotate(180.0F);
                for (int i = 0; i < 20; i++) {
                    paint.setColor((i < batteryLevel / 5) ? highColor : normalColor);
                    paramCanvas.drawLine(getResolution(7), centerY, getResolution(7), centerY + getResolution(25), paint);
                    paramCanvas.rotate(18.0F, 0.0F, 0.0F);
                }
            } else {
                paint.setStrokeWidth(getResolution(7));
                centerY += (height + 5);
                paramCanvas.translate(center, centerY);
                paramCanvas.scale((float) 2 * radius / Config.SCREEN_HEIGHT, (float) 2 * radius / Config.SCREEN_WIDTH);
                paramCanvas.rotate(180.0F);
                for (int i = 0; i < 20; i++) {
                    paint.setColor((i < batteryLevel / 5) ? highColor : normalColor);
                    paramCanvas.drawLine(getResolution(7), centerY, getResolution(7), centerY + getResolution(25), paint);
                    paramCanvas.rotate(18.0F, 0.0F, 0.0F);
                }
            }
            paramCanvas.restore();
        }

    }


    void drawChargingInfo(Context paramContext, Canvas paramCanvas, Drawable paramDrawable, int centerX, int centerY, int paramInt3) {
        int batteryLevel = getBatteryLevel(paramContext), width, height;
        width = getResolution(paramDrawable.getIntrinsicWidth());
        height = getResolution(paramDrawable.getIntrinsicHeight());
        paramDrawable.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        paramDrawable.draw(paramCanvas);
        String battery = batteryLevel + "%";
        Paint localPaint = new Paint();
        localPaint.setTextSize(getResolution(20));
        localPaint.setAntiAlias(true);
        localPaint.setColor(paramInt3);
        paramCanvas.drawText(battery, paramDrawable.getBounds().right + getResolution(5), paramDrawable.getBounds().bottom - getResolution(6), localPaint);
    }

    void drawClockQuietPicture(Canvas paramCanvas, Drawable paramDrawable, int centerX, int centerY) {
        int width = getResolution(paramDrawable.getIntrinsicWidth());
        int height = getResolution(paramDrawable.getIntrinsicHeight());
        if (width <= 3) {
            paramDrawable.setBounds(centerX - width / 2 + 1, centerY - height / 2, centerX + width / 2 + 1, centerY + height / 2);
        } else {
            paramDrawable.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        }
        paramDrawable.draw(paramCanvas);
    }

    void drawDigitalHourAndMinute(Canvas canvas, Drawable drawableHour1, Drawable drawableHour2, Drawable drawableColon, Drawable drawableMinute1, Drawable drawableMinute2, Drawable drawableAMPM, int centerX, int centerY, int second) {
        int widthHour = getResolution(drawableHour1.getIntrinsicWidth());
        int heightHour = getResolution(drawableHour1.getIntrinsicHeight());
        int widthColon = getResolution(drawableColon.getIntrinsicWidth());
        int heightColon = getResolution(drawableColon.getIntrinsicHeight());
        int widthAMPM, heightAMPM;
        if (drawableAMPM != null) {
            widthAMPM = getResolution(drawableAMPM.getIntrinsicWidth());
            heightAMPM = getResolution(drawableAMPM.getIntrinsicHeight());
        } else {
            widthAMPM = 0;
            heightAMPM = 0;
        }
        centerX -= (widthHour * 2 + widthColon / 2 + widthAMPM / 2);
        drawableHour1.setBounds(centerX, centerY - heightHour / 2, centerX + widthHour, centerY + heightHour / 2);
        drawableHour1.draw(canvas);
        centerX += widthHour;
        drawableHour2.setBounds(centerX, centerY - heightHour / 2, centerX + widthHour, centerY + heightHour / 2);
        drawableHour2.draw(canvas);
        centerX += widthHour;
        if (second % 2 == 0) {
            drawableColon.setBounds(centerX, centerY - heightColon / 2, centerX + widthColon, centerY + heightColon / 2);
            drawableColon.draw(canvas);
        }
        centerX += widthColon;
        drawableMinute1.setBounds(centerX, centerY - heightHour / 2, centerX + widthHour, centerY + heightHour / 2);
        drawableMinute1.draw(canvas);
        centerX += widthHour;
        drawableMinute2.setBounds(centerX, centerY - heightHour / 2, centerX + widthHour, centerY + heightHour / 2);
        drawableMinute2.draw(canvas);
        centerX += widthHour;
        if (drawableAMPM != null) {
            drawableAMPM.setBounds(centerX, centerY - heightHour / 2, centerX + widthAMPM, centerY - heightHour / 2 + heightAMPM);
            drawableAMPM.draw(canvas);
        }
    }

    void drawDigitalMonthAndDay(Canvas canvas, Drawable month1, Drawable month2, Drawable colon, Drawable day1, Drawable day2, int centerX, int centerY) {
        int widthM1 = getResolution(month1.getIntrinsicWidth());
        int heightM1 = getResolution(month1.getIntrinsicHeight());
        int widthM2 = getResolution(month2.getIntrinsicWidth());
        int heightM2 = getResolution(month2.getIntrinsicHeight());
        int widthColon = getResolution(colon.getIntrinsicWidth());
        int heightColon = getResolution(colon.getIntrinsicHeight());
        int widthD1 = getResolution(day1.getIntrinsicWidth());
        int heightD1 = getResolution(day1.getIntrinsicHeight());
        int widthD2 = getResolution(day2.getIntrinsicWidth());
        int heightD2 = getResolution(day2.getIntrinsicHeight());
        centerX -= (widthM1 + widthM2 + widthColon + widthD1 + widthD2) / 2;
        month1.setBounds(centerX, centerY - (heightM1 / 2), centerX + widthM1, centerY + heightM1 / 2);
        month1.draw(canvas);
        centerX += widthM1;
        month2.setBounds(centerX, centerY - (heightM2 / 2), centerX + widthM2, centerY + (heightM2 / 2));
        month2.draw(canvas);
        centerX += widthM2;
        colon.setBounds(centerX, centerY - (heightColon / 2), centerX + widthColon, centerY + (heightColon / 2));
        colon.draw(canvas);
        centerX += widthColon;
        day1.setBounds(centerX, centerY - (heightD1 / 2), centerX + widthD1, centerY + (heightD1 / 2));
        day1.draw(canvas);
        centerX += widthD1;
        day2.setBounds(centerX, centerY - (heightD2 / 2), centerX + widthD2, centerY + (heightD2 / 2));
        day2.draw(canvas);
    }

    void drawDigitalOnePicture(Canvas canvas, Drawable drawable, int centerX, int centerY) {
        int width = getResolution(drawable.getIntrinsicWidth());
        int height = getResolution(drawable.getIntrinsicHeight());
        centerX -= width / 2;
        centerY -= height / 2;
        drawable.setBounds(centerX, centerY, centerX + width, centerY + height);
        drawable.draw(canvas);
    }


    void drawDigitalTwoPicture(Canvas canvas, Drawable drawable1, Drawable drawable2, int centerX, int centerY) {
        int width = getResolution(drawable1.getIntrinsicWidth());
        int height = getResolution(drawable1.getIntrinsicHeight());
        int width2 = getResolution(drawable2.getIntrinsicWidth());
        int height2 = getResolution(drawable2.getIntrinsicHeight());
        centerX -= (width + width2) / 2;
        centerY -= (height) / 2;
        drawable1.setBounds(centerX, centerY, centerX + width, centerY + height);
        drawable1.draw(canvas);
        centerX = centerX + width;
        centerY -= (height - height2) / 2;
        drawable2.setBounds(centerX, centerY, centerX + width2, centerY + height2);
        drawable2.draw(canvas);
    }

    void drawDigitalYear(Canvas canvas, List<Drawable> drawables, int currentYear, int centerX, int centerY) {
        int l = (drawables.get(0)).getIntrinsicWidth();
        int k = (drawables.get(0)).getIntrinsicHeight();
        int i = getResolution(l);
        int j = getResolution(k);
        k = centerY - j / 2;
        centerY += j / 2;
        j = currentYear / 1000;
        l = currentYear / 100 % 10;
        int i1 = currentYear / 10 % 10;
        currentYear %= 10;
        centerX -= i * 2;
        int i2 = centerX + i;
        int i3 = i2 + i;
        int i4 = i3 + i;
        drawables.get(j).setBounds(centerX, k, i2, centerY);
        drawables.get(j).draw(canvas);
        drawables.get(l).setBounds(i2, k, i3, centerY);
        drawables.get(l).draw(canvas);
        drawables.get(i1).setBounds(i3, k, i4, centerY);
        drawables.get(i1).draw(canvas);
        drawables.get(currentYear).setBounds(i4, k, i4 + i, centerY);
        drawables.get(currentYear).draw(canvas);
    }

    void drawDigitalYearMonthDay(Canvas paramCanvas, List<Drawable> paramList, Calendar calendar, int centerX, int centerY) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int width = getResolution((paramList.get(0)).getIntrinsicWidth());
        int height = getResolution((paramList.get(0)).getIntrinsicHeight());
        int colonHeight = getResolution((paramList.get(10)).getIntrinsicHeight());
        centerX = centerX - (width * 8 + colonHeight * 2) / 2;
        centerY -= height / 2;
        (paramList.get(year / 1000)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(year / 1000)).draw(paramCanvas);
        centerX += width;
        (paramList.get(year / 100 % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(year / 100 % 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(year / 10 % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(year / 10 % 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(year % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(year % 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(10)).setBounds(centerX, centerY, centerX + width, centerY + colonHeight);
        (paramList.get(10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(month / 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(month / 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(month % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(month % 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(day / 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(day / 10)).draw(paramCanvas);
        centerX += width;
        (paramList.get(day % 10)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(day % 10)).draw(paramCanvas);
    }

    void drawHeartRatePicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int hearRate) {
        int b = 0, t, n;
        hearRate = Math.min(hearRate, 999);
        hearRate = Math.max(hearRate, 0);
        if (hearRate > 99) {
            b = hearRate / 100;
            hearRate = hearRate % 100;
        }
        t = hearRate / 10 % 10;
        n = hearRate % 10;
        int width = getResolution((paramList.get(0)).getIntrinsicWidth());
        int height = getResolution((paramList.get(0)).getIntrinsicHeight());
        centerY -= height / 2;
        if (b > 0) {
            centerX -= width * 3 / 2;
            (paramList.get(b)).setBounds(centerX, centerY, centerX + width, centerY + height);
            (paramList.get(b)).draw(paramCanvas);
            centerX += width;
        } else {
            centerX -= width;
        }
        (paramList.get(t)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += width;
        (paramList.get(n)).setBounds(centerX, centerY, centerX + width, centerY + height);
        (paramList.get(n)).draw(paramCanvas);


    }

    void drawSpecialSecond(Canvas paramCanvas, String colorsInfo, int minute, int second) {
        int centerX = mClockWidth / 2;
        int highColor;
        int normalColor;
        Paint paint;
        if (colorsInfo.contains(",")) {
            highColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[0], 16);
            normalColor = 0xFF000000 | Integer.valueOf(colorsInfo.split(",")[1], 16);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(getResolution(10));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAlpha(255);
            paramCanvas.save();
            paramCanvas.translate(centerX, centerX);
            float f = -centerX + 5;
            for (int i = 0; i < 60; i++) {
                if (minute % 2 == 0) {
                    paint.setColor((i < second) ? highColor : normalColor);
                } else {
                    paint.setColor((i < second) ? normalColor : highColor);
                }

                paramCanvas.drawLine(getResolution(5), f, getResolution(5), f + getResolution(15), paint);
                paramCanvas.rotate(6.0F, 0.0F, 0.0F);
            }

            paramCanvas.restore();
        }
    }

    void drawStepsPicture(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int step) {
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int w = step / 10000;
        int k = step / 1000 % 10;
        int h = step / 100 % 10;
        int t = step / 10 % 10;
        int g = step % 10;
        int widthW = getResolution((paramList.get(w)).getIntrinsicWidth());
        int widthK = getResolution((paramList.get(k)).getIntrinsicWidth());
        int widthH = getResolution((paramList.get(h)).getIntrinsicWidth());
        int widthT = getResolution((paramList.get(t)).getIntrinsicWidth());
        int widthG = getResolution((paramList.get(g)).getIntrinsicWidth());
        int height = getResolution((paramList.get(w)).getIntrinsicHeight());
        centerX -= (widthW + widthK + widthH + widthT + widthG) / 2;
        centerY -= height / 2;
        (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
        (paramList.get(w)).draw(paramCanvas);
        centerX += widthW;
        (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
        (paramList.get(k)).draw(paramCanvas);
        centerX += widthK;
        (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
        (paramList.get(h)).draw(paramCanvas);
        centerX += widthH;
        (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += widthT;
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);

    }

    void drawStepsPicturenew(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int step) {
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int w = step / 10000;
        int k = step / 1000 % 10;
        int h = step / 100 % 10;
        int t = step / 10 % 10;
        int g = step % 10;

        int widthW = getResolution((paramList.get(w)).getIntrinsicWidth());
        int widthK = getResolution((paramList.get(k)).getIntrinsicWidth());
        int widthH = getResolution((paramList.get(h)).getIntrinsicWidth());
        int widthT = getResolution((paramList.get(t)).getIntrinsicWidth());
        int widthG = getResolution((paramList.get(g)).getIntrinsicWidth());
        int height = getResolution((paramList.get(w)).getIntrinsicHeight());
        int ljdmm = 5;
        if (w == 0) {
            widthW = 0;
            ljdmm = 4;
            if (k == 0) {
                widthK = 0;
                ljdmm = 3;
                if (h == 0) {
                    widthH = 0;
                    ljdmm = 2;
                    if (t == 0) {
                        widthT = 0;
                        ljdmm = 1;
                    }
                }

            }

        }
        centerX -= (widthW + widthK + widthH + widthT + widthG) / 2;
        centerY -= height / 2;
        switch (ljdmm){
            case 1:
                (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
                (paramList.get(t)).draw(paramCanvas);
                centerX += widthT;
                break;
            case 2:
                (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
                (paramList.get(h)).draw(paramCanvas);
                centerX += widthH;
                break;
            case 3:
                (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
                (paramList.get(k)).draw(paramCanvas);
                centerX += widthK;
                break;
            case 4:
                (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
                (paramList.get(w)).draw(paramCanvas);
                centerX += widthW;
                break;

        }
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);

    }

    void drawKalPicturenew(Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, double kal) {
        kal = Math.max(kal, 0.0);
        kal = Math.min(kal, 99999.9);
        int w = (int) (kal / 10000);
        int k = (int) (kal / 1000 % 10);
        int h = (int) (kal / 100 % 10);
        int t = (int) (kal / 10 % 10);
        int g = (int) (kal % 10);
        int d = (int) (kal * 10 % 10);
        int widthW = getResolution((paramList.get(w)).getIntrinsicWidth());
        int widthK = getResolution((paramList.get(k)).getIntrinsicWidth());
        int widthH = getResolution((paramList.get(h)).getIntrinsicWidth());
        int widthT = getResolution((paramList.get(t)).getIntrinsicWidth());
        int widthG = getResolution((paramList.get(g)).getIntrinsicWidth());
        int widthDot = getResolution((paramList.get(paramList.size() - 1)).getIntrinsicWidth());
        int widthD = getResolution((paramList.get(d)).getIntrinsicWidth());
        int height = getResolution((paramList.get(w)).getIntrinsicHeight());
        int index = 5;
        if (w == 0) {
            widthW = 0;
            index = 4;
            if (k == 0) {
                widthK = 0;
                index = 3;
                if (h == 0) {
                    widthH = 0;
                    index = 2;
                    if (t == 0) {
                        widthT = 0;
                        index = 1;
                    }
                }

            }

        }
        centerX -= (widthW + widthK + widthH + widthT + widthG + widthDot + widthD) / 2;
        centerY -= height / 2;
        switch (index){
            case 1:
                (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
                (paramList.get(t)).draw(paramCanvas);
                centerX += widthT;
                break;
            case 2:
                (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
                (paramList.get(h)).draw(paramCanvas);
                centerX += widthH;
                break;
            case 3:
                (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
                (paramList.get(k)).draw(paramCanvas);
                centerX += widthK;
                break;
            case 4:
                (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
                (paramList.get(w)).draw(paramCanvas);
                centerX += widthW;
                break;
        }
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);
        centerX += widthG;
        (paramList.get(paramList.size() - 1)).setBounds(centerX, centerY, centerX + widthDot, centerY + height);
        (paramList.get(paramList.size() - 1)).draw(paramCanvas);
        centerX += widthDot;
        (paramList.get(d)).setBounds(centerX, centerY, centerX + widthD, centerY + height);
        (paramList.get(d)).draw(paramCanvas);
    }


    void drawStepsPictureWithCircle(Context paramContext, Canvas paramCanvas, List<Drawable> paramList, int centerX, int centerY, int step, String colorsInfo) {
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int center = centerX;
        int w = step / 10000;
        int k = step / 1000 % 10;
        int h = step / 100 % 10;
        int t = step / 10 % 10;
        int g = step % 10;
        int widthW = getResolution((paramList.get(w)).getIntrinsicWidth());
        int widthK = getResolution((paramList.get(k)).getIntrinsicWidth());
        int widthH = getResolution((paramList.get(h)).getIntrinsicWidth());
        int widthT = getResolution((paramList.get(t)).getIntrinsicWidth());
        int widthG = getResolution((paramList.get(g)).getIntrinsicWidth());
        int height = getResolution((paramList.get(w)).getIntrinsicHeight());
        centerX -= (widthW + widthK + widthH + widthT + widthG) / 2;
        centerY -= height / 2;
        (paramList.get(w)).setBounds(centerX, centerY, centerX + widthW, centerY + height);
        (paramList.get(w)).draw(paramCanvas);
        centerX += widthW;
        (paramList.get(k)).setBounds(centerX, centerY, centerX + widthK, centerY + height);
        (paramList.get(k)).draw(paramCanvas);
        centerX += widthK;
        (paramList.get(h)).setBounds(centerX, centerY, centerX + widthH, centerY + height);
        (paramList.get(h)).draw(paramCanvas);
        centerX += widthH;
        (paramList.get(t)).setBounds(centerX, centerY, centerX + widthT, centerY + height);
        (paramList.get(t)).draw(paramCanvas);
        centerX += widthT;
        (paramList.get(g)).setBounds(centerX, centerY, centerX + widthG, centerY + height);
        (paramList.get(g)).draw(paramCanvas);
        centerY += height / 2;
        int highColor;
        int normalColor;
        Paint paint;
        normalColor = Integer.valueOf(colorsInfo.split(",")[0], 16);
        highColor = Integer.valueOf(colorsInfo.split(",")[1], 16);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(getResolution(10));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paramCanvas.save();
        RectF rectF = new RectF();
        rectF.set(center - (float)mClockWidth / 8, centerY - (float)mClockHeight / 8, (float)mClockWidth / 8 + center, (float)mClockHeight / 8 + centerY);
        int j = Settings.System.getInt(paramContext.getContentResolver(), "suggest_steps", 0);
        paint.setColor(0xFF000000 | normalColor);
        paramCanvas.drawCircle(center, centerY, (float)mClockWidth / 8, paint);
        paint.setColor(0xFF000000 | highColor);
        if (step > j) {
            paramCanvas.drawCircle(center, centerY, (float)mClockWidth / 8, paint);
        }
        paramCanvas.drawArc(rectF, 270.0F, step / (float) j * 360.0F, false, paint);

        paramCanvas.restore();
    }

    void drawStepsCircle(Context paramContext, Canvas paramCanvas, int centerX, int centerY, int step) {
        userManager = UserManager.getInstance(paramContext);
        step = Math.max(step, 0);
        step = Math.min(step, 99999);
        int normalColor = 0xFF3CD62B;
        Paint paint;
        TextPaint textPaint;
        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setTextSize(20);
        int r = mClockWidth * 2 / 13;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(getResolution(7));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paramCanvas.save();
        RectF rectF = new RectF();
        rectF.set(centerX - r, centerY - r,r + centerX, r + centerY);
        int j = userManager.getTargetDaySteps();
        paint.setColor(normalColor);
        if (step > j) {
            paramCanvas.drawCircle(centerX, centerY, r, paint);
        }
        paramCanvas.drawArc(rectF, 270.0F, step / (float) j * 360.0F, false, paint);
        paramCanvas.drawText(String.valueOf(step), centerX - textPaint.measureText(String.valueOf(step)) / 2, centerY + 30, textPaint);
        paramCanvas.restore();
    }

    void drawBatteryCircle(Canvas paramCanvas, int centerX, int centerY, int batteryLevel, List<Drawable> paramList) {
        int normalColor = 0xFFEF3062;
        Paint paint;
        TextPaint textPaint;
        textPaint = new TextPaint();
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setTextSize(20);
        int r = mClockWidth * 2 / 13;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(getResolution(7));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paramCanvas.save();
        RectF rectF = new RectF();
        rectF.set(centerX - r, centerY - r,r + centerX, r + centerY);
        paint.setColor(normalColor);
        if (batteryLevel > 99) {
            paramCanvas.drawCircle(centerX, centerY, r, paint);
        }
        paramCanvas.drawArc(rectF, 270.0F, batteryLevel * 3.6f, false, paint);
        paramCanvas.drawText(batteryLevel + "%", centerX - textPaint.measureText(batteryLevel + "%") / 2, centerY + 30, textPaint);
        paramCanvas.restore();
        Drawable batteryPic;
        int index = 0;
        switch (batteryLevel){
            case 100:
                index = 10;
                break;
            case 90:
                index = 9;
                break;
            case 80:
                index = 8;
                break;
            case 70:
                index = 7;
                break;
            case 60:
                index = 6;
                break;
            case 50:
                index = 5;
                break;
            case 40:
                index = 4;
                break;
            case 30:
                index = 3;
                break;
            case 20:
                index = 2;
                break;
            case 10:
                index = 1;
                break;
            case 0:
                index = 0;
                break;
        }
        batteryPic = paramList.get(index);
        int width = getResolution(batteryPic.getIntrinsicWidth());
        int height = getResolution(batteryPic.getIntrinsicHeight());
        centerX -= (width) / 2;
        centerY -= height * 3 / 2;
        batteryPic.setBounds(centerX, centerY, centerX + width, centerY + height);
        batteryPic.draw(paramCanvas);
    }

    void drawsecondCircle(Canvas paramCanvas, int centerX, int centerY, int second) {
        int juli = 25;
        Paint paint;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(32);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paramCanvas.save();
        RectF rectF = new RectF();
        rectF.set(centerX - ((float) mClockWidth / 2 - juli), centerY - ((float) mClockHeight / 2 - juli),
                centerX + ((float) mClockWidth / 2 - juli), ((float) mClockHeight / 2 - juli) + centerY);
        paint.setColor(0x7AFFFF00);
        for (int s = 0; s < second + 1; s++) {
            paramCanvas.drawArc(rectF, 270 - 0.75f + s * 6, 1.5f, false, paint);
            paramCanvas.drawArc(rectF, 270 - 0.75f + s * 6 + 3, 1.5f, false, paint);
        }
        paramCanvas.restore();
    }

    void drawTemperature(Canvas paramCanvas, Drawable minus, Drawable temp1, Drawable temp2, Drawable tempUnit, int centerX, int centerY, boolean paramBoolean1) {
        int widthMinus = getResolution(minus.getIntrinsicWidth());
        int heightMinus = getResolution(minus.getIntrinsicHeight());
        int widthTemp1 = getResolution(temp1.getIntrinsicWidth());
        int heightTemp1 = getResolution(temp1.getIntrinsicHeight());
        int widthTemp2 = getResolution(temp2.getIntrinsicWidth());
        int heightTemp2 = getResolution(temp2.getIntrinsicHeight());
        int widthTempUnit = getResolution(tempUnit.getIntrinsicWidth());
        int heightTempUnit = getResolution(tempUnit.getIntrinsicHeight());
        centerX -= (widthMinus + widthTemp1 + widthTemp2 + widthTempUnit) / 2;
        if (paramBoolean1) {
            minus.setBounds(centerX, centerY - heightMinus / 2, centerX + widthMinus, centerY + heightMinus / 2);
            minus.draw(paramCanvas);
            centerX += widthMinus;
        }
        temp1.setBounds(centerX, centerY - heightTemp1 / 2, centerX + widthTemp1, centerY + heightTemp1 / 2);
        temp1.draw(paramCanvas);
        centerX += widthTemp1;
        temp2.setBounds(centerX, centerY - heightTemp2 / 2, centerX + widthTemp2, centerY + heightTemp2 / 2);
        temp2.draw(paramCanvas);
        centerX += widthTemp2;
        tempUnit.setBounds(centerX, centerY - heightTemp2 / 2, centerX + widthTempUnit, centerY - heightTemp2 / 2 + heightTempUnit);
        tempUnit.draw(paramCanvas);
    }

    void drawValueWithProgress(Context paramContext, Canvas paramCanvas, ArrayList<Drawable> drawables, int valueType, String colorsInfo, float diliverArc, int dilivercount,
                               int centerX, int centerY, int radius, int strokewidth, int textSize) {
        centerX = getResolution(centerX);
        centerY = getResolution(centerY);
        radius = getResolution(radius);
        strokewidth = getResolution(strokewidth);
        textSize = getResolution(textSize);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokewidth);
        paint.setStyle(Paint.Style.STROKE);
        final float arc;
        if (diliverArc > 0) {
            arc = (float)360 / dilivercount - diliverArc;
        } else {
            arc = 360;
        }
        final float hightArc = getHightArc(paramContext, valueType);
        final int colornormal = Integer.valueOf(colorsInfo.split(",")[0], 16) + 0xFF000000;
        final int colorhighlight = Integer.valueOf(colorsInfo.split(",")[1], 16) + 0xFF000000;
        RectF rectf = new RectF();
        rectf.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float startArc = 0.0f;
        float arcTmp;
        paramCanvas.save();
        while (startArc < 360.0f) {
            arcTmp = hightArc - startArc;
            if (arcTmp >= arc) {
                paint.setColor(colorhighlight);
                drawArcStartAtUp(paramCanvas, rectf, startArc, arc, paint);
            } else if (arcTmp > 0) {
                paint.setColor(colorhighlight);
                drawArcStartAtUp(paramCanvas, rectf, startArc, arcTmp, paint);
                paint.setColor(colornormal);
                drawArcStartAtUp(paramCanvas, rectf, startArc + arcTmp, (arc - arcTmp), paint);
            } else {
                paint.setColor(colornormal);
                drawArcStartAtUp(paramCanvas, rectf, startArc, arc, paint);
            }
            startArc += (arc + diliverArc);
        }
        if (drawables != null && drawables.size() > 0) {
            final int index = (int) ((drawables.size() - 1) * hightArc / 360);
            final Drawable drawable = drawables.get(index);
            int width = getResolution(drawable.getIntrinsicWidth());
            int height = getResolution(drawable.getIntrinsicHeight());
            final int left = centerX - width / 2;
            final int right = centerX + width / 2;
            final int top = centerY - height / 2;
            final int bottom = centerY + height / 2;
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(paramCanvas);
        } else {
            final String valueString = getValueString(paramContext, valueType);
            if (valueString != null) {
                final Rect rect = new Rect();
                Paint textPaint = new Paint();
                textPaint.setTextSize(textSize);
                textPaint.setColor(colorhighlight);
                textPaint.setFakeBoldText(true);
                textPaint.setAntiAlias(true);
                textPaint.setDither(true);
                textPaint.getTextBounds(valueString, 0, valueString.length(), rect);

                paramCanvas.drawText(valueString, centerX - (float) rect.width() / 2, centerY + (float) rect.height() / 2, textPaint);
            }
        }
        paramCanvas.restore();
    }

    void drawValueString(Canvas paramCanvas, int valueType, String colorsInfo,int centerX, int centerY, int textSize) {
        centerX = getResolution(centerX);
        centerY = getResolution(centerY);
        textSize = getResolution(textSize);
        final int textColor = Integer.valueOf(colorsInfo.split(",")[0], 16) + 0xFF000000;
        final String valueString = getValueString(valueType);
        final Rect rect = new Rect();
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setFakeBoldText(true);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.getTextBounds(valueString, 0, valueString.length(), rect);
        paramCanvas.drawText(valueString, centerX - (float) rect.width() / 2, centerY + (float) rect.height() / 2, textPaint);
    }

    void drawValueWithClipPicture(Context paramContext, Canvas paramCanvas, int valueType, ArrayList<Drawable> drawables, int centerX, int centerY) {
        int widthMinus = getResolution(drawables.get(0).getIntrinsicWidth());
        int heightMinus = getResolution(drawables.get(0).getIntrinsicHeight());
        centerX = getResolution(centerX);
        centerY = getResolution(centerY);
        final int left = centerX - widthMinus / 2;
        final int right = centerX + widthMinus / 2;
        final int top = centerY - heightMinus / 2;
        final int bottom = centerY + heightMinus / 2;
        if (drawables.size() < 1) {
            return;
        }
        final Drawable bg = drawables.get(0);
        bg.setBounds(left, top, right, bottom);
        bg.draw(paramCanvas);

        if (drawables.size() < 2) {
            return;
        }
        final Drawable clipBg = drawables.get(1);
        final float radius = (float) Math.max(widthMinus, heightMinus) / 2;
        final float startArc = 0;
        final float sweelArc = getHightArc(paramContext, valueType);
        final float startX = centerX;
        final float startY = centerY - radius;
        final float endX = (float) (centerX + radius * Math.sin(sweelArc * Math.PI / 180));
        final float endY = (float) (centerY - radius * Math.cos(sweelArc * Math.PI / 180));
        Path path = new Path();
        path.moveTo(centerX, centerY);
        path.lineTo(startX, startY);
        path.lineTo(endX, endY);
        path.close();
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        path.addArc(rectF, startArc + 270, sweelArc);
        paramCanvas.save();
        paramCanvas.clipPath(path);
        clipBg.setBounds(left, top, right, bottom);
        clipBg.draw(paramCanvas);
        paramCanvas.restore();
        if (drawables.size() < 3) {
            return;
        }
        final Drawable snap = drawables.get(2);

        paramCanvas.save();
        paramCanvas.rotate(sweelArc, centerX, centerY);
        snap.setBounds(left, top, right, bottom);
        snap.draw(paramCanvas);
        paramCanvas.restore();
    }

    void drawClockRotatePictureNew(Canvas paramCanvas, Drawable drawable, int centerX, int centerY, float rotateAngle) {
        int width = getResolution(drawable.getIntrinsicWidth());
        int height = getResolution(drawable.getIntrinsicHeight());
        paramCanvas.save();
        paramCanvas.translate(0, ClockEngineConstants.PICTUTE_SHADOW_CENTERY);
        centerY += ClockEngineConstants.PICTUTE_SHADOW_CENTERY;
        paramCanvas.rotate(rotateAngle, centerX, centerY);
        drawable.setBounds(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
        drawable.draw(paramCanvas);
        paramCanvas.restore();

    }

    void drawClockInputItem(Canvas canvas, Drawable drawable, int centerX, int centerY, ClockEngineDrawableItem clockEngineDrawableItem){
        int width = getResolution(drawable.getIntrinsicWidth());
        int height = getResolution(drawable.getIntrinsicHeight());
        centerX -= width / 2;
        centerY -= height / 2;
        drawable.setBounds(centerX, centerY, centerX + width, centerY + height);
        drawable.draw(canvas);

    }



    public void onTouch(int touchX, int touchY, ClockEngineDrawableItem item){
        int drawableWigth = 50;//item.getDrawable().getMinimumWidth();
        int drawableHeight = 50;//item.getDrawable().getMinimumHeight();

        Log.d("TouchClock", "\n\nTouched on x:"+touchX+" | y:"+touchY +" ||\n" +
                "  \t\t\tDrawable data\n" +
                "------------------------------------\n" +
                "Width: "+drawableWigth+"\n" +
                "Height: "+drawableHeight+"\n" +
                "Class: "+item.getClassName()+"\n" +
                "Package: "+item.getPackageName()+"\n" +
                "PositionX (center): "+item.getCenterX()+"\n" +
                "PositionY (center): "+item.getCenterY()+"\n" +
                "StartY: "+(item.getCenterY()-(drawableHeight/2))+"\n" +
                "EndY: "+(item.getCenterY()+(drawableHeight/2))+"\n" +
                "StartX: "+(item.getCenterY() - (drawableWigth/2))+"\n" +
                "EndX: "+(item.getCenterY() + (drawableWigth/2)));
    }

    private String getValueString(int valueType) {
        switch (valueType) {
            case ClockEngineConstants.VALUE_TYPE_STEP:
                return "Шаги";
            case ClockEngineConstants.VALUE_TYPE_KCAL:
                return "Ккал";
            case ClockEngineConstants.VALUE_TYPE_BATTERY:
                return "Батарея";
            case ClockEngineConstants.VALUE_TYPE_SECOND:
                return String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
            case ClockEngineConstants.VALUE_TYPE_MONTH_AND_DAY:
                return (Calendar.getInstance().get(Calendar.MONTH) + 1)
                        + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            case ClockEngineConstants.VALUE_TYPE_WEEKDAY:
                return ClockEngineConstants.getWeekDayString(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        }
        return "";
    }

    private void drawArcStartAtUp(Canvas paramCanvas, RectF oval, float startAngle, float sweepAngle, Paint paint) {
        paramCanvas.drawArc(oval, startAngle + 270, sweepAngle, false, paint);
    }

    private static Object String2Object(String paramString)
            throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(paramString.getBytes(), 0)));
        Object localObject = objectInputStream.readObject();
        objectInputStream.close();
        return localObject;
    }

    private static ClockEngine defaultSkin(Context paramContext) {
        return new ClockEngineParser().getChildSkinByPosition(paramContext, 0);
    }

    public static ClockEngine getDefaultSkin(Context paramContext) {
        ClockEngine clockEngine;
        String localObject = android.preference.PreferenceManager.getDefaultSharedPreferences(paramContext).getString("sp_clock_skin", null);
        if (localObject != null)
            try {
                clockEngine = (ClockEngine) String2Object(localObject);
                return clockEngine;
            } catch (StreamCorruptedException localStreamCorruptedException) {
                localStreamCorruptedException.printStackTrace();
                return defaultSkin(paramContext);
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
                return defaultSkin(paramContext);
            } catch (ClassNotFoundException localClassNotFoundException) {
                localClassNotFoundException.printStackTrace();
                return defaultSkin(paramContext);
            }
        return defaultSkin(paramContext);
    }

    public ClockEngine getChildSkin(Context paramContext, ClockEngine paramSkin) {
        return getChildSkinByPosition(paramContext, paramSkin.position);
    }

    int getBatteryLevel(Context paramContext) {
        Intent intent = paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        return Objects.requireNonNull(intent).getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    boolean isChargingNow(Context paramContext) {
        boolean isChargingNow = false;
        if (Objects.requireNonNull(paramContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))).getIntExtra("plugged", 0) != 0)
            isChargingNow = true;
        return isChargingNow;
    }

    private float getHightArc(Context paramContext, int valueType) {
        switch (valueType) {
            case ClockEngineConstants.VALUE_TYPE_KCAL:
                return (360 * getCal(paramContext) / getCalTarget(paramContext));
            case ClockEngineConstants.VALUE_TYPE_STEP:
                return (float) 360 * getSteps(paramContext) / getStepsTarget(paramContext);
            case ClockEngineConstants.VALUE_TYPE_BATTERY:
                return (float) 360 * getBatteryLevel(paramContext) / 100;
            default:
                return 0;
        }
    }

    private String getValueString(Context paramContext, int valueType) {
        switch (valueType) {
            case ClockEngineConstants.VALUE_TYPE_KCAL:
                return new DecimalFormat("0.0").format(getCal(paramContext));
            case ClockEngineConstants.VALUE_TYPE_STEP:
                return String.valueOf(getSteps(paramContext));
            case ClockEngineConstants.VALUE_TYPE_BATTERY:
                return String.valueOf(getBatteryLevel(paramContext));
            default:
                return String.valueOf(0);
        }
    }

    private int getSteps(Context paramContext) {
        return ClockEngineData.getSteps(false);
    }

    private int getCal(Context paramContext) {
        float calories = ClockEngineData.getCalories(false);
        return (int) calories;
    }

    private float getCalTarget(Context paramContext) {
        userManager = UserManager.getInstance(paramContext);
        return userManager.getTargetDayCalories();
    }

    private int getStepsTarget(Context paramContext) {
        userManager = UserManager.getInstance(paramContext);
        return userManager.getTargetDaySteps();
    }

    boolean isTime24Format(Context paramContext) {
        return DateFormat.is24HourFormat(paramContext);
    }

    public abstract void drawClock(Canvas paramCanvas, Context paramContext);

    protected abstract ClockEngine getChildSkinByPosition(Context paramContext, int paramInt);

    protected abstract List<ClockEngine> getSkins(Context paramContext);


    public abstract void recycleDrawable();

    protected boolean showSecond = true;

    public void setShowSecond(boolean isShow) {
        this.showSecond = isShow;
    }
}

