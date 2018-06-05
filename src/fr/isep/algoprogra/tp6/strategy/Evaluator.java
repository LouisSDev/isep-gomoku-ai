package fr.isep.algoprogra.tp6.strategy;

import fr.isep.algoprogra.tp6.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Evaluator {

    protected final Long timeout;
    protected final Long startTime;
    protected final GameState gameState;
    protected final int playerToWinId;

    protected List<Evaluator> evaluators;

    private Float score;

    private Status status = Status.NOT_COMPUTED;
    private boolean scoreComputed = false;

    private enum Status {
        DRAW,
        WIN,
        LOSE,
        HAS_CHILD_NODES,
        NOT_COMPUTED
    }

    private FutureStatus futureStatus;

    private boolean nodeComputed = false;

    private enum FutureStatus {
        IS_GONNA_WIN,
        IS_GONNA_LOSE,
        IS_GONNA_DRAW
    }

    public Evaluator(Long timeout, GameState gameState, int playerToWinId) {
        this.timeout = timeout;
        this.gameState = gameState;
        this.playerToWinId = playerToWinId;
        this.startTime = System.nanoTime();
    }

    public void evaluate() throws TimeoutException {

        if(System.nanoTime() > startTime + timeout)
            throw new TimeoutException();

        status = analyseGame();
        switch (status) {
            case WIN:
                score = 1f;
                break;
            case LOSE:
                score = -1f;
                break;
            default:
                break;
        }


    }

    public void computeNodes() throws TimeoutException {
        if(!nodeComputed) {
            nodeComputed = true;
            computeNodes(
                    evaluator -> {
                        evaluator.evaluate();
                        return true;
                    }
            );
        }
    }

    public void computeNodes(Function<Evaluator, Boolean> function) throws TimeoutException {
        if(status == Status.HAS_CHILD_NODES)
        {
            createEvaluators();
            for(Evaluator evaluator : evaluators) {
                function.apply(evaluator);
            }
        }
    }


    public boolean evaluateChildren(int i) throws TimeoutException {
        if( status != Status.HAS_CHILD_NODES){
            return false;
        }
        else if(i == 0) {
            computeNodes();
            return true;
        }
        else
        {
            final BooleanWrapper has2ndDegreeChild = new BooleanWrapper(false);
            computeNodes(
                    evaluator -> {
                        if(evaluator.evaluateChildren(i - 1))
                        {
                            has2ndDegreeChild.setVal(true);
                            return true;
                        }
                        return false;
                    }
            );
            return has2ndDegreeChild.isTrue();
        }
    }


    public Float getScore() {
        if(status == Status.HAS_CHILD_NODES && !scoreComputed)
        {
            scoreComputed = true;
            computeScore();
        }

        return score;
    }

    private void computeScore() {
        if(nodeComputed)
        {
            int visited = 0;
            score = 0f;
            for (Evaluator evaluator : evaluators) {
                if(evaluator.getScore() != null) {
                    score += evaluator.getScore();
                    visited ++;
                }
            }

            if(visited != 0)
                score /= visited;
        }
        else
        {
            futureStatus = analyseFutureGame();
            if (futureStatus != null) {
                switch (futureStatus) {
                    case IS_GONNA_WIN:
                        score = 0.5f;
                        break;
                    case IS_GONNA_LOSE:
                        score = -0.5f;
                        break;
                    case IS_GONNA_DRAW:
                        score = 0f;
                        break;
                }
            }
        }

    }


    protected void createEvaluators() {
        evaluators = new ArrayList<>();
        List<int[]> possibilities = Utils.buildPossibilities(gameState.getBoard());
        for (int[] possibility :  possibilities) {
            evaluators.add(
                    new Evaluator(
                            calculateTimeoutLeft(),
                            new GameState(
                                    gameState.getBoard(),
                                    possibility,
                                    gameState.getNextPlayerId(),
                                    gameState.getCurrentPlayerId()
                            ),
                            playerToWinId
                    )
            );
        }
    }


    protected Long calculateTimeoutLeft() {
        return timeout - (System.nanoTime() - startTime);
    }


    private Status analyseGame() {
        int check = Utils.evaluate(gameState.getBoard());
        switch (check) {
            case -1 :
                return Status.DRAW;
            case 0 :
                return Status.HAS_CHILD_NODES;
            default:
                if(check == playerToWinId + 1)
                    return Status.WIN;
                return Status.LOSE;
        }
    }


    private FutureStatus analyseFutureGame() {
        int check = Utils.evaluate(gameState.getBoard(), Utils.DEFAULT_ALIGNED_COUNT_REQUIRED - 1);
        switch (check) {
            case -1 :
                return FutureStatus.IS_GONNA_DRAW;
            case 0 :
                return null;
            default:
                if(check == playerToWinId + 1)
                    return FutureStatus.IS_GONNA_WIN;
                return FutureStatus.IS_GONNA_LOSE;
        }
    }


    private static final class BooleanWrapper {
        private boolean val;

        public BooleanWrapper(boolean val) {
            this.val = val;
        }

        public boolean isTrue() {
            return val;
        }

        public BooleanWrapper setVal(boolean val) {

            this.val = val;
            return this;
        }
    }


    protected String stringifyPossibility(int[] possibility) {

        return possibility == null ? null : "[ " + possibility[0] + ", " + possibility[1] + " ]";
    }
}
