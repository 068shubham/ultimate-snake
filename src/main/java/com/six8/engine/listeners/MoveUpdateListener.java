package com.six8.engine.listeners;

public interface MoveUpdateListener {
    void onMoveUpdated();
    void onGameOver();
    void onGameStart();
    void onError(String message);
}
