package omega.entities.components;

import flounder.devices.*;
import flounder.guis.*;
import flounder.resources.*;
import flounder.sounds.*;
import omega.*;
import omega.entities.*;
import omega.room.*;

public class ComponentHazard extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();
	public static final Sound SOUND = Sound.loadSoundNow(new MyFile(MyFile.RES_FOLDER, "sounds", "live-remove.wav"), 1.0f);

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
				FlounderSound.playSystemSound(SOUND);
			}
		}
	}

	@Override
	public void dispose() {

	}
}
