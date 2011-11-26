package com.epl603.ava.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class DrawSettings {



	public static int getDrawColor(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		int color = settings.getInt("drawColor", 0xFFFFFF00);
		return color;
	}

	public static float getPPM(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		float ppm = settings.getFloat(AppConstants.PPM, 2.6520F);
		return ppm;
	}

	public static void saveDrawColor(Context ctx, int color) {
		SharedPreferences settings = ctx.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(AppConstants.DRAW_COLOR, color);
		editor.commit();
	}

	public static void savePPM(Context ctx, float ppm) {
		SharedPreferences settings = ctx.getSharedPreferences(AppConstants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(AppConstants.PPM, ppm);
		editor.commit();
	}

}
