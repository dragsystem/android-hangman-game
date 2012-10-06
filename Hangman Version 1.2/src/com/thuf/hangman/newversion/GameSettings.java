package com.thuf.hangman.newversion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.thuf.hangman.constants.GameConstants;

public class GameSettings extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.game_settings);
	}

	@Override
	public void finish() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String gameDifficulty = settings.getString(GameConstants.KEY_SETTINGS_DIFFICULTY, "EASY");
		boolean inGameSounds = settings.getBoolean(GameConstants.KEY_SETTINGS_SOUNDS, true);
		
		Intent data = new Intent();
		data.putExtra(GameConstants.EXTRAS_GAME_SETTINGS_DIFFICULTY, gameDifficulty);
		data.putExtra(GameConstants.EXTRAS_GAME_SETTINGS_SOUNDS, inGameSounds);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
