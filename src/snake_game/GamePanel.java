package snake_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import util.Strobable;
import util.Strobe;

public class GamePanel extends JFrame implements Strobable, ActionListener {

	/**
	 * generated by eclipse
	 */
	private static final long serialVersionUID = -1760024189809488571L;

	private static final double FPS = 5.0;

	private final SnakeGame game;
	private final JLabel score = new JLabel("Score: 1");
	private final JLabel time = new JLabel("Time: 0.0");
	private final Strobe strobe;
	private final JPanel gameArea = new JPanel();
	private final JMenuBar menuBar;

	// private final HighScoreFrame hsFrame = new HighScoreFrame();

	public GamePanel(final SnakeGame game, final int padding, final Color bg) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.game = game;

		// game.addGamePanel(this);

		score.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		time.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
		score.setForeground(Color.WHITE);
		time.setForeground(Color.WHITE);

		gameArea.setBackground(bg);
		setupPanel(padding);
		this.setContentPane(gameArea);
		this.strobe = new Strobe(this, FPS);

		menuBar = new JMenuBar();
		setupMenuBar();
		this.setJMenuBar(menuBar);
	}

	private void setupMenuBar() {
		final JMenu gameMenu = new JMenu("Game");

		menuBar.add(gameMenu);

		final JMenuItem newGame = new JMenuItem("New Game");
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
		newGame.setActionCommand("n");
		newGame.addActionListener(this);
		gameMenu.add(newGame);

		final JMenuItem hsMenu = new JMenuItem("High Scores");
		hsMenu.setActionCommand("h");
		hsMenu.addActionListener(this);
		gameMenu.add(hsMenu);
	}

	private void setupPanel(int padding) {
		final GridBagConstraints gbc = new GridBagConstraints();
		gameArea.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.insets = new Insets(padding, padding, padding, padding);

		gameArea.add(game, gbc);

		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridy++;
		gbc.gridwidth = 1;

		gameArea.add(score, gbc);

		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx++;

		gameArea.add(time, gbc);
	}

	public void start() {
		this.setVisible(true);
		this.pack();
		strobe.start();
		game.start();
	}

	private void newGame() {
		game.reset();
	}

	@Override
	public void strobe() {
		// System.out.println(game.getLength());
		score.setText("Length: " + game.getLength());
		time.setText("Time: " + game.getTime());
		gameArea.validate();
	}

	/*
	 * public void showHighScores() { hsFrame.updateHighScores();
	 * hsFrame.setLocationRelativeTo(this); hsFrame.setVisible(true);
	 * hsFrame.validate(); hsFrame.pack(); }
	 */

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch (ae.getActionCommand()) {
		case "n":
			newGame();
			break;
		case "h":
			// showHighScores();
			break;
		}
	}

	public static void main(String... args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// do nothing
		}

		final GamePanel panel = SnakeParameters.getDefaultGamePanel();

		panel.start();
	}
}
