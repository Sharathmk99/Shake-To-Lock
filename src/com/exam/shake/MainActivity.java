package com.exam.shake;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.widgets.ColorSelector;
import com.gc.materialdesign.widgets.ColorSelector.OnColorSelectedListener;
import com.gc.materialdesign.widgets.Dialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.revmob.RevMob;
import com.revmob.ads.fullscreen.RevMobFullscreen;
import com.shashi.appanalyticslibrary.tracking.PushUserData;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, OnColorSelectedListener, OnClickListener {

	long lock_time = 0;
	int first_time = 1;
	LinearLayout rlt;
	Sensor mAccelerometer;
	static final int ACTIVATION_REQUEST = 47;
	ComponentName demoDeviceAdmin;
	DevicePolicyManager mDPM;
	ListView lt;
	String[] mainMenu = { "Activate", "Auto Start", "Activate Administator" };
	String[] Aray = { "Sensitivity", "Uninstall", "Uninstall Tips", "Ads",
			"More Apps" };
	String[] Hint = { "On for lock your screen", "Auto start when rebooted",
			"Activate or deactivate the administator permision",
			"Set the sensitivity according to you", "Uninstall this app..:(",
			"Tips to Uninstall", "Greats Apps", "More from Developer" };
	TextView heading;
	// ImageView im;
	private SharedPreferences shared;
	private String Filename = "Shake";
	private String Filename1 = "Seek";
	private SharedPreferences shared1;
	ButtonFloatSmall buttonSelectColor;
	int backgroundColor = Color.parseColor("#1E88E5");
	RelativeLayout relativeLayout;
	Switch switchAct, switchAuto, switchAdmi;
	Slider slider;
	LayoutRipple layoutRippleAct, layoutRippleAuto, layoutRippleAdmi;
	private RevMob revmob;
	private RevMobFullscreen fullscreen;
	Tracker t;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PushUserData.getInstance(this).startTrack();
		setupHeader();
		revmob = RevMob.start(this);
		revmob.showFullscreen(this);
		t = getTracker();
		t.setScreenName("MainScreen");
		fullscreen = revmob.createFullscreen(this, null);
		switchAct = (Switch) findViewById(R.id.switchViewActi);
		switchAuto = (Switch) findViewById(R.id.switchViewAuto);
		switchAdmi = (Switch) findViewById(R.id.switchViewAdmin);
		layoutRippleAct = (LayoutRipple) findViewById(R.id.activateButtons);
		layoutRippleAct.setOnClickListener(this);
		layoutRippleAuto = (LayoutRipple) findViewById(R.id.autoButtons);
		layoutRippleAuto.setOnClickListener(this);
		layoutRippleAdmi = (LayoutRipple) findViewById(R.id.adminButtons);
		layoutRippleAdmi.setOnClickListener(this);
		askAdministatorPermision();
		lt = (ListView) findViewById(R.id.listView1);
		lt.setOnItemClickListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Aray);
		lt.setAdapter(adapter);
		int temp1 = GetStoreShared2("SeekBar");
		if (temp1 == -1) {
			StoreShared2("SeekBar", 12);
		}
		shared = getSharedPreferences(Filename, 0);
		String temp = shared.getString(Aray[0], "error");
		if (!shared.getString(Aray[2], "error").equals("error")) {
			if (shared.getString(Aray[2], "error").equals("true")) {
				startService(new Intent(this, MyService.class));
			}
		}
		if (temp.equals("error")) {
			for (int j = 0; j < 2; j++) {
				StoreShared(Aray[j], "true");
			}
		}
		if (!GetStoreShared(mainMenu[0]).equals("error")) {
			if (GetStoreShared(mainMenu[0]).equals("true")) {
				switchAct.setChecked(true);
			} else {
				switchAct.setChecked(false);
			}
		}
		if (!GetStoreShared(mainMenu[1]).equals("error")) {
			if (GetStoreShared(mainMenu[1]).equals("true")) {
				switchAuto.setChecked(true);
			} else {
				switchAuto.setChecked(false);
			}
		}
		if (!GetStoreShared(mainMenu[2]).equals("error")) {
			if (GetStoreShared(mainMenu[2]).equals("true")) {
				switchAdmi.setChecked(true);
			} else {
				switchAdmi.setChecked(false);
			}
		}
		AdView mAdView = (AdView) findViewById(R.id.ad);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	public void askAdministatorPermision() {
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		demoDeviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"To Work This Application Correctly Activate This");
		startActivityForResult(intent, ACTIVATION_REQUEST);
	}

	private void setupHeader() {
		relativeLayout = (RelativeLayout) findViewById(R.id.background);
		buttonSelectColor = (ButtonFloatSmall) findViewById(R.id.buttonColorSelector);
		buttonSelectColor.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ColorSelector colorSelector = new ColorSelector(
						MainActivity.this, backgroundColor, MainActivity.this);
				colorSelector.show();
			}
		});
	}

	private void StoreShared2(String Keyword, int value) {
		shared1 = this.getSharedPreferences(Filename1, 0);
		SharedPreferences.Editor editor = shared1.edit();
		editor.putInt(Keyword, value);
		editor.commit();
	}

	private int GetStoreShared2(String Keyword) {
		shared1 = getSharedPreferences(Filename1, 0);
		return shared1.getInt(Keyword, -1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode + "  " + resultCode);
		if (resultCode == RESULT_OK) {
			startService(new Intent(this, MyService.class));
			StoreShared(Aray[2], "true");
			switchAdmi.setChecked(true);
		} else if (resultCode == RESULT_CANCELED) {
			StoreShared(Aray[2], "false");
			switchAdmi.setChecked(false);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(9, "Shake To Lock", "Press Accept to Exit", true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showDialog(int id, String title, String message,
			final boolean exit) {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(MainActivity.this, title, message);
		dialog.setOnAcceptButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fullscreen.show();
				if (exit) {
					PushUserData.getInstance(MainActivity.this).stopTrack();
					MainActivity.this.finish();
				}

			}
		});
		dialog.setOnCancelButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fullscreen.show();
			}
		});
		dialog.show();
	}

	private void StoreShared(String Keyword, String value) {
		shared = this.getSharedPreferences(Filename, 0);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(Keyword, value);
		editor.commit();
	}

	private String GetStoreShared(String Keyword) {
		shared = getSharedPreferences(Filename, 0);
		return shared.getString(Keyword, "error");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 == 0) {
			t.setScreenName("Sensitivity");
			Intent i = new Intent(this, Seekbardailog.class);
			startActivity(i);
		} else if (arg2 == 1) {
			t.setScreenName("UninstallButton");
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:com.exam.shake"));
			startActivity(intent);
		} else if (arg2 == 2) {

			showDialog(
					99,
					"Uninstall Tips",
					"If the Administrator is activate click on administrtor to deactivate and the click on unistall butoon.\n Thanks for using this wonderfull application",
					false);
		} else if (arg2 == 3) {
			revmob.showFullscreen(this);
			revmob.showFullscreen(this);
		} else if (arg2 == 4) {
			t.setScreenName("MoreApps");
			startActivity(new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/developer?id=Shashi+Developer%27s")));
		}
	}

	public void onColorSelected(int color) {
		// TODO Auto-generated method stub
		backgroundColor = color;
		buttonSelectColor.setBackgroundColor(color);
		relativeLayout.setBackgroundColor(color);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.activateButtons) {
			t.setScreenName("ActivateButton");
			if (GetStoreShared(mainMenu[0]).equals("true")) {
				StoreShared(mainMenu[0], "false");
				switchAct.setChecked(false);
			} else {
				StoreShared(mainMenu[0], "true");
				switchAct.setChecked(true);
			}
		} else if (arg0.getId() == R.id.autoButtons) {
			t.setScreenName("AutoStartButton");
			if (GetStoreShared(mainMenu[1]).equals("true")) {
				StoreShared(mainMenu[1], "false");
				switchAuto.setChecked(false);
			} else {
				StoreShared(mainMenu[1], "true");
				switchAuto.setChecked(true);
			}
		} else if (arg0.getId() == R.id.adminButtons) {
			t.setScreenName("AdminPermision");
			if (GetStoreShared(mainMenu[2]).equals("true")) {
				DevicePolicyManager dpm = (DevicePolicyManager) this
						.getSystemService(Context.DEVICE_POLICY_SERVICE);
				dpm.removeActiveAdmin(demoDeviceAdmin);
				StoreShared(mainMenu[2], "false");
				switchAdmi.setChecked(false);
			} else {
				askAdministatorPermision();
				StoreShared(mainMenu[2], "true");
				switchAdmi.setChecked(true);
			}
		}
	}

	synchronized Tracker getTracker() {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		Tracker t = analytics.newTracker(R.xml.analytics_config);
		return t;
	}
}
