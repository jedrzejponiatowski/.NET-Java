import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.List;

public class Agressive extends Enemy{

    public Agressive(int row, int col, Color color, int[][] map, List<Bomb> bomb, int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector) {
        super(row, col, color, map, bomb,delay,inspector);
    }


}
