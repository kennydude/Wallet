package me.kennydude.wallet;

import android.app.Application;

import com.roscopeco.ormdroid.ORMDroidApplication;

import java.io.File;

/**
 * Created by kennydude on 19/01/2014.
 */
public class WalletApplication extends Application {

	@Override
	public void onCreate (){
		super.onCreate();
		ORMDroidApplication.initialize(this);
	}

}
