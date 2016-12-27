package omega;

import flounder.camera.*;
import flounder.devices.*;
import flounder.maths.matrices.*;
import flounder.maths.vectors.*;
import flounder.space.*;

public class OmegaCamera extends ICamera {
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 100.0f;
	private static final float FIELD_OF_VIEW = 90.0f;

	private Vector3f reusableViewVector;

	private Vector3f targetPosition;
	private Vector3f targetRotation;

	private Frustum viewFrustum;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;

	public OmegaCamera() {
		super();
	}

	@Override
	public void init() {
		this.reusableViewVector = new Vector3f();

		this.targetPosition = new Vector3f();
		this.targetRotation = new Vector3f();

		this.viewFrustum = new Frustum();
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f();
	}

	@Override
	public float getNearPlane() {
		return NEAR_PLANE;
	}

	@Override
	public float getFarPlane() {
		return FAR_PLANE;
	}

	@Override
	public float getFOV() {
		return FIELD_OF_VIEW;
	}


	@Override
	public void update(IPlayer player) {
		if (player != null) {
			this.targetPosition.set(player.getPosition());
			this.targetRotation.set(player.getRotation());
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public Frustum getViewFrustum() {
		return viewFrustum;
	}

	@Override
	public Matrix4f getViewMatrix() {
		updateViewMatrix();
		return viewMatrix;
	}

	private void updateViewMatrix() {
		viewMatrix.setIdentity();
		targetPosition.negate();
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(1.0f, 0.0f, 0.0f), (float) Math.toRadians(targetRotation.x), viewMatrix);
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(0.0f, 1.0f, 0.0f), (float) Math.toRadians(targetRotation.y), viewMatrix);
		Matrix4f.rotate(viewMatrix, reusableViewVector.set(0.0f, 0.0f, 1.0f), (float) Math.toRadians(targetRotation.z), viewMatrix);
		targetPosition.negate();
		Matrix4f.translate(viewMatrix, targetPosition, viewMatrix);
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		updateProjectionMatrix();
		return projectionMatrix;
	}

	private void updateProjectionMatrix() {
		Matrix4f.perspectiveMatrix(FIELD_OF_VIEW, FlounderDisplay.getAspectRatio(), NEAR_PLANE, FAR_PLANE, projectionMatrix);
	}

	@Override
	public void reflect(float waterHeight) {
		targetPosition.y -= 2.0f * (targetPosition.y - waterHeight);
		targetRotation.x = -targetRotation.x;
		updateViewMatrix();
	}

	@Override
	public Vector3f getPosition() {
		return targetPosition;
	}

	@Override
	public Vector3f getRotation() {
		return targetRotation;
	}

	@Override
	public void setRotation(Vector3f rotation) {
		targetRotation.set(rotation);
	}
}
