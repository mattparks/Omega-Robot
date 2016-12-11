package omega.entities.components;

import flounder.maths.vectors.*;
import flounder.physics.*;
import omega.entities.*;

public class ComponentCollider extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private AABB aabb;

	public ComponentCollider(Entity entity) {
		super(entity, ID);
		this.aabb = new AABB();
	}

	@Override
	public void update() {
		if (super.getEntity().hasMoved()) {
			updateAABB();
		}
	}

	private void updateAABB() {
		ComponentTexture textureComponent = (ComponentTexture) getEntity().getComponent(ComponentTexture.ID);
		Vector2f position = super.getEntity().getPosition();
		float scale = textureComponent.getScale();

		if (textureComponent != null && textureComponent.getTexture() != null) {
			aabb.setMinExtents(position.x, position.y, -1.0f);
			aabb.setMaxExtents(position.x + scale, position.y + scale, 1.0f);
		}
	}

	public AABB getAABB() {
		if (super.getEntity().hasMoved()) {
			updateAABB();
		}

		return aabb;
	}

	@Override
	public void dispose() {
	}
}
