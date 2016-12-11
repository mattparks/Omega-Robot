package omega.guis;

import flounder.fonts.*;
import flounder.guis.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

import java.util.*;

public class GuiFinished extends GuiComponent {
	private Text winText;
	private Text restartingText;

	private int restartTime;

	public static boolean RESTING = false;

	public GuiFinished() {
		winText = Text.newText("You win!").setFont(FlounderFonts.FFF_FORWARD).setFontSize(3.0f).create();
		winText.setColour(0.8f, .1f, .1f);
		addText(winText, 0.5f, 0.3f, 0.75f);

		restartTime = 0;
		restartingText = Text.newText("Restarting in 0").setFont(FlounderFonts.FFF_FORWARD).setFontSize(1.0f).create();
		restartingText.setColour(0.8f, .1f, .1f);
		addText(restartingText, 0.5f, 0.7f, 0.75f);
	}

	public void setLost() {
		if (!RESTING) {
			RESTING = true;
			winText.setText("You lost!");
			winText.setColour(0.1f, 0.2f, 0.5f);
			restartingText.setColour(0.1f, 0.2f, 0.5f);
			show(true);
			resetTimer();
			((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().show(false);

			OmegaEntities.clear();
			OmegaEntities.getEntities().add(OmegaRoom.getEntityPlayer());
		}
	}

	public void setWon() {
		if (!RESTING) {
			RESTING = true;
			winText.setText("You won!");
			winText.setColour(0.8f, 0.1f, 0.1f);
			restartingText.setColour(0.8f, 0.1f, 0.1f);
			show(true);
			resetTimer();
			((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().show(false);

			OmegaEntities.clear();
			OmegaEntities.getEntities().add(OmegaRoom.getEntityPlayer());
		}
	}

	private void resetTimer() {
		restartTime = 5;
		restartingText.setText("Restarting in " + restartTime);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				restartTime--;
				if (restartTime < 0) {
					restartTime = 0;
					timer.cancel();
					show(false);
					OmegaRoom.reset();
					RESTING = false;

					((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().setLives(3);
					((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().show(true);
				} else {
					restartingText.setText("Restarting in " + restartTime);
				}
			}
		}, 1000, 1000);
	}

	@Override
	protected void updateSelf() {

	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {

	}
}
