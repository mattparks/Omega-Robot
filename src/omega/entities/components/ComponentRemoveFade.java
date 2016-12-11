package omega.entities.components;

import flounder.framework.*;
import omega.entities.*;

public class ComponentRemoveFade extends ComponentRemove {
	private boolean removesAfterDuration;
	private double timer;
	private double duration;
	private boolean testMode;

	public ComponentRemoveFade(Entity entity, double duration) {
		super(entity);
		timer = 0.0;
		this.duration = duration;
		this.removesAfterDuration = true;
		this.testMode = false;
	}

	@Override
	public void deactivate() {
		timer = 0.0;
	}

	@Override
	public void onActivate() {
	}

	@Override
	public void removeUpdate() {
		timer += FlounderFramework.getDelta();
		double fadeAmount = (duration - timer) / duration;

		ComponentTexture mc = (ComponentTexture) super.getEntity().getComponent(ComponentTexture.ID);

		if (timer >= duration) {
			if (testMode) {
				if (mc != null) {
					fadeAmount = 1.0f;
					timer = 0.0f;
					super.deactivate();
				}
			} else if (removesAfterDuration) {
				getEntity().removeComponent(ComponentCollision.ID);
				getEntity().forceRemove();
			}
		}

		if (mc != null) {
			mc.setTransparency((float) fadeAmount);
		}
	}

	public boolean removesAfterDuration() {
		return removesAfterDuration;
	}

	public void setRemovesAfterDuration(boolean removesAfterDuration) {
		this.removesAfterDuration = removesAfterDuration;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Override
	public void dispose() {
	}
}