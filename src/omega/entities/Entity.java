package omega.entities;

import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.space.*;
import omega.entities.components.*;

import java.util.*;

public class Entity implements ISpatialObject {
	private ISpatialStructure<Entity> structure;
	private List<IEntityComponent> components;
	private Vector2f position;
	private boolean hasMoved;
	private boolean isRemoved;

	public Entity(ISpatialStructure<Entity> structure, Vector2f position) {
		this.structure = structure;
		this.components = new ArrayList<>();
		this.position = position;
		this.hasMoved = true;
		this.isRemoved = false;
		this.structure.add(this);
	}

	public void addComponent(IEntityComponent component) {
		components.add(component);
	}

	public void removeComponent(IEntityComponent component) {
		components.remove(component);
		component.dispose();
	}

	public List<IEntityComponent> getComponents() {
		return components;
	}

	public void removeComponent(int id) {
		for (IEntityComponent c : components) {
			if (c.getId() == id) {
				c.dispose();
				components.remove(c);
				return;
			}
		}
	}

	public void visitInRange(int id, AABB range, IEntityVisitor visitor) {
		for (Entity entity : structure.queryInBounding(new ArrayList<>(), range)) {
			if (entity.isRemoved) {
				continue;
			}

			IEntityComponent component = id == -1 ? null : entity.getComponent(id);

			if (component != null || id == -1) {
				visitor.visit(entity, component);
			}
		}
	}

	public IEntityComponent getComponent(int id) {
		for (IEntityComponent component : components) {
			if (component.getId() == id) {
				return component;
			}
		}

		return null;
	}

	public void update() {
		try {
			components.forEach(IEntityComponent::update);
		} catch (ConcurrentModificationException e) {
			FlounderLogger.exception(e);
		}

		hasMoved = false;
	}

	public void move(Vector2f move) {
		if (move.x == 0.0f && move.y == 0.0f) {
			return;
		}

		structure.remove(this);
		hasMoved = true;
		float moveAmountX = move.getX();
		float moveAmountY = move.getY();

		ComponentCollision collision = (ComponentCollision) getComponent(ComponentCollision.ID);

		if (collision != null) {
			Vector2f amounts = collision.resolveAABBCollisions(new Vector2f(moveAmountX, moveAmountY));
			moveAmountX = amounts.getX();
			moveAmountY = amounts.getY();
		}

		position.set(position.getX() + moveAmountX, position.getY() + moveAmountY);
		structure.add(this);
	}

	public void switchStructure(ISpatialStructure<Entity> structure) {
		structure.remove(this);
		this.structure = structure;
		structure.add(this);
	}

	public ISpatialStructure<Entity> getStructure() {
		return structure;
	}

	public void remove() {
		if (isRemoved) {
			return;
		}

		isRemoved = true;
		ComponentRemove removeComponent = (ComponentRemove) getComponent(ComponentRemove.ID);

		if (removeComponent != null) {
			removeComponent.activate();
		} else {
			forceRemove();
		}
	}

	public void forceRemove() {
		isRemoved = true;
		structure.remove(this);

		for (IEntityComponent component : components) {
			component.dispose();
		}
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public AABB getBounding() {
		ComponentCollider ac = (ComponentCollider) getComponent(ComponentCollider.ID);

		if (ac != null) {
			return ac.getAABB();
		} else {
			return null;
		}
	}
}