package com.six8.gui.impl;

import com.six8.common.GridItem;
import com.six8.gui.GuiOutputManager;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class JFrameGuiOutputManagerImpl implements GuiOutputManager {
    private static final Map<GridItem, String> gridSymbols;

    static {
        gridSymbols = new HashMap<>();
        gridSymbols.put(GridItem.EMPTY, "_");
        gridSymbols.put(GridItem.SNAKE_BODY, "*");
        gridSymbols.put(GridItem.SNAKE_FOOD, "0");
        gridSymbols.put(GridItem.SNAKE_HEAD, "#");
        gridSymbols.put(GridItem.SNAKE_TAIL, "$");
        gridSymbols.put(GridItem.COLLISION_POINT, "X");
    }

    JTextArea scoreArea;
    JTextArea displayArea;
    JTextArea messageArea;

    public JFrameGuiOutputManagerImpl(JTextArea scoreArea, JTextArea messageArea, JTextArea displayArea) {
        this.scoreArea = scoreArea;
        this.messageArea = messageArea;
        this.displayArea = displayArea;
    }

    @Override
    public void paintGamePlay(GridItem[][] gridItems) {
        StringBuilder sb = new StringBuilder();
        for (GridItem[] row : gridItems) {
            for (GridItem cell : row) {
                sb.append(JFrameGuiOutputManagerImpl.gridSymbols.get(cell));
            }
            sb.append("\n");
        }
        this.displayArea.setText(sb.toString());
    }

    @Override
    public void updateScore(int score) {
        this.scoreArea.setText("Score: " + score);
    }

    @Override
    public void showMessage(String message) {
        this.messageArea.setText(message);
    }
}
