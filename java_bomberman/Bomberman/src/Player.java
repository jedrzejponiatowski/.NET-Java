import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * The type Player. Class implements Player object and all its parameters and methods
 */
public class Player {
    private int row;
    private int col;
    private final Color color;
    private final int[][] map;
    private Image playerIcon;

    /**
     * Parameter constructor of the class Player.
     * @param row [int] - vertical position of the player
     * @param col [int] - horizontal position of the player
     * @param color [color] - in the basic version color of the square symbolising the player
     * @param map [int[][]] - two dimensional table of int that creates the board
     */
    public Player(int row, int col, Color color, int[][] map) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.map = map;

        try {
            playerIcon = ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/linux.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Interface of the class
     * @return row [int] - player's vertical position
     */
    public int getRow() {
        return row;
    }

    /**
     * Interface of the class
     * @return col [int] - player's horizontal position
     */
    public int getCol() {
        return col;
    }

    /**
     * Moves player up
     * @return [Integer] - an object that represents number of the vertex in the graph where player is
     */
    public Integer moveUp() {
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        return row*100 + col;
    }
    /**
     * Moves player down
     * @return [Integer] - an object that represents number of the vertex in the graph where player is
     */
    public Integer moveDown(int rows) {
        int newRow = row + 1;
        if (newRow < rows && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
        return row*100 + col;
    }
    /**
     * Moves player left
     * @return [Integer] - an object that represents number of the vertex in the graph where player is
     */
    public Integer moveLeft() {
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        return row*100 + col;
    }
    /**
     * Moves player right
     * @return [Integer] - an object that represents number of the vertex in the graph where player is
     */
    public Integer moveRight(int cols) {
        int newCol = col + 1;
        if (newCol < cols && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
        return row*100 + col;
    }

    /**
     * Method that draws player's object on the board
     * @param g [Graphics] - a board object
     * @param tileSize [int] - size of the one side of the square that creates board
     */
    public void draw(Graphics g, int tileSize) {
        g.setColor(Color.WHITE);
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
        g.drawImage(playerIcon, col * tileSize, row * tileSize, tileSize, tileSize, null);
    }
}
