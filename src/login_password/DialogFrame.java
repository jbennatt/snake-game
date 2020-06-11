package login_password;

import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class DialogFrame implements ActionListener {

	private final JLabel instLabel;
	private final JTextField input;
	private final JFrame frame = new JFrame();
	private final GridBagConstraints gbc = new GridBagConstraints();
	private final JButton okButton = new JButton("OK");

	private JFrame parent;

	/**
	 * Creates a dialog with the given instructions and then whether or not it's
	 * a password field (i.e. display typed text as *'s).
	 * 
	 * @param textQuery
	 *            instructions displayed to user
	 * @param pw
	 *            whether or not this is a password field, <code>true</code> if
	 *            it is, and <code>false</code> if the typed text should display
	 *            normally.
	 */
	public DialogFrame(final String inst, final boolean pw) {
		instLabel = new JLabel(inst);
		input = pw ? new JPasswordField(20) : new JTextField(20);
		instLabel.setLabelFor(input);

		input.addActionListener(this);
		okButton.addActionListener(this);
		this.parent = null;

		setupFrame();
	}

	public void setParent(final JFrame parent) {
		this.parent = parent;
	}

	private void setupFrame() {
		frame.setLayout(new GridBagLayout());

		// setup grid bag constraints.
		gbc.gridx = 0;
		gbc.gridy = 0;

		// add instruction label at the top of frame
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(instLabel, gbc);

		// add text field in second row
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		frame.add(input, gbc);

		// add OK Button to the right of text field
		gbc.gridx++;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		frame.add(okButton, gbc);
	}

	public synchronized String getAnswer() {
		input.setText("");

		if (parent != null)
			frame.setLocationRelativeTo(parent);

		frame.setVisible(true);
		frame.pack();

		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		frame.setVisible(false);

		return input.getText();
	}

	@Override
	public synchronized void actionPerformed(ActionEvent arg0) {
		notify();
	}
}
