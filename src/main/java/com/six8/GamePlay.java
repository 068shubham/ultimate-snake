package com.six8;

import com.six8.engine.enums.Direction;
import com.six8.engine.enums.GameState;
import com.six8.engine.listeners.MoveUpdateListener;
import com.six8.engine.managers.GameManager;
import com.six8.engine.managers.impl.BasicSnakeGameManagerImpl;
import com.six8.gui.GuiOutputManager;
import com.six8.gui.impl.ConsoleGuiOutputManagerImpl;
import com.six8.gui.impl.JFrameGuiOutputManagerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GamePlay implements KeyListener, ActionListener, FocusListener, MoveUpdateListener {

    JButton button;
    JTextArea displayArea;
    JTextArea messageArea;
    JTextArea scoreArea;
    GameManager gameManager;
    GuiOutputManager guiOutputManager;
    GuiOutputManager consoleOutputManager = new ConsoleGuiOutputManagerImpl();
    boolean shiftDown = false;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GamePlay gamePlay = new GamePlay();
            gamePlay.init();
        });
    }

    private void init() {

        //Set up the content pane.
        this.addComponentsToPane();

        this.gameManager = new BasicSnakeGameManagerImpl(15, 20);
        this.gameManager.addMoveListener(this);
        this.guiOutputManager = new JFrameGuiOutputManagerImpl(scoreArea, messageArea, displayArea);

    }

    private void addComponentsToPane() {

        scoreArea = new JTextArea();
        scoreArea.setEditable(false);
        scoreArea.setPreferredSize(new Dimension(400, 50));

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.addKeyListener(this);
        displayArea.addFocusListener(this);
        displayArea.setPreferredSize(new Dimension(400, 500));

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBounds(0, 80, 400, 50);
        messageArea.setPreferredSize(new Dimension(400, 50));

        button = new JButton("Play");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 50));

        JFrame jFrame = new JFrame();//creating instance of JFrame
        jFrame.addFocusListener(this);
        jFrame.setSize(500, 600);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.add(scoreArea, BorderLayout.NORTH);
        jFrame.add(displayArea, BorderLayout.CENTER);
        jFrame.add(messageArea, BorderLayout.SOUTH);
        jFrame.add(button, BorderLayout.WEST);

        jFrame.setVisible(true);//making the frame visible

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        this.consoleOutputManager.showMessage("keyTyped keyCode: " + keyEvent.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        this.consoleOutputManager.showMessage("keyPressed keyCode: " + keyEvent.getKeyCode());
        if (keyEvent.getKeyCode() == 16) {
            this.shiftDown = true;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        this.consoleOutputManager.showMessage("keyReleased keyCode: " + keyEvent.getKeyCode());
        switch (keyEvent.getKeyCode()) {
            case 16:
                this.shiftDown = false;
                break;
            case 32:
                if (this.gameManager.getState() == GameState.IN_PLAY) {
                    this.gameManager.increaseMoveSpeed(this.shiftDown ? -2 : 2);
                    this.guiOutputManager.showMessage("Speed changed to: " + this.gameManager.getSpeed());
                } else {
                    this.startGame();
                }
                break;
            case 37:
            case 65:
                this.gameManager.changeDirection(Direction.LEFT);
                break;
            case 38:
            case 87:
                this.gameManager.changeDirection(Direction.UP);
                break;
            case 39:
            case 68:
                this.gameManager.changeDirection(Direction.RIGHT);
                break;
            case 40:
            case 83:
                this.gameManager.changeDirection(Direction.DOWN);
                break;
        }
    }

    private void startGame() {
        if (this.gameManager.getState() != GameState.IN_PLAY) {
            displayArea.requestFocus();
            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(this.gameManager::play);
        } else {
            messageArea.setText("Game already in progress");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.startGame();
    }

    @Override
    public void onMoveUpdated() {
        this.guiOutputManager.paintGamePlay(this.gameManager.getGameGrid());
        this.guiOutputManager.updateScore(this.gameManager.getScore());
//        this.consoleOutputManager.updateScore(this.gameManager.getScore());
    }

    @Override
    public void onGameOver() {
        this.guiOutputManager.showMessage("Game Over!");
        this.consoleOutputManager.showMessage("Game Over!");
    }

    @Override
    public void onGameStart() {
        this.guiOutputManager.showMessage("Game Started!");
        this.consoleOutputManager.showMessage("Game Started!");
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        this.displayArea.requestFocus();
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        this.shiftDown = false;
        this.gameManager.pause();
    }

    @Override
    public void onError(String message) {
        this.guiOutputManager.showMessage(message);
    }
}
