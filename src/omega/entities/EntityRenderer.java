package omega.entities;

import flounder.camera.*;
import flounder.devices.*;
import flounder.helpers.*;
import flounder.loaders.*;
import flounder.maths.vectors.*;
import flounder.profiling.*;
import flounder.renderer.*;
import flounder.resources.*;
import flounder.shaders.*;
import flounder.textures.*;
import omega.entities.components.*;
import omega.room.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class EntityRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "entities", "entityFragment.glsl");

	private static final float POSITION_MIN = 0.0f;
	private static final float POSITION_MAX = 1.0f;
	private static final float[] POSITIONS = {POSITION_MIN, POSITION_MIN, POSITION_MIN, POSITION_MAX, POSITION_MAX, POSITION_MIN, POSITION_MAX, POSITION_MAX};

	private Shader shader;
	private int vaoID;
	private Texture lightMap;

	public EntityRenderer() {
		shader = Shader.newShader("entities").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
		vaoID = FlounderLoader.createInterleavedVAO(POSITIONS, 2);
		lightMap = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "lightmap.png")).clampEdges().create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || OmegaEntities.getEntities() == null || OmegaRoom.getEntityPlayer() == null || OmegaRoom.CANCEL_RENDERS) {
			return;
		}

		prepareRendering(clipPlane, camera);

		int rendered = 0;

		for (Entity entity : OmegaEntities.getEntities().getAll(new ArrayList<>())) {
			float minX = camera.getPosition().x - entity.getPosition().x - entity.getBounding().getMinExtents().x;
			float maxX = camera.getPosition().x - entity.getPosition().x + entity.getBounding().getMaxExtents().x;
			float minY = camera.getPosition().y - entity.getPosition().y - entity.getBounding().getMinExtents().y;
			float maxY = camera.getPosition().y - entity.getPosition().y + entity.getBounding().getMaxExtents().y;
			//	if (maxX >= 0.0f || maxY >= 0.0f) {
			renderEntity(entity);
			rendered++;
			//	}
		}

		FlounderProfiler.add(OmegaEntities.PROFILE_TAB_NAME, "Rendered", rendered);

		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());
		OpenGlUtils.disableDepthTesting();
		OpenGlUtils.enableAlphaBlending();

		shader.getUniformFloat("aspectRatio").loadFloat(FlounderDisplay.getAspectRatio());
		shader.getUniformBool("polygonMode").loadBoolean(OpenGlUtils.isInWireframe());
		shader.getUniformVec2("worldSize").loadVec2(OmegaRoom.roomWidth, OmegaRoom.roomHeight);
		shader.getUniformVec2("cameraPosition").loadVec2(FlounderCamera.getCamera().getPosition().x, FlounderCamera.getCamera().getPosition().y);
		shader.getUniformVec2("playerPosition").loadVec2(OmegaRoom.getEntityPlayer().getPosition());
	}

	private void renderEntity(Entity entity) {
		ComponentTexture componentTexture = (ComponentTexture) entity.getComponent(ComponentTexture.ID);

		if (componentTexture == null || componentTexture.getTexture() == null) {
			return;
		}

		OpenGlUtils.bindVAO(vaoID, 0);
		OpenGlUtils.cullBackFaces(true); // Enable face culling if the object does not have transparency.

		OpenGlUtils.bindTexture(componentTexture.getTexture(), 0);
		OpenGlUtils.bindTexture(lightMap, 1);

		shader.getUniformBool("flipX").loadBoolean(componentTexture.isFlipX());
		shader.getUniformBool("flipY").loadBoolean(componentTexture.isFlipY());
		shader.getUniformVec4("transform").loadVec4(
				entity.getPosition().x,
				entity.getPosition().y,
				componentTexture.getScale(), componentTexture.getScale()
		);
		shader.getUniformFloat("alpha").loadFloat(componentTexture.getTransparency());

		if (componentTexture.getTransparency() != 1.0 || componentTexture.getTexture().hasTransparency()) {
			OpenGlUtils.cullBackFaces(false); // Disable face culling if the object has transparency.
		}

		shader.getUniformFloat("atlasRows").loadFloat(componentTexture.getTexture().getNumberOfRows());
		shader.getUniformVec2("atlasOffset").loadVec2(componentTexture.getTextureOffset());

		glDrawArrays(GL_TRIANGLE_STRIP, 0, POSITIONS.length / 2);
		OpenGlUtils.unbindVAO(0);
	}


	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderProfiler.add(OmegaEntities.PROFILE_TAB_NAME, "Render Time", super.getRenderTime());
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
