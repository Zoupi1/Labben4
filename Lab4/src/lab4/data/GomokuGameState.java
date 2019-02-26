/*
 * Created on 2007 feb 8
 */
package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 */

public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private static GameGrid gameGrid;

	// Possible game states
	private final int NOT_STARTED = 0;
	private final int MY_TURN = 1;
	private final int OTHERS_TURN = 2;
	private final int FINISHED = 3;
	private int currentState;

	private GomokuClient client;

	private String message;

	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc) {
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString() {
		return message;
	}

	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {
		return gameGrid;
	}
	
	
	/**
	 * Returns the current state
	 * 
	 * @return the current state
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * This player makes a move at a specified location
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y) {
		if (currentState == NOT_STARTED) {
			message = "Game has not started yet!";
			setChanged();
			notifyObservers();
		} else if (currentState == FINISHED) {
			message = "Game is over!";
			setChanged();
			notifyObservers();
		} else {
			if (currentState == MY_TURN) {
				if (gameGrid.move(x, y, GameGrid.ME)) {
					message = "Move done!";
					client.sendMoveMessage(x, y);
					receivedMove(x, y);
					setChanged();
					notifyObservers();
					currentState = OTHERS_TURN;
					if (gameGrid.isWinner(GameGrid.ME)) {
						message = "YOU WIN!";
						currentState = FINISHED;
						setChanged();
						notifyObservers();
					}
				} else {
					message = "Square is not empty!";
					setChanged();
					notifyObservers();
				}
			} else {
				message = "Not your turn!";
				setChanged();
				notifyObservers();
			}
		}
	}

	/**
	 * Starts a new game with the current client
	 */
	public void newGame() {
		gameGrid.clearGrid();
		currentState = OTHERS_TURN;
		message = "New game! Opponents turn!";
		setChanged();
		notifyObservers();
	}

	/**
	 * Other player has requested a new game, so the game state is changed
	 * accordingly
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		currentState = MY_TURN;
		message = "New game! My turn!";
		setChanged();
		notifyObservers();
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "Your opponent disconnected. Clearing grid...";
		client.disconnect();
		setChanged();
		notifyObservers();
	}

	/**
	 * The player disconnects from the client
	 */
	public void disconnect() {
		gameGrid.clearGrid();
		currentState = NOT_STARTED;
		message = "You have disconnected. Clearing grid...";
		client.disconnect();
		setChanged();
		notifyObservers();
	}

	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		gameGrid.move(x, y, GameGrid.OTHER);
		if (gameGrid.isWinner(GameGrid.OTHER)) {
			message = "OTHER PLAYER WON!";
			currentState = FINISHED;
			setChanged();
			notifyObservers();
		}
	}

	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHERS_TURN;
			break;
		}
		setChanged();
		notifyObservers();

	}

}
