package com.lgy.util;

import android.content.Context;

/**
 * Android通過文件名獲取資源id
 */
public class LResourceUtil {

	private LResourceUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	public static int getLayoutId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "layout",
				context.getPackageName());
	}

	public static int getStringId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "string",
				context.getPackageName());
	}

	public static int getDrawableId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "drawable",
				context.getPackageName());
	}

	public static int getStyleId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "style",
				context.getPackageName());
	}

	public static int getId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "id",
				context.getPackageName());
	}

	public static int getColorId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "color",
				context.getPackageName());
	}

	public static int getArrayId(Context context, String paramString) {
		return context.getResources().getIdentifier(paramString, "array",
				context.getPackageName());
	}
}