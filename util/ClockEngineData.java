package com.liner.linerlauncher.ClockEngine.util;

import android.content.Context;
import android.provider.Settings;

import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;

import static com.liner.linerlauncher.LauncherUtil.Other.Config.*;

public class ClockEngineData {
    private static Context context;
    private static PreferenceManager preferenceManager;

    public ClockEngineData(Context context){
        ClockEngineData.context = context;
        preferenceManager = PreferenceManager.getInstance(context);
    }

    public static int getHeartRate(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getInt(SYSTEM_HEART_RATE_KEY, 0);
        } else {
            return Settings.System.getInt(context.getContentResolver(), SYSTEM_HEART_RATE_KEY, 0);
        }
    }

    public static int getSteps(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getInt(SYSTEM_STEP_KEY, 0);
        } else {
            return Settings.System.getInt(context.getContentResolver(), SYSTEM_STEP_KEY, 0);
        }
    }

    public static float getCalories(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getFloat(SYSTEM_CALORIES_KEY, 0);
        } else {
            return Settings.System.getFloat(context.getContentResolver(), SYSTEM_CALORIES_KEY, 0);
        }
    }


    public static int getWeatherIcon(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getInt(SYSTEM_WEATHER_ICON_KEY, 0);
        } else {
            return Settings.System.getInt(context.getContentResolver(), SYSTEM_WEATHER_ICON_KEY, 0);
        }
    }

    public static int getWeatherTemp(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getInt(SYSTEM_WEATHER_TEMP_KEY, 0);
        } else {
            return Settings.System.getInt(context.getContentResolver(), SYSTEM_WEATHER_TEMP_KEY, 0);
        }
    }

    public static int getMoonPhase(boolean fromSystem){
        if(!fromSystem) {
            return preferenceManager.getInt(SYSTEM_MOON_PHASE_KEY, 0);
        } else {
            return Settings.System.getInt(context.getContentResolver(), SYSTEM_MOON_PHASE_KEY, 0);
        }
    }
}
