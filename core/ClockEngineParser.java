package com.liner.linerlauncher.ClockEngine.core;

import android.content.Context;

import com.liner.linerlauncher.ClockEngine.util.ClockEngineData;
import com.liner.linerlauncher.R;
import com.liner.linerlauncher.ClockEngine.util.ClockEngineUtil;
import com.liner.linerlauncher.LauncherUtil.Launcher;
import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.liner.linerlauncher.LauncherUtil.Other.Config.*;
public class ClockEngineParser extends ClockEngine {
    private static Drawable mDrawBattery;
    private static Drawable mDrawBatteryGray;
    private static ClockEngineDrawableItem myDrawable;
    private static ArrayList<ClockEngineDrawableItem> myDrawables = null;
    private static long startAnimationTime = -1;
    private long animationTimeCount = 0;
    private boolean mChanged;

    public static ArrayList<ClockEngineDrawableItem> getMyDrawables() {
        return myDrawables;
    }

    public ClockEngineParser(){
        this(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
    public ClockEngineParser(int clockWidth, int clockHeight){
        this.mClockWidth = clockWidth;
        this.mClockHeight = clockHeight;

        myDrawable = null;
        startAnimationTime = 0;
    }
    private void loadDrawableArray(Context paramContext, ClockEngineDrawableItem paramDrawableInfo, String paramString1, String paramString2) {
        PreferenceManager preferenceManager = PreferenceManager.getInstance(Launcher.getAppContext());
        int eventType;
        String itemName;
        ArrayList<Drawable> drawables = null;
        ArrayList<Integer> durations = null;
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            InputStream inputStream;
            if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
                inputStream = new FileInputStream(new File(CLOCK_ENGINE_ROOT_PATH + paramString1 + File.separator + paramString2));
                xmlPullParser.setInput(inputStream, "UTF-8");
            }else{
                inputStream = paramContext.getAssets().open(ClockEngineUtil.getClockSkinDetail(paramString1, paramString2));
                xmlPullParser.setInput(inputStream, "UTF-8");
            }
            eventType = xmlPullParser.getEventType();
            boolean isDone = false;
            while (!isDone) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        if (drawables == null) {
                            drawables = new ArrayList<>();
                        }
                        if (durations == null) {
                            durations = new ArrayList<>();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        isDone = true;
                        paramDrawableInfo.setDrawableArrays(drawables);
                        paramDrawableInfo.setDurationArrays(durations);
                        break;
                    case XmlPullParser.START_TAG:
                        itemName = xmlPullParser.getName();
                        if (ClockEngineConstants.TAG_ANIMATION_ITEMS.equals(itemName)) {
                            startAnimationTime = 0;
                            animationTimeCount = 0;
                        } else if (ClockEngineConstants.TAG_IMAGE.equals(itemName)) {
                            Bitmap bmp;
                            if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
                                String ss = CLOCK_ENGINE_ROOT_PATH + paramString1 + "/" + xmlPullParser.nextText();
                                bmp = BitmapFactory.decodeFile(ss);
                            }else{
                                InputStream in = paramContext.getAssets().open(
                                        ClockEngineUtil.getClockSkinDetail(paramString1, xmlPullParser.nextText()));
                                bmp = BitmapFactory.decodeStream(in);

                            }

                            DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                            bmp.setDensity(dm.densityDpi);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                            if (drawables != null) {
                                drawables.add(bitmapDrawable);
                            }
                        } else if (ClockEngineConstants.TAG_NAME.equals(itemName)) {
                            Bitmap bmp;

                            if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
                                String ss = CLOCK_ENGINE_ROOT_PATH + paramString1 + "/" + xmlPullParser.nextText();
                                bmp = BitmapFactory.decodeFile(ss);
                            }else{
                                InputStream in = paramContext.getAssets().open(
                                        ClockEngineUtil.getClockSkinDetail(paramString1, xmlPullParser.nextText()));
                                bmp = BitmapFactory.decodeStream(in);

                            }
                            DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                            bmp.setDensity(dm.densityDpi);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                            if (drawables != null) {
                                drawables.add(bitmapDrawable);
                            }
                        } else if (ClockEngineConstants.TAG_DURATION.equals(itemName)) {
                            int i = Integer.valueOf(xmlPullParser.nextText());
                            animationTimeCount += i;
                            if (durations != null) {
                                durations.add(i);
                            }
                        }else if(ClockEngineConstants.TAG_CHILD_FOLDER.equals(itemName)){
                            paramDrawableInfo.setChildrenFolderName(xmlPullParser.nextText());
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClockSkinByPath(Context paramContext, String paramString) {
        XmlPullParser xmlPullParser;
        InputStream inputStream;
        try {
            if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
                File file = new File(CLOCK_ENGINE_ROOT_PATH + paramString + "/" + CLOCK_ENGINE_MANIFEST_FILE);
                inputStream = new FileInputStream(file);
            }else{
                inputStream = paramContext.getAssets().open(
                        ClockEngineUtil.getClockSkinXmlFile(paramString));
            }
            xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            boolean isDone = false;
            String localName;
            while (!isDone) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        if (myDrawables == null) {
                            myDrawables = new ArrayList<>();
                        } else {
                            myDrawables.clear();
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        isDone = true;
                        break;
                    case XmlPullParser.START_TAG: {
                        localName = xmlPullParser.getName();
                        if (ClockEngineConstants.TAG_DRAWABLE.equals(localName)) {
                            myDrawable = new ClockEngineDrawableItem();
                        } else if (myDrawable != null) {
                            if (ClockEngineConstants.TAG_NAME.equals(localName)) {
                                localName = xmlPullParser.nextText();
                                int index = localName.lastIndexOf(".");
                                if (index > 0) {
                                    if (ClockEngineConstants.TAG_DRAWABLE_FILE_TYPE.equalsIgnoreCase(localName.substring(index + 1))) {
                                        loadDrawableArray(paramContext, myDrawable, paramString, localName);
                                    } else if (ClockEngineConstants.TAG_DRAWABLE_TYPE.equalsIgnoreCase(localName.substring(index + 1))) {

                                        Bitmap bmp;
                                        if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
                                            String ss = CLOCK_ENGINE_ROOT_PATH + paramString + "/" + localName;
                                            bmp = BitmapFactory.decodeFile(ss);
                                        }else{
                                            InputStream in = paramContext.getAssets().open(
                                                    ClockEngineUtil.getClockSkinDetail(paramString, localName));
                                            bmp = BitmapFactory.decodeStream(in);

                                        }
                                        DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                                        try {
                                            bmp.setDensity(dm.densityDpi);
                                        }catch (RuntimeException e){
                                            e.printStackTrace();
                                        }
                                        BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                                        myDrawable.setDrawable(bitmapDrawable);
                                    }
                                }
                            } else if (ClockEngineConstants.TAG_CENTERX.equals(localName)) {
                                myDrawable.setCenterX(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_PACKAGE_NAME.equals(localName)) {
                                myDrawable.setPackageName(xmlPullParser.nextText());
                            } else if (ClockEngineConstants.TAG_CLASS_NAME.equals(localName)) {
                                myDrawable.setClassName(xmlPullParser.nextText());
                            } else if (ClockEngineConstants.TAG_CENTERY.equals(localName)) {
                                myDrawable.setCenterY(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_ROTATE.equals(localName)) {
                                myDrawable.setRotate(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_MUL_ROTATE.equals(localName)) {
                                myDrawable.setMulRotate(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_OFFSET_ANGLE.equals(localName)) {
                                myDrawable.setAngle(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_ARRAY_TYPE.equals(localName)) {
                                myDrawable.setArrayType(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_COLOR.equals(localName)) {
                                myDrawable.setColor(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_START_ANGLE.equals(localName)) {
                                myDrawable.setStartAngle(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_DIRECTION.equals(localName)) {
                                myDrawable.setDirection(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_TEXT_SIZE.equals(localName)) {
                                myDrawable.setTextsize(Integer.valueOf(xmlPullParser.nextText()));
                            } else if (ClockEngineConstants.TAG_COLOR_ARRAY.equals(localName)) {
                                myDrawable.setColorArray(xmlPullParser.nextText());
                            } else if (ClockEngineConstants.TAG_COUNT.equals(localName)) {
                                startAnimationTime = 0;
                                int count = Integer.valueOf(xmlPullParser.nextText());
                                ArrayList<ClockEngineInfo> snowInfos = new ArrayList<>();
                                Random random = new Random();
                                for (int i = 0; i < count; i++) {
                                    ClockEngineInfo snowInfo = new ClockEngineInfo();
                                    float scale = getResolutionFloat(random.nextFloat());
                                    if (scale < 0.1f) {
                                        scale = 0.1f;
                                    }
                                    snowInfo.setDrawable(zoomDrawable(myDrawable.getDrawable(), scale));
                                    snowInfo.setX(random.nextInt(mClockWidth));
                                    snowInfo.setY(random.nextInt(mClockHeight));
                                    snowInfo.setSpeed(random.nextFloat()/2);
                                    snowInfo.setScale(random.nextFloat());
                                    snowInfos.add(snowInfo);
                                }
                                myDrawable.setClockEngineInfos(snowInfos);
                            } else if (ClockEngineConstants.TAG_DURATION.equals(localName)) {
                                startAnimationTime = 0;
                                int i = Integer.valueOf(xmlPullParser.nextText());
                                myDrawable.setDuration(i);
                            }else if(ClockEngineConstants.TAG_VALUE_TYPE.equals(localName)){
                                myDrawable.setValusType(Integer.valueOf(xmlPullParser.nextText()));
                            }else if(ClockEngineConstants.TAG_PROGRESS_DILIVER_COUNT.equals(localName)){
                                myDrawable.setProfressDiliverCount(Integer.valueOf(xmlPullParser.nextText()));
                            }else if(ClockEngineConstants.TAG_PROGRESS_DILIVER_ARC.equals(localName)){
                                myDrawable.setProgressDiliverArc(Float.valueOf(xmlPullParser.nextText()));
                            }else if(ClockEngineConstants.TAG_PROGRESS_RADIUS.equals(localName)){
                                myDrawable.setCircleRadius(Integer.valueOf(xmlPullParser.nextText()));
                            } else if(ClockEngineConstants.TAG_PROGRESS_STROKEN.equals(localName)){
                                myDrawable.setCircleStroken(Integer.valueOf(xmlPullParser.nextText()));
                            } else if(ClockEngineConstants.TAG_PICTURE_SHADOW.equals(localName)){
                                localName = xmlPullParser.nextText();
                                String ss = paramString + "/" + localName;

                                Bitmap bmp = BitmapFactory.decodeFile(ss);
                                DisplayMetrics dm = paramContext.getResources().getDisplayMetrics();
                                try {
                                    bmp.setDensity(dm.densityDpi);
                                }catch (RuntimeException e){
                                    e.printStackTrace();
                                }
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(paramContext.getResources(), bmp);
                                myDrawable.setShadowDrawable(bitmapDrawable);
                            }
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG: {
                        localName = xmlPullParser.getName();
                        if (ClockEngineConstants.TAG_DRAWABLE.equals(localName)) {
                            if (myDrawable.getArrayType() == ClockEngineConstants.ARRAY_ROTATE_ANIMATION) {
                                myDrawable.setCurrentAngle(myDrawable.getStartAngle());
                                if (myDrawable.getDuration() != 0) {
                                    myDrawable.setRotateSpeed(myDrawable.getAngle() * 1.0f / myDrawable.getDuration());
                                }
                                myDrawable.setDirection(1);
                            }
                            if (myDrawable != null) {
                                myDrawables.add(myDrawable);
                                myDrawable = null;
                            }
                        }
                    }
                    break;
                }
                eventType = xmlPullParser.next();
            }

            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawClockArray(Context paramContext, Canvas paramCanvas, ClockEngineDrawableItem localDrawableInfo) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        boolean is24TimeFormat = isTime24Format(paramContext);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        switch (localDrawableInfo.getArrayType()) {
            case ClockEngineConstants.ARRAY_YEAR_MONTH_DAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalYearMonthDay(paramCanvas, localDrawableInfo.getDrawableArrays(), calendar,
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_MONTH_DAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalMonthAndDay(paramCanvas, localDrawableInfo.getDrawableArrays().get(month / 10),
                            localDrawableInfo.getDrawableArrays().get(month % 10), localDrawableInfo.getDrawableArrays().get(10),
                            localDrawableInfo.getDrawableArrays().get(day / 10),
                            localDrawableInfo.getDrawableArrays().get(day % 10), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;

            case ClockEngineConstants.ARRAY_MONTH:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > (month - 1)) {
                    drawDigitalOnePicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(month - 1),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_MONTH_NEW:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > 0) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get((month + 1) / 10),
                            localDrawableInfo.getDrawableArrays().get((month ) % 10),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_DAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > 0) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(day / 10),
                            localDrawableInfo.getDrawableArrays().get(day % 10),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_DAY_NEW:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() > 0) {
                    drawDigitalOnePicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(day - 1),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;

            case ClockEngineConstants.ARRAY_WEEKDAY:
                if ((localDrawableInfo.getDrawableArrays() != null) && localDrawableInfo.getDrawableArrays().size() >= dayOfWeek - 1) {
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(dayOfWeek - 1),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_HOUR_MINUTE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    Drawable amPm = null;
                    if (!is24TimeFormat && localDrawableInfo.getDrawableArrays().size() == 13) {
                        amPm = localDrawableInfo.getDrawableArrays().get(hour >= 12 ? 12 : 11);
                        hour = hour % 12;
                        if (hour == 0) {
                            hour = 12;
                        }
                    }
                    Drawable hour1 = localDrawableInfo.getDrawableArrays().get(hour / 10);
                    Drawable hour2 = localDrawableInfo.getDrawableArrays().get(hour % 10);
                    Drawable colon = localDrawableInfo.getDrawableArrays().get(10);
                    Drawable minute1 = localDrawableInfo.getDrawableArrays().get(minute / 10);
                    Drawable minute2 = localDrawableInfo.getDrawableArrays().get(minute % 10);
                    drawDigitalHourAndMinute(paramCanvas, hour1, hour2, colon, minute1, minute2, amPm,
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), second);
                }
                break;
            case ClockEngineConstants.ARRAY_HOUR:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(hour / 10),
                            localDrawableInfo.getDrawableArrays().get(hour % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_MINUTE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(minute / 10),
                            localDrawableInfo.getDrawableArrays().get(minute % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY());
                }
            }
            break;
            case ClockEngineConstants.ARRAY_SECOND: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalTwoPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(second / 10),
                            localDrawableInfo.getDrawableArrays().get(second % 10), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY());
                }
            }
            break;
            case ClockEngineConstants.ARRAY_SECOND_NEW: {
                drawsecondCircle(paramCanvas,  localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(),
                        second);

            }
            break;

            case ClockEngineConstants.ARRAY_WEATHER: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(ClockEngineData.getWeatherIcon(false)), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
            }
            break;
            case ClockEngineConstants.ARRAY_TEMPERATURE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int temp = ClockEngineData.getWeatherTemp(false);
                    Drawable minus = localDrawableInfo.getDrawableArrays().get(10);
                    Drawable temp1 = localDrawableInfo.getDrawableArrays().get(Math.abs(temp / 10));
                    Drawable temp2 = localDrawableInfo.getDrawableArrays().get(Math.abs(temp % 10));
                    Drawable tempUnit = localDrawableInfo.getDrawableArrays().get(11);
                    drawTemperature(paramCanvas, minus, temp1, temp2, tempUnit, localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), temp < 0);
                }
            }
            break;
            case ClockEngineConstants.ARRAY_STEPS: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawStepsPicture(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), ClockEngineData.getSteps(false));
                }
            }
            break;
            case ClockEngineConstants.ARRAY_STEPS_NEW: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawStepsPicturenew(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), ClockEngineData.getSteps(false));
                }
            }
            break;
            case ClockEngineConstants.ARRAY_KCAL_NEW: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawKalPicturenew(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), ClockEngineData.getCalories(false));
                }
            }
            break;

            case ClockEngineConstants.ARRAY_HEART_RATE: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawHeartRatePicture(paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), ClockEngineData.getHeartRate(false));
                }
            }
            break;
            case ClockEngineConstants.ARRAY_BATTERY: {
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPicture(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel);
                }
            }
            break;
            case ClockEngineConstants.ARRAY_SPECIAL_SECOND:
                if (localDrawableInfo.getColorArray() != null) {
                    drawSpecialSecond(paramCanvas, localDrawableInfo.getColorArray(), minute, second);
                }
                break;
            case ClockEngineConstants.ARRAY_YEAR:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    drawDigitalYear(paramCanvas, localDrawableInfo.getDrawableArrays(), year, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_BATTERY_WITH_CIRCLE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)
                        && localDrawableInfo.getColorArray() != null) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPictureWithCircleNew(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel,
                            localDrawableInfo.getColorArray(), localDrawableInfo.getCircleRadius());
                }
                break;
            case ClockEngineConstants.ARRAY_BATTERY_WITH_CIRCLE_PIC:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)
                        ) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryPictureWithCirclePic(paramCanvas, localDrawableInfo.getDrawableArrays(),
                            localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), batteryLevel);
                }
                break;
            case ClockEngineConstants.ARRAY_STEPS_WITH_CIRCLE:
                if ((localDrawableInfo.getDrawableArrays() != null) &&
                        (localDrawableInfo.getDrawableArrays().size() > 0) && (localDrawableInfo.getColorArray() != null)) {
                    drawStepsPictureWithCircle(paramContext, paramCanvas, localDrawableInfo.getDrawableArrays(), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(),
                            ClockEngineData.getSteps(false), localDrawableInfo.getColorArray());
                }
                break;
            case ClockEngineConstants.ARRAY_STEPS_CIRCLE_NEW:
            {
                drawStepsCircle(paramContext, paramCanvas, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), ClockEngineData.getSteps(false));
            }
            break;
            case ClockEngineConstants.ARRAY_BATTERY_CIRCLE_NEW:
            {
                if ((localDrawableInfo.getDrawableArrays() != null) &&
                        (localDrawableInfo.getDrawableArrays().size() > 0) ) {
                    int batteryLevel = getBatteryLevel(paramContext);
                    drawBatteryCircle(paramCanvas, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(),
                            batteryLevel, localDrawableInfo.getDrawableArrays());
                }
            }
            break;

            case ClockEngineConstants.ARRAY_MOON_PHASE:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int phase = ClockEngineData.getMoonPhase(false);
                    if(phase > 7){
                        phase = 7;
                    } else if (phase < 0){
                        phase = 0;
                    }
                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(phase), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                }
                break;
            case ClockEngineConstants.ARRAY_AM_PM:
                if ((localDrawableInfo.getDrawableArrays() != null) && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                    if (!is24TimeFormat) {
                        int index = (currentHour >= 12) ? 1 : 0;
                        drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(index), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                    }
                }
                break;
            case ClockEngineConstants.ARRAY_FRAME_ANIMATION:
                if (startAnimationTime <= 0) {
                    startAnimationTime = calendar.getTimeInMillis();
                }
                if (localDrawableInfo.getDrawableArrays() != null && (localDrawableInfo.getDrawableArrays().size() > 0)) {
                    if (localDrawableInfo.getDurationArrays() != null && (localDrawableInfo.getDurationArrays().size() > 0)) {
                        if (animationTimeCount > 0) {
                            long diff = calendar.getTimeInMillis() - startAnimationTime;
                            diff = diff % animationTimeCount;
                            for (int i = 0; i < localDrawableInfo.getDurationArrays().size(); i++) {
                                if (diff < localDrawableInfo.getDurationArrays().get(i)) {
                                    drawClockQuietPicture(paramCanvas, localDrawableInfo.getDrawableArrays().get(i), localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                                    break;
                                }
                                diff -= localDrawableInfo.getDurationArrays().get(i);
                            }
                        }
                    }
                }
                break;
            case ClockEngineConstants.ARRAY_ROTATE_ANIMATION:
                if (startAnimationTime <= 0) {
                    startAnimationTime = calendar.getTimeInMillis();
                }
                if (localDrawableInfo.getDirection() == ClockEngineConstants.ROTATE_CLOCKWISE) {
                    localDrawableInfo.setCurrentAngle(localDrawableInfo.getCurrentAngle() + localDrawableInfo.getRotateSpeed());
                } else {
                    localDrawableInfo.setCurrentAngle(localDrawableInfo.getCurrentAngle() - localDrawableInfo.getRotateSpeed());
                }
                if ((localDrawableInfo.getCurrentAngle() >= localDrawableInfo.getStartAngle() + localDrawableInfo.getAngle()) ||
                        localDrawableInfo.getCurrentAngle() <= localDrawableInfo.getStartAngle()) {
                    if (localDrawableInfo.getDirection() == ClockEngineConstants.ROTATE_CLOCKWISE) {
                        localDrawableInfo.setDirection(ClockEngineConstants.ANTI_ROTATE_CLOCKWISE);
                    } else {
                        localDrawableInfo.setDirection(ClockEngineConstants.ROTATE_CLOCKWISE);
                    }
                }
                if (localDrawableInfo.getDrawable() != null) {
                    drawClockRotatePictureNew(paramCanvas, localDrawableInfo.getDrawable(), localDrawableInfo.getCenterX(),
                            localDrawableInfo.getCenterY(), localDrawableInfo.getCurrentAngle());

                }
                break;
            case ClockEngineConstants.ARRAY_SNOW_ANIMATION:
                if (localDrawableInfo.getDrawable() != null && localDrawableInfo.getClockEngineInfos() != null &&
                        localDrawableInfo.getClockEngineInfos().size() > 0) {
                    if (startAnimationTime <= 0) {
                        startAnimationTime = calendar.getTimeInMillis();
                    }
                    for (ClockEngineInfo snowInfo : localDrawableInfo.getClockEngineInfos()) {
                        float y = snowInfo.getY() + snowInfo.getSpeed();
                        if (y > mClockHeight) {
                            Random random = new Random();
                            y = random.nextInt(mClockHeight / 2);
                        }
                        snowInfo.setY(y);
                        int diffX = Math.abs(snowInfo.getX() - mClockWidth / 2);
                        int diffY = Math.abs((int) snowInfo.getY() - mClockWidth / 2);
                        if (diffX * diffX + diffY * diffY <= (mClockHeight * mClockWidth / 4)) {
                            drawClockQuietPicture(paramCanvas, snowInfo.getDrawable(),
                                    snowInfo.getX(), (int) snowInfo.getY());
                        }
                    }
                }
                break;
            case ClockEngineConstants.ARRAY_CHARGING: {
                if (isChargingNow(paramContext)) {
                    int textColor = Color.WHITE;
                    Drawable chargingDrawable = mDrawBattery;
                    if (localDrawableInfo.getColor() == 1) {
                        chargingDrawable = mDrawBatteryGray;
                        textColor = Color.BLACK;
                    }
                    drawChargingInfo(paramContext, paramCanvas, chargingDrawable, localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY(), textColor);
                }
            }
            break;
            case ClockEngineConstants.ARRAY_PICTURE_HOUR:{
                final int index = calendar.get(Calendar.HOUR)*5 + calendar.get(Calendar.MINUTE)%12;
                final Drawable  currDrawable = localDrawableInfo.getDrawableArrays().get(index);

                drawClockRotatePictureNew(paramCanvas, currDrawable, localDrawableInfo.getCenterX(),
                        localDrawableInfo.getCenterY(), 0);
                break;
            }
            case ClockEngineConstants.ARRAY_PICTURE_MINUTER:{
                final int index = calendar.get(Calendar.MINUTE);
                final Drawable currDrawable = localDrawableInfo.getDrawableArrays().get(index);
                drawClockRotatePictureNew(paramCanvas, currDrawable, localDrawableInfo.getCenterX(),
                        localDrawableInfo.getCenterY(), 0);
                break;
            }
            case ClockEngineConstants.ARRAY_PICTURE_SECOND:{

                final int index = calendar.get(Calendar.SECOND);
                final Drawable currDrawable = localDrawableInfo.getDrawableArrays().get(index);
                drawClockRotatePictureNew(paramCanvas, currDrawable, localDrawableInfo.getCenterX(),
                        localDrawableInfo.getCenterY(), 0);
                break;
            }
            case ClockEngineConstants.ARRAY_PICTURE_HOUR_DIGITE:{

                final int index = calendar.get(Calendar.HOUR_OF_DAY);
                final Drawable currDrawable = localDrawableInfo.getDrawableArrays().get(index);
                drawClockQuietPicture(paramCanvas, currDrawable,
                        localDrawableInfo.getCenterX(), localDrawableInfo.getCenterY());
                break;
            }
            case ClockEngineConstants.ARRAY_VALUE_WITH_PROGRESS:{
                drawValueWithProgress(paramContext, paramCanvas, localDrawableInfo.getDrawableArrays(),
                        localDrawableInfo.getValusType(), localDrawableInfo.getColorArray(),
                        localDrawableInfo.getProgressDiliverArc(), localDrawableInfo.getProfressDiliverCount(),
                        localDrawableInfo.getCenterXNew(), localDrawableInfo.getCenterYNew(),
                        localDrawableInfo.getCircleRadius(), localDrawableInfo.getCircleStroken(),
                        localDrawableInfo.getTextsize());
                break;
            }
            case ClockEngineConstants.ARRAY_VALUE_STRING:{
                drawValueString(paramCanvas, localDrawableInfo.getValusType(),
                        localDrawableInfo.getColorArray(),
                        localDrawableInfo.getCenterXNew(), localDrawableInfo.getCenterYNew(),
                        localDrawableInfo.getTextsize());
                break;
            }
            case ClockEngineConstants.ARRAY_VALUE_WITH_CLIP_PICTURE:{
                drawValueWithClipPicture(paramContext, paramCanvas,localDrawableInfo.getValusType(),
                        localDrawableInfo.getDrawableArrays(),localDrawableInfo.getCenterXNew(), localDrawableInfo.getCenterYNew());
                break;
            }
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public void drawClock(Canvas paramCanvas, Context paramContext) {
        if (myDrawables != null && myDrawables.size() > 0) {
            if (this.mChanged) {
                this.mChanged = false;
                mDrawBattery = paramContext.getResources().getDrawable(R.drawable.clock_battery_panel);
                mDrawBatteryGray = paramContext.getResources().getDrawable(R.drawable.clock_battery_panel_gray);
            }
            ClockEngineDrawableItem drawable = myDrawables.get(0);
            if (drawable != null && drawable.getDrawable() != null) {
                int width = drawable.getDrawable().getIntrinsicWidth();
                int height = drawable.getDrawable().getIntrinsicHeight();
                width = Math.min(width, height);
                if (width != getScaleWidth()) {
                    setScaleWidth(width);
                }
            }
            Calendar calendar = Calendar.getInstance();
            for (ClockEngineDrawableItem drawableInfo : myDrawables) {
                switch (drawableInfo.getRotate()) {
                    case ClockEngineConstants.ROTATE_NONE: {
                        if (drawableInfo.getArrayType() > 0) {
                            drawClockArray(paramContext, paramCanvas, drawableInfo);
                        } else {
                            if (drawableInfo.getDrawable() != null) {
                                drawClockQuietPicture(paramCanvas, drawableInfo.getDrawable(),
                                        drawableInfo.getCenterX(), drawableInfo.getCenterY());
                            }
                        }
                    }
                    break;
                    case ClockEngineConstants.ROTATE_HOUR: {
                        float hourAngle = calendar.get(Calendar.HOUR_OF_DAY) / 12.0f * 360.0f;
                        int minute = calendar.get(Calendar.MINUTE);
                        hourAngle += (float)(minute * 30 / 60);
                        if(drawableInfo.getDirection() == ClockEngineConstants.ANTI_ROTATE_CLOCKWISE){
                            hourAngle = -hourAngle;
                        }
                        drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), hourAngle + drawableInfo.getAngle());
                        if(drawableInfo.getShadowDrawable() != null){
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getShadowDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), hourAngle + drawableInfo.getAngle());
                        }
                    }
                    break;
                    case ClockEngineConstants.ROTATE_MINUTE: {
                        float minuteAngle = calendar.get(Calendar.MINUTE) / 60.0f * 360.0f;
                        if(drawableInfo.getDirection() == ClockEngineConstants.ANTI_ROTATE_CLOCKWISE){
                            minuteAngle = -minuteAngle;
                        }
                        drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), minuteAngle + drawableInfo.getAngle());
                        if(drawableInfo.getShadowDrawable() != null){
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getShadowDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), minuteAngle + drawableInfo.getAngle());
                        }
                    }
                    break;
                    case ClockEngineConstants.ROTATE_SECOND:
                            int second = calendar.get(Calendar.SECOND);
                            int milliSecond = calendar.get(Calendar.MILLISECOND);
                            float secondAngle = (second * 1000 + milliSecond) * 6.0f / 1000.0f;
                            if (drawableInfo.getMulRotate() > 0) {
                                secondAngle *= drawableInfo.getMulRotate();
                            } else if (drawableInfo.getMulRotate() < 0) {
                                secondAngle = secondAngle / (Math.abs(drawableInfo.getMulRotate()));
                            }
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), secondAngle + drawableInfo.getAngle());
                            if(drawableInfo.getShadowDrawable() != null) {
                                drawClockRotatePictureNew(paramCanvas, drawableInfo.getShadowDrawable(), drawableInfo.getCenterX(),
                                        drawableInfo.getCenterY(), secondAngle + drawableInfo.getAngle());
                            }
                        break;
                    case ClockEngineConstants.ROTATE_MONTH:
                        if (drawableInfo.getDrawable() != null) {
                            int maxDay = getDaysByYearMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
                            float angle = (calendar.get(Calendar.MONTH) + 1) * 30.0F + calendar.get(Calendar.DAY_OF_MONTH) * 30.0F / maxDay;
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle());
                        }
                        break;
                    case ClockEngineConstants.ROTATE_WEEK:
                        if (drawableInfo.getDrawable() != null) {
                            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
                            float angle = (weekDay + calendar.get(Calendar.HOUR_OF_DAY) * 1.0F / 24) * 360.0F / 7;
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle());
                        }
                        break;
                    case ClockEngineConstants.ROTATE_BATTERY:
                        if (drawableInfo.getDrawable() != null) {
                            int batteryLevel = getBatteryLevel(paramContext);
                            int direction = drawableInfo.getDirection();
                            final float startoffset = drawableInfo.getProgressDiliverArc();
                            if(startoffset == 0){
                                float angle =  batteryLevel * 180.0F / 100;
                                drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                        drawableInfo.getCenterY(), angle *((direction==1)?1:-1) + drawableInfo.getAngle());
                            }else{
                                float angle1 =  batteryLevel * (180.0F - startoffset*2 ) / 100;
                                drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                        drawableInfo.getCenterY(), angle1 * ((direction == 1) ? 1 : -1) + drawableInfo.getAngle() + startoffset * ((direction == 1) ?1:-1));
                            }
                        }
                        break;
                    case ClockEngineConstants.ROTATE_DAY_NIGHT: {
                        if (drawableInfo.getDrawable() != null) {
                            float angle = calendar.get(Calendar.HOUR_OF_DAY) * 15.0F + calendar.get(Calendar.MINUTE) / 60.0F * 15.0F;
                            drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                    drawableInfo.getCenterY(), angle + drawableInfo.getAngle());
                        }
                    }
                    break;
                    case ClockEngineConstants.ROTATE_HOUR_BG:{
                        float hourAngle = calendar.get(Calendar.HOUR_OF_DAY) / 24.0f * 360.0f;
                        int minute = calendar.get(Calendar.MINUTE);
                        hourAngle += (float)(minute * 30 / 60);
                        drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                drawableInfo.getCenterY(), hourAngle + drawableInfo.getAngle());
                    }
                    break;
                    case ClockEngineConstants.ROTATE_BATTERY_CIRCLE:{
                        if (drawableInfo.getDrawable() != null) {
                            int batteryLevel = getBatteryLevel(paramContext);
                            int direction = drawableInfo.getDirection();
                            final float startoffset = drawableInfo.getProgressDiliverArc();
                            if(startoffset == 0){
                                float angle =  batteryLevel * 360.0F / 100;
                                drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                        drawableInfo.getCenterY(), angle *((direction==1)?1:-1) + drawableInfo.getAngle());
                            }else{
                                float angle1 =  batteryLevel * (360.0F - startoffset*2 ) / 100;
                                drawClockRotatePictureNew(paramCanvas, drawableInfo.getDrawable(), drawableInfo.getCenterX(),
                                        drawableInfo.getCenterY(), angle1 * ((direction == 1) ? 1 : -1) + drawableInfo.getAngle() + startoffset * ((direction == 1) ?1:-1));
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    @SuppressWarnings("deprecation")
    private Drawable zoomDrawable(Drawable drawable, float scale) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    private int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    @Override
    public ClockEngine getChildSkinByPosition(Context paramContext, int paramInt) {
        this.mChanged = true;
        if(CLOCK_ENGINE_USE_EXTERNAL_STORAGE){
            File file = new File(CLOCK_ENGINE_ROOT_PATH);
            if (file.isDirectory()) {
                loadClockSkinByPath(paramContext, file.list()[paramInt]);
            }
            return this;
        }else{
            final String name = ClockEngineUtil.getClockSkinByPosition(paramContext, paramInt);
            if(name != null){
                loadClockSkinByPath(paramContext, name);
            }else{
                return null;
            }
            return this;
        }
    }

    @Override
    public List<ClockEngine> getSkins(Context paramContext) {
        ArrayList<ClockEngine> clockSkins = new ArrayList<>();
        File file = new File(CLOCK_ENGINE_ROOT_PATH);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int i = 0;
            while (i < files.length) {
                File str = files[i];
                String fileName = str.getAbsolutePath() + "/" + "clock_skin_model.png";
                ClockEngineParser clockSkinParse = new ClockEngineParser();
                clockSkinParse.mPreview = fileName;
                clockSkinParse.position = i;
                clockSkins.add(clockSkinParse);
                i += 1;
            }
        }
        return clockSkins;
    }



    @Override
    public void recycleDrawable() {
        if ((myDrawables == null) || (myDrawables.size() <= 0))
            return;
        for (ClockEngineDrawableItem drawableInfo : myDrawables) {
            if (drawableInfo.getDrawable() != null) {
                drawableInfo.getDrawable().setCallback(null);
            }
            if (drawableInfo.getDrawableArrays() != null) {
                for (Drawable drawable : drawableInfo.getDrawableArrays()) {
                    drawable.setCallback(null);
                }
            }
        }
    }
}
