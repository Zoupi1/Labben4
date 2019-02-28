package lab4.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.glass.events.MouseEvent;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;

/*
 * The GUI class
 */

public class GomokuGUI implements Observer {

	private GomokuClient client;
	private GomokuGameState gamestate;

	GamePanel gameGridPanel;
	GameGrid grid;
	JButton connectButton;
	JButton newGameButton;
	JButton disconnectButton;
	JLabel messageLabel;
	JFrame frame;
	ConnectionWindow connectionWindow;

	/**
	 * The constructor
	 * 
	 * @param g The game state that the GUI will visualize
	 * @param c The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c) {
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);

		// Frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Gomoku");
		frame.setVisible(true);
		//
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		gameGridPanel.setVisible(true);

		// Buttons & Labels
		messageLabel = new JLabel();
		connectButton = new JButton("Connect");
		newGameButton = new JButton("New Game");
		disconnectButton = new JButton("Disconnect");
		if (g.getCurrentState() == 0) {// 0 är not started
			newGameButton.setEnabled(false);
			disconnectButton.setEnabled(false);
		}

		// Körs då connectButton trycks
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String inp = arg0.getActionCommand();
				if (inp.equals("Connect")) {
					connectionWindow = new ConnectionWindow(client);
					messageLabel.setText("Trying to connect!");
				}
			}
		});

		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inp = e.getActionCommand();
				if (inp.equals("New Game")) {
					gamestate.newGame();
					messageLabel.setText("New game!");
				}
			}
		});

		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inp = e.getActionCommand();
				if (inp.equals("Disconnect")) {
					gamestate.disconnect();
					messageLabel.setText("Disconnecting!");
				}
			}
		});

		gameGridPanel.addMouseListener(new MouseListener() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				g.move(e.getX() / gameGridPanel.getUNIT_SIZE(), e.getY() / gameGridPanel.getUNIT_SIZE());
			}

			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		JPanel panel = new JPanel(); // Allt 
		Box b = Box.createVerticalBox();
		b.add(gameGridPanel);
		Box b2 = Box.createHorizontalBox();
		b2.add(connectButton);
		b2.add(disconnectButton);
		b2.add(newGameButton);
		b.add(b2);
		messageLabel.setText("Welcome To Gomoku! Press New Game To Get Started!");
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.add(messageLabel);
		
		panel.add(b);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Min size på fönstret
		frame.setMinimumSize(new Dimension(g.getGameGrid().getSize() * gameGridPanel.getUNIT_SIZE() + 25,
				g.getGameGrid().getSize() * gameGridPanel.getUNIT_SIZE() + 100));

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				Dimension size = frame.getSize();
				Dimension min = frame.getMinimumSize();
				if (size.getWidth() < min.getWidth()) {
					frame.setSize((int) min.getWidth(), (int) size.getHeight());
					gameGridPanel.repaint();
				}
				if (size.getHeight() < min.getHeight()) {
					frame.setSize((int) size.getWidth(), (int) min.getHeight());
					gameGridPanel.repaint();

				}
			}
		});

		frame.add(panel);
		frame.pack();

	}

	public void update(Observable arg0, Object arg1) {

		// Update the buttons if the connection status has changed
		if (arg0 == client) {
			if (client.getConnectionStatus() == GomokuClient.UNCONNECTED) {
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			} else {
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}

		// Update the status text if the gamestate has changed
		if (arg0 == gamestate) {
			messageLabel.setText(gamestate.getMessageString());
		}
	}
}
