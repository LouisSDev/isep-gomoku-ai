package fr.isep.algoprogra.tp6;

import fr.isep.algoprogra.tp6.utils.Utils;

import java.util.Random;
import java.util.Scanner;

public class Gomoku {

	public static final int BOARD_SIZE = 9;
	public static Scanner scan;

	// GAME MANAGEMENT
	// Checks whether a move is valid against the current board
	// feel free to re-use ^^
	public static boolean valid(int[] move, int[][] board) {
		if (move[0] < 0 || move[0] >= BOARD_SIZE || move[1] < 0 || move[1] >= BOARD_SIZE)
			return false;
		else if (board[move[0]][move[1]] != 0)
			return false;
		else
			return true;
	}



	public static int gameLoop(int[][] board, Player[] players) {
		Random rand = new Random(System.currentTimeMillis());
		int currentPlayer = rand.nextInt(2);
		int status = 0;
		while (status == 0) {
			System.out.println("It's " + players[currentPlayer].getName() + "'s turn");
			printBoard(board);
			int[] move = players[currentPlayer].getMove(board);
			while (!valid(move, board)) {
				System.out.println("NOPE ! Try again! ");
				move = players[currentPlayer].getMove(board);
			}
			board[move[0]][move[1]] = currentPlayer + 1;
			status = Utils.evaluate(board);
			currentPlayer = currentPlayer == 0 ? 1 : 0;
		}
		return status;
	}


	private static void printBoard(int[][] board) {
		  
		for(int i =0; i<BOARD_SIZE;i++)
			System.out.print("  |  " + (i+1));
		System.out.println(" |");
		for(int i =0; i<BOARD_SIZE;i++)
			System.out.print("------");
		System.out.println(" |");
		
		for(int i=0; i<BOARD_SIZE;i++) {
			System.out.print((i+1) + " |");
			for(int j=0;j<BOARD_SIZE;j++) {
				 System.out.print("  " + board[i][j] + "  |");
			}
			
			System.out.println("");
			
			for(int j =0; j<BOARD_SIZE;j++)
				System.out.print("------");
			System.out.println("");
			
		}	

	}

	// PLAYER CREATION
	public static Player createHumanPlayer(int id) {
		System.out.println("Hello human player " + id);
		System.out.println("What is your name ? ");
		String name = scan.nextLine();
		return new HumanPlayer(name,id,scan);
	}

	private static Player createComputerPlayer(int id, int otherPlayerId) {
		return new ComputerPlayer(id, otherPlayerId);
	}

	public static void main(String[] args) {

		Player[] players = new Player[2];
		int[][] board = new int[9][9];

		// Scanner init
		scan = new Scanner(System.in);

		System.out.println("== GO-MOKU ==");
		System.out.println("Who do you want to play with ? ");
		System.out.println("1) Human");
		System.out.println("2) Computer");
		System.out.println("3) Computer Vs Computer  : just watch it!");
		System.out.print("\n Your choice ? ");
		int choice = scan.nextInt();
		System.out.println(choice);
		scan.nextLine();
		while (choice != 1 && choice != 2 && choice != 3) {
			System.out.print("\n Wrong choice... Your choice ? ");
			choice = scan.nextInt();
		}

		switch (choice) {
		case 1:
            players[0] = createHumanPlayer(0);
			players[1] = createHumanPlayer(1);
			break;
		case 2:
            players[0] = createHumanPlayer(0);
			players[1] = createComputerPlayer(1, 0);
			break;
		case 3:
            players[0] = createComputerPlayer(0, 1);
            players[1] = createComputerPlayer(1, 0);
			break;
		}

		int winner = gameLoop(board, players);
		System.out.println("============================");
		System.out.println("=== WINNER IS PLAYER : " + winner + " ===");
		System.out.println("============================");
	}

}
