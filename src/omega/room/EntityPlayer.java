package omega.room;

import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import omega.entities.*;
import omega.entities.components.*;

public class EntityPlayer extends Entity {
	private static Texture texture;

	public EntityPlayer(Vector2f position) {
		super(OmegaEntities.getEntities(), position);

		if (texture == null) {
			texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "player.png")).clampEdges().create();
			texture.setNumberOfRows(2);
			texture.setHasTransparency(true);
		}

		new ComponentCollider(this);
		new ComponentCollision(this);
		new ComponentTexture(this, texture, 0.09f, 0);
		new ComponentPlayer(this);
	}
}
