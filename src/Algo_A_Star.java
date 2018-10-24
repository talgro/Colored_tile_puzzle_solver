import java.util.*;

public class Algo_A_Star extends I_Algo_InformedSearchAlgorithm {

    //members
    PriorityQueue<TilePuzzle> _frontier;

    //constructors
    public Algo_A_Star(){
        super();
        _frontier = new PriorityQueue<>(new Comp_Cost2HerePlusEstimated2Goal());
    }

    @Override
    public void calc(TilePuzzle state) throws Exception {
        //add to frontier list and save its priority ('f' function value) value in an hash table
        frontierPush(state, new statePriority(heuristicFunction(state), 0));
        //start searching for a goal state
        while (!_frontier.isEmpty()){
            //pop the best valued state and expand it by its valid operators
            TilePuzzle currState = _frontier.poll();
            double costToCurrState = _generatedStatesWithPriority.get(currState).getCostToState();
            ArrayList<MoveOperator.Direction> validOperators = MoveOperator.calcValidOperators(currState);
            _solution.incNumExpanded();
            for (MoveOperator.Direction operator : validOperators){
                TilePuzzle neighbour = MoveOperator.generateNeighbour(currState, operator);
                //if current neighbour state has not already been generated before, check if it is a goal
                if (!_generatedStatesWithPriority.containsKey(neighbour)){
                    if (isGoal(neighbour)){
                        //goal was found, save the path to it and stop the algorithm
                        _solution.setPathAndCost(neighbour);
                        return;
                    }
                    //current neighbour is not a goal, so add it to the frontier with its calculated potential
                    double neighbourHeuristicVal = heuristicFunction(neighbour);
                    double lastMoveCost = calcLastMoveCostByFather(currState, neighbour);
                    double costToNeighbour = costToCurrState + lastMoveCost;
                    frontierPush(neighbour, new statePriority(neighbourHeuristicVal, costToNeighbour));
                }
            }
        }
        //if frontier is empty, a solution was not found
        _solution.setNoSolution();
    }

    private void frontierPush(TilePuzzle state, statePriority potential){
        _generatedStatesWithPriority.put(state, potential);
        _frontier.add(state);
    }
}