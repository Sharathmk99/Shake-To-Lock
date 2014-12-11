package com.exam.shake;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;
import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "z2z7IhHifnbNnzXcDcvWxKhgKbC5D3P0mpp8Z9sl",
				"LUUQgmSGjsx16CmjEgans2O2pYU5xcRYaca2iFIi");

		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
}
