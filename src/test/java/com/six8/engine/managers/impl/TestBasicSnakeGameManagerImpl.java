package com.six8.engine.managers.impl;

import com.six8.engine.enums.Direction;
import com.six8.engine.models.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TestBasicSnakeGameManagerImpl {
    BasicSnakeGameManagerImpl gameManager;
    int rows = 6;
    int columns = 4;

    Queue<Position> testFoods = new LinkedList<>();

    @BeforeEach
    public void setup() {
        testFoods.add(new Position(0, columns - 1));
        testFoods.add(new Position(1, columns - 1));
        testFoods.add(new Position(5, columns - 1));
        gameManager = new BasicSnakeGameManagerImpl(rows, columns);
        Random mockedRandom = Mockito.mock(Random.class);
        Mockito.when(mockedRandom.nextInt(Mockito.anyInt())).then((invocation) -> gameManager.foods.indexOf(testFoods.poll()));
        gameManager.random = mockedRandom;
        gameManager.init();
    }

    @Test
    public void testDirectionChangeMovement() {
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        assert gameManager.snake.size() == 3;
        assert gameManager.snake.getFirst().getRow() == (rows / 2);
        assert gameManager.snake.getFirst().getColumn() == ((columns - 1) / 2 + 2);
        assert gameManager.snake.size() == 3;
    }

    @Test
    public void testUpMovement() {
        gameManager.changeDirection(Direction.UP);
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        assert gameManager.snake.size() == 3;
        assert gameManager.snake.getFirst().getRow() == (rows / 2 - 1);
        assert gameManager.snake.getFirst().getColumn() == ((columns - 1) / 2 + 1);
        assert gameManager.snake.size() == 3;
    }

    @Test
    public void testDownMovement() {
        gameManager.changeDirection(Direction.DOWN);
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        assert gameManager.snake.size() == 3;
        assert gameManager.snake.getFirst().getRow() == (rows / 2 + 1);
        assert gameManager.snake.getFirst().getColumn() == ((columns - 1) / 2 + 1);
        assert gameManager.snake.size() == 3;
    }

    @Test
    public void testLeftMovement() {
        gameManager.changeDirection(Direction.LEFT);
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        assert gameManager.snake.size() == 3;
        assert gameManager.snake.getFirst().getRow() == (rows / 2);
        assert gameManager.snake.getFirst().getColumn() == ((columns - 1) / 2 + 2);
        assert gameManager.snake.size() == 3;
    }

    @Test
    public void testRightMovement() {
        gameManager.changeDirection(Direction.RIGHT);
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        assert gameManager.snake.size() == 3;
        assert gameManager.snake.getFirst().getRow() == (rows / 2);
        assert gameManager.snake.getFirst().getColumn() == ((columns - 1) / 2 + 2);
        assert gameManager.snake.size() == 3;
    }

    @Test
    public void testFoodEat() {
        assert gameManager.snake.size() == 3;
        gameManager.changeDirection(Direction.UP);
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.changeDirection(Direction.RIGHT);
        gameManager.moveSnake();
        assert gameManager.snake.size() == 4;
        assert !gameManager.isGameOver();
        assert gameManager.snake.getFirst().getRow() == 0;
        assert gameManager.snake.getFirst().getColumn() == columns - 1;
    }

    @Test
    public void testCollision() {
        assert gameManager.snake.size() == 3;
        gameManager.changeDirection(Direction.UP);
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        assert gameManager.snake.size() == 3;
        gameManager.changeDirection(Direction.RIGHT);
        gameManager.moveSnake();
        assert gameManager.snake.size() == 4;
        assert !gameManager.isGameOver();
        assert gameManager.snake.getFirst().getRow() == 0;
        assert gameManager.snake.getFirst().getColumn() == columns - 1;
        gameManager.changeDirection(Direction.DOWN);
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
        gameManager.changeDirection(Direction.LEFT);
        gameManager.moveSnake();
        assert gameManager.isGameOver();
    }

    @Test
    public void testWallPassThrough() {
        assert gameManager.snake.size() == 3;
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        gameManager.moveSnake();
        assert !gameManager.isGameOver();
    }

}
