package com.baby.play.abc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baby.play.abc.config.CommConfigXML;

import java.util.ArrayList;

public class CustomeActivity extends Activity implements OnClickListener,OnFocusChangeListener{
   
	private ImageView cancelView;
	private ImageView enterView;
	public static Handler MsgHandler;
	public static ArrayList<Activity> activityList = new ArrayList<Activity>();

	private EditText radioname;
	private EditText radiourl;
	private Context context;
	
	private String ccuri = "da_uri";
	private String ccname = "da_name";
	private int type = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custome_dialog);
		cancelView = (ImageView) findViewById(R.id.main_cancel_image);
		enterView = (ImageView) findViewById(R.id.main_enter_image);

		radioname = (EditText) findViewById(R.id.radioname);
		radiourl = (EditText) findViewById(R.id.radiourl);
		radioname.requestFocus();
		radioname.setBackgroundResource(R.drawable.textbox_on);

		radioname.setOnClickListener((OnClickListener) this);
		radiourl.setOnClickListener((OnClickListener) this);
		radiourl.setOnFocusChangeListener((OnFocusChangeListener) this);
		radioname.setOnFocusChangeListener((OnFocusChangeListener) this);

		Intent intent = getIntent();
		
		if (intent != null && intent.hasExtra("type")) { 
			type = intent.getExtras().getInt("type");
		}
		
		if(type != 0){
			switch(type){
			case MainUi.a:
				ccuri = "da_uri";
				ccname = "da_name";
				break;			
			case MainUi.b:
				ccuri = "db_uri";
				ccname = "db_name";
				break;
			case MainUi.c:
				ccuri = "dc_uri";
				ccname = "dc_name";
				break;
			default:
				break;
			}
		}
		String c_uri = CommConfigXML.getInstance(this).getValueByName(ccuri,"");
		radiourl.setText(c_uri);
		String c_name = CommConfigXML.getInstance(this).getValueByName(ccname,"");
		radioname.setText(c_name);

		context = this.getApplicationContext();
		CustomeActivity.activityList.add(this);
		cancelView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		enterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 读取配置文件的一个定制电台
				if(radioname.getText().toString().length() > 0 && radiourl.getText().toString().length() > 0){
					String newname = radioname.getText().toString();
					String newuri = radiourl.getText().toString();
					
					CommConfigXML.getInstance(context).setValueByName(ccname, newname);
					CommConfigXML.getInstance(context).setValueByName(ccuri, newuri);
					
					if(type != 0){
						switch(type){
						case MainUi.a:
							IeltsConstant.D_A = newuri;
							IeltsConstant.D_AN = newname;
							break;			
						case MainUi.b:
							IeltsConstant.D_B = newuri;
							IeltsConstant.D_BN = newname;
							break;
						case MainUi.c:
							IeltsConstant.D_C = newuri;
							IeltsConstant.D_CN = newname;
							break;
						default:
							break;
						}
					}
					
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(CustomeActivity.this, "Input station info.", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }
	
	@Override
	public void onFocusChange(View v, boolean arg1) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radioname:
			radioname.setBackgroundResource(R.drawable.textbox_on);
			radiourl.setBackgroundResource(R.drawable.textbox_nor);
			break;
		case R.id.radiourl:
			radiourl.setBackgroundResource(R.drawable.textbox_on);
			radioname.setBackgroundResource(R.drawable.textbox_nor);
			break;
	
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.radioname:
			radioname.setBackgroundResource(R.drawable.textbox_on);
			radiourl.setBackgroundResource(R.drawable.textbox_nor);
			break;
		case R.id.radiourl:
			radiourl.setBackgroundResource(R.drawable.textbox_on);
			radioname.setBackgroundResource(R.drawable.textbox_nor);
			break;
	
		}
	}
}