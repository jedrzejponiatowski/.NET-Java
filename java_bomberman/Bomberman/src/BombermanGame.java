import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BombermanGame extends JPanel implements KeyListener {
    private static final int TILE_SIZE = 40;
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private int playerRow;
    private int playerCol;

    public BombermanGame() {
        playerRow = 0;
        playerCol = 0;

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Rysowanie gracza na planszy
        g.setColor(Color.BLUE);
        g.fillRect(playerCol * TILE_SIZE, playerRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                playerRow = Math.max(0, playerRow - 1);
                break;
            case KeyEvent.VK_DOWN:
                playerRow = Math.min(ROWS - 1, playerRow + 1);
                break;
            case KeyEvent.VK_LEFT:
                playerCol = Math.max(0, playerCol - 1);
                break;
            case KeyEvent.VK_RIGHT:
                playerCol = Math.min(COLS - 1, playerCol + 1);
                break;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bomberman Game");
        BombermanGame game = new BombermanGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
