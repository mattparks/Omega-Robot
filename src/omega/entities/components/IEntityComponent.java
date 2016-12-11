package omega.entities.components;

import omega.entities.*;

public abstract class IEntityComponent {
	private Entity entity;
	private int id;

	public IEntityComponent(Entity entity, int id) {
		this.entity = entity;
		this.id = id;

		if (entity != null) {
			entity.addComponent(this);
		}
	}

	public int getId() {
		return id;
	}

	public Entity getEntity() {
		return entity;
	}

	public abstract void update();

	public abstract void dispose();
}