package com.liner.linerlauncher.ClockEngine.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.liner.linerlauncher.ClockEngine.core.ClockEngine;
import com.liner.linerlauncher.ClockEngine.view.ClockEngineChooseSkin;
import com.liner.linerlauncher.ClockEngine.view.ClockEngineView;
import com.liner.linerlauncher.LauncherUtil.Launcher;
import com.liner.linerlauncher.LauncherUtil.Other.Config;
import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;


@SuppressLint("StaticFieldLeak")
public class ClockEngineViewModel {
    private PreferenceManager preferenceManager;
    private static ClockEngineView clockView;
    private static ClockEngineLoader clockSkinLoader;
    private static Activity activity;

    public ClockEngineViewModel(Context context, ClockEngineView clockView, ClockEngineLoader clockSkinLoader, Activity activity){
        ClockEngineViewModel.clockView = clockView;
        ClockEngineViewModel.clockSkinLoader = clockSkinLoader;
        ClockEngineViewModel.activity = activity;
        preferenceManager = PreferenceManager.getInstance(context);

    }

    public void drawClockSkin(){
        startGetClockSkin(preferenceManager.getInt("clock_index", 0));
    }
    public static void setClockSkin(ClockEngine clockSkin){
        if(clockView == null){
            return;
        }
        if(clockSkin == null){
            startGetClockSkin(0);
        }else{
            clockView.setClockSkin(clockSkin);
        }
    }
    private static void startGetClockSkin(int position){
        if(clockSkinLoader != null){
            final ClockEngineLoader oldTask = clockSkinLoader;
            oldTask.cancel(true);
            clockSkinLoader = null;
        }
        clockSkinLoader = new ClockEngineLoader(Launcher.getAppContext(), clockView);
        clockSkinLoader.execute(Config.SCREEN_WIDTH+Config.CLOCK_ENGINE_FIX_SCALE, position);

    }
    public void gotoChangeClock() {
        Intent intent = new Intent(activity, ClockEngineChooseSkin.class);
        activity.startActivity(intent);
    }

}
