package lab4.data;

import java.util.Observable;

import javax.print.DocFlavor.STRING;

/**
 * Represents the 2-d game grid
 */

public class GameGrid extends Observable {

	private static int[][] array;
	public static final int EMPTY = 0;
	public static final int ME = 1;
	public static final int OTHER = 2;
	private static final int INROW = 5;

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size) {
		array = new int[size][size];

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				array[i][j] = EMPTY;
			}
		}
		array[1][0] = 1;
		array[2][0] = 1;
	}

	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return array[x][y];
	}

	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return array.length;
	}

	/**
	 * Enters a move in the game grid
	 * 
	 * @param x      the x position
	 * @param y      the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		if (getLocation(x, y) != EMPTY) {
			return false;
		}
		array[x][y] = player;
		setChanged();
		notifyObservers();
		return true;
	}

	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length; j++) {
				array[i][j] = EMPTY;
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Check if a player has <code>INROW</code> in row
	 * 
	 * @param player the player to check for
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player) {
		int count = 0;
		int count2 = 0;
		// verticalt & horizontalt
		for (int j = 0; j < getSize(); j++) {
			for (int i = 0; i < getSize(); i++) {
				if (array[j][i] == player)
					count++;
				else
					count = 0;
				if (count >= INROW) {
					System.out.println("1");
					return true;
				}
					
				if (array[i][j] == player)
					count2++;
				else
					count2 = 0;
				if (count2 >= INROW){
					System.out.println("2");
					return true;
				}
			}
			count = 0;
			count2 = 0;
		}

		// diagonalt

		int delta = 0;
		for (int j = 0; j < getSize(); j++) {
			for (int i = 0; i < getSize(); i++) {
				if (array[j][i] == player) {
					while (j + delta <= getSize() - 1 && i + delta <= getSize() - 1) {
						if (array[j + delta][i + delta] == player) {
							count++;
							delta++;
							
						} else {
							count = 0;
							delta++;
							System.out.println(delta);
						}
						if (count >= INROW) {
							System.out.println("3");
							return true;
						}
					}
					
					//reset
					delta = 0;
					count=0;
					while (j + delta <= getSize() - 1  && i - delta >=0) {
						System.out.println("test");
						if (array[j + delta][i - delta] == player) {
							count++;
							delta++;
							System.out.println(delta);
						} else {
							count = 0;
							delta++;
							System.out.println(delta);
						}
						if (count >= INROW) {
							System.out.println("4");
							return true;
						}
					}
					delta = 0;

				}
			}
			count = 0;
			count2 = 0;
		}

		return false;

	}

}
