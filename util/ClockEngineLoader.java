package com.liner.linerlauncher.ClockEngine.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.liner.linerlauncher.ClockEngine.core.ClockEngine;
import com.liner.linerlauncher.ClockEngine.core.ClockEngineParser;
import com.liner.linerlauncher.ClockEngine.view.ClockEngineView;

@SuppressLint("StaticFieldLeak")
public class ClockEngineLoader extends AsyncTask<Integer, Void, ClockEngine> {
    private Context context;
    private static ClockEngineView mClockView;


    public ClockEngineLoader(Context context, ClockEngineView clockView){
        this.context = context;
        mClockView = clockView;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected ClockEngine doInBackground(Integer... params) {
        ClockEngineParser parser = new ClockEngineParser(params[0], params[0]);
        ClockEngine clockSkin = null;
        try{
            clockSkin = parser.getChildSkinByPosition(context, params[1]);
            if(!isCancelled()){
                int clockSkinIndex = params[1];
            }
        }catch (Exception e){
            if(!isCancelled()){
                return null;
            }
        }
        return clockSkin;
    }

    @Override
    protected void onPostExecute(ClockEngine result) {
        super.onPostExecute(result);
        if(!isCancelled()){
            mClockView.setClockSkin(result);
        }
    }
}