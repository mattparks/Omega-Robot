package omega.room;

import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import omega.entities.*;
import omega.entities.components.*;

public class EntitySlime extends Entity {
	private static Texture texture;

	public EntitySlime(Vector2f position) {
		super(OmegaEntities.getEntities(), position);

		if (texture == null) {
			texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "slime.png")).clampEdges().create();
			texture.setHasTransparency(true);
		}

		new ComponentCollider(this);
		new ComponentCollision(this);
		new ComponentHazard(this);
		new ComponentAI(this);
		new ComponentTexture(this, texture, 0.09f, 0);
		new ComponentRemoveFade(this, 0.5f);
	}
}
