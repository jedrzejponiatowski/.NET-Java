import java.awt.Color;
import java.awt.Graphics;

/**
 * The type Bomb. Class that represents bombs and places them on the board.
 */
public class Bomb {
    private final int row;
    private final int col;
    private Color color;
    private int countdown;
    private long startTime;

    /**
     * Instantiates a new Bomb.
     *
     * @param row       [int] vertical position of the bomb
     * @param col       horizontal position of the bomb
     * @param color     in basic version the color of the square symbolising bomb
     * @param countdown the countdown
     */
    public Bomb(int row, int col, Color color, int countdown) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.countdown = countdown;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Draw bomb object on the board
     *
     * @param g        board object
     * @param tileSize size of the one square's side
     */
    public void draw(Graphics g, int tileSize) {
        g.setColor(color);
        g.fillOval(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    /**
     * Countdown tick.
     */
    public void countdownTick() {
        countdown--;
    }

    /**
     * Is exploded boolean.
     *
     * @return the boolean / true - countdown is 0 / false - else
     */
    public boolean isExploded() {
        return countdown == 0;
    }

    /**
     * Gets row.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets col.
     *
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * Is expired boolean. Checks if the bomb is on the map more or equal to 3s
     *
     * @return the boolean / true - bomb's life time >= 3s / false - else
     */
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return currentTime - startTime >= 3000;
    }

    /**
     * Sets expired.
     */
    public void setExpired()
    {
        startTime -= 3000;
    }

}