package fr.isep.algoprogra.tp6.strategy;

import fr.isep.algoprogra.tp6.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GlobalEvaluator extends Evaluator{

    private final int possibilitiesCount;

    private int[] nextMove;

    public GlobalEvaluator(Long timeout, GameState gameState, int playerToWinId) {
        super(timeout, gameState, playerToWinId);
        possibilitiesCount = Utils.buildPossibilities(gameState.getBoard()).size();
    }

    @Override
    public void evaluate() throws TimeoutException {

        List<int[]> possibilities = Utils.buildPossibilities(gameState.getBoard());
        evaluators = new ArrayList<>();
        for (int[] possibility : possibilities) {
            analysePossibility(possibility);
        }

        evaluators.sort(
                (o1, o2) ->
                        o2.getScore()
                                .compareTo(o1.getScore())
        );
        nextMove = evaluators.get(0).gameState.getPreviousPlay();
    }

    private void analysePossibility(int[] possibility) {

        System.out.println("Analysing possibility " + stringifyPossibility(possibility));
        boolean shouldContinue = true;
        int i = 0;
        Evaluator evaluator =
                new Evaluator(
                    calculateTimeoutLeft(),
                        new GameState(
                                gameState.getBoard(),
                                possibility,
                                playerToWinId,
                                gameState.getNextPlayerId()
                        ),
                        playerToWinId
                );

        evaluators.add(evaluator);


        try {

            evaluator.evaluate();

            do {

                System.out.println("Working on node of deep " + i);
                shouldContinue = evaluator.evaluateChildren(i);
                i++;

            } while (shouldContinue) ;
        }
        catch (TimeoutException e) {

            e.printStackTrace();
        }
        finally {
            System.out.println("Possibility " + stringifyPossibility(possibility) + " has a score of " + evaluator.getScore());
        }


    }


    @Override
    protected Long calculateTimeoutLeft() {
        return (long) ( (float) timeout ) / possibilitiesCount;
    }

    public int[] getNextMove() {

        return nextMove;
    }
}
