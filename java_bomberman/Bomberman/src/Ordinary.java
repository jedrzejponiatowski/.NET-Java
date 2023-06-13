import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Ordinary extends Enemy{
    public Ordinary(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector, Integer playerPosition) {
        super(row, col, color, mobility, map,  bomb, delay,inspector,playerPosition);
    }


    public void run(){
        Random random = new Random();

        while (this.status() && !Thread.currentThread().isInterrupted()) {
            int direction = random.nextInt(4); // Losowanie kierunku (0 - góra, 1 - dół, 2 - lewo, 3 - prawo)
            switch (direction) {
                case 0 -> moveUp();
                case 1 -> moveDown();
                case 2 -> moveLeft();
                case 3 -> moveRight();
            }
        }
    }

}
