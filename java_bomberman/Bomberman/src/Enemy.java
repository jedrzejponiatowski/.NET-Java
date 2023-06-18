import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
    protected  Graph<Integer,DefaultEdge> board;
    protected Graph<Integer,DefaultEdge> paths;
    protected Integer playerPosition;

    protected int mobility;

    protected volatile boolean updated = false;

    public Enemy(int row, int col, Color color, int mobility , int[][] map, List<Bomb> bomb , int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.mobility = mobility;
        this.map = map;
        this.bombs = bomb;
        this.playerPosition = playerPosition;
        this.board = board;

        BiconnectivityInspector<Integer, DefaultEdge> inspector
                = new BiconnectivityInspector<>(board);

        this.paths = inspector.getConnectedComponent(row * 100 + col);

        timer = new Timer(delay, this);
        timer.start();
    }

    // Kod do wykonania przy każdym odświeżeniu planszy
    public void actionPerformed(ActionEvent e) {
        // Kod do wykonania przy każdym odświeżeniu planszy
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

    protected void move(){
        do{
            if(isSafe(row*100+col - (row-1)*100 - col))
                if(moveUp()) break;
            if(isSafe(row*100+col - (row)*100 - col+1))
                if(moveRight()) break;
            if(isSafe(row*100+col - (row)*100 - col-1))
                if(moveLeft()) break;
            if(isSafe(row*100+col - (row+1)*100 - col))
                if(moveDown()) break;
        }while(true);

    }
    protected void move(Integer destination){
        boolean result = false;
        int[] array = {100,-100,1,-1};
        int n = -1;
        int way =row * 100 + col - destination;
        do{
            switch(way) {
                case 100 -> result = moveUp();
                case -100 -> result = moveDown();
                case 1 -> result = moveLeft();
                case -1 -> result = moveRight();
            }
            ++n;
            if(n < 4)
                way = array[n];
        }while(!result || n == 4);
    }

    protected boolean moveUp(){
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
            return true;
        }
        return false;
    }

    protected boolean moveDown() {
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int newRow = row + 1;
        if (newRow < ROWS && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
            return true;
        }
        return false;
    }

    protected boolean moveLeft() {
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
            return true;
        }
        return false;
    }

    protected boolean moveRight() {
        try {
            // Odczekaj pewien czas przed kolejnym ruchem
            Thread.sleep(mobility);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int newCol = col + 1;
        if (newCol < COLS && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
            return true;
        }
        return false;
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
    }

    protected boolean isSafe(){
        for (Bomb bomb : bombs) {
            int away = Math.abs(100 * row + col - (100 * bomb.getRow() + bomb.getCol()));
            if (away == 1 || away == 100 || away == 0) {
                return false;
            }
        }
        return true;
    }

    protected boolean isSafe(Integer x){
        for (Bomb bomb : bombs) {
            if(bomb != null){
                int away = Math.abs(x - (100 * bomb.getRow() + bomb.getCol()));
                if (away == 1 || away == 100 || away == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean safeBombPlacement() {
            for(Integer x: paths.vertexSet()){
                if (safeBombPlacement(x) && isSafe(x))
                    return true;
            }
            return false;
    }
    protected boolean safeBombPlacement(Integer x) {
        int away = Math.abs(x - (100 * row + col));
        return away != 1 && away != 100 && away != 0;
    }

    protected  boolean nearPlayer(){
        int x = Math.abs( row * 100 + col - playerPosition ) ;
        return x == 1 || x == 100;
    }

    protected void update(Integer x, List<Bomb> z,Graph<Integer,DefaultEdge> graph){
        playerPosition = x;
        bombs = z;
        board = graph;
        BiconnectivityInspector<Integer, DefaultEdge> inspector
                = new BiconnectivityInspector<>(board);
        paths = inspector.getConnectedComponent(row * 100 + col);
        paths.addVertex(this.row*100+col);
        for(Integer n : paths.vertexSet()){
            int result = Math.abs(n - row*100-col);
            if(result == 1 || result == 100)
                paths.addEdge(this.row*100+col,n);
        }
        updated = true;
    }

    static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }


    static protected double euclideanNorm(int x, int y){
        int x1 = Math.floorDiv(x, 100);
        int y1 = x - x1*100;
        int x2 = Math.floorDiv(y, 100);
        int y2 = y - x2*100;

        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }

}
