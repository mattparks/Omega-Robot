package omega;

import flounder.camera.*;
import flounder.devices.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import omega.entities.components.*;
import omega.room.*;

public class OmegaPlayer extends IPlayer {
	public static final float PLAYER_X_SPEED = 10.0f;
	public static final float PLAYER_Y_SPEED = 10.0f;
	public static final float PLAYER_R_SPEED = 10.0f;

	private Vector3f position;
	private Vector3f rotation;

	public OmegaPlayer() {
		super(FlounderProfiler.class, FlounderKeyboard.class);
	}

	@Override
	public void init() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
	}

	@Override
	public void update() {

	}

	@Override
	public Vector3f getPosition() {
		if (OmegaRoom.getEntityPlayer() != null) {
			ComponentTexture ct = (ComponentTexture) OmegaRoom.getEntityPlayer().getComponent(ComponentTexture.ID);
			position.set(OmegaRoom.getEntityPlayer().getPosition().toVector3f());
			position.x += (ct.getScale() - FlounderDisplay.getAspectRatio()) / 2.0f;
			position.y += (ct.getScale() - 1.0f) / 2.0f;
		}

		return position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
