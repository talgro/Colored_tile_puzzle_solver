import java.util.Comparator;
import java.util.HashMap;

abstract class I_Algo_InformedSearchAlgorithm extends I_Algo_SearchAlgorithm {
    /*
    this abstract class is a template of an any informed search algorithm
     */

    protected class statePriority {
        /*
        this inner class represents the priority (grade) given to any state.
         */
        //members
        private final double _hVal; //heuristic value
        private double _gVal;   //cost till this state
        private double _fVal;   //f = g + h
        //constructor
        public statePriority(double h, double g){
            _hVal = h;
            _gVal = g;
            _fVal = _gVal + _hVal;
        }
        //methods
        public void updateCostToState(double updatedG){
            _gVal = updatedG;
            _fVal = _hVal + _gVal;
        }
        public double getHeuristic(){
            return _hVal;
        }
        public double getCostToState(){
            return _gVal;
        }
        public double getPriority(){
            return _fVal;
        }
    }

    protected HashMap<TilePuzzle, statePriority> _generatedStatesWithPriority;  //hash table of all states were generated to a point of time, and their priority

    public I_Algo_InformedSearchAlgorithm(){
        super();
        _generatedStatesWithPriority = new HashMap<>();
    }

    protected class Comp_Cost2HerePlusEstimated2Goal implements Comparator<TilePuzzle> {
        /*
        cost to a state + weighted manhattan distance comparing
         */
        @Override
        public int compare(TilePuzzle s1, TilePuzzle s2) {
            //compare
            if (_generatedStatesWithPriority.get(s1).getPriority() < _generatedStatesWithPriority.get(s2).getPriority()){
                return -1;  //if grade is smaller, pop it first
            }
            if (_generatedStatesWithPriority.get(s1).getPriority() > _generatedStatesWithPriority.get(s2).getPriority()){
                return 1;   //if grade is greater, pop it later
            }
            return (s2.getLastMove().compareTo(s1.getLastMove()));
        }
    }

    protected class Comp_EstimatedCost2Goal implements Comparator<TilePuzzle> {
        /*
        heuristic weighted manhattan distance comparing
         */

        private class Comp_Reversed_EstimatedCost2Goal implements Comparator<TilePuzzle> {
            @Override
            public int compare(TilePuzzle s2, TilePuzzle s1) {
                //compare
                if (_generatedStatesWithPriority.get(s1).getHeuristic() < _generatedStatesWithPriority.get(s2).getHeuristic()){
                    return -1;  //if h is smaller, pop it first
                }
                if (_generatedStatesWithPriority.get(s1).getPriority() > _generatedStatesWithPriority.get(s2).getPriority()){
                    return 1;   //if h is greater, pop it later
                }
                return (s2.getLastMove().compareTo(s1.getLastMove()));
            }
        }

        @Override
        public int compare(TilePuzzle s1, TilePuzzle s2) {
            //compare
            if (_generatedStatesWithPriority.get(s1).getHeuristic() < _generatedStatesWithPriority.get(s2).getHeuristic()){
                return -1;  //if h is smaller, pop it first
            }
            if (_generatedStatesWithPriority.get(s1).getPriority() > _generatedStatesWithPriority.get(s2).getPriority()){
                return 1;   //if h is greater, pop it later
            }
            return (s2.getLastMove().compareTo(s1.getLastMove()));
        }

        @Override
        public Comparator<TilePuzzle> reversed() {
            return new Comp_Reversed_EstimatedCost2Goal();
        }
    }

    protected final static double heuristicFunction(TilePuzzle state) {
        //weighted manhattan distance
        int numCols = state.getNumCols();
        double ans = 0;
        for (int row = 0; row < state.getNumRows(); row++) {
            for (int col = 0; col < numCols; col++) {
                Tile currTile = state.getBlock(row, col);
                double currBlockCost = currTile.get_cost();
                int currIndex = currTile.get_index();
                int goalRow = (currIndex - 1) / numCols;
                int goalCol = (currIndex + numCols - 1) % numCols;
                int currMDcost = Math.abs(col - goalCol) + Math.abs(row - goalRow);
                ans = ans + currMDcost * currBlockCost;
            }
        }
        return ans;
    }

    protected final static double calcLastMoveCostByFather(TilePuzzle father, TilePuzzle son){
        int lastMovedBlock_row = father.getBlankExactLocation().getRow();
        int lastMovedBlock_col = father.getBlankExactLocation().getCol();
        return son.getBoard()[lastMovedBlock_row][lastMovedBlock_col].get_cost();
    }
}
