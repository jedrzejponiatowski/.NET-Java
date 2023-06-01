import java.awt.Color;
import java.awt.Graphics;

public class Bomb {
    private int row;
    private int col;
    private Color color;
    private int countdown;
    private long startTime;

    public Bomb(int row, int col, Color color, int countdown) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.countdown = countdown;
        this.startTime = System.currentTimeMillis();
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(color);
        g.fillOval(col * tileSize, row * tileSize, tileSize, tileSize);
    }

    public void countdownTick() {
        countdown--;
    }

    public boolean isExploded() {
        return countdown == 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        return currentTime - startTime >= 3000;
    }

}