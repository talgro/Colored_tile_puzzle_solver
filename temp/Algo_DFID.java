import java.util.ArrayList;
import java.util.HashMap;

public class Algo_DFID extends I_Algo_SearchAlgorithm {

    private HashMap<TilePuzzle, Integer> _statesOnCurrentPath;
    private int _limit;
    private boolean _limitIsReachable;

    public Algo_DFID(){
        super();
        _statesOnCurrentPath = new HashMap<>();
        _limit = 0;
        _limitIsReachable = true;
    }

    @Override
    public void calc(TilePuzzle state) throws Exception {
        boolean solved = false;
        while (!solved && _limitIsReachable) {
            _limit++;
            _limitIsReachable = false;
            solved = recursiveLimitedDFS(state, _limit);
        }
        if (!solved){
            _solution.setNoSolution();
        }
    }

    private boolean recursiveLimitedDFS(TilePuzzle state, long limit) throws Exception {
        //check if current state was already generated
        if (_statesOnCurrentPath.containsKey(state)){
            return false;
        }
        //check if did not cross depth limit
        if (limit == 0){
            _limitIsReachable = true;
            return false;
        }
        else{
            _statesOnCurrentPath.put(state, 0);
        }
        //if current state is a goal, return true. else, keep looking
        if (isGoal(state)){
            _solution.setPathAndCost(state);
            return true;
        }
        if (limit > 1){
            _solution.incNumExpanded();
        }
        ArrayList<MoveOperator.Direction> validOperators = MoveOperator.calcValidOperators(state);
        for (MoveOperator.Direction operator : validOperators){
            TilePuzzle neighbour = MoveOperator.generateNeighbour(state, operator);
            if (recursiveLimitedDFS(neighbour,limit - 1)){
                return true;
            }
        }
        _statesOnCurrentPath.remove(state);
        return false;
    }

}