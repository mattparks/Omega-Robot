package omega.entities.components;

import flounder.maths.vectors.*;
import flounder.physics.*;
import omega.entities.*;

public class ComponentCollision extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	public ComponentCollision(Entity entity) {
		super(entity, ID);
	}

	public Vector2f resolveAABBCollisions(Vector2f amount) {
		Vector2f result = new Vector2f(amount);
		ComponentCollider collider1 = (ComponentCollider) getEntity().getComponent(ComponentCollider.ID);

		if (collider1 == null) {
			return result;
		}

		AABB aabb1 = collider1.getAABB();
		final AABB collisionRange = AABB.stretch(aabb1, null, amount.x, amount.y, 0.0f); // The range in where there can be collisions!

		getEntity().visitInRange(ComponentCollision.ID, collisionRange, (Entity entity, IEntityComponent component) -> {
			if (entity.equals(getEntity())) {
				return;
			}

			ComponentCollider collider2 = (ComponentCollider) entity.getComponent(ComponentCollider.ID);

			if (collider2 == null) {
				return;
			}

			AABB aabb2 = collider2.getAABB();

			if (aabb2 != null) {
				if (aabb2.intersects(collisionRange).isIntersection()) {
					result.set((float) resolveCollisionX(aabb1, aabb2, result.getX()), (float) resolveCollisionY(aabb1, aabb2, result.getY()));
				}
			}
		});

		return result;
	}

	public double resolveCollisionX(AABB thisAABB, AABB other, double moveAmountX) {
		double newAmtX;

		if (moveAmountX == 0.0) {
			return moveAmountX;
		}

		if (moveAmountX > 0) { // This max == other min
			newAmtX = other.getMinExtents().getX() - thisAABB.getMaxExtents().getX();
		} else { // This min == other max
			newAmtX = other.getMaxExtents().getX() - thisAABB.getMinExtents().getX();
		}

		if (Math.abs(newAmtX) < Math.abs(moveAmountX)) {
			moveAmountX = newAmtX;
		}

		return moveAmountX;
	}

	public double resolveCollisionY(AABB thisAABB, AABB other, double moveAmountY) {
		double newAmtY;

		if (moveAmountY == 0.0) {
			return moveAmountY;
		}

		if (moveAmountY > 0) { // This max == other min.
			newAmtY = other.getMinExtents().getY() - thisAABB.getMaxExtents().getY();
		} else { // This min == other max.
			newAmtY = other.getMaxExtents().getY() - thisAABB.getMinExtents().getY();
		}

		if (Math.abs(newAmtY) < Math.abs(moveAmountY)) {
			moveAmountY = newAmtY;
		}

		return moveAmountY;
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
