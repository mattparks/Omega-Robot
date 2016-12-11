package omega.entities.components;

import omega.entities.*;

public interface IEntityVisitor {
	void visit(Entity entity, IEntityComponent component);
}
