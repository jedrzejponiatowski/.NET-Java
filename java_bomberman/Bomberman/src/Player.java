import java.awt.*;

public class Player {
    private int row;
    private int col;
    private final Color color;
    private final int[][] map;


    public Player(int row, int col, Color color, int[][] map) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.map = map;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Integer moveUp() {
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        return row*100 + col;
    }

    public Integer moveDown(int rows) {
        int newRow = row + 1;
        if (newRow < rows && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        return row*100 + col;
    }

    public Integer moveLeft() {
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        return row*100 + col;
    }

    public Integer moveRight(int cols) {
        int newCol = col + 1;
        if (newCol < cols && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        return row*100 + col;
    }

    public void draw(Graphics g, int tileSize) {
        g.setColor(color);
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
    }
}
