package snake_game;

import javax.swing.JApplet;
import javax.swing.UIManager;

public class GamePanelApplet extends JApplet {

	/**
	 * generated by eclipse
	 */
	private static final long serialVersionUID = 72475026503343759L;

	@Override
	public void init() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// do nothing
		}

		final GamePanel panel = SnakeParameters.getDefaultGamePanel();

		panel.start();
	}

}
