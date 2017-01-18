package omega.background;

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
import omega.*;
import omega.guis.*;
import omega.room.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class BackgroundRenderer extends IRenderer {
	private static final MyFile VERTEX_SHADER = new MyFile(Shader.SHADERS_LOC, "background", "backgroundVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile(Shader.SHADERS_LOC, "background", "backgroundFragment.glsl");

	private static final float POSITION_MIN = 0.0f;
	private static final float POSITION_MAX = 1.0f;
	private static final float[] POSITIONS = {POSITION_MIN, POSITION_MIN, POSITION_MIN, POSITION_MAX, POSITION_MAX, POSITION_MIN, POSITION_MAX, POSITION_MAX};

	private Shader shader;
	private int vaoID;

	private Texture background;
	private Texture lightMap;

	public BackgroundRenderer() {
		shader = Shader.newShader("overlay").setShaderTypes(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_SHADER),
				new ShaderType(GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
		).create();
		vaoID = FlounderLoader.createInterleavedVAO(POSITIONS, 2);
		background = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "background.png")).create();
		lightMap = Texture.newTexture(new MyFile(MyFile.RES_FOLDER, "lightmap.png")).clampEdges().create();
	}

	@Override
	public void renderObjects(Vector4f clipPlane, ICamera camera) {
		if (!shader.isLoaded() || OmegaRoom.getEntityPlayer() == null) {
			return;
		}

		prepareRendering(clipPlane, camera);
		renderBackground();
		endRendering();
	}

	private void prepareRendering(Vector4f clipPlane, ICamera camera) {
		shader.start();
		OpenGlUtils.antialias(FlounderDisplay.isAntialiasing());
		OpenGlUtils.disableDepthTesting();
		OpenGlUtils.enableAlphaBlending();

		shader.getUniformFloat("aspectRatio").loadFloat(FlounderDisplay.getAspectRatio());
		shader.getUniformVec2("worldSize").loadVec2(OmegaRoom.roomWidth, OmegaRoom.roomHeight);
		shader.getUniformVec2("cameraPosition").loadVec2(FlounderCamera.getCamera().getPosition().x, FlounderCamera.getCamera().getPosition().y);
		shader.getUniformVec2("playerPosition").loadVec2(OmegaRoom.getEntityPlayer().getPosition());
	}

	private void renderBackground() {
		if (background == null || OmegaGuis.ON_START || GuiFinished.RESTING) {
			return;
		}

		OpenGlUtils.bindVAO(vaoID, 0);
		OpenGlUtils.cullBackFaces(false); // Enable face culling if the object does not have transparency.

		OpenGlUtils.bindTexture(background, 0);
		OpenGlUtils.bindTexture(lightMap, 1);

		shader.getUniformVec4("transform").loadVec4(0.0f, 0.0f, FlounderDisplay.getAspectRatio(), 1.0f);

		if (OmegaRoom.getEntityPlayer() != null) {
			shader.getUniformVec2("offset").loadVec2(OmegaRoom.getEntityPlayer().getPosition().x / OmegaRoom.roomWidth, OmegaRoom.getEntityPlayer().getPosition().y / OmegaRoom.roomHeight);
		} else {
			shader.getUniformVec2("offset").loadVec2(0.0f, 0.0f);
		}

		glDrawArrays(GL_TRIANGLE_STRIP, 0, POSITIONS.length / 2);
		OpenGlUtils.unbindVAO(0);
	}

	private void endRendering() {
		shader.stop();
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Overlay", "Render Time", super.getRenderTime());
	}

	@Override
	public void dispose() {
		shader.dispose();
	}
}
