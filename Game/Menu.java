package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JPanel {
    public Menu(GamePanel parent) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));

        startButton.addActionListener(e -> parent.startGame());
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridy = 0;
        add(startButton, gbc);
        gbc.gridy++;
        add(exitButton, gbc);
    }
}

