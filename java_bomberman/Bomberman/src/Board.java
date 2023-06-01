import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 40;
    private static final int ROWS = 11;
    private static final int COLS = 17;
    private Player player;
    private int[][] map;
    public Board() {
        map = new int[ROWS][COLS];
        player = new Player(0, 0, Color.BLUE, map);
        // Inicjalizacja mapy gry
        for (int row = 1; row < ROWS - 1; row += 2) {
            for (int col = 1; col < COLS - 1; col += 2) {
                map[row][col] = 2;
            }
        }

        // Dodanie losowych ścian
        Random random = new Random();
        for (int row = 1; row < ROWS - 1; row += 2) {
            for (int col = 1; col < COLS - 1; col += 2) {
                int randomRow = row;
                int randomCol = col;

                // Wylosowanie losowego sąsiada
                do {
                    randomRow = row + random.nextInt(3) - 1;
                    randomCol = col + random.nextInt(3) - 1;
                } while (map[randomRow][randomCol] != 0);

                // Ustawienie ściany w losowym sąsiedzie
                map[randomRow][randomCol] = 1;
            }
        }

        //Usunięcie scian z narożnika
        map[0][0]=0;
        map[1][0]=0;
        map[0][1]=0;

        map[ROWS-1][COLS-1]=0;
        map[ROWS-2][COLS-1]=0;
        map[ROWS-1][COLS-2]=0;

        map[0][COLS-1]=0;
        map[0][COLS-2]=0;
        map[1][COLS-1]=0;

        map[ROWS-1][0]=0;
        map[ROWS-2][0]=0;
        map[ROWS-1][1]=0;

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rysowanie planszy
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int tile = map[row][col];
                if (tile == 2) {
                    // Rysowanie ściany
                    g.setColor(Color.BLACK);
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else if (tile ==  1){
                    // Rysowanie pustego pola
                    g.setColor(Color.GRAY);
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        // Rysowanie gracza
        player.draw(g, TILE_SIZE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP: {
                player.moveUp();
                break;
            }
            case KeyEvent.VK_DOWN: {
                player.moveDown(ROWS);
                break;
            }
            case KeyEvent.VK_LEFT: {
                player.moveLeft();
                break;
            }
            case KeyEvent.VK_RIGHT: {
                player.moveRight(COLS);
                break;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}

