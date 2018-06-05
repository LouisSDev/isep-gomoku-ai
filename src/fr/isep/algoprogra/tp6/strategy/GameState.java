package fr.isep.algoprogra.tp6.strategy;

public class GameState {

    private final int[][] board;
    private final int[] previousPlay;

    private final int currentPlayerId;
    private final int nextPlayerId;

    public GameState(int[][] board, int currentPlayerId, int nextPlayerId) {

        this.board = board;
        this.currentPlayerId = currentPlayerId;
        this.nextPlayerId = nextPlayerId;
        this.previousPlay = null;
    }

    public GameState(int[][] board, int[] nextPlay, int currentPlayerId, int nextPlayerId) {
        this.board = new int[board.length][board.length];
        this.previousPlay = nextPlay;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = board[i][j];
            }
        }

        board[nextPlay[0]][nextPlay[1]] = currentPlayerId;
        this.currentPlayerId = currentPlayerId;
        this.nextPlayerId = nextPlayerId;
    }

    public int[][] getBoard() {

        return board;
    }

    public int getCurrentPlayerId() {

        return currentPlayerId;
    }

    public int getNextPlayerId() {

        return nextPlayerId;
    }

    public int[] getPreviousPlay() {

        return previousPlay;
    }
}
