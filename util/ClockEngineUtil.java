package com.liner.linerlauncher.ClockEngine.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.liner.linerlauncher.LauncherUtil.Launcher;
import com.liner.linerlauncher.LauncherUtil.Other.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClockEngineUtil {
    private static final String CLOCK_ROOT = "ClockSkin";
    private static final String CLOCK_XML_NAME = "clock_skin.xml";
    private static final String CLOCK_MODEL_NAME = "img_clock_preview.png";
    private static final String CLOCK_MODEL_NAME_ALT = "clock_skin_model.png";

    static String[] getAllClockSkins(Context context){
        try {
               return context.getAssets().list(CLOCK_ROOT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initAllClockIndex(){
        new ClockIndexLoader().execute();
    }

    public static String getClockSkinByPosition(Context context, int position){
        try {
            String[] files = context.getAssets().list(CLOCK_ROOT);
            if(files != null && files.length > position){
                return files[position];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getClockSkinModelByName(Context context, String skinName){

        InputStream in;
        Bitmap bitmap = null;

        try {
            in = context.getAssets().open(getClockSkinModelFile(skinName));
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap == null){
            try {
                in = context.getAssets().open(getClockSkinModelFileAlt(skinName));
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return new BitmapDrawable(context.getResources(),bitmap);

    }

    public static String getClockSkinXmlFile(String skinName){
        return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_XML_NAME;
    }
    public static String getClockSkinDetail(String skinName, String pngName){
        return CLOCK_ROOT + File.separator + skinName + File.separator + pngName;
    }
    private static String getClockSkinModelFile(String skinName){
        return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_MODEL_NAME;
    }
    private static String getClockSkinModelFileAlt(String skinName){
        return CLOCK_ROOT + File.separator + skinName + File.separator + CLOCK_MODEL_NAME_ALT;
    }

    private static class ClockIndexLoader extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            if(Launcher.getAppContext() != null){
                try {
                    final String[] clocks = getAllClockSkins(Launcher.getAppContext());

                    final StringBuilder indexString = new StringBuilder();
                    assert clocks != null;
                    for(String clock: clocks){
                        final String[] indexs = clock.split("_");
                        if(indexs.length == 2){
                            indexString.append(indexs[1]).append("#");
                        }else{
                            indexString.append(indexs[0]).append("#");
                        }
                    }
                    if(indexString.length() > 1){
                        return indexString.substring(0, indexString.length() - 1);
                    }
                } catch (Exception e) {
                    cancel(true);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(!isCancelled() && result != null && Launcher.getAppContext()!= null){
                PreferenceManager preferenceManager = PreferenceManager.getInstance(Launcher.getAppContext());
                preferenceManager.saveInt("clock_index", Integer.parseInt(result));
            }
        }


    }
}
