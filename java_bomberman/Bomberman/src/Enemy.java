import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

/**
 * The type Enemy. Abstract class that represents each enemy and implements thread functions class Runnable
 */
public abstract class Enemy implements Runnable, ActionListener {
    /**
     * The constant ROWS.
     */
    protected static final int ROWS = 11;
    /**
     * The constant COLS.
     */
    protected static final int COLS = 17;
    /**
     * The Is alive.
     */
    protected boolean isAlive = true;
    /**
     * The Timer.
     */
    protected Timer timer;

    /**
     * The Row.
     */
    protected int row;
    /**
     * The Col.
     */
    protected int col;

    /**
     * The Color.
     */
    protected Color color;

    /**
     * The Map.
     */
    protected int[][] map;
    /**
     * The Bombs.
     */
    protected List<Bomb> bombs;
    /**
     * The Board.
     */
    protected  Graph<Integer,DefaultEdge> board;
    /**
     * The Paths.
     */
    protected Graph<Integer,DefaultEdge> paths;
    /**
     * The Player position.
     */
    protected Integer playerPosition;
    private Image enemyIcon;
    /**
     * The Mobility.
     */
    protected int mobility;

    /**
     * The Updated.
     */
    protected volatile boolean updated = false;

    /**
     * Instantiates a new Enemy.
     *
     * @param row            the vertical position of the bomb
     * @param col            the horizontal position of the bomb
     * @param color          in basic version the color of the square that symbolises enemy
     * @param mobility       the movement speed of the enemy
     * @param map            two-dimensional int table that represents playing board
     * @param bomb           the array af the active bombs
     * @param delay          the delay for some options
     * @param board          the graph representing possible moves ont the board
     * @param playerPosition the player position
     */
    public Enemy(int row, int col, Color color, int mobility , int[][] map, List<Bomb> bomb , int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.mobility = mobility;
        this.map = map;
        this.bombs = bomb;
        this.playerPosition = playerPosition;
        this.board = board;

        try {
            enemyIcon = ImageIO.read(new File("windows.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    /**
     * Get row int.
     *
     * @return the int - vertical position of the enemy
     */
    public int getRow(){
        return row;
    }

    /**
     * Get col int.
     *
     * @return the int - horizontal position of the enemy
     */
    public int getCol(){
        return col;
    }

    /**
     * Get color Color.
     *
     * @return the color of the square in basic version
     */
    public Color getColor(){
        return color;
    }

    /**
     * Status boolean.
     *
     * @return the boolean representing life of the enemy / true - enemy is alive / false - enemy is dead
     */
    public boolean status(){
        return isAlive;
    }

    /**
     * Move - method that tries to move object anywhere. Used by AI when safe options run out
     */
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

    /**
     * Move to the destination place
     *
     * @param destination the destination of the movement
     */
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

    /**
     * Move up boolean.
     *
     * @return the boolean / true - enemy moved / false - enemy couldn't move
     */
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

    /**
     * Move down boolean.
     *
     * @return the boolean / true - enemy moved / false - enemy couldn't move
     */
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

    /**
     * Move left boolean.
     *
     * @return the boolean / true - enemy moved / false - enemy couldn't move
     */
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

    /**
     * Move right boolean.
     *
     * @return the boolean / true - enemy moved / false - enemy couldn't move
     */
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

    /**
     * Draw the enemy on the board
     *
     * @param g        the board object
     * @param tileSize the tile size
     */
    public void draw(Graphics g, int tileSize) {
        if(isAlive){
            g.setColor(Color.WHITE);
            g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            g.drawImage(enemyIcon, col * tileSize, row * tileSize, tileSize, tileSize, null);

        }
    }

    /**
     * Enemy place bomb. Places the bomb object beneath the enemy if possible
     */
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

    /**
     * Kills the enemy
     */
    protected void kill(){
        isAlive = false;
    }

    /**
     * Is safe boolean. Checks if the current position of the player is safe
     *
     * @return the boolean / true - enemy won't die / false - enemy will die
     */
    protected boolean isSafe(){
        for (Bomb bomb : bombs) {
            int away = Math.abs(100 * row + col - (100 * bomb.getRow() + bomb.getCol()));
            if (away == 1 || away == 100 || away == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is safe boolean. Checks if the given position is safe for the enemy
     *
     * @param x the Integer that stands for the vertex
     * @return the boolean / true - that vertex is safe / false - else
     */
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

    /**
     * Safe bomb placement boolean. Checks if placing the bomb will cause enemy's death
     *
     * @return the boolean / true - when enemy places the bomb it can run / false - else
     */
    protected boolean safeBombPlacement() {
            for(Integer x: paths.vertexSet()){
                if (safeBombPlacement(x) && isSafe(x))
                    return true;
            }
            return false;
    }

    /**
     * Safe bomb placement boolean. Checks if the given vertex is safe for the bomb placement
     *
     * @param x the Integer that stands for the vertex
     * @return the boolean / true - when enemy places the bomb there it can run / false - else
     */
    protected boolean safeBombPlacement(Integer x) {
        int away = Math.abs(x - (100 * row + col));
        return away != 1 && away != 100 && away != 0;
    }

    /**
     * Near player boolean. Checks if the player is one tile close to the enemy
     *
     * @return the boolean / true - player is reachable / false - else
     */
    protected  boolean nearPlayer(){
        int x = Math.abs( row * 100 + col - playerPosition ) ;
        return x == 1 || x == 100;
    }

    /**
     * Updates the parameters which are given by the board - current game status
     *
     * @param x     the player position as a vertex
     * @param z     the list with the current placed bombs
     * @param graph the graph of the board after explosions
     */
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

    /**
     * Gets random set element.
     *
     * @param <E> the type parameter
     * @param set the set
     * @return the random set element
     */
    static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }


    /**
     * Euclidean norm double. Takes two points of vertex that stands for position 100y+x as alias
     *
     * @param x the first object position as 100x+y
     * @param y the second object position
     * @return the double Euclidean norm between two points
     */
    static protected double euclideanNorm(int x, int y){
        int x1 = Math.floorDiv(x, 100);
        int y1 = x - x1*100;
        int x2 = Math.floorDiv(y, 100);
        int y2 = y - x2*100;

        return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }

}
