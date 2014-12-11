package com.exam.shake;



import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener{

	float mLastX, mLastY, mLastZ;
	 boolean mInitialized;
	 long lock_time=0;
	 SensorManager mSensorManager;
	 int first_time=1;
	 Sensor mAccelerometer;
	 private float NOISE = (float) 12.0;
	private SharedPreferences shared1;
	private String Filename="Seek";
	private SharedPreferences shared;
	private String Filename1 = "Shake";
	ComponentName demoDeviceAdmin;
	DevicePolicyManager mDPM;
	Context c;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mSensorManager.unregisterListener(this,mAccelerometer);
		startService(new Intent(this, MyService.class));
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		mInitialized = false;
        c = this;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this,DemoDeviceAdminReceiver.class);
        
	}

	private int GetStoreShared(String Keyword){
		shared1= getSharedPreferences(Filename , 0);
		return shared1.getInt(Keyword,-1);
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		long time_t = System.currentTimeMillis();
		NOISE = GetStoreShared("SeekBar")/1.0f;
		//System.out.println(NOISE);
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		
		mInitialized = true;
		} else {
			
		float deltaX = Math.abs(mLastX - x);
		float deltaY = Math.abs(mLastY - y);
		float deltaZ = Math.abs(mLastZ - z);
		if (deltaX < NOISE) deltaX = (float)0.0;
		if (deltaY < NOISE) deltaY = (float)0.0;
		if (deltaZ < NOISE) deltaZ = (float)0.0;
		mLastX = x;
		mLastY = y;
		mLastZ = z;
		if(time_t >= lock_time || first_time == 1){
			
			if (deltaX > deltaY) {
				lock_time = time_t+2000;
				first_time=0;
				if(GetStoreShared1("Activate").equals("true")){
					mDPM.lockNow();

				}
				
			} else if (deltaY > deltaX) {
				Log.v("SHAKE", "SS");
			} else {
			
			}
		}
		
		}
	}
	private String GetStoreShared1(String Keyword){
		shared= getSharedPreferences(Filename1 , 0);
		return shared.getString(Keyword,"error");
	}

}
