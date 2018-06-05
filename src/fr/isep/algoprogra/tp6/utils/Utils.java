package fr.isep.algoprogra.tp6.utils;

import java.util.ArrayList;
import java.util.List;

import static fr.isep.algoprogra.tp6.Gomoku.BOARD_SIZE;

public class Utils {

    public static final int DEFAULT_ALIGNED_COUNT_REQUIRED = 4;

    public static List<int[]> buildPossibilities(int[][] board) {

        List<int[]> possibilities = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == 0)
                    possibilities.add(new int[]{i, j});
            }
        }
        return possibilities;
    }



    static public int evaluate(int[][] board) {
        return evaluate(board, DEFAULT_ALIGNED_COUNT_REQUIRED);
    }

    /**
     * Checks whether the game has ended and returns -1: the game is over and no
     * player won (it is not possible to play anymore) 0: if no player has won so
     * far (the game continues !) 1: if black player has won (the game is over) 2:
     * if white player has won (the game is over)
     *
     * Feel free to re-use !
     * @param board
     * @return int indicating wether game has ended
     */
    static public int evaluate(int[][] board, int alignedCountRequired) {

        // determine if there are still some positions left
        boolean ended = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    ended = false;
                } else {
                    // check in 8 directions
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (di != 0 || dj != 0) {
                                int chk = check(board, board[i][j], i, j, new int[] {di, dj}, 1);

                                if(chk >= alignedCountRequired + 1) return board[i][j];
                            }
                        }
                    }
                }
            }
        }
        return ended ? -1 : 0;
    }


    /**
     * Recursive helper function to find chains in the board. Stops when reaches the
     * sides or when color changes (!=p)
     *
     * @param board
     * @param p
     * @param i
     * @param j
     * @param cnt
     * @return
     */
    static private int check(int[][] board, int p, int i, int j, int[] dir, int cnt) {
        if (i > BOARD_SIZE-1 || i < 0 || j < 0 || j > BOARD_SIZE-1)
            return cnt;
        else if (board[i][j] != p)
            return cnt;
        else {
            return check(board, p, i + dir[0], j + dir[1], dir, cnt + 1);
        }
    }
}
