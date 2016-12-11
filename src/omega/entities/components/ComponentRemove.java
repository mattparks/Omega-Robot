package omega.entities.components;

import omega.entities.*;

public abstract class ComponentRemove extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private boolean activated;

	public ComponentRemove(Entity entity) {
		super(entity, ID);
		activated = false;
	}

	public void activate() {
		activated = true;
		onActivate();
	}

	public void deactivate() {
		activated = false;
	}

	public abstract void onActivate();

	@Override
	public void update() {
		if (!activated) {
			return;
		}

		removeUpdate();
	}

	public abstract void removeUpdate();
}
