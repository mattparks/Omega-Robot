package omega.room;

import flounder.camera.*;
import flounder.guis.*;
import flounder.logger.*;
import flounder.maths.vectors.*;
import flounder.resources.*;
import flounder.standard.*;
import omega.entities.*;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class OmegaRoom extends IStandard {
	private static final MyFile FILE_ROOM = new MyFile(MyFile.RES_FOLDER, "room.png");

	public static boolean CANCEL_RENDERS = true;

	private static EntityPlayer entityPlayer;
	private static List<EntityTerrain> entityTerrains;

	public static float roomWidth;
	public static float roomHeight;

	public OmegaRoom() {
		super(FlounderCamera.class, OmegaEntities.class);
	}

	@Override
	public void init() {
		/*FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_R);

			@Override
			public boolean eventTriggered() {
				if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
					return false;
				}
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiFinished().setWon();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_DOWN);

			@Override
			public boolean eventTriggered() {
				if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
					return false;
				}
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().removeLife();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_UP);

			@Override
			public boolean eventTriggered() {
				if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
					return false;
				}
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiOverlay().addLife();
			}
		});

		FlounderEvents.addEvent(new IEvent() {
			private KeyButton button = new KeyButton(GLFW_KEY_LEFT);

			@Override
			public boolean eventTriggered() {
				if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
					return false;
				}
				return button.wasDown();
			}

			@Override
			public void onEvent() {
				((OmegaGuis) FlounderGuis.getGuiMaster()).getGuiFinished().setWon();
			}
		});*/

		entityTerrains = new ArrayList<>();

		// loadRoom();

		//	new EntityTerrain(new Vector2f(0.5f, 0.5f));
		//	new EntityTerrain(new Vector2f(0.5f + 0.2f, 0.5f));

		/*for (int i = 0; i < 100; i++) {
			for (int w = 0; w < 100; w++) {
				new EntityTerrain(new Vector2f(0.5f + (i * 0.2f), 0.5f + (w * 0.2f)));
			}
		}*/
	}

	private static void loadRoom() {
		CANCEL_RENDERS = true;
		OmegaEntities.clear();
		entityTerrains.clear();

		try {
			BufferedImage image = ImageIO.read(FILE_ROOM.getInputStream());
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			int[] imagePixels = new int[imageWidth * imageHeight];
			image.getRGB(0, 0, imageWidth, imageHeight, imagePixels, 0, imageWidth);

			float minX = 0.0f, maxX = 0.0f;
			float minY = 0.0f, maxY = 0.0f;

			for (int y = 0; y < imageHeight; y++) {
				for (int x = 0; x < imageWidth; x++) {
					int pixel = imagePixels[y * imageWidth + x];
					int r = (pixel) & 0xFF;
					int g = (pixel >> 8) & 0xFF;
					int b = (pixel >> 16) & 0xFF;
					int a = (pixel >> 24) & 0xFF;

					float entityX = 0.5f + (x * 0.1f) - (imageWidth * 0.1f / 2.0f);
					float entityY = 0.5f + (y * 0.1f) - (imageHeight * 0.1f / 2.0f);

					if (r == 0 && g == 0 && b == 0 && a == 255) {
						boolean above = false;
						boolean below = false;
						boolean left = false;
						boolean right = false;

						if (y + 1 < imageHeight) {
							int rgbae = imagePixels[(y + 1) * imageWidth + x];
							above = ((rgbae) & 0xFF) == 0 && ((rgbae >> 8) & 0xFF) == 0 && ((rgbae >> 16) & 0xFF) == 0 && ((rgbae >> 24) & 0xFF) == 255;
						}

						if (y - 1 >= 0) {
							int rgbae = imagePixels[(y - 1) * imageWidth + x];
							below = ((rgbae) & 0xFF) == 0 && ((rgbae >> 8) & 0xFF) == 0 && ((rgbae >> 16) & 0xFF) == 0 && ((rgbae >> 24) & 0xFF) == 255;
						}

						if (x - 1 >= 0) {
							int rgbae = imagePixels[y * imageWidth + (x - 1)];
							left = ((rgbae) & 0xFF) == 0 && ((rgbae >> 8) & 0xFF) == 0 && ((rgbae >> 16) & 0xFF) == 0 && ((rgbae >> 24) & 0xFF) == 255;
						}

						if (x + 1 < imageWidth) {
							int rgbae = imagePixels[y * imageWidth + (x + 1)];
							right = ((rgbae) & 0xFF) == 0 && ((rgbae >> 8) & 0xFF) == 0 && ((rgbae >> 16) & 0xFF) == 0 && ((rgbae >> 24) & 0xFF) == 255;
						}

						EntityTerrain.TerrainType type = EntityTerrain.TerrainType.MIDDLE;

						if (left && above && right && below) {
							type = EntityTerrain.TerrainType.MIDDLE;
						} else if (left && !above && right) {
							type = EntityTerrain.TerrainType.BOTTOM;
						} else if (left && !below && right) {
							type = EntityTerrain.TerrainType.TOP;
						} else if (left && below && above && !right) {
							type = EntityTerrain.TerrainType.RIGHT;
						} else if (!left && below && above && right) {
							type = EntityTerrain.TerrainType.LEFT;
						} else if (left && !below && above && !right) {
							type = EntityTerrain.TerrainType.RIGHT_UP;
						} else if (!left && below && !above && right) {
							type = EntityTerrain.TerrainType.LEFT_DOWN;
						} else if (!left && !below && above && right) {
							type = EntityTerrain.TerrainType.LEFT_UP;
						} else if (left && below && !above && !right) {
							type = EntityTerrain.TerrainType.RIGHT_DOWN;
						} else if (left && !below && !above && !right) {
							type = EntityTerrain.TerrainType.RIGHT_CORNER;
						} else if (!left && !below && !above && right) {
							type = EntityTerrain.TerrainType.LEFT_CORNER;
						} else if (!left && below && !above && !right) {
							type = EntityTerrain.TerrainType.BOTTOM_CORNER;
						} else if (!left && !below && above && !right) {
							type = EntityTerrain.TerrainType.TOP_CORNER;
						}

						Entity e = new EntityTerrain(new Vector2f(entityX, entityY), type);
						if (e.getPosition().getX() > maxX) {
							maxX = e.getPosition().getX();
						} else if (e.getPosition().getX() < minX) {
							minX = e.getPosition().getX();
						}

						if (e.getPosition().getY() > maxY) {
							maxY = e.getPosition().getY();
						} else if (e.getPosition().getY() < minY) {
							minY = e.getPosition().getY();
						}
						entityTerrains.add((EntityTerrain) e);
					} else if (r != 255) {
						Entity e = new EntityChip(new Vector2f(entityX, entityY));
						if (e.getPosition().getX() > maxX) {
							maxX = e.getPosition().getX();
						} else if (e.getPosition().getX() < minX) {
							minX = e.getPosition().getX();
						}

						if (e.getPosition().getY() > maxY) {
							maxY = e.getPosition().getY();
						} else if (e.getPosition().getY() < minY) {
							minY = e.getPosition().getY();
						}
					} else if (b != 255) {
						Entity e = new EntitySlime(new Vector2f(entityX, entityY));
						if (e.getPosition().getX() > maxX) {
							maxX = e.getPosition().getX();
						} else if (e.getPosition().getX() < minX) {
							minX = e.getPosition().getX();
						}

						if (e.getPosition().getY() > maxY) {
							maxY = e.getPosition().getY();
						} else if (e.getPosition().getY() < minY) {
							minY = e.getPosition().getY();
						}
					} else if (r == 255 && g == 255 && b == 255 && a == 255) {
						entityPlayer = new EntityPlayer(new Vector2f(entityX, entityY));
						if (entityPlayer.getPosition().getX() > maxX) {
							maxX = entityPlayer.getPosition().getX();
						} else if (entityPlayer.getPosition().getX() < minX) {
							minX = entityPlayer.getPosition().getX();
						}

						if (entityPlayer.getPosition().getY() > maxY) {
							maxY = entityPlayer.getPosition().getY();
						} else if (entityPlayer.getPosition().getY() < minY) {
							minY = entityPlayer.getPosition().getY();
						}
					}
				}
			}


			roomWidth = maxX - minX;//0.5f + (1 * 0.1f) - (width * 0.1f / 2.0f);
			roomHeight = maxY - minY;//0.5f + (1 * 0.1f) - (imageHeight * 0.1f / 2.0f);
			//	roomWidth = 0.5f + (1 * 0.1f) - (imageWidth * 0.1f / 2.0f);
			//	roomHeight = 0.5f + (1 * 0.1f) - (imageHeight * 0.1f / 2.0f);
			FlounderLogger.log(roomWidth + ", " + roomHeight);
		} catch (IOException e) {
			FlounderLogger.exception(e);
		}

		CANCEL_RENDERS = false;
	}

	@Override
	public void update() {
		if (FlounderGuis.getGuiMaster() == null || FlounderGuis.getGuiMaster().isGamePaused()) {
			return;
		}
	}

	@Override
	public void profile() {

	}

	public static EntityPlayer getEntityPlayer() {
		return entityPlayer;
	}

	public static List<EntityTerrain> getEntityTerrains() {
		return entityTerrains;
	}

	public static void reset() {
		loadRoom();
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean isActive() {
		return true;
	}
}
