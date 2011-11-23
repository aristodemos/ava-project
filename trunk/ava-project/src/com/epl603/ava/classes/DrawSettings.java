package com.epl603.ava.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class DrawSettings {

	public static final String PREFS_NAME = "MedImageProPrefs";
	public static final String DRAW_COLOR = "drawColor";
	public static final String PPM = "ppm";

	public static int getDrawColor(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		int color = settings.getInt("drawColor", 0xFFFFFF00);
		return color;
	}

	public static float getPPM(Context ctx) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		float ppm = settings.getFloat(PPM, 2.6520F);
		return ppm;
	}

	public static void saveDrawColor(Context ctx, int color) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(DRAW_COLOR, color);
		editor.commit();
	}

	public static void savePPM(Context ctx, float ppm) {
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(PPM, ppm);
		editor.commit();
	}

}
