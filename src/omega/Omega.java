package omega;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.parsing.*;
import flounder.resources.*;
import flounder.sounds.*;
import omega.room.*;

public class Omega extends FlounderFramework {
	public static Config configMain;
	public static Config configPost;

	public static void main(String[] args) {
		Omega omega = new Omega();
		omega.run();
		omega.closeConfigs();
		System.exit(0);
	}

	public Omega() {
		super("Omega Robot", -1, new OmegaInterface(), new OmegaGuis(), new OmegaRenderer(), new OmegaRoom(), new OmegaPlayer(), new OmegaCamera());

		configMain = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "settings.conf"));
		configPost = new Config(new MyFile(FlounderFramework.getRoamingFolder(), "configs", "post.conf"));

		FlounderDisplay.setup(
				configMain.getIntWithDefault("width", 1080, FlounderDisplay::getWindowWidth),
				configMain.getIntWithDefault("height", 720, FlounderDisplay::getWindowHeight),
				"Omega Robot", new MyFile[]{new MyFile(MyFile.RES_FOLDER, "icon.png")},
				configMain.getBooleanWithDefault("vsync", false, FlounderDisplay::isVSync),
				configMain.getBooleanWithDefault("antialias", true, FlounderDisplay::isAntialiasing),
				configMain.getIntWithDefault("sampled", 0, FlounderDisplay::getSamples),
				configMain.getBooleanWithDefault("fullscreen", false, FlounderDisplay::isFullscreen)
		);
		FlounderMouse.setup(new MyFile(MyFile.RES_FOLDER, "guis", "cursor.png"));
		setFpsLimit(configMain.getIntWithDefault("fps_limit", -1, FlounderFramework::getFpsLimit));

		TextBuilder.DEFAULT_TYPE = FlounderFonts.FFF_FORWARD;
		MusicPlayer.SOUND_VOLUME = (float) configMain.getDoubleWithDefault("sound_volume", 0.75f, () -> MusicPlayer.SOUND_VOLUME);
	}

	public void closeConfigs() {
		configMain.dispose();
		configPost.dispose();
	}
}
