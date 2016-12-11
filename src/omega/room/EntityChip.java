package omega.room;

import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import omega.entities.*;
import omega.entities.components.*;

public class EntityChip extends Entity {
	private static Texture texture;

	public EntityChip(Vector2f position) {
		super(OmegaEntities.getEntities(), position);

		if (texture == null) {
			texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "chip.png")).clampEdges().create();
			texture.setHasTransparency(true);
		}

		new ComponentCollider(this);
		new ComponentRemoveFade(this, 0.5f);
		new ComponentComplete(this);
		new ComponentTexture(this, texture, 0.1f, 0);
	}
}
