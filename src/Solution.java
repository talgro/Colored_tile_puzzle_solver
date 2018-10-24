import java.util.Stack;

public class Solution {
    /*
    this class represents a solution solved by a search algorithm
    */

    private Stack<MoveOperator.Direction> _path;
    private int _numExpended;
    private double _cost;
    private double _runningTime;
    private boolean _pathFound;

    public Solution(){
        _path = new Stack<>();
        _cost = _runningTime = _numExpended = 0;
        _pathFound = true;
    }

    @Override
    public String toString() {
        if (_pathFound) {
            String path = getPath() + "\n";
            String numExpended = "Num: " + _numExpended + "\n";
            String cost = "Cost: " + _cost + "\n";
            String time = "" + getRunningTime();
            String solution = path + numExpended + cost + time;
            return solution;
        }
        else{
            return "no path";
        }
    }

    private String getPath(){
        Stack<MoveOperator.Direction> temp = new Stack<>();
        String ans = new String();
        //print all moves with "-"
        while (!_path.isEmpty()){
            MoveOperator.Direction currMove = _path.pop();
            temp.push(currMove);
            ans = ans + "-" + currMove;
        }
        //remove first "-"
        ans = ans.substring(1,ans.length());
        //return all moves to path stack
        while (!temp.isEmpty()){
            _path.push(temp.pop());
        }
        //return string
        return ans;
    }

    public void incNumExpanded(){
        _numExpended++;
    }

    public void decNumExpanded(){
        _numExpended--;
    }


    public void startCountTime(){
        _runningTime = System.currentTimeMillis();
    }

    public void stopCountTime(){
        _runningTime = System.currentTimeMillis() - _runningTime;
    }

    public double getRunningTime(){
        return _runningTime/1000;
    }

    public int getNumExpanded(){
        return _numExpended;
    }

    public void setPathAndCost(TilePuzzle state) throws Exception {
        while (state != null && state.getLastMove()!=null){
            _path.add(state.getLastMove());
            _cost = _cost + getCost(state);
            state = state.getFather();
        }
    }

    private double getCost(TilePuzzle state) throws Exception {
        TilePuzzle.BlankExactLocation blankLocation = state.getBlankExactLocation();
        try {
            switch (state.getLastMove()) {
                case D:
                    return state.getBoard()[blankLocation.getRow() - 1][blankLocation.getCol()].get_cost();
                case U:
                    return state.getBoard()[blankLocation.getRow() + 1][blankLocation.getCol()].get_cost();
                case R:
                    return state.getBoard()[blankLocation.getRow()][blankLocation.getCol() - 1].get_cost();
                case L:
                    return state.getBoard()[blankLocation.getRow()][blankLocation.getCol() + 1].get_cost();
                default:
                    throw new Exception("Error calculating cost of a state!");
            }
        }
        catch (Exception e){
            return 0;
        }
    }

    public void setNoSolution(){
        _pathFound = false;
    }
}
