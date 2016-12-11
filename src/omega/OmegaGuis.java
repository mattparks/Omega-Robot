package omega;

import flounder.devices.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.inputs.*;
import flounder.logger.*;
import flounder.profiling.*;
import omega.guis.*;
import omega.room.*;
import org.lwjgl.glfw.*;

public class OmegaGuis extends IExtension implements IGuiMaster {
	public static boolean ON_START = true;

	private GuiStartup guiStartup;
	private GuiFinished guiFinished;
	private GuiOverlay guiOverlay;
	private MenuPause menuPause;

	private boolean paused;
	private KeyButton pause;

	public OmegaGuis() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderGuis.class, FlounderFonts.class);
	}

	@Override
	public void init() {
		guiStartup = new GuiStartup();
		FlounderGuis.addComponent(guiStartup, 0.0f, 0.0f, 1.0f, 1.0f);
		guiStartup.show(true);

		guiFinished = new GuiFinished();
		FlounderGuis.addComponent(guiFinished, 0.0f, 0.0f, 1.0f, 1.0f);
		guiFinished.show(false);

		guiOverlay = new GuiOverlay();
		FlounderGuis.addComponent(guiOverlay, 0.0f, 0.0f, 1.0f, 1.0f);
		guiOverlay.show(false);

		menuPause = new MenuPause();
		FlounderGuis.addComponent(menuPause, 0.0f, 0.0f, 1.0f, 1.0f);
		menuPause.show(false);

		paused = true;
		pause = new KeyButton(GLFW.GLFW_KEY_ESCAPE);
	}

	@Override
	public void update() {
		if (guiStartup.loaded()) {
			paused = false;
			OmegaRoom.reset();
			guiOverlay.show(true);
			guiStartup.stop();
			ON_START = false;
		}

		if (!guiStartup.enabled) {
			if (pause.wasDown()) {
				pause(!paused);
			}
		}

		FlounderMouse.setCursorHidden(!isGamePaused());
	}

	@Override
	public boolean isGamePaused() {
		return menuPause.isShown();
	}

	@Override
	public void openMenu() {

	}

	@Override
	public float getBlurFactor() {
		return menuPause.getBlur();
	}

	@Override
	public void dispose() {

	}

	public void pause(boolean pause) {
		this.paused = pause;
		menuPause.toggle(paused);
		guiFinished.show(!paused && GuiFinished.RESTING);
	}

	public GuiFinished getGuiFinished() {
		return guiFinished;
	}

	public GuiOverlay getGuiOverlay() {
		return guiOverlay;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
