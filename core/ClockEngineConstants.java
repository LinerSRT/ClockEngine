package com.liner.linerlauncher.ClockEngine.core;

import com.liner.linerlauncher.R;
import com.liner.linerlauncher.LauncherUtil.Launcher;

import java.util.Calendar;

public class ClockEngineConstants {
    public static final int ROTATE_CLOCKWISE = 1;
    public static final int ANTI_ROTATE_CLOCKWISE = 2;
    public static final int ROTATE_NONE = 0;
    public static final int ROTATE_HOUR = 1;
    public static final int ROTATE_MINUTE = 2;
    public static final int ROTATE_SECOND = 3;
    public static final int ROTATE_MONTH = 4;
    public static final int ROTATE_WEEK = 5;
    public static final int ROTATE_BATTERY = 6;
    public static final int ROTATE_DAY_NIGHT = 7;
    public static final int ROTATE_HOUR_BG = 8;
    public static final int ROTATE_MINUTE_BG = 9;
    public static final int ROTATE_SECOND_BG = 10;
    public static final int ROTATE_BATTERY_CIRCLE = 11 ;

    public static final int ARRAY_YEAR_MONTH_DAY = 1;
    public static final int ARRAY_MONTH_DAY = 2;
    public static final int ARRAY_MONTH = 3;
    public static final int ARRAY_DAY = 4;
    public static final int ARRAY_WEEKDAY = 5;
    public static final int ARRAY_HOUR_MINUTE = 6;
    public static final int ARRAY_HOUR = 7;
    public static final int ARRAY_MINUTE = 8;
    public static final int ARRAY_SECOND = 9;
    public static final int ARRAY_WEATHER = 10;
    public static final int ARRAY_TEMPERATURE = 11;
    public static final int ARRAY_STEPS = 12;
    public static final int ARRAY_HEART_RATE = 13;
    public static final int ARRAY_BATTERY = 14;
    public static final int ARRAY_SPECIAL_SECOND = 15;
    public static final int ARRAY_YEAR = 16;
    public static final int ARRAY_BATTERY_WITH_CIRCLE = 17;
    public static final int ARRAY_STEPS_WITH_CIRCLE = 18;
    public static final int ARRAY_MOON_PHASE = 19;
    public static final int ARRAY_AM_PM = 20;
    public static final int ARRAY_FRAME_ANIMATION = 21;
    public static final int ARRAY_ROTATE_ANIMATION = 22;
    public static final int ARRAY_SNOW_ANIMATION = 23;
    public static final int ARRAY_BATTERY_WITH_CIRCLE_PIC = 24;
    public static final int ARRAY_PICTURE_HOUR = 30;
    public static final int ARRAY_PICTURE_MINUTER = 31;
    public static final int ARRAY_PICTURE_SECOND = 32;
    public static final int ARRAY_PICTURE_HOUR_DIGITE = 33;
    public static final int ARRAY_VALUE_WITH_PROGRESS = 34;
    public static final int ARRAY_VALUE_STRING = 35;
    public static final int ARRAY_VALUE_WITH_CLIP_PICTURE= 36;
    public static final int ARRAY_MONTH_NEW= 37;
    public static final int ARRAY_DAY_NEW= 38;
    public static final int ARRAY_SECOND_NEW= 39;
    public static final int ARRAY_STEPS_NEW= 40;
    public static final int ARRAY_KCAL_NEW= 41;

    public static final int ARRAY_STEPS_CIRCLE_NEW= 42;
    public static final int ARRAY_BATTERY_CIRCLE_NEW= 43;

    public static final int ARRAY_TEXT_PEDOMETER = 97;
    public static final int ARRAY_TEXT_HEARTRATE = 98;
    public static final int ARRAY_CHARGING = 99;
    public static final int VALUE_TYPE_DRAWABLE = 0;
    public static final int VALUE_TYPE_STEP = 1;
    public static final int VALUE_TYPE_KCAL = 2;
    public static final int VALUE_TYPE_BATTERY = 3;
    public static final int VALUE_TYPE_SECOND = 4;
    public static final int VALUE_TYPE_MONTH_AND_DAY = 5;
    public static final int VALUE_TYPE_WEEKDAY = 6;

    public static final String TAG_ARRAY_TYPE = "arraytype";
    public static final String TAG_CENTERX = "centerX";
    public static final String TAG_CENTERY = "centerY";
    public static final String TAG_COLOR = "color";
    public static final String TAG_COLOR_ARRAY = "colorarray";
    public static final String TAG_DIRECTION = "direction";
    public static final String TAG_DRAWABLE = "drawable";
    public static final String TAG_DRAWABLES = "drawables";
    public static final String TAG_IMAGE = "image";
    public static final String TAG_MUL_ROTATE = "mulrotate";
    public static final String TAG_NAME = "name";
    public static final String TAG_OFFSET_ANGLE = "angle";
    public static final String TAG_ROTATE = "rotate";
    public static final String TAG_START_ANGLE = "startAngle";
    public static final String TAG_TEXT_SIZE = "textsize";
    public static final String TAG_DRAWABLE_FILE_TYPE = "xml";
    public static final String TAG_DRAWABLE_TYPE = "png";
    public static final String TAG_ANIMATION_ITEMS = "animationItems";
    public static final String TAG_ANIMATION_ITEM = "animationItem";
    public static final String TAG_DURATION = "duration";
    public static final String TAG_COUNT = "count";

    public static final String TAG_CHILD_FOLDER = "child_folder";
    public static final String TAG_VALUE_TYPE = "value_type";
    public static final String TAG_PROGRESS_DILIVER_ARC = "diliver_arc";
    public static final String TAG_PROGRESS_DILIVER_COUNT = "diliver_count";
    public static final String TAG_PROGRESS_RADIUS = "circle_radius";
    public static final String TAG_PROGRESS_STROKEN= "circle_stroken";
    public static final String TAG_PICTURE_SHADOW = "shadow_picture";
    public static final String CURRENT_CLOCK_SKIN_KEY = "current_clock";

    public static final String TAG_CLASS_NAME = "cls";
    public static final String TAG_PACKAGE_NAME = "pkg";


    public static  String getWeekDayString(int which){
        switch (which){
            case Calendar.SUNDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_sunday);
            case Calendar.MONDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_monday);
            case Calendar.TUESDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_tue);
            case Calendar.WEDNESDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_wed);
            case Calendar.THURSDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_thu);
            case Calendar.FRIDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_fri);
            case Calendar.SATURDAY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_sat);
        }
        return "";
    }
    public static String getValueTypeString(int valueType){
        switch (valueType){
            case VALUE_TYPE_STEP:
                return Launcher.getAppContext().getResources().getString(R.string.clock_steps);
            case VALUE_TYPE_KCAL:
                return Launcher.getAppContext().getResources().getString(R.string.clock_cal);
            case VALUE_TYPE_BATTERY:
                return Launcher.getAppContext().getResources().getString(R.string.clock_bat);
        }
        return "";
    }

    public static final int PICTUTE_SHADOW_CENTERY = 2;
}
