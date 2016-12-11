package omega.guis;

import flounder.guis.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;
import flounder.textures.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

import java.util.*;

public class GuiOverlay extends GuiComponent {
	private GuiTexture lives;

	public GuiOverlay() {
		lives = new GuiTexture(Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "lives.png")).create());
		lives.getTexture().setHasTransparency(true);
		lives.getTexture().setNumberOfRows(2);
		lives.setSelectedRow(0);
	}

	@Override
	protected void updateSelf() {
		if (!OmegaRoom.CANCEL_RENDERS) {
			show(true);
		}

		lives.setPosition(0.05f, 0.02f, 0.1f, 0.1f);
		lives.update();

		Entity p = OmegaRoom.getEntityPlayer();

		if (p != null) {
			AABB aabb = p.getBounding();
			Vector3f min = new Vector3f(aabb.getMinExtents());
			Vector3f max = new Vector3f(aabb.getMaxExtents());

			Vector3f.subtract(min, p.getPosition().toVector3f(), min);
			Vector3f.subtract(max, p.getPosition().toVector3f(), max);
		}
	}

	public void setLives(int lives) {
		this.lives.setSelectedRow((lives == 3 ? 0 : lives == 2 ? 1 : 2));
	}

	public void addLife() {
		if (!GuiFinished.RESTING) {
			int l = lives.getSelectedRow() - 1;

			if (l >= 0) {
				this.lives.setSelectedRow(l);
			}
		}
	}

	public void removeLife() {
		if (!GuiFinished.RESTING) {
			int l = lives.getSelectedRow() + 1;

			if (l > 2) {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiFinished().setLost();
				this.lives.setSelectedRow(0);
			} else {
				this.lives.setSelectedRow(l);
			}
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
		if (!GuiFinished.RESTING && !FlounderGuis.getGuiMaster().isGamePaused()) {
			guiTextures.add(lives);
		}
	}
}
