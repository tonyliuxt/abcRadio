package com.baby.play.abc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;

public class ConfirmActivity extends Activity {
   
	private ImageView cancelView;
	private ImageView enterView;
	public static Handler MsgHandler;
	public static ArrayList<Activity> activityList = new ArrayList<Activity>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.quit_dialog);
		cancelView = (ImageView) findViewById(R.id.main_cancel_image);
		enterView = (ImageView) findViewById(R.id.main_enter_image);

		final Context context = this.getApplicationContext();
		ConfirmActivity.activityList.add(this);
		cancelView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		enterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exitThisApplication(context);
			}
		});
		
//    	showAdvsOwn();
    }
	
//	private IMvInterstitialAd interstitialAd = null;
//	public void showAdvsOwn(){
//    	// -------------------------------------------------------------------------------
//		/**
//		 * 插屏广告加载完成后立即显示
//		 */
//		interstitialAd = Mvad.showInterstitial(this, IeltsConstant.adSpaceidChaPing, false);
//		interstitialAd.closeAds();
//		interstitialAd.showAds(this);
//    	// -------------------------------------------------------------------------------
//	}
//
	
	public void exitThisApplication(Context context){
		for(Activity act : activityList){
			if(act != null){
				Log.v("two",act.getLocalClassName());
				act.finish();
			}
		}
		
		activityList.clear();
		
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}