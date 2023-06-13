import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.Set;
//import java.util.Timer;
import javax.swing.Timer;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

public abstract class Enemy implements Runnable, ActionListener {
    private static final int ROWS = 11;
    private static final int COLS = 17;
    private boolean isAlive = true;
    private Timer timer;
    private Point position;
    private int row;
    private int col;
    private Color color;
    private int[][] map;
    private List<Bomb> bombs;
    private Graph<Integer,DefaultEdge> path;

    public Enemy(int row, int col, Color color, int[][] map, List<Bomb> bomb , int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.map = map;
        this.bombs = bomb;
        this.position = new Point(row,col);
        this.path = inspector.getConnectedComponent(row * 100 + col);

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
        if (isAlive)
            enemyPlaceBomb();
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

    public Point getPosition(){ return position; }
    public boolean status(){
        return isAlive;
    }


    protected void moveUp(){
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
    }

    protected void moveDown() {
        int newRow = row + 1;
        if (newRow < ROWS && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
    }

    protected void moveLeft() {
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
    }

    protected void moveRight() {
        int newCol = col + 1;
        if (newCol < COLS && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
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


}
