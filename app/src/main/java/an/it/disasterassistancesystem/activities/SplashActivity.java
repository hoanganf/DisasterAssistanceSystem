package an.it.disasterassistancesystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import an.it.disasterassistancesystem.R;

public class SplashActivity extends Activity{
	private CountDownTimer timerCount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if(timerCount!=null) timerCount.cancel();
		timerCount=new CountDownTimer(3000,1000) {
			@Override
			public void onTick(long l) {

			}

			@Override
			public void onFinish() {
				Intent i=new Intent(SplashActivity.this,MainActivity.class);
				startActivity(i);
				SplashActivity.this.finish();
			}
		};
		timerCount.start();
		
		
	}

	@Override
	protected void onDestroy() {
		if(timerCount!=null) timerCount.cancel();
		super.onDestroy();
	}
	
}
