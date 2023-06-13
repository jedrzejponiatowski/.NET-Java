import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
//import java.util.Timer;
import javax.swing.Timer;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

public abstract class Enemy implements Runnable, ActionListener {
    protected static final int ROWS = 11;
    protected static final int COLS = 17;
    protected boolean isAlive = true;
    protected Timer timer;

    protected int row;
    protected int col;

    protected Color color;

    protected int[][] map;
    protected List<Bomb> bombs;
    protected Graph<Integer,DefaultEdge> paths;
    protected Integer playerPosition;

    protected int mobility;

    public Enemy(int row, int col, Color color, int mobility , int[][] map, List<Bomb> bomb , int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector, Integer playerPosition) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.mobility = mobility;
        this.map = map;
        this.bombs = bomb;
        this.playerPosition = playerPosition;
        this.paths = inspector.getConnectedComponent(row * 100 + col);

        timer = new Timer(delay, this);
        timer.start();


        /*Timer enemyBombTimer = new Timer(4500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kod do wykonania przy każdym odświeżeniu planszy
                if (isAlive)
                    enemyPlaceBomb();
            }
        });*/
    }

    // Kod do wykonania przy każdym odświeżeniu planszy
    public void actionPerformed(ActionEvent e) {
        // Kod do wykonania przy każdym odświeżeniu planszy
        if (isAlive && this.isSafe());
            //enemyPlaceBomb();
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
    public Color getColor(){
        return color;
    }
    public boolean status(){
        return isAlive;
    }


    protected void move(Integer destination){
        Integer way =row * 100 + col - destination;
        switch(way){
            case  100 -> moveUp();
            case -100 -> moveDown();
            case    1 -> moveLeft();
            case   -1 -> moveRight();
        }
    }

    protected void moveUp(){
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void moveDown() {
        int newRow = row + 1;
        if (newRow < ROWS && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void moveLeft() {
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void moveRight() {
        int newCol = col + 1;
        if (newCol < COLS && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void draw(Graphics g, int tileSize) {
        if(isAlive){
            g.setColor(color);
            g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
        }
    }

    protected void enemyPlaceBomb(){
        for (Bomb bomb : bombs) {
            if (bomb.getRow() == row && bomb.getCol() == col) {
                return; // Jeśli jest już bomba na tym polu, nie można postawić kolejnej
            }
        }
        // Dodaj nową bombę na aktualne położenie gracza
        Bomb newBomb = new Bomb(row, col, Color.RED, 3);
        bombs.add(newBomb);
        map[row][col] = 3;
    }

    protected void kill(){
        isAlive = false;
        col = -10;
        row = -10;
    }

    protected boolean isSafe(){
        for (Bomb bomb : bombs) {
            int away = Math.abs(100 * row + col - (100 * bomb.getRow() - bomb.getCol()));
            if (away == 1 || away == 100 || away == 0) {
                return false;
            }
        }
        return true;
    }

    protected boolean isSafe(Integer x){
        for (Bomb bomb : bombs) {
            int away = Math.abs(x - (100 * bomb.getRow() - bomb.getCol()));
            if (away == 1 || away == 100 || away == 0) {
                return false;
            }
        }
        return true;
    }

    protected void update(Integer x, BiconnectivityInspector<Integer, DefaultEdge> inspector){
        playerPosition = x;
        paths = inspector.getConnectedComponent(row * 100 + col);
    }

    static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }


}
