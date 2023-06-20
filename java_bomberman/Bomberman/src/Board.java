import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.Image;
import javax.imageio.ImageIO;


import org.jgrapht.Graph;
import org.jgrapht.graph.*;


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
    private JFrame rankingFrame;
    private Timer boardTimer;
    private Timer gameTimer;
    private Image bombIcon, blackWall, woodWall, explosionIcon;

    private int gameTime;


    public Board() {
        gameTime = 0;
        bombs = new ArrayList<>();
        map = new int[ROWS][COLS];
        rankingFrame = new JFrame("Ranking");


        player = new Player(0,0 , Color.BLUE, map);
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
        enemies.add(new Freaky(0, 16, Color.PINK,450, map, bombs,6000,paths,playerPosition));
        enemies.add(new Aggressive(10, 0, Color.RED,450, map, bombs,3000,paths,playerPosition));

        for (Enemy enemy : enemies) {
            Thread enemyThread = new Thread(enemy);
            enemyThread.start();
        }


        rankingFrame = new JFrame("Ranking");
        rankingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rankingFrame.setLocationRelativeTo(null);

        try {
            bombIcon = ImageIO.read(new File("bomb2.png"));
            blackWall = ImageIO.read(new File("black_wall.jpg"));
            woodWall = ImageIO.read(new File("wood.png"));
            explosionIcon = ImageIO.read(new File("explosion.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Kod do wykonania przy każdym odświeżeniu planszy
        // Odświeżenie planszy
        boardTimer = new Timer(15, e -> {
            // Kod do wykonania przy każdym odświeżeniu planszy
            repaint(); // Odświeżenie planszy

            checkDamages();

            update();
        });
        boardTimer.start();

        gameTimer = new Timer(1000, e-> ++gameTime);
        gameTimer.start();

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE + 50));
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
                    case 0, 5  -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                    case 1 -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        g.drawImage(woodWall, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    case 2 -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        g.drawImage(blackWall, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    case 3 -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        g.drawImage(bombIcon, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                    case 4 -> {
                        g.setColor(Color.WHITE);
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                        g.drawImage(explosionIcon, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
        for(int col = 0; col <COLS; ++col){
            g.setColor(Color.darkGray);
            g.fillRect(col*TILE_SIZE,ROWS*TILE_SIZE, TILE_SIZE, 50);
        }
        drawTimer(g,gameTime);
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

                //g.fillRect(bombCol * TILE_SIZE, bombRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);


                //g.setColor(Color.WHITE);
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
          boardTimer.stop();
          gameTimer.stop();
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
        Timer explosionTimer = new Timer(500, e -> removeExpiredExplosions(row, col));
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
            showRanking();
        }
        else{
            msg = "Game Over";
            g.setColor(Color.red);
        }
        Font big = new Font("Helvetica", Font.BOLD, 50);

        g.setFont(big);
        g.drawString(msg, (ROWS * TILE_SIZE) / 2,
                COLS*TILE_SIZE / 4);
        showRanking();
    }


    private void drawTimer(Graphics g, int seconds){
        StringBuilder builder = new StringBuilder();
        int minutes = Math.floorDiv(seconds,60);
        int sec = seconds - minutes * 60;
        builder.append(minutes);
        builder.append(':');
        builder.append(sec);
        String time = builder.toString();
        g.setColor(Color.WHITE);
        Font big = new Font("Helvetica", Font.ITALIC, 30);
        g.setFont(big);


        g.drawString(time, (ROWS * TILE_SIZE) / 2 + TILE_SIZE * 2,
                (COLS - 5 )*TILE_SIZE);
    }


    private void showRanking() {
        try {
            File file = new File("ranking.txt");
            Scanner scanner = new Scanner(file);

            List<String> rankings = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                rankings.add(line);
            }

            scanner.close();

            // Sortowanie wyników w odwrotnej kolejności (od najwyższych punktów do najniższych)
            Collections.sort(rankings, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int score1 = extractScore(o1);
                    int score2 = extractScore(o2);
                    return Integer.compare(score1, score2);
                }
            });

            rankingFrame.getContentPane().removeAll();

            // Dodanie napisu "SCORE: X" na górze okna rankingowego
            JPanel scorePanel = new JPanel();
            JLabel scoreLabel = new JLabel("SCORE: " + gameTime);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            scorePanel.add(scoreLabel);
            rankingFrame.getContentPane().add(scorePanel, BorderLayout.NORTH);

            // Utworzenie panelu z dwoma kolumnami
            JPanel rankingPanel = new JPanel(new GridLayout(0, 2));
            rankingFrame.getContentPane().add(rankingPanel, BorderLayout.CENTER);

            JPanel nicknamePanel = new JPanel();
            JTextField nicknameField = new JTextField(20);
            JButton submitButton = new JButton("Submit");
            nicknamePanel.add(nicknameField);
            nicknamePanel.add(submitButton);
            rankingFrame.getContentPane().add(nicknamePanel, BorderLayout.SOUTH);

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nickname = nicknameField.getText();
                    if(nickname.length() == 0)
                        nickname = "no_name";
                    String newRecord = nickname + " " + gameTime;

                    try {
                        FileWriter writer = new FileWriter("ranking.txt", true);
                        writer.write(newRecord + "\n");
                        writer.close();
                        System.out.println("Nowy wynik został zapisany.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            int count = 0;
            for (String line : rankings) {
                // Podział linii na nickname i score
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    String nickname = parts[0];
                    String score = parts[1];
                    // Utworzenie dwóch etykiet dla nickname i score
                    JLabel nicknameLabel = new JLabel(nickname);
                    JLabel scoreLabel2 = new JLabel(score);
                    // Wyśrodkowanie tekstu na etykietach
                    nicknameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    scoreLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                    // Ustawienie niestandardowego fontu
                    nicknameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    scoreLabel2.setFont(new Font("Arial", Font.PLAIN, 14));
                    // Dodanie etykiet do panelu
                    rankingPanel.add(nicknameLabel);
                    rankingPanel.add(scoreLabel2);
                    count++;
                    if (count >= 10) {
                        break; // Wyświetl tylko 10 najlepszych wyników
                    }
                }
            }
            rankingFrame.setSize(300, 400);
            rankingFrame.pack();

            rankingFrame.setVisible(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int extractScore(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length >= 2) {
            String score = parts[1];
            try {
                return Integer.parseInt(score);
            } catch (NumberFormatException e) {
                // Jeżeli nie można sparsować liczby, zwróć wartość domyślną (0 lub inną)
            }
        }
        return 0; // Domyślna wartość punktacji
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

