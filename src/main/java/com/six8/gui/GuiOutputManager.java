package com.six8.gui;

import com.six8.common.GridItem;

public interface GuiOutputManager {
    void paintGamePlay(GridItem[][] gridItems);
    void updateScore(int score);
    void showMessage(String message);
}
