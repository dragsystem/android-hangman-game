package com.thuf.hangman.game.engine;

import com.thuf.hangman.enums.Difficulty;

public class GameEngine {
	private GameWord word;
	private int cheatsUsed;
	private int cheatsAvailable;
	private boolean soundsState;

	public GameEngine(GameWord word) {
		this.word = word;
		this.cheatsUsed = 0;
		this.cheatsAvailable = 0;
		this.soundsState = true;
	}
	
	public void setSoundsState(boolean state){
		this.soundsState = state;
	}
	
	public boolean getSoundsState(){
		return this.soundsState;
	}

	public void setGameDifficulty(Difficulty difficulty) {
		switch (difficulty) {
		case EASY:
			this.cheatsAvailable = 3;
			break;
		case MEDIUM:
			this.cheatsAvailable = 1;
			break;
		case HARD:
			this.cheatsAvailable = 0;
			break;
		}
	}
	
	public void setGameDifficulty(String difficulty) {
		if(difficulty.equals("EASY")){
			setGameDifficulty(Difficulty.EASY);
		}else if(difficulty.equals("MEDIUM")){
			setGameDifficulty(Difficulty.MEDIUM);
		}else{
			setGameDifficulty(Difficulty.HARD);
		}
	}

	public boolean revealLetter(char userLetter) {
		userLetter = Character.toUpperCase(userLetter);
		String word = this.word.getWord();
		char[] guessedLettersArray = this.word.getGuessedLetters()
				.toCharArray();
		int wordLength = this.word.getWord().length();

		boolean isRevelaed = false;
		for (int i = 0; i < wordLength; i++) {
			char wordLetter = word.charAt(i);
			if (wordLetter == userLetter) {
				guessedLettersArray[i] = wordLetter;
				isRevelaed = true;
			}
		}
		String guessedLettersResult = new String(guessedLettersArray);
		this.word.setGuessedLetters(guessedLettersResult);

		return isRevelaed;
	}

	/**
	 * @return true if cheat is used, false if all cheats are used.
	 */
	public boolean cheat() {
		if (this.cheatsUsed < this.cheatsAvailable) {
			this.cheatsUsed++;

			String word = this.word.getWord();
			char[] guessedLettersArray = this.word.getGuessedLetters()
					.toCharArray();
			int wordLength = this.word.getWord().length();

			for (int i = 0; i < wordLength; i++) {
				char nextGuessedLetter = guessedLettersArray[i];
				if (nextGuessedLetter == '_') {
					char replaceWith = word.charAt(i);
					revealLetter(replaceWith);
					return true;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
