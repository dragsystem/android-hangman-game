package com.thuf.hangman.newversion;

import com.thuf.hangman.constants.GameConstants;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MyCustomDialog extends Activity {
	private static final int HELP_DIALOG_DELAY = 4500;
	private static final int END_OF_GAME_DIALOG_DELAY = 3000;
	private static final int CHEAT_DIALOG_DELAY = 2000;
	private Runnable closeDialog;
	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_my);
		setRunnable();
		getDialogType();
	}

	private void setRunnable() {
		this.closeDialog = new Runnable() {

			@Override
			public void run() {
				MyCustomDialog.this.finish();
			}
		};
	}

	private void getDialogType() {
		boolean isHelpDialog = getIntent().hasExtra("wordMean");
		boolean isEndOfGame = getIntent().hasExtra("endOfGame");

		if (isHelpDialog) {
			Bundle data = getIntent().getExtras();
			String mean = (String) data.get("wordMean");
			TextView helpDialog = (TextView) findViewById(R.id.tv_my_dialog);
			helpDialog.setText(mean);
			// Setting delay for the help dialog
			setHelpDialog();
		} else if (isEndOfGame) {
			Bundle data = getIntent().getExtras();
			String endOfGameMessage = (String) data.get("endOfGame");

			if (endOfGameMessage.equals(GameConstants.MESSAGE_LOSE)) {
				this.mediaPlayer = MediaPlayer.create(this, R.raw.losing);
			} else {
				this.mediaPlayer = MediaPlayer.create(this, R.raw.winning);
			}
			TextView helpDialog = (TextView) findViewById(R.id.tv_my_dialog);
			helpDialog.setText(endOfGameMessage);
			boolean soundsEnabled = getIntent().getExtras().getBoolean(
					GameConstants.EXTRAS_GAME_SETTINGS_SOUNDS);
			if (soundsEnabled) {
				this.mediaPlayer.start();
			}
			// Setting delay for the end of game dialog
			setEndOfGameDialog();
		} else {
			// Setting delay for the cheat dialog
			setCheatDialog();
		}
	}

	private void setHelpDialog() {
		new Handler().postDelayed(this.closeDialog, HELP_DIALOG_DELAY);
	}

	private void setCheatDialog() {
		new Handler().postDelayed(this.closeDialog, CHEAT_DIALOG_DELAY);
	}

	private void setEndOfGameDialog() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent data = new Intent();
				setResult(RESULT_OK, data);
				mediaPlayer.stop();
				mediaPlayer.release();
				MyCustomDialog.this.finish();
			}
		}, END_OF_GAME_DIALOG_DELAY);
	}

	@Override
	public void finish() {
		super.finish();
	}
}