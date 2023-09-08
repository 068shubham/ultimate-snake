package com.six8.engine.managers.impl;

import com.six8.GamePlay;
import com.six8.engine.enums.GameState;
import com.six8.engine.listeners.MoveUpdateListener;
import com.six8.engine.enums.Direction;
import com.six8.common.GridItem;
import com.six8.engine.managers.GameManager;
import com.six8.engine.models.Position;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class BasicSnakeGameManagerImpl implements GameManager {
    protected int rows;
    protected int columns;
    protected Random random = new Random();
    protected LinkedList<Position> snake;
    protected LinkedList<Position> foods;
    protected Position nextFood;
    protected Direction direction;
    protected Direction nextDirection;
    protected Boolean isDead;
    private Integer moveSpeed;
    private Long delay;
    protected GameState gameState = GameState.NEW;
    private MoveUpdateListener moveUpdateListener;

    public BasicSnakeGameManagerImpl(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void init() {
        int startingRow = this.rows / 2;
        int startingColumn = (this.columns - 1) / 2;
        this.snake = new LinkedList<>();
        this.foods = new LinkedList<>();
        this.isDead = false;
        this.delay = 500L;
        this.moveSpeed = 2;
        this.direction = this.nextDirection = Direction.RIGHT;
        this.nextFood = null;
        this.gameState = GameState.INITIALISED;
        this.snake.addFirst(new Position(startingRow, startingColumn - 1));
        this.snake.addFirst(new Position(startingRow, startingColumn));
        this.snake.addFirst(new Position(startingRow, startingColumn + 1));
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                Position food = new Position(i, j);
                if (!this.snake.contains(food)) {
                    this.foods.add(food);
                }
            }
        }
        this.generateFood();
    }

    @Override
    public void moveSnake() {
        if (!this.isDead) {
            this.direction = this.nextDirection;
            Position next = this.getNext();
            if (!next.equals(this.nextFood)) {
                this.foods.addLast(new Position(this.snake.removeLast()));
            } else {
                this.generateFood();
            }
            this.snake.addFirst(next);
            this.foods.remove(next);
            if (this.snake.lastIndexOf(next) != this.snake.indexOf(next)) {
                this.isDead = true;
            }
        }
    }

    @Override
    public boolean isGameOver() {
        return this.isDead;
    }

    @Override
    public void changeDirection(Direction direction) {
        if (!this.isDead) {
            if (Arrays.asList(Direction.UP, Direction.DOWN).contains(direction) && Arrays.asList(Direction.LEFT, Direction.RIGHT).contains(this.direction)) {
                this.nextDirection = direction;
            } else if (Arrays.asList(Direction.LEFT, Direction.RIGHT).contains(direction) && Arrays.asList(Direction.UP, Direction.DOWN).contains(this.direction)) {
                this.nextDirection = direction;
            }
        }
    }

    @Override
    public GridItem[][] getGameGrid() {
        GridItem[][] out = new GridItem[this.rows][this.columns];
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                Position cur = new Position(i, j);
                if (this.nextFood.equals(cur)) {
                    out[i][j] = GridItem.SNAKE_FOOD;
                } else if (this.snake.getFirst().equals(cur)) {
                    if (this.snake.lastIndexOf(this.snake.getFirst()) != this.snake.indexOf(this.snake.getFirst())) {
                        out[i][j] = GridItem.COLLISION_POINT;
                    } else {
                        out[i][j] = GridItem.SNAKE_HEAD;
                    }
                } else if (this.snake.getLast().equals(cur)) {
                    out[i][j] = GridItem.SNAKE_TAIL;
                } else if (this.snake.contains(cur)) {
                    out[i][j] = GridItem.SNAKE_BODY;
                } else {
                    out[i][j] = GridItem.EMPTY;
                }
            }
        }
        return out;
    }

    @Override
    public int getScore() {
        return this.snake.size();
    }

    @Override
    public void increaseMoveSpeed(int movesPerSecond) {
        if (this.moveSpeed + movesPerSecond > 0) {
            this.moveSpeed += movesPerSecond;
            this.delay = Math.round(1000.0 / this.moveSpeed);
        }
    }

    @Override
    public int getSpeed() {
        return this.moveSpeed;
    }

    @Override
    public long getMoveDelay() {
        return this.delay;
    }

    @Override
    public void addMoveListener(MoveUpdateListener listener) {
        this.moveUpdateListener = listener;
    }

    @Override
    public void play() {
        try {
            if (Arrays.asList(GameState.NEW, GameState.OVER).contains(this.gameState)) {
                this.init();
                this.moveUpdateListener.onGameStart();
                this.infiniteMove();
            } else if (this.gameState == GameState.PAUSED) {
                this.moveUpdateListener.onGameStart();
                this.infiniteMove();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            this.gameState = GameState.ERRED_OUT;
            this.moveUpdateListener.onError(exception.getMessage());
        }
    }

    private void infiniteMove() {
        this.gameState = GameState.IN_PLAY;
        while (!this.isGameOver() && !this.isGamePaused()) {
            this.moveSnake();
            try {
                Thread.sleep(this.getMoveDelay());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.moveUpdateListener.onMoveUpdated();
        }
        if (this.isGameOver()) {
            this.gameState = GameState.OVER;
        }
        this.moveUpdateListener.onGameOver();
    }

    @Override
    public void pause() {
        if (this.gameState == GameState.IN_PLAY) {
            this.gameState = GameState.PAUSED;
        }
    }

    @Override
    public GameState getState() {
        return this.gameState;
    }

    @Override
    public boolean isGamePaused() {
        return this.gameState == GameState.PAUSED;
    }

    protected Position getRandomFood() {
        int foodCount = this.foods.size();
        int randomIndex = this.random.nextInt(foodCount);
        return this.foods.get(randomIndex);
    }

    protected void generateFood() {
        if (!this.foods.isEmpty()) {
            this.nextFood = this.getRandomFood();
            this.foods.remove(this.nextFood);
        }
    }

    protected Position next(int rowShift, int columnShift) {
        Position head = this.snake.getFirst();
        int nextRow = this.getCircularRow(head.getRow() + rowShift);
        int nextColumn = this.getCircularColumn(head.getColumn() + columnShift);
        return new Position(nextRow, nextColumn);
    }

    protected Position getNext() {
        switch (this.direction) {
            case RIGHT:
                return this.next(0, 1);
            case LEFT:
                return this.next(0, -1);
            case UP:
                return this.next(-1, 0);
            case DOWN:
                return this.next(1, 0);
        }
        return null;
    }
}
