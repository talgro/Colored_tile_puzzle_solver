import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class Algo_DFBnB extends I_Algo_InformedSearchAlgorithm {

    //members
    private Stack<TilePuzzle> _stack;
    private HashMap<TilePuzzle,Integer> _numNeighboursOnPath;
    private double _costBest;
    private TilePuzzle _goalBest;
    //constructor
    public Algo_DFBnB(){
        super();
        _stack = new Stack<>();
        _numNeighboursOnPath = new HashMap<>();
        _costBest = Double.MAX_VALUE;
        _goalBest = null;
    }

    @Override
    public void calc(TilePuzzle state) throws Exception {
        //add first state to stack, init its priority and set num of children as 0;
        _stack.push(state);
        _generatedStatesWithPriority.put(state, new statePriority(heuristicFunction(state), 0));
        _numNeighboursOnPath.put(state, 0);
        //start searching for a goal state
        while (!_stack.isEmpty()) {
            //take the first state in the stack and expand it
            TilePuzzle currState = popFromStack(); //also increase num of expanded states with 1
            double costToCurrState = _generatedStatesWithPriority.get(currState).getCostToState(); //for future use
            ArrayList<MoveOperator.Direction> validOperators = MoveOperator.calcValidOperators(currState);
            PriorityQueue<TilePuzzle> nodeOrderingQueue = new PriorityQueue<>(new Comp_EstimatedCost2Goal().reversed());
            for (MoveOperator.Direction operator : validOperators) {
                //calc neighbour priority
                TilePuzzle neighbour = MoveOperator.generateNeighbour(currState, operator);
                double neighbourHeuristicVal = heuristicFunction(neighbour);
                double lastMoveCost = calcLastMoveCostByFather(currState, neighbour);
                double costToNeighbour = costToCurrState + lastMoveCost;
                statePriority currNeighbourPriority = new statePriority(neighbourHeuristicVal, costToNeighbour);
                //make sure cost is smaller than the limit
                if (costToNeighbour < _costBest) {
                    //make sure current neighbour has not already been generated on current path
                    if (!isAlreadyOnCurrPath(neighbour)) {
                        _generatedStatesWithPriority.put(neighbour, currNeighbourPriority);
                        //check if it is a goal, and update best-cost and best-goal if it is
                        if (isGoal(neighbour)) {
                            _costBest = costToNeighbour;
                            _goalBest = neighbour;
                        }
                        //init num of neighbours of neighbour to 0, and increase his father's (currState) num of neighbours with 1
                        _numNeighboursOnPath.put(neighbour, 0);
                        incNumOfNeighbours(currState);
                        //node ordering (step 1: push neighbours to priority queue)
                        nodeOrderingQueue.add(neighbour);
                    }
                    else{
                        //current state has already been generated, so apply loop-avoidance
                        double oldCost = _generatedStatesWithPriority.get(neighbour).getCostToState();
                        double newCost = currNeighbourPriority.getCostToState();
                        if (newCost < oldCost){
                            _stack.remove(neighbour);
                            nodeOrderingQueue.add(neighbour);
                            _generatedStatesWithPriority.put(neighbour, currNeighbourPriority);
                        }
                    }
                }
            }
            //make sure that _generatedStatesWithPriority contains only states from current path
            tryRemoveFromHash(currState);
            //node ordering (step 2: pop from queue and push to stack)
            while (!nodeOrderingQueue.isEmpty()){
                _stack.push(nodeOrderingQueue.poll());
            }
        }
        //set solution
        if (_goalBest != null){
            _solution.setPathAndCost(_goalBest);
        }
        else{
            _solution.setNoSolution();
        }
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

    private TilePuzzle popFromStack(){
        _solution.incNumExpanded();
        return _stack.pop();
    }

}
