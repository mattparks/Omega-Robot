package omega.entities.components;

import flounder.devices.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

public class ComponentComplete extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();
	public static final Sound SOUND = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "won.wav"), 1.0f, 1.0f);

	public ComponentComplete(Entity entity) {
		super(entity, ID);
	}

	@Override
	public void update() {
		if (getEntity().isRemoved()) {
			return;
		}
		ComponentCollider colliderComponent = (ComponentCollider) getEntity().getComponent(ComponentCollider.ID);

		/*getEntity().visitInRange(ComponentComplete.ID, colliderComponent.getAABB(), new IEntityVisitor() {
			@Override
			public void visit(Entity entity, IEntityComponent component) {
				if (entity.equals(getEntity())) {
					return;
				}

				FlounderLogger.log(entity);
			//	ComponentCollider collider = (ComponentCollider) entity.getComponent(ComponentCollider.ID);

			//	if (collider != null && !colliderComponent.getAABB().intersects(collider.getAABB()).isIntersection()) {
			//		return;
			//	}

				if (entity.getComponent(ComponentPlayer.ID) != null) {
					((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiFinished().setWon();
				}
			}
		});*/

		if (OmegaRoom.getEntityPlayer() != null) {
			if (OmegaRoom.getEntityPlayer().getBounding().intersects(colliderComponent.getAABB()).isIntersection()) {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiFinished().setWon();
				FlounderSound.playSystemSound(SOUND);
			}
		}
	}

	@Override
	public void dispose() {

	}
}
