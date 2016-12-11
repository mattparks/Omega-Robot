package omega.entities;

import flounder.framework.*;
import flounder.guis.*;
import flounder.logger.*;
import flounder.models.*;
import flounder.physics.bounding.*;
import flounder.profiling.*;
import flounder.shaders.*;
import flounder.space.*;
import flounder.textures.*;
import omega.room.*;

import java.util.*;

public class OmegaEntities extends IModule {
	private static final OmegaEntities instance = new OmegaEntities();

	private StructureBasic<Entity> entityStructure;

	public OmegaEntities() {
		super(ModuleUpdate.UPDATE_PRE, FlounderLogger.class, FlounderProfiler.class, FlounderModels.class, FlounderBounding.class, FlounderShaders.class, FlounderTextures.class);
	}

	@Override
	public void init() {
		this.entityStructure = new StructureBasic<>();
	}

	@Override
	public void update() {
		if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
			return;
		}

		if (entityStructure != null && !OmegaRoom.CANCEL_RENDERS) {
			entityStructure.getAll(new ArrayList<>()).forEach(Entity::update);
		}
	}

	@Override
	public void profile() {
		FlounderProfiler.add("Entities", "Count", entityStructure.getSize());
	}

	public static void clear() {
		instance.entityStructure.getAll(new ArrayList<>()).forEach(Entity::forceRemove);
	}

	public static StructureBasic<Entity> getEntities() {
		return instance.entityStructure;
	}

	@Override
	public IModule getInstance() {
		return instance;
	}

	@Override
	public void dispose() {
		if (entityStructure != null) {
			entityStructure.clear();
			entityStructure = null;
		}
	}
}