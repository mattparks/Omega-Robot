package omega.entities.components;

import flounder.guis.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

public class ComponentHazard extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	public ComponentHazard(Entity entity) {
		super(entity, ID);
	}

	@Override
	public void update() {
		if (getEntity().isRemoved()) {
			return;
		}
		ComponentCollider colliderComponent = (ComponentCollider) getEntity().getComponent(ComponentCollider.ID);

		if (OmegaRoom.getEntityPlayer() != null) {
			if (OmegaRoom.getEntityPlayer().getBounding().intersects(colliderComponent.getAABB()).isIntersection()) {
				getEntity().remove();
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().removeLife();
			}
		}
	}

	@Override
	public void dispose() {

	}
}
