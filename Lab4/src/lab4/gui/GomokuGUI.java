package lab4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
		frame.setResizable(false);

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
		
		gameGridPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.BUTTON_LEFT==e.CLICK) {
					System.out.println("sads");
				}
			}
			
		});
		
		frame.add(gameGridPanel);
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
