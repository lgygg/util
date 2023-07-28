package com.lgy.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {

	private ScreenUtils() {
		throw new AssertionError();
	}
	
    /**
     * 
     * 获取屏幕宽
     * 
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
    	if(null == context)
    	{
    		return -1;
    	}
    	DisplayMetrics dm = context.getApplicationContext().getResources()
				.getDisplayMetrics();
        return dm.widthPixels;
    }

	/**
	 *
	 * 获取Density
	 *
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context)
	{
		if(null == context)
		{
			return -1;
		}
		return context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
	}
    
    /**
     * 
     * 获取屏幕的高
     * 
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
    	if(null == context)
    	{
    		return -1;
    	}
    	DisplayMetrics dm = context.getApplicationContext().getResources()
    			.getDisplayMetrics();
    	return dm.heightPixels;
    }

	/**
	 * 将dp转换成px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context,float dpValue){
		final float scale = context.getResources ().getDisplayMetrics ().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将像素转换成dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources ().getDisplayMetrics ().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
