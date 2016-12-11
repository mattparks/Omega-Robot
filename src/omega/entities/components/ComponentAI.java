package omega.entities.components;

import flounder.framework.*;
import flounder.maths.vectors.*;
import omega.entities.*;

public class ComponentAI extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private static final float MOVE_SPEED = 0.5f;

	private boolean reverseX;
	private boolean reverseY;
	private Vector2f moveSpeed;
	private Vector2f moveDelta;

	public ComponentAI(Entity entity) {
		super(entity, ID);
		reverseX = false;
		reverseY = false;
		moveSpeed = new Vector2f();
		moveDelta = new Vector2f();
	}

	@Override
	public void update() {
		if (getEntity().isRemoved()) {
			return;
		}

		move();
	}

	private void move() {
		moveSpeed.x = (reverseX ? -1.0f : 1.0f) * MOVE_SPEED * FlounderFramework.getDelta();
		//	moveSpeed.y = (reverseY ? -1.0f : 1.0f) * MOVE_SPEED * FlounderFramework.getDelta();
		Vector2f posPrv = new Vector2f(getEntity().getPosition());
		getEntity().move(moveSpeed);
		Vector2f posNew = new Vector2f(getEntity().getPosition());
		Vector2f.subtract(posNew, posPrv, moveDelta);

		if (moveDelta.x == 0.0f) {
			reverseX = !reverseX;
		}

		//	if (moveDelta.y == 0.0f) {
		//		reverseY = !reverseY;
		//	}
	}

	@Override
	public void dispose() {

	}
}
