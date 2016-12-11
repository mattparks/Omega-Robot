package omega.room;

import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.textures.*;
import omega.entities.*;
import omega.entities.components.*;

public class EntityTerrain extends Entity {
	private static Texture texture;

	public EntityTerrain(Vector2f position, TerrainType type) {
		super(OmegaEntities.getEntities(), position);

		if (texture == null) {
			texture = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "terrain.png")).clampEdges().create();
			texture.setNumberOfRows(3);
			texture.setHasTransparency(true);
		}

		new ComponentCollider(this);
		new ComponentCollision(this);
		new ComponentTexture(this, texture, 0.1f, type.index);

		((ComponentTexture) getComponent(ComponentTexture.ID)).setFlipX(type.flipX);
		((ComponentTexture) getComponent(ComponentTexture.ID)).setFlipY(type.flipY);
	}

	public enum TerrainType {
		MIDDLE(0, false, false),
		TOP(6, false, false),
		BOTTOM(6, false, true),
		LEFT(3, true, false),
		RIGHT(3, false, false),
		LEFT_UP(1, true, false),
		LEFT_DOWN(1, true, true),
		RIGHT_UP(1, false, false),
		RIGHT_DOWN(1, false, true),
		TOP_CORNER(5, false, true),
		BOTTOM_CORNER(5, false, false),
		LEFT_CORNER(4, true, false),
		RIGHT_CORNER(4, false, false);

		public final int index;
		public final boolean flipX;
		public final boolean flipY;

		TerrainType(int index, boolean flipX, boolean flipY) {
			this.index = index;
			this.flipX = flipX;
			this.flipY = flipY;
		}
	}
}
