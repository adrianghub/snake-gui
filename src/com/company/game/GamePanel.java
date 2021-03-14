package com.company.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 640;
    static final int SCREEN_HEIGHT = 640;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT / UNIT_SIZE);
    static final int DELAY = 55;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 5;
    int birdsEaten;
    int feedX;
    int feedY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    static boolean gameOn = false;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newFlyingBird();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void pause() {
        GamePanel.gameOn = true;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = false;
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            // Generate grid
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */
            g.setColor(Color.MAGENTA.brighter());
            g.fillRect(feedX, feedY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(new Color(255, 150, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(210, 80, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.green);
            g.setFont(new Font("Courier New", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + birdsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + birdsEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newFlyingBird() {
        feedX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        feedY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFeed() {
        if ((x[0] == feedX) && (y[0] == feedY)) {
            bodyParts++;
            birdsEaten++;
            if (birdsEaten > 1) {
                birdsEaten += random.nextInt(50) + 1;
            }
            newFlyingBird();
        }
    }

    public void checkCollisions() {
        // Check if snake head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Display score text
        g.setColor(Color.green);
        g.setFont(new Font("Courier New", Font.BOLD, 35));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + birdsEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + birdsEaten)) / 2, g.getFont().getSize());
        // Display game over text
        g.setColor(Color.red);
        g.setFont(new Font("Courier New", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFeed();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (GamePanel.gameOn) {
                        resume();
                    } else {
                        pause();
                    }
                    break;
            }
        }
    }
}
