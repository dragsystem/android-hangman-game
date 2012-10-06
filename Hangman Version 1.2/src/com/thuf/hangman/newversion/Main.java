package com.thuf.hangman.newversion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.thuf.hangman.constants.GameConstants;
import com.thuf.hangman.enums.DerrickStatus;
import com.thuf.hangman.game.engine.GameEngine;
import com.thuf.hangman.game.engine.GameWord;

public class Main extends Activity implements OnEditorActionListener {
	private static final int REQUEST_CODE_RESTART = 15;
	private static final int REQUEST_CODE_GAME_SETTINGS = 30;

	private GameWord myWord;
	private GameEngine engine;
	private DerrickStatus derrickStatus;

	private ImageView ivDerrick;
	private TextView tvDisplay;
	private EditText etUserInput;

	private MediaPlayer mediaPlayerWrongLetter;

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String difficulty = settings.getString(GameConstants.KEY_SETTINGS_DIFFICULTY,
				"EASY");
		boolean soundsState = settings
				.getBoolean(GameConstants.KEY_SETTINGS_SOUNDS, true);
		String sounds = null;
		if (soundsState) {
			sounds = "ON";
		} else {
			sounds = "OFF";
		}
		Toast.makeText(this,
				"-=GAME SETTINGS=-\nDIFFICULTY: " + difficulty + "\nSOUNDS: " + sounds,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		int test = 151;
		test += 30;
		incialize();
		updateTvDisplay(this.myWord.getGuessedLettersFormated());
		this.etUserInput.setOnEditorActionListener(this);
	}

	private void incialize() {
		this.myWord = new GameWord(getApplicationContext());
		this.engine = new GameEngine(this.myWord);
		this.derrickStatus = new DerrickStatus();

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String difficulty = settings.getString(GameConstants.KEY_SETTINGS_DIFFICULTY,
				"EASY");
		boolean soundsState = settings
				.getBoolean(GameConstants.KEY_SETTINGS_SOUNDS, true);
		this.engine.setGameDifficulty(difficulty);
		this.engine.setSoundsState(soundsState);

		this.ivDerrick = (ImageView) findViewById(R.id.ivDerrick);
		this.tvDisplay = (TextView) findViewById(R.id.textView1);
		this.etUserInput = (EditText) findViewById(R.id.editText1);

		this.mediaPlayerWrongLetter = MediaPlayer.create(this, R.raw.wrong_letter);
	}

	private void updateTvDisplay(String update) {
		this.tvDisplay.setText(update);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_GO) {
			String input = this.etUserInput.getText().toString();
			this.etUserInput.setText("");

			// User's input must be exactly one letter
			// Not performing check if input is letter or not
			boolean isInvalid = TextUtils.isEmpty(input) || input.length() != 1;
			if (isInvalid) {
				Toast.makeText(getApplicationContext(), "Please enter only one letter.",
						Toast.LENGTH_SHORT).show();
			} else {
				char inputLetter = input.charAt(0);
				boolean isRevealed = engine.revealLetter(inputLetter);
				String wordAfterRevealing = myWord.getGuessedLettersFormated();
				updateTvDisplay(wordAfterRevealing);

				if (isRevealed) {
					if (this.myWord.isWordGuessed()) {
						setAndStartEndOfGameIntent(GameConstants.MESSAGE_WIN);
					}
					// The user revealed a letter(s)
					// Edit Text View stays active
					return true;
				} else {
					int derricResId = this.derrickStatus.getNextDerricCode();
					this.ivDerrick.setBackgroundResource(derricResId);

					boolean isPlayerLosing = this.derrickStatus.isPlayerLosing();
					if (isPlayerLosing) {
						setAndStartEndOfGameIntent(GameConstants.MESSAGE_LOSE);
					} else {
						if (this.engine.getSoundsState()) {
							this.mediaPlayerWrongLetter.start();
						}
					}
					// Close Edit Text View
					// (user input is not valid, or no letters are revealed).
					return false;
				}
			}
		}
		return false;
	}

	public void setAndStartEndOfGameIntent(String message) {
		Intent intent = new Intent(Main.this, MyCustomDialog.class);
		intent.putExtra("endOfGame", message);
		boolean soundsState = this.engine.getSoundsState();
		intent.putExtra(GameConstants.EXTRAS_GAME_SETTINGS_SOUNDS, soundsState);
		startActivityForResult(intent, REQUEST_CODE_RESTART);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_RESTART:
				onCreate(null);
				break;
			case REQUEST_CODE_GAME_SETTINGS:
				boolean soundsState = data.getExtras().getBoolean(
						GameConstants.EXTRAS_GAME_SETTINGS_SOUNDS);
				String gameDifficulty = data.getExtras().getString(
						GameConstants.EXTRAS_GAME_SETTINGS_DIFFICULTY);
				this.engine.setSoundsState(soundsState);
				this.engine.setGameDifficulty(gameDifficulty);
				break;
			default:
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.menu_cheat:
			boolean isCheated = this.engine.cheat();
			if (isCheated) {
				updateTvDisplay(this.myWord.getGuessedLettersFormated());
				if (this.myWord.isWordGuessed()) {
					setAndStartEndOfGameIntent(GameConstants.MESSAGE_WIN);
				}
			} else {
				intent = new Intent(Main.this, MyCustomDialog.class);
				startActivity(intent);
			}
			break;
		case R.id.menu_help:
			String help = this.myWord.getWordMean();
			intent = new Intent(Main.this, MyCustomDialog.class);
			intent.putExtra("wordMean", help);
			startActivity(intent);
			break;
		case R.id.menu_new_game:
			onCreate(null);
			break;
		case R.id.menu_settings:
			intent = new Intent(Main.this, GameSettings.class);
			startActivityForResult(intent, REQUEST_CODE_GAME_SETTINGS);
			break;
		default:
		}
		return true;
	}

	@Override
	public void finish() {
		this.mediaPlayerWrongLetter.release();
		super.finish();
	}
}
