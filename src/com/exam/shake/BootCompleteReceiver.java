package com.exam.shake;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootCompleteReceiver extends BroadcastReceiver{

	private SharedPreferences shared;
	Context c ;
	String Filename =  "Shake";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		c = arg0;
		String temp = GetStoreShared("Auto Start");
		if(temp.equals("true")){
			c.startService(new Intent(c, MyService.class) );
		}
	}

	private String GetStoreShared(String Keyword){
		
		shared= c.getSharedPreferences(Filename , 0);
		return shared.getString(Keyword,"error");
	}
	
}
