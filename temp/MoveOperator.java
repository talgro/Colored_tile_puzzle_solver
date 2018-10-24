import java.util.ArrayList;
import java.util.LinkedList;

public class MoveOperator {
    /*
    this class takes care of the operators and their methods in the game
     */

    public enum Direction{
        U,L,D,R;
    }

    public static ArrayList<Direction> calcValidOperators(TilePuzzle state){
        ArrayList<Direction> validOperators = new ArrayList<>();
        switch (state.getBlankGeneralLocation()){
            case TOP_LEFT:
                validOperators.add(Direction.R);
                validOperators.add(Direction.D);
                break;
            case TOP:
                validOperators.add(Direction.R);
                validOperators.add(Direction.D);
                validOperators.add(Direction.L);
                break;
            case TOP_RIGHT:
                validOperators.add(Direction.D);
                validOperators.add(Direction.L);
                break;
            case LEFT:
                validOperators.add(Direction.R);
                validOperators.add(Direction.D);
                validOperators.add(Direction.U);
                break;
            case MIDDLE:
                validOperators.add(Direction.R);
                validOperators.add(Direction.D);
                validOperators.add(Direction.L);
                validOperators.add(Direction.U);
                break;
            case RIGHT:
                validOperators.add(Direction.D);
                validOperators.add(Direction.L);
                validOperators.add(Direction.U);
                break;
            case BOT_LEFT:
                validOperators.add(Direction.R);
                validOperators.add(Direction.U);
                break;
            case BOT:
                validOperators.add(Direction.R);
                validOperators.add(Direction.L);
                validOperators.add(Direction.U);
                break;
            case BOT_RIGHT:
                validOperators.add(Direction.L);
                validOperators.add(Direction.U);
                break;
        }
        return validOperators;
    }

    public static TilePuzzle generateNeighbour(TilePuzzle state, Direction direction){
        return new TilePuzzle(state, direction);
    }
}