package omega.entities.components;

import flounder.guis.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

public class ComponentLife extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	public ComponentLife(Entity entity) {
		super(entity, ID);
	}

	@Override
	public void update() {
		ComponentCollider colliderComponent = (ComponentCollider) getEntity().getComponent(ComponentCollider.ID);

		if (OmegaRoom.getEntityPlayer() != null) {
			if (OmegaRoom.getEntityPlayer().getBounding().intersects(colliderComponent.getAABB()).isIntersection()) {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().addLife();
			}
		}
	}

	@Override
	public void dispose() {

	}
}
