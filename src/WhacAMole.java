import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 650;
    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton resetButton = new JButton("Reset");

    JButton[] board = new JButton[9];
    ImageIcon moleIcon;
    ImageIcon plantIcon;
    JButton currMoleTile;
    ArrayList<JButton> currPlantTiles = new ArrayList<>();

    Random random = new Random();
    Timer setMoleTimer;
    Timer setPlantTimer;
    int score = 0;
    int highScore = 0;

    WhacAMole() {
        frame.setSize(boardHeight, boardWidth);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: 0  High Score: 0");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        resetButton.addActionListener(e -> resetGame());
        textPanel.add(resetButton, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        frame.add(boardPanel);

        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        textLabel.setText("Score: " + score + "  High Score: " + highScore);
                    } else if (currPlantTiles.contains(tile)) {
                        textLabel.setText("Game Over! Final Score: " + score);
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (JButton btn : board) {
                            btn.setEnabled(false);
                        }
                        if (score > highScore) {
                            highScore = score;
                        }
                    }
                }
            });
        }

        setMoleTimer = new Timer(1000, e -> {
            if (currMoleTile != null) {
                currMoleTile.setIcon(null);
                currMoleTile = null;
            }
            int num = random.nextInt(9);
            JButton tile = board[num];

            if (currPlantTiles.contains(tile)) return;

            currMoleTile = tile;
            currMoleTile.setIcon(moleIcon);
        });

        setPlantTimer = new Timer(1500, e -> {
            for (JButton plantTile : currPlantTiles) {
                plantTile.setIcon(null);
            }
            currPlantTiles.clear();
            
            int plantCount = 2; // Number of piranha plants
            while (currPlantTiles.size() < plantCount) {
                int num = random.nextInt(9);
                JButton tile = board[num];

                if (tile != currMoleTile && !currPlantTiles.contains(tile)) {
                    currPlantTiles.add(tile);
                    tile.setIcon(plantIcon);
                }
            }
        });

        setMoleTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }

    void resetGame() {
        score = 0;
        textLabel.setText("Score: 0  High Score: " + highScore);
        for (JButton btn : board) {
            btn.setEnabled(true);
            btn.setIcon(null);
        }
        currMoleTile = null;
        currPlantTiles.clear();
        setMoleTimer.start();
        setPlantTimer.start();
    }
}