package com.baby.play.abc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baby.play.abc.PlayService.MyBinder;
import com.baby.play.abc.config.CommConfigXML;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.Vitamio;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.WeatherInfo.ForecastInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.SEARCH_MODE;
import zh.wang.android.apis.yweathergetter4a.YahooWeather.UNIT;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherExceptionListener;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

public class MainUi extends Activity implements YahooWeatherInfoListener, YahooWeatherExceptionListener{
	public static final String tag = "MainUi";

	public final static int GRID_MAIN_PAGE_TEMP = 1000;

	public final static int country = 1001;
	public final static int classic = 1002;
	public final static int grandstand = 1003;

	public final static int sydney = 1004;
	public final static int melbourne = 1005;
	public final static int adelaide = 1006;

	public final static int canberra = 1007;
	public final static int brisbane = 1008;
	public final static int perth = 1009;

	public final static int a = 1010;
	public final static int b = 1011;
	public final static int c = 1012;

	public final static int abcjazz = 1013;
	public final static int classictwo = 1014;
	public final static int doublej = 1015;

	public final static int radionational = 1016;
	public final static int nationalnews = 1017;
	public final static int tripplej = 1018;

	public final static int itinerantone = 1019;
	public final static int itineranttwo = 1020;
	public final static int itinerantthree = 1021;

	public final static int unearthed = 1022;
	public final static int extra = 1023;

	public final static int request_code_2001 = 2001;
	public final static int request_code_2002 = 2002;

	public final static int play_stat_url = 1;
	public final static int play_stat_pause = 2;
	public final static int play_stat_play = 3;

	private OnItemClickListener gridMainItemClickListener;
	private OnItemLongClickListener gridMainItemLongClickListener;
	private GridView mainGridView;
	private MainAdapter gridAdapter;
	private List<MainVO> gridItemList = new ArrayList<MainVO>();
	// 
	private ImageView quitView;

	private static ArrayList<Handler> mUpdateHandler;
	public static Handler filelistHandler;
	private PlayService bindService = null;
	private boolean isBind = false;
	private Context context;

	private ImageView currentClickImgView;
	private ImageView firtinimg;

	private MyMediaPlayer mPlayer;

	private RelativeLayout initRelative;
	private RelativeLayout mainRelative;

	public final static int INIT_DO_OK = 1;
	public final static int INIT_DO_ING = 2;
	public final static int radio_service_buffering = 101;
	public final static int radio_service_playing = 102;
	public final static int radio_service_stop = 103;
	public final static int radio_service_rate = 104;
	public final static int radio_service_error = 105; // oper media error

	private Handler MainHandle;
	public static int globalid = 0;

	// weather forecast
	private YahooWeather mYahooWeather = YahooWeather.getInstance(15000, 15000, true);
	private LinearLayout mWeatherInfosLayout;


	static class InnerHandler extends Handler {
        WeakReference<MainUi> mActivity;

        public InnerHandler(MainUi activity) {
                mActivity = new WeakReference<MainUi>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
        	MainUi theActivity = mActivity.get();
                switch (msg.what) {
                case INIT_DO_OK:
                	theActivity.enterMain();
                	break;
                case radio_service_buffering:
    				Bundle data = msg.getData();
    				int tempid = data.getInt("id");
                	theActivity.startBufferingAnimit(tempid);
                	break;
                case radio_service_playing:
    				Bundle data2 = msg.getData();
    				int tempid2 = data2.getInt("id");
    				globalid = tempid2;
                	theActivity.startPlayingAnimit(tempid2);
                	break;
                case radio_service_stop:
    				Bundle data3 = msg.getData();
    				int tempid3 = data3.getInt("id");
                	theActivity.stopAnimit(tempid3);
                	theActivity.PreSetRateDisplay(0);
                	break;
                case radio_service_rate:
    				Bundle data4 = msg.getData();
    				int rate = data4.getInt("rate");
                	theActivity.PreSetRateDisplay(rate);
                	break;
                case radio_service_error:
                	theActivity.switchUrl();
                	break;
                default:
                	break;
                }
      	}
    };

    /**
     * Because abc radio url switch between radio1--radio2 and maybe url:port
     * This step just switch radio1-radio2
     * Sencond step may switch aac-mp3
     * Third step may switch url-url:port
     */
    private void switchUrl(){
    	if(IeltsConstant.DYNAMIC_PREFIX.equals(IeltsConstant.URL_PREFIX[0])){
			CommConfigXML.getInstance(context).setValueByName("da_prefix", IeltsConstant.URL_PREFIX[1]);
    		IeltsConstant.DYNAMIC_PREFIX = IeltsConstant.URL_PREFIX[1];
    	}else{
			CommConfigXML.getInstance(context).setValueByName("da_prefix", IeltsConstant.URL_PREFIX[0]);
    		IeltsConstant.DYNAMIC_PREFIX = IeltsConstant.URL_PREFIX[0];
    	}
    	Toast.makeText(getApplicationContext(), IeltsConstant.URL_SWITCH, Toast.LENGTH_SHORT).show();
    }

	private void prepareMain()
	{
        //--------------------------------------------------------
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				MainHandle.sendEmptyMessage(INIT_DO_OK);
			}
		};
		timer.schedule(task, 3500);

		// vitamio
		if (!Vitamio.isInitialized(getApplicationContext())){
			return;
		}
		Intent intent = new Intent(this, PlayService.class);
		isBind = getApplicationContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);

        //--------------------------------------------------------
	}
	private void enterMain(){
		initRelative.setVisibility(View.GONE);
		mainRelative.setVisibility(View.VISIBLE);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //--------------------------------------------------------
        setContentView(R.layout.ieltsls_ui);
        ConfirmActivity.activityList.add(this);
        context = getApplicationContext();
		MainHandle = new InnerHandler(this);
		PlayService.MainUiUseHandle = MainHandle;

		mYahooWeather.setExceptionListener(this);
        mWeatherInfosLayout = (LinearLayout) findViewById(R.id.weather_infos);

		progressBarW = (ProgressBar) findViewById(R.id.progressBarW);
		lineparent = (LinearLayout) findViewById(R.id.weather_infos);
		locationName = (TextView) findViewById(R.id.locationname);
        //--------------------------------------------------------
    	// 当前网络状态判
		mUpdateHandler = new ArrayList<Handler>();
		if (!NetworkUtils.DetectNetwork(getApplicationContext())) {
			// 当前网络不可用，按钮应该不可点击
        	Toast.makeText(getApplicationContext(), IeltsConstant.NET_HINT, Toast.LENGTH_SHORT).show();
		}

		// 读取配置文件的一个定制电
		String da_prefix = CommConfigXML.getInstance(context).getValueByName("da_prefix","");
		if(da_prefix == null || da_prefix.length() == 0 ||da_prefix == null || da_prefix.length() == 0){
			CommConfigXML.getInstance(context).setValueByName("da_prefix", IeltsConstant.URL_PREFIX[0]);
		}else{
			IeltsConstant.DYNAMIC_PREFIX = da_prefix;
		}

		String da_uri = CommConfigXML.getInstance(context).getValueByName("da_uri","");
		String da_name = CommConfigXML.getInstance(context).getValueByName("da_name","");
		if(da_uri == null || da_uri.length() == 0 ||da_name == null || da_name.length() == 0){
			CommConfigXML.getInstance(context).setValueByName("da_uri", IeltsConstant.getD_A());
			CommConfigXML.getInstance(context).setValueByName("da_name", IeltsConstant.D_AN);
			IeltsConstant.D_A = IeltsConstant.getD_A();
		}else{
			IeltsConstant.D_A = da_uri;
			IeltsConstant.D_AN = da_name;
		}

		String db_uri = CommConfigXML.getInstance(context).getValueByName("db_uri","");
		String db_name = CommConfigXML.getInstance(context).getValueByName("db_name","");
		if(db_uri == null || db_uri.length() == 0 ||db_name == null || db_name.length() == 0){
			CommConfigXML.getInstance(context).setValueByName("db_uri", IeltsConstant.getD_B());
			CommConfigXML.getInstance(context).setValueByName("db_name", IeltsConstant.D_BN);
			IeltsConstant.D_B = IeltsConstant.getD_B();
		}else{
			IeltsConstant.D_B = db_uri;
			IeltsConstant.D_BN = db_name;
		}

		String dc_uri = CommConfigXML.getInstance(context).getValueByName("dc_uri","");
		String dc_name = CommConfigXML.getInstance(context).getValueByName("dc_name","");
		if(dc_uri == null || dc_uri.length() == 0 ||dc_name == null || dc_name.length() == 0){
			CommConfigXML.getInstance(context).setValueByName("dc_uri", IeltsConstant.getD_C());
			CommConfigXML.getInstance(context).setValueByName("dc_name", IeltsConstant.D_CN);
			IeltsConstant.D_C = IeltsConstant.getD_C();
		}else{
			IeltsConstant.D_C = dc_uri;
			IeltsConstant.D_CN = dc_name;
		}

		mainRelative = (RelativeLayout)findViewById(R.id.mainPage);
		mainRelative.setVisibility(View.GONE);
		initRelative = (RelativeLayout)findViewById(R.id.initPage);
		initRelative.setVisibility(View.VISIBLE);
		startMainAnimit();
		// 显示启动界面3.5
		prepareMain();

        mainGridView = (GridView)findViewById(R.id.gridMainPageGridView);
    	mainGridView.setLongClickable(true);

    	quitView = (ImageView) findViewById(R.id.quit);
    	quitView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showConfirm();
			}
		});

        //--------------------------------------------------------
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);


        //--------------------------------------------------------
    	initGridItemClickListener();
    	initFixedGridListFirstPart();
    	refreshPageView();
    	mainGridView.setOnItemClickListener(gridMainItemClickListener);
    	mainGridView.setOnItemLongClickListener(gridMainItemLongClickListener);
    }

	private void refreshPageView(){
        gridAdapter = new MainAdapter(MainUi.this,gridItemList);
        mainGridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
	}

    @Override
    public void onResume() {
    	super.onResume();
    }

    // 缓冲动画播放
	private void startBufferingAnimit(int id){
    	if(gridItemList != null && gridItemList.size() > 0){
    		for(MainVO uvo: gridItemList){
    			if(uvo.getContentId() == id){
    				uvo.setImageresourceId(R.drawable.loading);
    			}
    		}
    	}
		currentClickImgView.setImageResource(R.drawable.loading);

        Object ob = null;
		AnimationDrawable anim = null;
		ob = currentClickImgView.getDrawable();
		if(ob != null){
			anim = (AnimationDrawable) ob;
			anim.stop();
			anim.start();
		}
	}

	// 播放中动
	private void startPlayingAnimit(int id){
    	if(gridItemList != null && gridItemList.size() > 0){
    		for(MainVO uvo: gridItemList){
    			if(uvo.getContentId() == id){
    				uvo.setImageresourceId(R.drawable.playing);
    			}
    		}
    	}
		currentClickImgView.setImageResource(R.drawable.playing);

        Object ob = null;
		AnimationDrawable anim = null;
		ob = currentClickImgView.getDrawable();
		if(ob != null){
			anim = (AnimationDrawable) ob;
			anim.stop();
			anim.start();
		}
		searchByPlaceName(currentLocation);
	}

	// 播放中动
	private void startMainAnimit(){
		firtinimg = (ImageView)findViewById(R.id.firstinimg);
		firtinimg.setImageResource(R.drawable.openning);

        Object ob = null;
		AnimationDrawable anim = null;
		ob = firtinimg.getDrawable();
		if(ob != null){
			anim = (AnimationDrawable) ob;
			anim.stop();
			anim.start();
		}
	}
	// 清除动画播放，全页面复位
	private void stopAnimit(int id){
    	if(gridItemList != null && gridItemList.size() > 0){
    		for(MainVO uvo: gridItemList){
    			if(uvo.getContentId() == id){
    				uvo.setImageresourceId(0);
    			}
    		}
    	}
		if(currentClickImgView != null)currentClickImgView.setImageResource(0);
	}
	private TextView ratev;
	private void PreSetRateDisplay(int rate){
		if(ratev == null){
			ratev = (TextView) findViewById(R.id.radioRate);
		}
		if (bindService.isPlaying()) {
			if(rate < 0){
				ratev.setText("");
			}else{
				if(rate == 0){
					ratev.setText("Buffering...");
				}else{
					ratev.setText("stream speed:" + rate + " k/s");
				}
			}
		}else{
			ratev.setText("");
		}
	}

    private void initFixedGridListFirstPart(){
		//-------------------------------------------------------------------
    	gridItemList = new ArrayList<MainVO>();

		MainVO mvo = new MainVO();
		mvo.setContentId(country);
		mvo.setName("CountryAU");
		mvo.setWeatherName("Canberra");
		mvo.setBackgroundSourceId(R.xml.country_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(classic);
		mvo.setName("Classic");
		mvo.setWeatherName("Canberra");
		mvo.setBackgroundSourceId(R.xml.classic_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(grandstand);
		mvo.setName("Grandstand");
		mvo.setWeatherName("Canberra");
		mvo.setBackgroundSourceId(R.xml.grandstand_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		//-------------------------------------------------------------------
		mvo = new MainVO();
		mvo.setContentId(sydney);
		mvo.setName("Sydney");
		mvo.setWeatherName("Sydney");
		mvo.setBackgroundSourceId(R.xml.sydney_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(melbourne);
		mvo.setName("Melbourne");
		mvo.setWeatherName("Melbourne");
		mvo.setBackgroundSourceId(R.xml.melbourne_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(adelaide);
		mvo.setName("Adelaide");
		mvo.setWeatherName("Adelaide");
		mvo.setBackgroundSourceId(R.xml.adelaide_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		//-------------------------------------------------------------------
		mvo = new MainVO();
		mvo.setContentId(canberra);
		mvo.setName("Canberra");
		mvo.setWeatherName("Canberra");
		mvo.setBackgroundSourceId(R.xml.canberra_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(brisbane);
		mvo.setName("Brisbane");
		mvo.setWeatherName("Brisbane");
		mvo.setBackgroundSourceId(R.xml.brisbane_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(perth);
		mvo.setName("Perth");
		mvo.setWeatherName("Perth");
		mvo.setBackgroundSourceId(R.xml.perth_selector);
		mvo.setMessageType(0);
		gridItemList.add(mvo);

		//-------------------------------------------------------------------
		// 可定制电
		mvo = new MainVO();
		mvo.setContentId(a);
		mvo.setBackgroundSourceId(R.xml.a_selector);
		mvo.setMessageType(0);
		mvo.setName(IeltsConstant.D_AN);
		mvo.setWeatherName(IeltsConstant.D_AN);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(b);
		mvo.setBackgroundSourceId(R.xml.b_selector);
		mvo.setMessageType(0);
		mvo.setName(IeltsConstant.D_BN);
		mvo.setWeatherName(IeltsConstant.D_BN);
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(c);
		mvo.setBackgroundSourceId(R.xml.c_selector);
		mvo.setMessageType(0);
		mvo.setWeatherName(IeltsConstant.D_CN);
		mvo.setName(IeltsConstant.D_CN);
		gridItemList.add(mvo);
		//------------------------------------------------------------------
		// E
		mvo = new MainVO();
		mvo.setContentId(abcjazz);
		mvo.setBackgroundSourceId(R.xml.abcjazz_selector);
		mvo.setMessageType(0);
		mvo.setName("ABC JAZZ");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(classictwo);
		mvo.setBackgroundSourceId(R.xml.classictwo_selector);
		mvo.setMessageType(0);
		mvo.setName("Classic 2");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(doublej);
		mvo.setBackgroundSourceId(R.xml.doublej_selector);
		mvo.setMessageType(0);
		mvo.setWeatherName("Canberra");
		mvo.setName("Double J");
		gridItemList.add(mvo);

		// F
		mvo = new MainVO();
		mvo.setContentId(radionational);
		mvo.setBackgroundSourceId(R.xml.radionational_selector);
		mvo.setMessageType(0);
		mvo.setName("National");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(nationalnews);
		mvo.setBackgroundSourceId(R.xml.nationalnews_selector);
		mvo.setMessageType(0);
		mvo.setName("News");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(tripplej);
		mvo.setBackgroundSourceId(R.xml.tripplej_selector);
		mvo.setMessageType(0);
		mvo.setWeatherName("Canberra");
		mvo.setName("Tripple J");
		gridItemList.add(mvo);
		// G
		mvo = new MainVO();
		mvo.setContentId(itinerantone);
		mvo.setBackgroundSourceId(R.xml.itinerantone_selector);
		mvo.setMessageType(0);
		mvo.setName("Itinerant 1");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(itineranttwo);
		mvo.setBackgroundSourceId(R.xml.itineranttwo_selector);
		mvo.setMessageType(0);
		mvo.setName("Itinerant 2");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(itinerantthree);
		mvo.setBackgroundSourceId(R.xml.itinerantthree_selector);
		mvo.setMessageType(0);
		mvo.setWeatherName("Canberra");
		mvo.setName("Itinerant 3");
		gridItemList.add(mvo);
		// H
		mvo = new MainVO();
		mvo.setContentId(unearthed);
		mvo.setBackgroundSourceId(R.xml.unearthed_selector);
		mvo.setMessageType(0);
		mvo.setName("Unearthed");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		mvo = new MainVO();
		mvo.setContentId(extra);
		mvo.setBackgroundSourceId(R.xml.extra_selector);
		mvo.setMessageType(0);
		mvo.setName("Extra");
		mvo.setWeatherName("Canberra");
		gridItemList.add(mvo);

		//------------------------------------------------------------------
    }
    private int currentRadioId = country;
    private void initGridItemClickListener(){
    	// 设置长按可定制一个电
    	gridMainItemLongClickListener = new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
    			int itemId = gridItemList.get(position).getContentId();
    			switch(itemId)
    			{
    			case MainUi.a:
    		    	Intent alertIntent1 = new Intent();
    		    	alertIntent1.putExtra("type", MainUi.a);
    		        alertIntent1.setClass(MainUi.this, CustomeActivity.class);
    		        startActivity(alertIntent1);
    				break;
    			case MainUi.b:
    		    	Intent alertIntent2 = new Intent();
    		    	alertIntent2.putExtra("type", MainUi.b);
    		        alertIntent2.setClass(MainUi.this, CustomeActivity.class);
    		        startActivity(alertIntent2);
    				break;
    			case MainUi.c:
    		    	Intent alertIntent3 = new Intent();
    		    	alertIntent3.putExtra("type", MainUi.c);
    		        alertIntent3.setClass(MainUi.this, CustomeActivity.class);
    		        startActivityForResult(alertIntent3, request_code_2002);
    				break;

    			default:
    				break;
    			}
				return true;
			}
    	};

    	gridMainItemClickListener = new OnItemClickListener(){
    		public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
    			int itemId = gridItemList.get(position).getContentId();

    			String locationName = gridItemList.get(position).getWeatherName();
    			startSearchByPlaceName(locationName);

    			// 如果原来已有动画，则清除
    			if(currentClickImgView != null)currentClickImgView.setImageResource(0);
    			// 找到当前View里面的子item
    			currentClickImgView = (ImageView)view.findViewById(R.id.gridViewMainBackground);
    			switch(itemId)
    			{
    			case MainUi.country:
    				startPlay(IeltsConstant.getA_COUNTRY(), view, itemId);
    				currentRadioId = country;
    				break;
    			case MainUi.classic:
    				startPlay(IeltsConstant.getA_CLASSIC(), view, itemId);
    				currentRadioId = classic;
    				break;
    			case MainUi.grandstand:
    				startPlay(IeltsConstant.getA_GRANDSTAND(), view, itemId);
    				currentRadioId = grandstand;
    				break;

    			case MainUi.sydney:
    				startPlay(IeltsConstant.getB_SYDNEY(), view, itemId);
    				currentRadioId = sydney;
    				break;
    			case MainUi.melbourne:
    				startPlay(IeltsConstant.getB_MELBOURNE(), view, itemId);
    				currentRadioId = melbourne;
    				break;
    			case MainUi.adelaide:
    				startPlay(IeltsConstant.getB_ADELAIDE(), view, itemId);
    				currentRadioId = adelaide;
    				break;

    			case MainUi.canberra:
    				startPlay(IeltsConstant.getC_CANBERRA(), view, itemId);
    				currentRadioId = canberra;
    				break;
    			case MainUi.brisbane:
    				startPlay(IeltsConstant.getC_BRISBANE(), view, itemId);
    				currentRadioId = brisbane;
    				break;
    			case MainUi.perth:
    				startPlay(IeltsConstant.getC_PERTH(), view, itemId);
    				currentRadioId = perth;
    				break;

    			case MainUi.a:
    				startPlay(IeltsConstant.D_A, view, itemId);
    				currentRadioId = a;
    				break;
    			case MainUi.b:
    				startPlay(IeltsConstant.D_B, view, itemId);
    				currentRadioId = b;
    				break;
    			case MainUi.c:
    				startPlay(IeltsConstant.D_C, view, itemId);
    				currentRadioId = c;
    				break;

                    case MainUi.abcjazz:
                        startPlay(IeltsConstant.getE_ABCJAZZ(), view, itemId);
                        currentRadioId = abcjazz;
                        break;
                    case MainUi.classictwo:
                        startPlay(IeltsConstant.getE_CLASSICTWO(), view, itemId);
                        currentRadioId = classictwo;
                        break;
                    case MainUi.doublej:
                        startPlay(IeltsConstant.getE_DOUBLEJ(), view, itemId);
                        currentRadioId = doublej;
                        break;

                    case MainUi.radionational:
                        startPlay(IeltsConstant.getF_2RNW(), view, itemId);
                        currentRadioId = radionational;
                        break;
                    case MainUi.nationalnews:
                        startPlay(IeltsConstant.getF_PBW(), view, itemId);
                        currentRadioId = nationalnews;
                        break;
                    case MainUi.tripplej:
                        startPlay(IeltsConstant.getF_2TJW(), view, itemId);
                        currentRadioId = tripplej;
                        break;

                    case MainUi.itinerantone:
                        startPlay(IeltsConstant.getH_IT1W(), view, itemId);
                        currentRadioId = itinerantone;
                        break;
                    case MainUi.itineranttwo:
                        startPlay(IeltsConstant.getH_IT2W(), view, itemId);
                        currentRadioId = itineranttwo;
                        break;
                    case MainUi.itinerantthree:
                        startPlay(IeltsConstant.getH_IT3W(), view, itemId);
                        currentRadioId = itinerantthree;
                        break;

                    case MainUi.unearthed:
                        startPlay(IeltsConstant.getG_UNEARTHED(), view, itemId);
                        currentRadioId = unearthed;
                        break;
                    case MainUi.extra:
                        startPlay(IeltsConstant.getG_XTDW(), view, itemId);
                        currentRadioId = extra;
                        break;
    			default:
    				break;
    			}
			}
    	};
    }

    /**
     * 停止当前的播
     */
    private void justStopRadio(){
		if (bindService.isPlaying()) {
			// 分发数据
			for (int i = 0; i < mUpdateHandler.size(); i++) {
				Handler h = mUpdateHandler.get(i);
				Message msg = h.obtainMessage();
				msg.what = play_stat_pause;
				h.sendMessage(msg);
			}
		}
    }
    /**
     * 播放或停止一个URL
     * @param url
     */
    private void startPlay(String url, View view, int gridID){
		if (!NetworkUtils.DetectNetwork(getApplicationContext())) {
			// 当前网络不可用，按钮应该不可点击
        	Toast.makeText(getApplicationContext(), IeltsConstant.NET_HINT, Toast.LENGTH_SHORT).show();
        	return;
		}

		if (bindService.isPlaying()) {
			// 分发数据
			for (int i = 0; i < mUpdateHandler.size(); i++) {
				Handler h = mUpdateHandler.get(i);
				Message msg = h.obtainMessage();
				Bundle data2 = new Bundle();
				data2.putInt("id", gridID);
				msg.setData(data2);
				msg.what = play_stat_pause;
				h.sendMessage(msg);
			}

		} else {
			for (int i = 0; i < mUpdateHandler.size(); i++) {
				Handler h = mUpdateHandler.get(i);
				Message msg = h.obtainMessage();

				msg.what = play_stat_url;
				Bundle data = new Bundle();
				data.putString("url", url);
				data.putInt("id", gridID);
				msg.setData(data);

				h.sendMessage(msg);
			}
			// 分发数据
			for (int i = 0; i < mUpdateHandler.size(); i++) {
				Handler h = mUpdateHandler.get(i);
				Message msg = h.obtainMessage();
				msg.what = play_stat_play;
				Bundle data = new Bundle();
				data.putInt("id", gridID);
				msg.setData(data);
				h.sendMessage(msg);
			}
		}
    }

	OnCompletionListener onCompletionListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
        }
	};

    public void showConfirm(){
    	Intent alertIntent = new Intent();
        alertIntent.setClass(MainUi.this, ConfirmActivity.class);
        startActivity(alertIntent);
    }
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showConfirm();
		}
		return false;
	}

	/**
	 * 只有电话来了之后才暂停音乐的播放
	 */
	private final class MyPhoneListener extends android.telephony.PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING://电话来了
				// 放音停止
				if(mPlayer != null) mPlayer.callIsComing();
				// 广播停止
				justStopRadio();
				break;
			case TelephonyManager.CALL_STATE_IDLE: //通话结束
				if(mPlayer != null) mPlayer.callIsDown();
				break;
			}
		}
	}

	public static void addUpdateHandler(Handler handler) {
		mUpdateHandler.add(handler);
	}

	public static void removeUpdateHandler(Handler handler) {
		mUpdateHandler.remove(handler);
	}


	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.e(tag, "Dis conn currentRadioId:" + currentRadioId);
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.e(tag, "Connected currentRadioId:" + currentRadioId);
			MyBinder binder = (MyBinder) service;
			bindService = binder.getService();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (isBind) {
			getApplicationContext().unbindService(conn);
			isBind = false;
		}
		super.onDestroy();
	}


    @Override
    public void onFailConnection(final Exception e) {
        // TODO Auto-generated method stub
        setNoResultLayout();
        Toast.makeText(getApplicationContext(), "Fail Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailParsing(final Exception e) {
        // TODO Auto-generated method stub
        setNoResultLayout();
        Toast.makeText(getApplicationContext(), "Fail Parsing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailFindLocation(final Exception e) {
        // TODO Auto-generated method stub
        setNoResultLayout();
        Toast.makeText(getApplicationContext(), "Fail Find Location", Toast.LENGTH_SHORT).show();
    }
	private void setNoResultLayout() {
	}

	private TextView locationName;
	private LinearLayout lineparent;
	private String currentLocation;
	private ProgressBar progressBarW;
	private void startSearchByPlaceName(String location) {

		currentLocation = location;

	}

	private void searchByPlaceName(String location) {
		mWeatherInfosLayout.removeAllViews();
		// lineparent.setVisibility(View.INVISIBLE);
		progressBarW.setVisibility(View.VISIBLE);
		mWeatherInfosLayout.addView(progressBarW);

		locationName.setText("Weather in "+location);
		locationName.setBackgroundResource(R.color.deep_bule_color);
		lineparent.setBackgroundResource(R.drawable.common_title);

		try{
			mYahooWeather.setNeedDownloadIcons(true);
			mYahooWeather.setUnit(UNIT.CELSIUS);
			mYahooWeather.setSearchMode(SEARCH_MODE.PLACE_NAME);
			mYahooWeather.queryYahooWeatherByPlaceName(getApplicationContext(), location, MainUi.this);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
        if (weatherInfo != null) {
    		progressBarW.setVisibility(View.INVISIBLE);
    		//lineparent.setVisibility(View.VISIBLE);

        	mWeatherInfosLayout.removeAllViews();
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        	layout.gravity= Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        	layout.weight = 1;

        	final LinearLayout currentInfoLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.forecast_info, null, false);
        	currentInfoLayout.setLayoutParams(layout);

        	final ImageView imagec = (ImageView) currentInfoLayout.findViewById(R.id.weather_image);
        	final TextView descc = (TextView) currentInfoLayout.findViewById(R.id.weather_desc);
        	final TextView tempc = (TextView) currentInfoLayout.findViewById(R.id.weather_temp);
        	final TextView weekc = (TextView) currentInfoLayout.findViewById(R.id.weather_week);

        	descc.setText(weatherInfo.getCurrentText());
        	tempc.setText(weatherInfo.getCurrentTemp() +"ºC");
        	weekc.setText("Current");
			if (weatherInfo.getCurrentConditionIcon() != null) {
				imagec.setImageBitmap(weatherInfo.getCurrentConditionIcon());
			}
			mWeatherInfosLayout.addView(currentInfoLayout);

//        	mTvWeather0.setText("====== CURRENT ======" + "\n" +
//					           "date: " + weatherInfo.getCurrentConditionDate() + "\n" +
//							   "weather: " + weatherInfo.getCurrentText() + "\n" +
//						       "temperature in ºC: " + weatherInfo.getCurrentTemp() + "\n" +
//						       "wind chill: " + weatherInfo.getWindChill() + "\n" +
//					           "wind direction: " + weatherInfo.getWindDirection() + "\n" +
//						       "wind speed: " + weatherInfo.getWindSpeed() + "\n" +
//					           "Humidity: " + weatherInfo.getAtmosphereHumidity() + "\n" +
//						       "Pressure: " + weatherInfo.getAtmospherePressure() + "\n" +
//					           "Visibility: " + weatherInfo.getAtmosphereVisibility()
//					           );

			for (int i = 0; i < 4; i++) { //i < YahooWeather.FORECAST_INFO_MAX_SIZE
				final LinearLayout forecastInfoLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.forecast_info, null, false);
				forecastInfoLayout.setLayoutParams(layout);

	        	final ImageView imagecF = (ImageView) forecastInfoLayout.findViewById(R.id.weather_image);
	        	final TextView desccF = (TextView) forecastInfoLayout.findViewById(R.id.weather_desc);
	        	final TextView tempcF = (TextView) forecastInfoLayout.findViewById(R.id.weather_temp);
	        	final TextView weekcF = (TextView) forecastInfoLayout.findViewById(R.id.weather_week);

				final ForecastInfo forecastInfo = weatherInfo.getForecastInfoList().get(i);

				desccF.setText(forecastInfo.getForecastText());
	        	tempcF.setText(forecastInfo.getForecastTempLow() +"/" + forecastInfo.getForecastTempHigh() + "ºC");
	        	if(i==0){
					weekcF.setText("Today");
				}else{
					weekcF.setText(forecastInfo.getForecastDay());
				}
				if (forecastInfo.getForecastConditionIcon() != null) {
					imagecF.setImageBitmap(forecastInfo.getForecastConditionIcon());
				}
				mWeatherInfosLayout.addView(forecastInfoLayout);
			}
        } else {
        	setNoResultLayout();
        }
	}
}
