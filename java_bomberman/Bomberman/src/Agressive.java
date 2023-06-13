import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Agressive extends Enemy{

    public Agressive(int row, int col, Color color, int[][] map, List<Bomb> bomb, int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector) {
        super(row, col, color, map, bomb,delay,inspector);
    }

    public void run() {
        Random random = new Random();

        while (this.status() && !Thread.currentThread().isInterrupted()) {
            int direction = random.nextInt(4); // Losowanie kierunku (0 - góra, 1 - dół, 2 - lewo, 3 - prawo)
            switch (direction) {
                case 0 -> moveUp();
                case 1 -> moveDown();
                case 2 -> moveLeft();
                case 3 -> moveRight();
            }
            try {
                // Odczekaj pewien czas przed kolejnym ruchem
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
        /*Timer enemyBombTimer = new Timer(4500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kod do wykonania przy każdym odświeżeniu planszy
                if (isAlive)
                    enemyPlaceBomb();
            }
        });*/
}

