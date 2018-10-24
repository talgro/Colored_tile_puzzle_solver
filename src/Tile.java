public class Tile {
    /*
    this class represents a tile in the puzzle
     */

    public enum Color{
        BLACK, WHITE, YELLOW, RED;
    }

    private final Color _color;
    private final int _index;
    private final double _cost;

    public Tile(Color color, int index){
        _color = color;
        _index = index;
        switch (_color){
            case WHITE:
                _cost = 1;
                break;
            case YELLOW:
                _cost = 0.5;
                break;
            case RED:
                _cost = 2;
                break;
            default:
                _cost = 0;
        }
    }

    public Color getColor() {
        return _color;
    }

    public int get_index() {
        return _index;
    }

    public double get_cost() {
        return _cost;
    }

    public static boolean isBlank(Tile tile){
        if (tile.getColor() == Color.BLACK){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return _index+"-"+_color;
    }
}
