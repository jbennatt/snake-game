package snake_game;

import java.awt.Color;

public class SnakeParameters {
	public static final int WIDTH = 37;// number of tiles
	public static final int HEIGHT = 26;// number of tiles
	public static final int SNAKE_WIDTH = 18;// pixels
	public static final int SNAKE_PADDING = 1;// pixels

	public static final int X0 = 1;
	public static final int Y0 = 1;

	public static final double DEF_SNAKE_SPEED = 15.0;// 15 advances per second

	public static final int DL = 3;

	public static final Color PANEL_BG = Color.decode("#16324C");
	public static final int PANEL_PADDING = 20;

	public static SnakeGame getDefaultGame() {
		return new SnakeGame(WIDTH, HEIGHT, X0, Y0, DL, DEF_SNAKE_SPEED, SNAKE_WIDTH, SNAKE_PADDING);
	}

	public static GamePanel getDefaultGamePanel() {
		return new GamePanel(getDefaultGame(), PANEL_PADDING, PANEL_BG);
	}
}
