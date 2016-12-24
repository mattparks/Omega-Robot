package omega.guis;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.resources.*;
import flounder.sounds.*;
import flounder.visual.*;
import org.lwjgl.glfw.*;

import java.util.*;

public class GuiStartup extends GuiComponent {
	public boolean enabled = true;
	private String[] texts = new String[]{
			"> Initializing Flounder Framework",
			"> Loading main start about screen",
			"NULL",
			"Robot Omega was created for Ludum Dare 37 (One room), by: Matthew Albrecht.",
			"http://mattyparks.com/",
			"https://twitter.com/Mattparks5855",
			"https://ldjam.com/users/matthew-albrecht/",
			"NULL",
			"Find the missing computer chip for your robot!",
			"This games was not created to be a maze game, it just turned into it.",
			"This is my first Compo submission, code created is a mess!",
			"Use WASD keys to move, Esc to pause, arrows amd enter to navigate;",
			"F2 screenshot, F11 fullscreen, screenshots and configs saved in home/roaming.",
			"NULL",
		//	"So, are you ready?",
			"NULL",
			"-- PRESS SPACE TO START --"
	};

	private List<Text> loadingTexts;
	private int loaded;
	private boolean load;
	private boolean fadeOut;
	private SlideDriver alphaDriver;
	private boolean finished;
	private KeyButton startGame;
	private boolean spaceBlinking;

	private Sound typingSound;
	private Sound loadSound;

	public GuiStartup() {
		loadingTexts = new ArrayList<>();
		loaded = 0;
		load = false;
		fadeOut = false;
		alphaDriver = null;
		finished = false;
		startGame = new KeyButton(GLFW.GLFW_KEY_SPACE);
		spaceBlinking = false;

		typingSound = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "typing.wav"), 1.0f);
		loadSound = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "enter.wav"), 1.0f);

		FlounderSound.playSystemSound(typingSound);

		for (int i = 0; i < texts.length; i++) {
			if (!texts[i].equals("NULL")) {
				Text line = Text.newText(texts[i].substring(0, 1)).setFont(FlounderFonts.FFF_FORWARD).setFontSize(0.9f).textAlign(GuiAlign.CENTRE).create();
				line.setColour(0, 1, 0);
				loadingTexts.add(line);
			} else {
				loadingTexts.add(null);
			}
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				load = true;

				if (loaded >= loadingTexts.size()) {
					//	alphaDriver = new SlideDriver(1.0f, 0.0f, 3.0f);
					spaceBlinking = true;
					load = false;
					timer.cancel();
				}
			}
		}, 0, 1000);
	}

	@Override
	protected void updateSelf() {
		if (!enabled) {
			return;
		}

		if (load && loaded < loadingTexts.size()) {
			Text t = loadingTexts.get(loaded);

			if (t != null) {
				addText(t, 0.5f, 0.04f + (0.05f * loaded), 5.0f);
			}

			loaded++;
			load = t == null;
		}

		for (int i = 0; i < loadingTexts.size(); i++) {
			Text text = loadingTexts.get(i);

			if (text != null && text.isLoaded() && !text.getTextString().equals(texts[i])) {
				String s = texts[i];
				int l = text.getTextString().length();
				text.setText(text.getTextString() + s.substring(l, l + 1));
			}
		}

		if (spaceBlinking) {
			Text blink = loadingTexts.get(loadingTexts.size() - 1);

			if (blink.getCurrentAlpha() == 0.0f) {
				blink.setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.0f));
			} else if (blink.getCurrentAlpha() == 1.0f) {
				blink.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 1.0f));
			}
			if (startGame.wasDown()) {
				FlounderSound.playSystemSound(loadSound);
				spaceBlinking = false;
				fadeOut = true;
			}
		}


		if (fadeOut) {
			if (alphaDriver == null) {

				alphaDriver = new SlideDriver(1.0f, 0.0f, 3.0f);
			}
			float value = alphaDriver.update(FlounderFramework.getDelta());
			if (value == 0.0f) {
				finished = true;
			}
			loadingTexts.forEach(text -> {
				if (text != null) {
					text.setAlphaDriver(alphaDriver);
				}
			});
		}
	}

	public boolean loaded() {
		return finished;
	}

	public void stop() {
		enabled = false;
		finished = false;
		fadeOut = false;
		loadingTexts.forEach(text -> {
			if (text != null) {
				deleteText(text, false);
			}
		});
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {

	}
}
