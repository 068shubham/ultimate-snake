package com.six8.gui.impl;

import com.six8.common.GridItem;
import com.six8.gui.GuiOutputManager;

import java.util.HashMap;
import java.util.Map;

public class ConsoleGuiOutputManagerImpl implements GuiOutputManager {
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

    @Override
    public void paintGamePlay(GridItem[][] gridItems) {
        StringBuilder sb = new StringBuilder();
        for (GridItem[] row : gridItems) {
            for (GridItem cell : row) {
                sb.append(ConsoleGuiOutputManagerImpl.gridSymbols.get(cell));
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    @Override
    public void updateScore(int score) {
        System.out.printf("Latest score is: %s\n", score);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
