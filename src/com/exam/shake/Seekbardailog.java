package com.exam.shake;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Slider.OnValueChangedListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Seekbardailog extends ActionBarActivity implements OnValueChangedListener {

	Slider slider;
	private String Filename = "Seek";
	private SharedPreferences shared1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seekbardailog);
		slider = (Slider) findViewById(R.id.sliderNumber);
		slider.setOnValueChangedListener(this);
		slider.setValue(GetStoreShared("SeekBar"));
		AdView mAdView = (AdView) findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    mAdView.loadAd(adRequest);
	    AdView mAdView2 = (AdView) findViewById(R.id.adView1);
	    AdRequest adRequest2 = new AdRequest.Builder().build();
	    mAdView2.loadAd(adRequest2);
		//System.out.println(GetStoreShared("SeekBar"));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	private void StoreShared(String Keyword, int value) {
		shared1 = this.getSharedPreferences(Filename, 0);
		SharedPreferences.Editor editor = shared1.edit();
		editor.putInt(Keyword, value);
		editor.commit();
	}

	private int GetStoreShared(String Keyword) {
		shared1 = getSharedPreferences(Filename, 0);
		return shared1.getInt(Keyword, -1);
	}

	public void onValueChanged(int value) {
		// TODO Auto-generated method stub
		StoreShared("SeekBar", value);
	}

}
