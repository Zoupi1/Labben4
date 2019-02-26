package lab4;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

public class GomokuMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GomokuClient client = new GomokuClient(4003);
		GomokuGameState gameState = new GomokuGameState(client);
		GomokuGUI GUI = new GomokuGUI(gameState, client);
//		GameGrid gg = new GameGrid(10);
//		System.out.println(gg.isWinner(1));
		
	}

}
