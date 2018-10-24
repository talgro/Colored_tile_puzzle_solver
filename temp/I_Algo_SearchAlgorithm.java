import java.util.ArrayList;
import java.util.PriorityQueue;

abstract class I_Algo_SearchAlgorithm {
    /*
    this abstract class is a template of an any search algorithm
     */

    protected Solution _solution;

    public I_Algo_SearchAlgorithm(){
        _solution = new Solution();
    }

    abstract public void calc(TilePuzzle tp) throws Exception;

    public Solution calculate(TilePuzzle tp) throws Exception{
        _solution.startCountTime();
        calc(tp);
        _solution.stopCountTime();
        return _solution;
    }

    final protected static boolean isGoal(TilePuzzle state){
        return TilePuzzle.getGoalHash() == state.getHashCode();
    }

    final protected void setGoalHash(TilePuzzle state){
        String strToHash = "";
        for (int idx = 1; idx < state.getNumRows() * state.getNumCols(); idx++){
            strToHash = strToHash+ "-" + idx;
        }
        strToHash = strToHash + "-" + '0';
        TilePuzzle.setGoalHash(strToHash.hashCode());
    }
}
