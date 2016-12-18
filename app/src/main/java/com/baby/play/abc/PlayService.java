package com.baby.play.abc;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.Metadata;
import io.vov.vitamio.Vitamio;

public class PlayService extends Service implements OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, OnErrorListener,
		OnInfoListener {

	private MediaPlayer mPlayer;
	private OnPreparedListener preparedListener = null;
	private OnErrorListener errorListener = null;
	private PhoneStateReceiver receiver = null;
	private static int currentPlayID;
	
	public void setOnPreparedListener(OnPreparedListener listener) {
		this.preparedListener = listener;
	}

	public interface OnPreparedListener {
		public void onPreparedStart(Metadata data);
	}

	public void setOnErrorListener(OnErrorListener listener) {
		this.errorListener = listener;
	}

	public interface OnErrorListener {
		public void onError();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	public class MyBinder extends Binder {

		public PlayService getService() {
			return PlayService.this;
		}
	}

	private MyBinder myBinder = new MyBinder();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MainUi.addUpdateHandler(selectItemHandler);
		Log.v("PlayService", "onCreate called");
		if (Vitamio.isInitialized(this)) {
			vplayerInit(false);
		} else {
			stopSelf();
		}
		receiver = new PhoneStateReceiver();
		registerReceiver(receiver, getFilter());
	}

	private IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		return filter;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MainUi.removeUpdateHandler(selectItemHandler);
		unregisterReceiver(receiver);
		super.onDestroy();
		releaseMediaPlayer();
	}

	private void releaseMediaPlayer() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		// 正在播放
		
		Message msg2 = new Message();
		msg2.what = MainUi.radio_service_stop;
		Bundle data2 = new Bundle();
		data2.putInt("id", currentPlayID);
		msg2.setData(data2);
		MainUiUseHandle.sendMessage(msg2);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.v("PlayService", "onPrepared called");
		if (mPlayer != null) {
			mPlayer.setBufferSize(512 * 1024); // 单位byte
			mPlayer.start();// 开始播
			

			mPlayer.setMetaEncoding("utf-8");
			Metadata data = mPlayer.getMetadata();
			if (preparedListener != null){
				preparedListener.onPreparedStart(data);
			}
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		Log.v("PlayService", "onCompletion called");
		releaseMediaPlayer();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		//Log.v("PlayService", "onBufferingUpdate called percent = " + percent);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		Log.v("PlayService", "onError called");
		releaseMediaPlayer();
		if (errorListener != null) {
			errorListener.onError();
		}
		return true;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		// Log.v("PlayService", "onInfo called");
		switch (what) {
		case MediaPlayer.MEDIA_INFO_FILE_OPEN_OK:
	        long buffersize = mPlayer.audioTrackInit();
	        mPlayer.audioInitedOk(buffersize);
	        break;
	        
	    case MediaPlayer.MEDIA_INFO_UNKNOW_TYPE:
	        Log.e("PlayService", " VITAMIO--TYPE_CHECK  stype  not include  onInfo mediaplayer unknow type ");
	        break;
	    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			// 开始缓存，暂停播放
			Log.v("PlayService", "onInfo start = " + extra);
			// 正在播放 
			Message msg2 = new Message();
			msg2.what = MainUi.radio_service_playing;
			Bundle data2 = new Bundle();
			data2.putInt("id", currentPlayID);
			msg2.setData(data2);
			MainUiUseHandle.sendMessage(msg2);
			
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			// 缓存完成，继续播 
			Log.v("PlayService", "onInfo end = " + extra);
			//mPlayer.start();
			
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			// 显示下载速度
			Log.v("PlayService", "onInfo rate = " + extra);
			// 正在播放 
			Message msg3 = new Message();
			msg3.what = MainUi.radio_service_rate;
			Bundle data3 = new Bundle();
			data3.putInt("rate", extra);
			msg3.setData(data3);
			MainUiUseHandle.sendMessage(msg3);
			break;
		default:
			Message msgerr = new Message();
			msgerr.what = MainUi.radio_service_error;
			Bundle dataerr = new Bundle();
			dataerr.putInt("rate", extra);
			msgerr.setData(dataerr);
			MainUiUseHandle.sendMessage(msgerr);
			break;
		}
		return false;
	}
	
	/**
	 * 播放URL
	 * @param url
	 */
	private void playSelectItem(String url) {
		// 正在缓存 
		Message msg1 = new Message();
		msg1.what = MainUi.radio_service_buffering;
		Bundle data1 = new Bundle();
		data1.putInt("id", currentPlayID);
		msg1.setData(data1);
		MainUiUseHandle.sendMessage(msg1);

		Log.v("PlayService", "playSelectItem url = " + url +" with id:"+currentPlayID);

		if (mPlayer == null){
			vplayerInit(false);
		}
		mPlayer.reset();
		Uri uri = Uri.parse(url);
		try {
			mPlayer.setDataSource(PlayService.this, uri); // 设置流媒体的数据 
			mPlayer.prepareAsync(); // 需要缓冲的不能使用prepare是阻塞的，prepareAsync是异 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startPlay() {
		if (mPlayer != null) {
			mPlayer.start();
		}
	}

	private void pause() {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	  private void vplayerInit(boolean isHWCodec) {
		try {
			mPlayer = new MediaPlayer(this.getApplicationContext(), isHWCodec);// 播放流媒体的对象
			mPlayer.setOnBufferingUpdateListener(this); // 在网络视频流缓冲变化时调 
			mPlayer.setOnCompletionListener(this); // 视频播放完成后调 
			mPlayer.setOnPreparedListener(this); // 在视频预处理完成后调 
			mPlayer.setOnErrorListener(this); // 在异步操作调用过程中发生错误时调用。例如视频打开失败
			mPlayer.setOnInfoListener(this); // 在有警告或错误信息时调用。例如：开始缓冲、缓冲结束、下载速度变化
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean isPlaying() {
		if (mPlayer != null)
			return mPlayer.isPlaying();
		return false;
	}
	public static Handler MainUiUseHandle;	
	private Handler selectItemHandler = new SelectItemHandler(this);
	private static class SelectItemHandler extends WeakHandler<PlayService> {

		public SelectItemHandler(PlayService owner) {
			super(owner);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			PlayService owner = getOwner();
			if (owner == null)
				return;
			switch (msg.what) {
			case MainUi.play_stat_url:
				Bundle data = msg.getData();
				String url = data.getString("url");
				int tempid = data.getInt("id");
				currentPlayID = tempid;
				Log.v("anim","id:"+tempid);
				if(url != null && url.trim().length() > 0){
					owner.playSelectItem(url);
				}
				break;
			case MainUi.play_stat_play:
				owner.startPlay();
				break;
			case MainUi.play_stat_pause:
				owner.pause();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 处理来电广播
	 * 
	 * @author Administrator
	 * 
	 */
	class PhoneStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();

			if (action.equalsIgnoreCase(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
				final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
				Log.v("PhoneStateReceiver","onReceive state = " + state);
				if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING) || state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
					// 接听会收到EXTRA_STATE_OFFHOOK
					pause();
				} else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
					// 挂断会收到EXTRA_STATE_IDLE
					startPlay();
				}
			}
		}
	}

}
