import java.awt.*;
import java.util.List;

public class Ordinary extends Enemy{
    public Ordinary(int row, int col, Color color, int[][] map, List<Bomb> bomb, int delay) {
        super(row, col, color, map, bomb, delay);
    }
}
