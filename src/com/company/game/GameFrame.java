package com.company.game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {
    JButton resetButton;
    GamePanel gamePanel;


    public GameFrame() {


        this.add(new GamePanel());
        this.setTitle("Snako");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLayout(null);

        resetButton = new JButton();
        resetButton.setText("Reset");
        resetButton.setSize(100, 50);
        resetButton.setLocation(0, 200);
        resetButton.addActionListener(this);

        this.add(resetButton);

        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            this.remove(gamePanel);
            gamePanel = new GamePanel();

        }
    }
}
