import java.util.*;

public class Algo_BFS extends I_Algo_SearchAlgorithm {

    //members
    LinkedList<TilePuzzle> _frontierQueue;
    HashMap<TilePuzzle, Integer> _frontierAndExploredHash;  //only one hash table is necessary for both frontier and explored list.

    //constructors
    public Algo_BFS(){
        super();
        _frontierQueue = new LinkedList<>();
        _frontierAndExploredHash = new HashMap<>();
    }

    @Override
    public void calc(TilePuzzle state) throws Exception {
        pushToQueue(state);
        while (!_frontierQueue.isEmpty()){
            TilePuzzle currState = _frontierQueue.pop();
            _solution.incNumExpanded();
            ArrayList<MoveOperator.Direction> validOperators = MoveOperator.calcValidOperators(currState);
            for (MoveOperator.Direction operator : validOperators){
                TilePuzzle neighbour = MoveOperator.generateNeighbour(currState, operator);
                if (!inFrontierOrExplored(neighbour)){
                    if (isGoal(neighbour)){
                        _solution.setPathAndCost(neighbour);
                        return;
                    }
                    pushToQueue(neighbour);
                }
            }
        }
        _solution.setNoSolution();
    }

    private void pushToQueue(TilePuzzle state){
        _frontierQueue.add(state);
        _frontierAndExploredHash.put(state, 0);
    }

    private boolean inFrontierOrExplored (TilePuzzle state){
        return _frontierAndExploredHash.containsKey(state);
    }

}
