package fr.isep.algoprogra.tp6;

import fr.isep.algoprogra.tp6.strategy.GameState;
import fr.isep.algoprogra.tp6.strategy.GlobalEvaluator;
import fr.isep.algoprogra.tp6.utils.Utils;

import java.util.List;

public class ComputerPlayer extends Player {

    private final int otherPlayerId;

    private int toursPlayed = 0;

    public ComputerPlayer(int id, int otherPlayerId) {
        super("Carlo le Robot n*" + id , id);
        this.otherPlayerId = otherPlayerId;
    }

    @Override
    public int[] getMove(int[][] board) {
        if(toursPlayed < 2) {
            List<int[]> possibilities = Utils.buildPossibilities(board);
            toursPlayed ++;
            return possibilities.get((int) (Math.random() * possibilities.size()) );
        }


        GlobalEvaluator globalEvaluator = new GlobalEvaluator(10_000_000_000L, new GameState(board, id, otherPlayerId), id);
        globalEvaluator.evaluate();
        return globalEvaluator.getNextMove();
    }


}
