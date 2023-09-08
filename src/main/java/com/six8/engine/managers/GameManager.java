package com.six8.engine.managers;

import com.six8.engine.enums.GameState;
import com.six8.engine.listeners.MoveUpdateListener;
import com.six8.engine.enums.Direction;
import com.six8.common.GridItem;

public interface GameManager {
    int getRows();

    int getColumns();

    void init();

    void moveSnake();

    boolean isGameOver();

    void changeDirection(Direction direction);

    GridItem[][] getGameGrid();

    int getScore();

    void increaseMoveSpeed(int movesPerSecond);
    int getSpeed();

    long getMoveDelay();

    void addMoveListener(MoveUpdateListener listener);

    void play();
    void pause();

    boolean isGamePaused();
    GameState getState();

    default int getCircularRow(int row) {
        return (row + this.getRows()) % this.getRows();
    }

    default int getCircularColumn(int column) {
        return (column + this.getColumns()) % this.getColumns();
    }
}
