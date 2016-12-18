package com.baby.play.abc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.Toast;

public class IeltsUtil {
	
	/**
	 * 判断对应的程序包是否安装
	 * @param context
	 * @param packagename
	 * @return
	 */
	public static boolean isAppInstalled(Context context,String packagename)
	{
		PackageInfo packageInfo;        
		try {	
			packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
		}catch (Exception e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			//System.out.println("没有安装");
			return false;
		}else{
			//System.out.println("已经安装");
			return true;
		}
	}
	
	/**
	 * 进入下载URL
	 * @param url
	 */
	public static void loadUrl(Context context, String url) {
		if (!NetworkUtils.DetectNetwork(context)) {
			// 当前网络不可用，按钮应该不可点击；
        	Toast.makeText(context, IeltsConstant.NET_HINT, Toast.LENGTH_SHORT).show();
        	return;
		}
        Intent intent = new Intent();        
        intent.setAction("android.intent.action.VIEW");    
        Uri content_url = Uri.parse(url);   
        intent.setData(content_url);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
   }

	/**
	 * 启动某个应用
	 * @param context
	 * @param packagename
	 */
	public static void startAppByName(Context context, String packagename){
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packagename);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);  
	}
	
	/**
	 * 根据报名，url自动处理：如果已经安装了就直接打开，否则才进入下载；
	 * @param context
	 * @param packagename
	 * @param loadurl
	 */
	public static void appClickDeal(Context context, String packagename, String loadurl){
		if(isAppInstalled(context, packagename)){
			startAppByName(context, packagename);
		}else{
			loadUrl(context, loadurl);
		}
	}
}
