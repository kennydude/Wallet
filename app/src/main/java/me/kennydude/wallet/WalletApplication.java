package me.kennydude.wallet;

import android.app.Application;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.roscopeco.ormdroid.ORMDroidApplication;

import java.io.File;

/**
 * Created by kennydude on 19/01/2014.
 */
public class WalletApplication extends Application {

	public static JobManager jobcentre;
	public static WalletApplication instance;

	@Override
	public void onCreate (){
		super.onCreate();

		instance = this;
		jobcentre = new JobManager(this);

		ORMDroidApplication.initialize(this);
	}

}
