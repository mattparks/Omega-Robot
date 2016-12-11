package omega.entities.components;

import flounder.maths.vectors.*;
import flounder.textures.*;
import omega.entities.*;

public class ComponentTexture extends IEntityComponent {
	public static final int ID = EntityIDAssigner.getId();

	private Texture texture;
	private float transparency;
	private float scale;
	private int textureIndex;
	private boolean flipX;
	private boolean flipY;

	public ComponentTexture(Entity entity, Texture texture, float scale, int textureIndex) {
		super(entity, ID);
		this.texture = texture;
		this.transparency = 1.0f;
		this.scale = scale;
		this.textureIndex = textureIndex;
		this.flipX = false;
		this.flipY = false;
	}

	public Vector2f getTextureOffset() {
		int column = textureIndex % texture.getNumberOfRows();
		int row = textureIndex / texture.getNumberOfRows();
		return new Vector2f((float) row / (float) texture.getNumberOfRows(), (float) column / (float) texture.getNumberOfRows());
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setTextureIndex(int index) {
		this.textureIndex = index;
	}

	public boolean isFlipX() {
		return flipX;
	}

	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	public boolean isFlipY() {
		return flipY;
	}

	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}

	@Override
	public void update() {
	}

	@Override
	public void dispose() {
	}
}
