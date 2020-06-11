package snake_game;

public class SnakeSquare {
	// giving package control over these variables
	boolean occupied = false;
	boolean apple = false;

	public final int i, j;

	SnakeSquare(final int i, final int j) {
		this.i = i;
		this.j = j;
	}

	public boolean apple() {
		return apple;
	}

	public boolean occupied() {
		return occupied;
	}
}
