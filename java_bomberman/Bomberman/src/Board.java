import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 40;
    private static final int ROWS = 11;
    private static final int COLS = 17;
    private boolean ingame = true;
    private boolean victory = false;
    private Player player;
    private int[][] map;
    private List<Bomb> bombs;
    private List<Enemy> enemies;
    private Timer boardTimer;
    public Board() {
        bombs = new ArrayList<>();
        map = new int[ROWS][COLS];
        enemies = new ArrayList<>(3);
        enemies.add(new Enemy(10, 16, Color.RED, map, bombs));
        enemies.add(new Enemy(0, 16, Color.RED, map, bombs));
        enemies.add(new Enemy(10, 0, Color.RED, map, bombs));

        for (Enemy enemy : enemies) {
            Thread enemyThread = new Thread(enemy);
            enemyThread.start();
        }

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

        boardTimer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kod do wykonania przy każdym odświeżeniu planszy


                repaint(); // Odświeżenie planszy
            }
        });
        boardTimer.start();

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 0 - puste pole
        // 1 - miekka sciana
        // 2 - twarda sciana
        // 3 - bomba
        // 4 - eksplozja
        // 5 - kolizja

//        if(!ingame)
//            drawGameEnd(g);
        checkDamages();
        if(map[player.getRow()][player.getCol()] != 4)
            map[player.getRow()][player.getCol()] = 5;
        for(Enemy enemy: enemies){
            if(map[enemy.getRow()][enemy.getCol()]!= 4)
                map[enemy.getRow()][enemy.getCol()] = 5;
        }
        // Rysowanie planszy
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int tile = map[row][col];
                switch (tile)
                {
                    case 0:
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                    case 1:
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                    case 2:
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                    case 3:
                        g.setColor(Color.BLACK);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                    case 4:
                        g.setColor(Color.YELLOW);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                    case 5:
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        break;
                }
            }
        }

        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            if (!bomb.isExpired()) {
                int bombRow = bomb.getRow();
                int bombCol = bomb.getCol();
                map[bombRow][bombCol] = 3;
                g.setColor(Color.BLACK);
                g.fillRect(bombCol * TILE_SIZE, bombRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else {
                int bombRow = bomb.getRow();
                int bombCol = bomb.getCol();
                placeBombExplosion(bombRow, bombCol);
                bombIterator.remove();
            }
        }

        // Rysowania przeciwników
        for (Enemy enemy : enemies)
        {
            enemy.draw(g, TILE_SIZE);
        }
        // Rysowanie gracza
        player.draw(g, TILE_SIZE);
    }


    private void placeBombExplosion(int row, int col) {
        // Ustawienie wybuchu bomby i efektów wybuchu wokół niej
        map[row][col] = 4; // Wybuch na pozycji bomby
        if (row > 0) {
            if(map[row - 1][col] != 2 && map[row - 1][col] != 3)
                map[row - 1][col] = 4; // Wybuch na górze
        }
        if (row < ROWS - 1) {
            if(map[row + 1][col] != 2 && map[row + 1][col] != 3)
                map[row + 1][col] = 4; // Wybuch na dole
        }
        if (col > 0) {
            if(map[row][col - 1] != 2 && map[row][col - 1] != 3)
                map[row][col - 1] = 4; // Wybuch po lewej
        }
        if (col < COLS - 1) {
            if(map[row][col + 1] != 2 && map[row][col + 1] != 3 )
                map[row][col + 1] = 4; // Wybuch po prawej
        }
        repaint();

        // Opóźnienie efektu wybuchu
        Timer explosionTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExpiredExplosions(row, col);
                //repaint();
            }
        });
        explosionTimer.setRepeats(false); // Timer wykonuje się tylko raz
        explosionTimer.start();
    }
/*
    private void removeExpiredExplosions() {
        // Usunięcie wygasłych efektów wybuchu
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (map[row][col] == 3) {
                    map[row][col] = 0; // Efekt wybuchu
                } else if (map[row][col] == 4) {
                    map[row][col] = 0; // Puste pole
                }
            }
        }
        removeExpiredBombs();
    }

 */
private void removeExpiredExplosions(int row, int col) {
    map[row][col] = 0; // Usunięcie wybuchu na pozycji bomby

    if (row > 0) {
        if (map[row - 1][col] != 2 && map[row - 1][col] != 3)
            map[row - 1][col] = 0;
    }
    if (row < ROWS - 1) {
        if (map[row + 1][col] != 2 && map[row + 1][col] != 3)
            map[row + 1][col] = 0;
    }
    if (col > 0) {
        if (map[row][col - 1] != 2 && map[row][col - 1] != 3)
            map[row][col - 1] = 0;
    }
    if (col < COLS - 1) {
        if (map[row][col + 1] != 2 && map[row][col + 1] != 3)
            map[row][col + 1] = 0;
    }
    repaint();
}

    private void updateExplosion() {
        // Aktualizacja efektu wybuchu
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int tile = map[row][col];
                if (tile == 3) {
                    // Zmiana wybuchu na efekt wybuchu
                    map[row][col] = 4;
                } else if (tile == 4) {
                    // Wygaszanie efektu wybuchu
                    map[row][col] = 1;
                }
            }
        }
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
            case KeyEvent.VK_SPACE: {
                placeBomb();
                break;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private void placeBomb() {
        int playerRow = player.getRow();
        int playerCol = player.getCol();

        // Sprawdź, czy na tym polu nie ma już bomby
        for (Bomb bomb : bombs) {
            if (bomb.getRow() == playerRow && bomb.getCol() == playerCol) {
                return; // Jeśli jest już bomba na tym polu, nie można postawić kolejnej
            }
        }

        // Dodaj nową bombę na aktualne położenie gracza
        Bomb newBomb = new Bomb(playerRow, playerCol, Color.RED, 3);
        bombs.add(newBomb);
    }

    private void checkDamages(){
        for(Enemy enemy: enemies){
            if(map[enemy.getRow()][enemy.getCol()] == 4){
                enemy.kill();
                enemies.remove(enemy);
                enemy = null;
            }
        }
        if(enemies.isEmpty()){
            ingame = false;
            victory = true;
        }
        if(map[player.getRow()][player.getCol()] == 4){
            ingame = false;
            victory = false;
        }
    }


    private void drawGameEnd(Graphics g) {
        String msg;
        if(victory){
            msg = "Victory!";
            g.setColor(Color.green);
        }
        else{
            msg = "Game Over";
            g.setColor(Color.red);
        }
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(small);

        g.setFont(small);
        g.drawString(msg, (ROWS * TILE_SIZE - fm.stringWidth(msg)) / 2,
                COLS*TILE_SIZE / 2);
    }




/*
    private void removeExpiredBombs() {
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            if (bomb.isExpired()) {
                iterator.remove();
            }
        }
    }*/




}

