import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Algo_IDA_Star extends I_Algo_InformedSearchAlgorithm {

    //members
    private Stack<TilePuzzle> _stack;
    private HashMap<TilePuzzle,Integer> _numNeighboursOnPath;
    private double _threshold;

    public Algo_IDA_Star(){
        super();
        _stack = new Stack<>();
        _numNeighboursOnPath = new HashMap<>();
    }

    @Override
    public void calc(TilePuzzle state) throws Exception {
        //add first state to stack
        pushToStack(state, new statePriority(heuristicFunction(state), 0));
        _numNeighboursOnPath.put(state, 0);
        //start searching for a goal state. init threshold to heuristic val of begin state, and search till the threshold is not reachable
        _threshold = _generatedStatesWithPriority.get(state).getHeuristic();
        double nextThreshold = Double.MAX_VALUE;
        boolean thresholdIsReachable = true;
        while (thresholdIsReachable) {
            thresholdIsReachable = false;
            //search A* to current threshold depth
            while (!_stack.isEmpty()) {
                //take the first state in the stack and expand it
                TilePuzzle currState = _stack.pop();
                double costToCurrState = _generatedStatesWithPriority.get(currState).getCostToState(); //for future use
                _solution.incNumExpanded(); //count expanded state
                ArrayList<MoveOperator.Direction> validOperators = MoveOperator.calcValidOperators(currState);
                for (MoveOperator.Direction operator : validOperators) {
                    //for each expanded neighbour, calc its grade and check if grade > threshold
                    TilePuzzle neighbour = MoveOperator.generateNeighbour(currState, operator);
                    double neighbourHeuristicVal = heuristicFunction(neighbour);
                    double lastMoveCost = calcLastMoveCostByFather(currState, neighbour);
                    double costToNeighbour = costToCurrState + lastMoveCost;
                    statePriority currNeighbourPotential = new statePriority(neighbourHeuristicVal, costToNeighbour);
                    //make sure grade <= threshold
                    if (currNeighbourPotential.getPriority() <= _threshold) {
                        //grade <= threshold, so make sure current neighbour has not already been generated on current path
                        if (!isAlreadyOnCurrPath(neighbour)) {
                            //current state has not been generated yet, so check if it is a goal
                            if (isGoal(neighbour)) {
                                //goal was found, save the path to it and stop the algorithm
                                _solution.setPathAndCost(neighbour);
                                return;
                            }
                            //init num of neighbours of neighbour to 0, and increase his father's (currState) num of neighbours with 1
                            _numNeighboursOnPath.put(neighbour, 0);
                            incNumOfNeighbours(currState);
                            //current neighbour is not a goal, so add it to the stack with its calculated potential
                            pushToStack(neighbour, currNeighbourPotential);
                        }
                        else{
                            //current state has already been generated, so apply loop-avoidance
                            double oldPriority = _generatedStatesWithPriority.get(neighbour).getPriority();
                            double newPriority = currNeighbourPotential.getPriority();
                            if (newPriority < oldPriority){
                                _stack.remove(neighbour);
                                pushToStack(neighbour, currNeighbourPotential);
                            }
                        }
                    }
                    else {
                        //grade  exceeds threshold, so (1)mark threshold as reachable, (2)update next-threshold-value if necessary
                        thresholdIsReachable = true;
                        if (nextThreshold < currNeighbourPotential.getPriority()) {
                            //do nothing
                        }
                        else if (nextThreshold > currNeighbourPotential.getPriority()) {
                            nextThreshold = currNeighbourPotential.getPriority();
                        }
                    }
                }
                //make sure that _generatedStatesWithPriority contains only states from current path
                tryRemoveFromHash(currState);
            }
            pushToStack(state, new statePriority(heuristicFunction(state), 0));
            _threshold = nextThreshold;
            nextThreshold = Double.MAX_VALUE;
        }
        _solution.setNoSolution();
    }


    private void pushToStack(TilePuzzle state, statePriority potential){
        _stack.push(state);
        _generatedStatesWithPriority.put(state, potential);
    }

    private boolean isAlreadyOnCurrPath(TilePuzzle state){
        return _generatedStatesWithPriority.containsKey(state);
    }

    private void incNumOfNeighbours(TilePuzzle state){
        int currNumNeighbours = _numNeighboursOnPath.get(state);
        _numNeighboursOnPath.put(state, currNumNeighbours + 1);
    }

    private void decNumOfNeighbours(TilePuzzle state){
        int currNumNeighbours = _numNeighboursOnPath.get(state);
        _numNeighboursOnPath.put(state, currNumNeighbours - 1);
    }

    private void tryRemoveFromHash(TilePuzzle state){
        if (_numNeighboursOnPath.get(state) == 0){
            _generatedStatesWithPriority.remove(state);
            try{
                TilePuzzle father = state.getFather();
                decNumOfNeighbours(father);
                tryRemoveFromHash(father);
            }
            catch (Exception e){
                //if father is null, state is beginning state. do nothing
            }
        }
    }
}
