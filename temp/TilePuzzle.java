import java.util.ArrayList;

public class TilePuzzle{
    /*
    this class represents the puzzle itself
     */

    public static class BoardSize{
        public final int _rows;
        public final int _cols;
        public BoardSize(int rows, int cols){
            _rows = rows;
            _cols = cols;
        }
    }

    public class BlankExactLocation{
        //members
        private int _row;
        private int _col;
        //constructor
        public BlankExactLocation(int row, int col){
            _row = row;
            _col = col;
        }
        //methods
        public int getRow(){
            return _row;
        }
        public int getCol(){
            return _col;
        }
        public void setRow(int row){
            _row = row;
        }
        public void setCol(int col){
            _col = col;
        }

    }

    public enum BlankGeneralLocation {
        TOP_LEFT, TOP_RIGHT, BOT_RIGHT, BOT_LEFT, RIGHT, LEFT, TOP, BOT, MIDDLE
    }

    //members
    private static int GoalHash;
    private final int _hashCode;
    private TilePuzzle _father;
    private MoveOperator.Direction _lastMove;
    private Tile[][] _board;
    private BlankGeneralLocation _blankGeneralLocation;
    private BlankExactLocation _blankExactLocation;

    //constructors
    public TilePuzzle(BoardSize size, ArrayList<Tile> locatedTiles){
        _father = null;
        _lastMove = null;
        _board = new Tile[size._rows][size._cols];
        initBoard(locatedTiles);   //sets general and exact blank location
        _hashCode = generateHashCode();
    }
    //copy constructor
    public TilePuzzle(TilePuzzle fatherState, MoveOperator.Direction lastMove){
        _father = fatherState;  //copy constructor is being called only from state that generating their neighbours.
        _lastMove = lastMove;
        deepCopyBoard(fatherState.getBoard());    //sets _board
        moveBlank(lastMove, fatherState.getBlankExactLocation());    //sets general & exact location
        _hashCode = generateHashCode();
    }

    //methods
    @Override
    public int hashCode() {
        //hash code itself is being initiating in the constructor
        return _hashCode;
    }

    private int generateHashCode() {
        String hash = new String();
        for (int row = 0; row < _board.length; row++){
            for (int col = 0; col < _board[0].length; col++){
                hash = hash + "-" + _board[row][col].get_index();
            }
        }
        return hash.hashCode();
    }

    public int getHashCode(){
        return _hashCode;
    }

    @Override
    public boolean equals(Object other) {
        return _hashCode==((TilePuzzle)other).getHashCode();
    }

    private void deepCopyBoard(Tile[][] boardToCopy){
        //deep copy board
        _board = new Tile[boardToCopy.length][boardToCopy[0].length];
        for(int row = 0; row < boardToCopy.length; row++) {
            for(int col = 0; col < boardToCopy[0].length; col++) {
                _board[row][col] = boardToCopy[row][col];
            }
        }
    }

    public void printBoard(){
        for (int i = 0; i<_board.length;i++){
            System.out.println();
            for (int j = 0; j<_board[0].length;j++){
                int index = _board[i][j].get_index();
                String color = _board[i][j].getColor().toString();
                System.out.print(index + ", ");
            }
        }
        System.out.println();
    }

    private void moveBlank(MoveOperator.Direction lastMove, BlankExactLocation locationBeforeMove){
        Tile tileToSwap, blank;
        blank = _board[locationBeforeMove._row][locationBeforeMove._col];
        switch (lastMove){
            case D:
                tileToSwap = _board[locationBeforeMove._row+1][locationBeforeMove._col];
                _board[locationBeforeMove._row+1][locationBeforeMove._col] = blank;
                _board[locationBeforeMove._row][locationBeforeMove._col] = tileToSwap;
                setBlankLocation(locationBeforeMove._row+1, locationBeforeMove._col);
                break;
            case L:
                tileToSwap = _board[locationBeforeMove._row][locationBeforeMove._col-1];
                _board[locationBeforeMove._row][locationBeforeMove._col-1] = blank;
                _board[locationBeforeMove._row][locationBeforeMove._col] = tileToSwap;
                setBlankLocation(locationBeforeMove._row, locationBeforeMove._col-1);
                break;
            case R:
                tileToSwap = _board[locationBeforeMove._row][locationBeforeMove._col+1];
                _board[locationBeforeMove._row][locationBeforeMove._col+1] = blank;
                _board[locationBeforeMove._row][locationBeforeMove._col] = tileToSwap;
                setBlankLocation(locationBeforeMove._row, locationBeforeMove._col+1);
                break;
            case U:
                tileToSwap = _board[locationBeforeMove._row-1][locationBeforeMove._col];
                _board[locationBeforeMove._row-1][locationBeforeMove._col] = blank;
                _board[locationBeforeMove._row][locationBeforeMove._col] = tileToSwap;
                setBlankLocation(locationBeforeMove._row-1, locationBeforeMove._col);
                break;
        }
    }

    public int getNumRows(){
        return _board.length;
    }

    public int getNumCols(){
        return _board[0].length;
    }

    void initBoard(ArrayList<Tile> placedTiles){
        //place blocks
        for (int row = 0; row < getNumRows(); row++){
            for (int col = 0; col < getNumCols(); col++){
                Tile currTile = placedTiles.get(getNumCols() * row + col);
                _board[row][col] = currTile;
                if (Tile.isBlank(currTile)){
                    setBlankLocation(row, col);
                }
            }
        }
    }

    public Tile getBlock(int row, int col){
        return _board[row][col];
    }

    public final Tile[][] getBoard(){
        return _board;
    }

    private void setBlankLocation(int row, int col){
        //set exact location
        _blankExactLocation = new BlankExactLocation(row, col);
        //set general location
        if (row == 0){
            if (col == 0){
                _blankGeneralLocation = BlankGeneralLocation.TOP_LEFT;
            }
            else if (col == _board[0].length-1){
                _blankGeneralLocation = BlankGeneralLocation.TOP_RIGHT;
            }
            else{
                _blankGeneralLocation = BlankGeneralLocation.TOP;
            }
        }
        else if (row == _board.length-1){
            if (col == 0){
                _blankGeneralLocation = BlankGeneralLocation.BOT_LEFT;
            }
            else if (col == _board[0].length-1){
                _blankGeneralLocation = BlankGeneralLocation.BOT_RIGHT;
            }
            else{
                _blankGeneralLocation = BlankGeneralLocation.BOT;
            }
        }
        else{   //row~MIDDLE
            if (col == 0){
                _blankGeneralLocation = BlankGeneralLocation.LEFT;
            }
            else if (col == _board[0].length-1){
                _blankGeneralLocation = BlankGeneralLocation.RIGHT;
            }
            else{
                _blankGeneralLocation = BlankGeneralLocation.MIDDLE;
            }
        }
    }

    public BlankGeneralLocation getBlankGeneralLocation(){
        return _blankGeneralLocation;
    }

    public BlankExactLocation getBlankExactLocation(){
        return _blankExactLocation;
    }

    public MoveOperator.Direction getLastMove(){
        return _lastMove;
    }

    public TilePuzzle getFather(){
        return _father;
    }

    public static void setGoalHash(int hash){
        GoalHash = hash;
    }

    public static int getGoalHash(){
        return GoalHash;
    }

}
