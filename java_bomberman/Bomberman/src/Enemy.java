import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
//import java.util.Timer;
import javax.swing.Timer;

public class Enemy implements Runnable {
    private static final int ROWS = 11;
    private static final int COLS = 17;
    private boolean isAlive = true;
    private int row;
    private int col;
    private Color color;
    private int[][] map;
    private List<Bomb> bombs;

    public Enemy(int row, int col, Color color, int[][] map, List<Bomb> bomb ) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.map = map;
        this.bombs = bomb;

        // Kod do wykonania przy każdym odświeżeniu planszy
        Timer enemyBombTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kod do wykonania przy każdym odświeżeniu planszy
                if (isAlive)
                    enemyPlaceBomb();
            }
        });
        enemyBombTimer.start();
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

    public void run() {
        Random random = new Random();

        while (isAlive && !Thread.currentThread().isInterrupted()) {
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

    private void moveUp() {
        int newRow = row - 1;
        if (newRow >= 0 && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
    }

    private void moveDown() {
        int newRow = row + 1;
        if (newRow < ROWS && map[newRow][col] == 0) {
            map[row][col] = 0;
            row = newRow;
        }
    }

    private void moveLeft() {
        int newCol = col - 1;
        if (newCol >= 0 && map[row][newCol] == 0) {
            map[row][col] = 0;
            col = newCol;
        }
    }

    private void moveRight() {
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

    private void enemyPlaceBomb(){
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

    public void kill(){
        isAlive = false;
        col = -10;
        row = -10;
    }

    public boolean status(){
        return isAlive;
    }
}
