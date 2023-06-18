import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.*;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.*;
import org.jgrapht.alg.connectivity.*;


public class Board extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 40;
    private static final int ROWS = 11;
    private static final int COLS = 17;
    private boolean inGame = true;
    private boolean victory = false;
    private final Player player;
    private Integer playerPosition;
    private final int[][] map;
    private final List<Bomb> bombs;
    private final List<Enemy> enemies;
    private final Graph<Integer,DefaultEdge> paths;

    public Board() {
        bombs = new ArrayList<>();
        map = new int[ROWS][COLS];


        player = new Player(0,16 , Color.BLUE, map);
        this.playerPosition = player.getRow() * 100 + player.getCol();
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
                int randomRow;
                int randomCol;

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


        // Graf dozwolonych miejsc oraz mozliwe polaczenia

         paths = new DefaultUndirectedGraph<>(DefaultEdge.class);
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1) {
                if (map[row][col] == 0 || map[row][col] == 1)
                    paths.addVertex(row * 100 + col);
                if(col != 0){
                    if(map[row][col] == 0 && map[row][col-1] == 0)
                        paths.addEdge(row * 100 + col - 1, row * 100 + col);
                }
                if(row != 0){
                    if(map[row][col] == 0 && map[row-1][col] == 0)
                        paths.addEdge(row * 100 + col, (row - 1) * 100 + col);
                }
            }
        }

        enemies = new ArrayList<>(3);
        enemies.add(new Cowardly(10, 16, Color.GREEN,600, map, bombs, 6000,paths,playerPosition));
        //enemies.add(new Ordinary(0, 16, Color.RED, map, bombs,6000,paths,playerPosition));
        //enemies.add(new Aggressive(10, 0, Color.RED,400, map, bombs,3000,paths,playerPosition));

        for (Enemy enemy : enemies) {
            Thread enemyThread = new Thread(enemy);
            enemyThread.start();
        }

        // Kod do wykonania przy każdym odświeżeniu planszy
        // Odświeżenie planszy
        Timer boardTimer = new Timer(15, e -> {
            // Kod do wykonania przy każdym odświeżeniu planszy
            repaint(); // Odświeżenie planszy

            checkDamages();

            update();
        });
        boardTimer.start();

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);
    }


    private  void drawBackground(Graphics g){
        // 0 - puste pole
        // 1 - miekka sciana
        // 2 - twarda sciana
        // 3 - bomba
        // 4 - eksplozja
        // 5 - kolizja

        // Ustawienie graczy
        if(map[player.getRow()][player.getCol()] != 4 && map[player.getRow()][player.getCol()] != 3 )
            map[player.getRow()][player.getCol()] = 5;
        for(Enemy enemy: enemies){
            if(map[enemy.getRow()][enemy.getCol()]!= 4 && map[player.getRow()][player.getCol()] != 3)
                map[enemy.getRow()][enemy.getCol()] = 5;
        }
        // Rysowanie planszy
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int tile = map[row][col];
                switch (tile) {
                    case 0, 5 -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case 1 -> {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case 2 -> {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case 3 -> {
                        g.setColor(Color.BLACK);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case 4 -> {
                        g.setColor(Color.YELLOW);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
    }

    private void drawBombs(Graphics g){
        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            if (!bomb.isExpired()) {
                int bombRow = bomb.getRow();
                int bombCol = bomb.getCol();
                map[bombRow][bombCol] = 3;
                paths.removeAllEdges(new ArrayList<>(paths.edgesOf(bombRow*100+bombCol)));
                g.setColor(Color.BLACK);
                g.fillRect(bombCol * TILE_SIZE, bombRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            } else {
                int bombRow = bomb.getRow();
                int bombCol = bomb.getCol();
                placeBombExplosion(bombRow, bombCol);
                bombIterator.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

      if(inGame){
          drawBackground(g);
          drawBombs(g);
          // Rysowanie przeciwnikow
          for (Enemy enemy : enemies)
          {
              if(enemy.status())
                  enemy.draw(g, TILE_SIZE);
          }
          // Rysowanie gracza
          player.draw(g, TILE_SIZE);
      }else{
          drawBackground(g);
          player.draw(g, TILE_SIZE);
          drawGameEnd(g);
      }
    }


    private void placeBombExplosion(int row, int col) {
        // Ustawienie wybuchu bomby i efektów wybuchu wokół niej
        map[row][col] = 4; // Wybuch na pozycji bomby
        if (row > 0) {
            if(map[row - 1][col] != 2)
                map[row - 1][col] = 4; // Wybuch na górze
        }
        if (row < ROWS - 1) {
            if(map[row + 1][col] != 2 )
                map[row + 1][col] = 4; // Wybuch na dole
        }
        if (col > 0) {
            if(map[row][col - 1] != 2 )
                map[row][col - 1] = 4; // Wybuch po lewej
        }
        if (col < COLS - 1) {
            if(map[row][col + 1] != 2  )
                map[row][col + 1] = 4; // Wybuch po prawej
        }
        checkDamages();
        repaint();

        // Opóźnienie efektu wybuchu
        Timer explosionTimer = new Timer(500, e -> {
            removeExpiredExplosions(row, col);
            //repaint();
        });
        explosionTimer.setRepeats(false); // Timer wykonuje się tylko raz
        explosionTimer.start();
    }
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

/*    private void updateExplosion() {
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
    }*/

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP -> playerPosition =  player.moveUp();
            case KeyEvent.VK_DOWN -> playerPosition =  player.moveDown(ROWS);
            case KeyEvent.VK_LEFT -> playerPosition =  player.moveLeft();
            case KeyEvent.VK_RIGHT -> playerPosition =  player.moveRight(COLS);
            case KeyEvent.VK_SPACE -> placeBomb();
            default -> throw new IllegalStateException("Unexpected value: " + keyCode);
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

        if(enemies.isEmpty()){
            inGame = false;
            victory = true;
        }else{
            List<Enemy> losers = new ArrayList<>();
            for(Enemy enemy: enemies){
                if(map[enemy.getRow()][enemy.getCol()] == 4){
                    enemy.kill();
                    losers.add(enemy);
                }
            }
            enemies.removeAll(losers);
        }
        if(map[player.getRow()][player.getCol()] == 4){
            inGame = false;
            victory = false;
        }
    }


    private void drawGameEnd(Graphics g){
        String msg;
        if(victory){
            msg = "Victory!";
            g.setColor(Color.green);
        }
        else{
            msg = "Game Over";
            g.setColor(Color.red);
        }
        Font big = new Font("Helvetica", Font.BOLD, 50);

        g.setFont(big);
        g.drawString(msg, (ROWS * TILE_SIZE) / 2,
                COLS*TILE_SIZE / 3);
    }


    private synchronized void update(){
        for (int row = 0; row < ROWS; row += 1) {
            for (int col = 0; col < COLS; col += 1 ) {
                if(col != 0){
                    if( map[row][col] == 0 && map[row][col-1] == 0 )
                        paths.addEdge(row * 100 + col - 1, row * 100 + col);
                }
                if(row != 0){
                    if(map[row][col] == 0 && map[row-1][col] == 0)
                        paths.addEdge(row * 100 + col, (row - 1) * 100 + col);
                }
            }
        }
        enemies.forEach(enemy -> enemy.update(playerPosition, bombs, paths));
    }
}

