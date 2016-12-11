package omega.entities;

public class EntityIDAssigner {
	private static int currentId = 0;

	public static int getId() {
		return currentId++;
	}
}