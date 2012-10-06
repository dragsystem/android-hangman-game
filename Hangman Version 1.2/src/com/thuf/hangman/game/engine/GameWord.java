package com.thuf.hangman.game.engine;

import java.util.Random;

import android.content.Context;

import com.thuf.hangman.newversion.R;

public class GameWord {
	private String word;
	private String wordMean;
	private String guessedLetters;

	public GameWord(Context context) {
		String[] words = context.getResources().getStringArray(R.array.words);
		String[] means = context.getResources().getStringArray(
				R.array.words_means);
		Random rand = new Random();
		int wordsCount = words.length;
		int randomIndex = rand.nextInt(wordsCount);

		this.word = words[randomIndex].toUpperCase();
		this.wordMean = means[randomIndex];

		int length = this.word.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append("_");
		}
		this.guessedLetters = sb.toString();
	}

	public String getWord() {
		return this.word;
	}

	public String getWordMean() {
		return this.wordMean;
	}

	public void setGuessedLetters(String guessedLetters) {
		this.guessedLetters = guessedLetters;
	}

	public String getGuessedLetters() {
		return this.guessedLetters;
	}

	public String getGuessedLettersFormated() {
		StringBuilder sb = new StringBuilder();

		int length = this.word.length();
		for (int i = 0; i < length; i++) {
			char letter = this.guessedLetters.charAt(i);
			sb.append(letter + " ");
		}
		String formated = sb.toString();
		return formated;
	}

	public boolean isWordGuessed() {
		int length = this.word.length();
		for (int i = 0; i < length; i++) {
			char nextLetter = this.guessedLetters.charAt(i);
			if (nextLetter == '_') {
				return false;
			}
		}
		return true;
	}
}
