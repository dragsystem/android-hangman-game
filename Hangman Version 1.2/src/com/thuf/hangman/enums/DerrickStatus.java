package com.thuf.hangman.enums;

import com.thuf.hangman.newversion.R;

public class DerrickStatus {
	private int currentStep = 0;
	private int allStagesCount;
	private int[] stages;
	private boolean playerLosing = false;

	public DerrickStatus() {
		int[] allDerrickParts = { R.drawable.stage_rope, R.drawable.stage_head,
				R.drawable.stage_body, R.drawable.stage_arm_left,
				R.drawable.stage_arm_right, R.drawable.stage_leg_left,
				R.drawable.stage_hangman_complete };
		this.stages = allDerrickParts;
		this.allStagesCount = allDerrickParts.length;
	}

	public int getNextDerricCode() {
		int resultCode = this.stages[this.currentStep];
		if (this.currentStep < this.allStagesCount - 1) {
			this.currentStep++;
		} else {
			this.playerLosing = true;
		}
		return resultCode;
	}

	public boolean isPlayerLosing() {
		return this.playerLosing;
	}
}
