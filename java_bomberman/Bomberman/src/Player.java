import java.awt.Color;
import java.awt.Graphics;

public class Player {
    private int row;
    private int col;
    private Color color;

    public Player(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void moveUp() {
        row = Math.max(0, row - 1);
    }

    public void moveDown(int maxRows) {
        row = Math.min(maxRows - 1, row + 1);
    }

    public void moveLeft() {
        col = Math.max(0, col - 1);
    }

    public void moveRight(int maxCols) {
        col = Math.min(maxCols - 1, col + 1);
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(color);
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
    }
}
