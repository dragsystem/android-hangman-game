package com.thuf.hangman.newversion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartScreen extends Activity {
	private static final long START_SCREEN_DELAY = 3500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);

		Runnable myRunner = new Runnable() {

			@Override
			public void run() {
				Intent goToMainActivity = new Intent(StartScreen.this,
						Main.class);
				StartScreen.this.startActivity(goToMainActivity);
				StartScreen.this.finish();
			}
		};
		new Handler().postDelayed(myRunner, START_SCREEN_DELAY);
	}
}
