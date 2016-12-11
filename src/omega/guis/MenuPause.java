package omega.guis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.visual.*;
import omega.*;
import omega.room.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class MenuPause extends GuiComponent {
	protected static final float SLIDE_TIME = 0.5f;

	private int loaded;
	private Text title;
	private Text reset;
	private Text about;
	private Text quit;

	private Text aboutMessage;

	private int selected;

	private KeyButton up;
	private KeyButton down;
	private KeyButton enter;

	private Sound downUpSound;
	private Sound enterSound;

	private ValueDriver slideDriver;
	private float backgroundAlpha;

	public MenuPause() {
		loaded = 0;

		title = Text.newText("Game Paused").setFontSize(2.0f).create();
		title.setColour(0, 1, 0);
		title.setAlphaDriver(new ConstantDriver(0.0f));
		addText(title, 0.5f, 0.08f, 5.0f);

		selected = 0;
		up = new KeyButton(GLFW.GLFW_KEY_UP);
		down = new KeyButton(GLFW.GLFW_KEY_DOWN);
		enter = new KeyButton(GLFW.GLFW_KEY_ENTER);

		reset = load("> Reset Room", 0.0f);
		about = load("About", 0.0f);
		quit = load("Quit Game", 0.0f);

		aboutMessage = load("Robot Omega was created for Ludum Dare 37 (One room), by Matthew Albrecht. Find the missing computer chip for your robot! Use WASD/Arrow Keys to move, Esc to pause, F2 screenshot, F11 fullscreen. All configs are found in your roaming folder.", 0.125f);
		aboutMessage.setAlphaDriver(new ConstantDriver(0.0f));

		downUpSound = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "button2.wav"), 1.0f);
		enterSound = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "button1.wav"), 1.0f);

		slideDriver = new ConstantDriver(0.0f);
		backgroundAlpha = 0.0f;
	}

	private Text load(String s, float extraOffset) {
		Text t = Text.newText(s).setFontSize(1.0f).textAlign(TextAlign.CENTRE).create();
		t.setColour(0, 1, 0);
		t.setAlphaDriver(new ConstantDriver(0.0f));
		addText(t, 0.5f, extraOffset + 0.3f + (0.1f * loaded++), 1.0f);
		return t;
	}

	@Override
	protected void updateSelf() {
		if (up.wasDown()) {
			FlounderSound.playSystemSound(downUpSound);
			selected--;
		} else if (down.wasDown()) {
			FlounderSound.playSystemSound(downUpSound);
			selected++;
		}

		if (selected < 0) {
			selected = 0;
		} else if (selected > 2) {
			selected = 2;
		}

		reset.setText((selected == 0 ? "> " : "") + "Reset Room");
		about.setText((selected == 1 ? "> " : "") + "About");
		quit.setText((selected == 2 ? "> " : "") + "Quit Game");

		backgroundAlpha = slideDriver.update(FlounderFramework.getDelta());

		if (enter.wasDown()) {
			FlounderSound.playSystemSound(enterSound);

			if (selected == 0) {
				OmegaRoom.reset();
				((OmegaGuis) FlounderGuis.getGuiMaster()).pause(false);
			} else if (selected == 1) {
				if (aboutMessage.getCurrentAlpha() == 0.0f) {
					aboutMessage.setAlphaDriver(new SlideDriver(aboutMessage.getCurrentAlpha(), 1.0f, SLIDE_TIME));
				} else {
					aboutMessage.setAlphaDriver(new SlideDriver(aboutMessage.getCurrentAlpha(), 0.0f, SLIDE_TIME));
				}
			} else if (selected == 2) {
				FlounderFramework.requestClose();
				((OmegaGuis) FlounderGuis.getGuiMaster()).pause(false);
			}
		}
	}

	public void toggle(boolean open) {
		if (!isShown()) {
			show(true);
		}

		if (!open) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					show(false);
					selected = 0;

					if (aboutMessage.getCurrentAlpha() == 1.0f) {
						aboutMessage.setAlphaDriver(new ConstantDriver(0.0f));
					}
				}
			}, 500);
		}

		title.setAlphaDriver(new SlideDriver(title.getCurrentAlpha(), open ? 1.0f : 0.0f, SLIDE_TIME));
		reset.setAlphaDriver(new SlideDriver(reset.getCurrentAlpha(), open ? 1.0f : 0.0f, SLIDE_TIME));
		about.setAlphaDriver(new SlideDriver(about.getCurrentAlpha(), open ? 1.0f : 0.0f, SLIDE_TIME));
		quit.setAlphaDriver(new SlideDriver(quit.getCurrentAlpha(), open ? 1.0f : 0.0f, SLIDE_TIME));
		aboutMessage.setAlphaDriver(new SlideDriver(aboutMessage.getCurrentAlpha(), 0.0f, SLIDE_TIME));
		slideDriver = new SlideDriver(backgroundAlpha, open ? 1.0f : 0.0f, SLIDE_TIME);
	}

	public float getBlur() {
		return backgroundAlpha;
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {

	}
}
