package com.liner.linerlauncher.ClockEngine.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.liner.linerlauncher.R;
import com.liner.linerlauncher.LauncherUtil.Adapters.ClockSkinChooseAdapter;
import com.liner.linerlauncher.ClockEngine.view.ClockEnginePager;
import com.liner.linerlauncher.LauncherUtil.Launcher;
import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;

public class ClockEngineTasker extends AsyncTask<Context, Void, String[]> {
    private ClockSkinChooseAdapter clockSkinChooseAdapter;
    private ClockEnginePager pagerRecylerView;
    private TextView textView;
    private PreferenceManager preferenceManager;


    public ClockEngineTasker(Context context, ClockEnginePager pagerRecylerView, ClockSkinChooseAdapter clockSkinChooseAdapter, TextView textView){
        this.pagerRecylerView = pagerRecylerView;
        this.clockSkinChooseAdapter = clockSkinChooseAdapter;
        this.textView = textView;
        preferenceManager = PreferenceManager.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(Context... params) {
        return ClockEngineUtil.getAllClockSkins(params[0]);

    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        if(!isCancelled()){
            setClockSkinFiles(result);
        }
    }

    public void setClockSkinFiles(String[] files){
        if(files == null || files.length == 0){
            if(textView != null){
                textView.setText(Launcher.getAppContext().getResources().getString(R.string.no_clockskins));
            }
            return;
        }
        if(clockSkinChooseAdapter != null){
            clockSkinChooseAdapter.setClockSkinFiles(files);
        }
        if(pagerRecylerView != null){
            pagerRecylerView.setInitPosition(preferenceManager.getInt("clock_index", 0));
        }
    }

}