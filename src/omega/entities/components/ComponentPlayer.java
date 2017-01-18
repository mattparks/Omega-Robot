package omega.entities.components;

import flounder.framework.*;
import flounder.inputs.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import omega.entities.*;
import omega.guis.*;

import static org.lwjgl.glfw.GLFW.*;

public class ComponentPlayer extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	public static final float PLAYER_X_SPEED = 10.0f;
	public static final float PLAYER_Y_SPEED = 10.0f;
	public static final float PLAYER_R_SPEED = 10.0f;

	private ButtonAxis movementX;
	private ButtonAxis movementY;

	public ComponentPlayer(Entity entity) {
		super(entity, ID);
		this.movementX = new ButtonAxis(new KeyButton(GLFW_KEY_A), new KeyButton(GLFW_KEY_D));
		this.movementY = new ButtonAxis(new KeyButton(GLFW_KEY_W), new KeyButton(GLFW_KEY_S));
	}

	@Override
	public void update() {
		Vector2f delta = new Vector2f();

		if (!GuiFinished.RESTING) {
			float x = movementX.getAmount();
			float y = movementY.getAmount();
			delta.x += x * FlounderFramework.getDelta();
			delta.y += y * FlounderFramework.getDelta();
		}

		Vector2f posLast = new Vector2f(super.getEntity().getPosition());
		super.getEntity().move(delta);
		Vector2f posNew = new Vector2f(super.getEntity().getPosition());
		Vector2f finalDelta = Vector2f.subtract(posNew, posLast, null);

		FlounderProfiler.add(OmegaEntities.PROFILE_TAB_NAME, "Player Delta X", finalDelta.x);
		FlounderProfiler.add(OmegaEntities.PROFILE_TAB_NAME, "Player Delta Y", finalDelta.y);

		changeFace(PlayerFace.NORMAL);
		if (finalDelta.x > 0.0f) {
			changeFace(PlayerFace.LEFT);
		} else if (finalDelta.x < -0.0f) {
			changeFace(PlayerFace.RIGHT);
		}
		//	if (finalDelta.y > -0.0f) {
		//		changeFace(PlayerFace.DOWN);
		//	}
	}

	public void changeFace(PlayerFace face) {
		((ComponentTexture) super.getEntity().getComponent(ComponentTexture.ID)).setTextureIndex(face.index);
	}

	@Override
	public void dispose() {

	}

	public enum PlayerFace {
		NORMAL(0), LEFT(2), RIGHT(1), DOWN(3);

		public final int index;

		PlayerFace(int index) {
			this.index = index;
		}
	}
}
