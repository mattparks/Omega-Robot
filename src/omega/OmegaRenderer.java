package omega;

import flounder.camera.*;
import flounder.devices.*;
import flounder.fbos.*;
import flounder.fonts.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.helpers.*;
import flounder.logger.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.physics.bounding.*;
import flounder.post.piplines.*;
import flounder.profiling.*;
import flounder.renderer.*;
import omega.background.*;
import omega.entities.*;

public class OmegaRenderer extends IExtension implements IRendererMaster {
	public static final Colour COLOUR_CLEAR = new Colour(0.0f, 0.0f, 0.0f);
	public static final Vector4f POSITIVE_INFINITY = new Vector4f(0.0f, 1.0f, 0.0f, Float.POSITIVE_INFINITY);

	private FBO multisamplingFBO;
	private FBO nonsampledFBO;

	private EntityRenderer entityRenderer;
	private BackgroundRenderer backgroundRenderer;
	private BoundingRenderer boundingRenderer;
	private GuiRenderer guiRenderer;
	private FontRenderer fontRenderer;

	private PipelinePaused pipelinePaused;

	public OmegaRenderer() {
		super(FlounderLogger.class, FlounderProfiler.class, FlounderDisplay.class, FlounderRenderer.class);
	}

	@Override
	public void init() {
		this.entityRenderer = new EntityRenderer();
		this.backgroundRenderer = new BackgroundRenderer();
		this.boundingRenderer = new BoundingRenderer();
		this.guiRenderer = new GuiRenderer();
		this.fontRenderer = new FontRenderer();

		this.pipelinePaused = new PipelinePaused();

		this.multisamplingFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).antialias(Omega.configMain.getIntWithDefault("msaa_samples", 2, () -> multisamplingFBO.getSamples())).create();
		this.nonsampledFBO = FBO.newFBO(1.0f).depthBuffer(DepthBufferType.TEXTURE).create();
	}

	@Override
	public void render() {
		/* Binds the relevant FBO. */
		bindRelevantFBO();

		/* Scene rendering. */
		renderScene(POSITIVE_INFINITY, COLOUR_CLEAR);

		/* Post rendering. */
		renderPost(FlounderGuis.getGuiMaster().isGamePaused(), FlounderGuis.getGuiMaster().getBlurFactor());

		/* Scene independents. */
		guiRenderer.render(POSITIVE_INFINITY, FlounderCamera.getCamera());
		fontRenderer.render(POSITIVE_INFINITY, FlounderCamera.getCamera());

		/* Unbinds the FBO. */
		unbindRelevantFBO();
	}

	private void bindRelevantFBO() {
		if (FlounderDisplay.isAntialiasing()) {
			multisamplingFBO.bindFrameBuffer();
		} else {
			nonsampledFBO.bindFrameBuffer();
		}
	}

	private void unbindRelevantFBO() {
		if (FlounderDisplay.isAntialiasing()) {
			multisamplingFBO.unbindFrameBuffer();
			multisamplingFBO.resolveFBO(nonsampledFBO);
		} else {
			nonsampledFBO.unbindFrameBuffer();
		}
	}

	private void renderScene(Vector4f clipPlane, Colour clearColour) {
		/* Clear and update. */
		ICamera camera = FlounderCamera.getCamera();
		OpenGlUtils.prepareNewRenderParse(clearColour);

		backgroundRenderer.render(clipPlane, camera);
		entityRenderer.render(clipPlane, camera);
		boundingRenderer.render(clipPlane, camera);
	}

	private void renderPost(boolean isPaused, float blurFactor) {
		FBO output = nonsampledFBO;

		if (isPaused || blurFactor != 0.0f) {
			pipelinePaused.setBlurFactor(blurFactor);
			pipelinePaused.renderPipeline(output);
			output = pipelinePaused.getOutput();
		}

		output.blitToScreen();
	}

	public int getSamples() {
		return multisamplingFBO.getSamples();
	}

	public void setSamples(int samples) {
		multisamplingFBO.setSamples(samples);
	}

	@Override
	public void profile() {

	}

	@Override
	public void dispose() {
		backgroundRenderer.dispose();
		entityRenderer.dispose();
		boundingRenderer.dispose();
		guiRenderer.dispose();
		fontRenderer.dispose();

		multisamplingFBO.delete();
		nonsampledFBO.delete();

		pipelinePaused.dispose();
	}

	public FBO getNonsampledFBO() {
		return nonsampledFBO;
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
