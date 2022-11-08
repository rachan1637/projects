package com.example.gameproject.obstacle_game.GameController;

import com.example.gameproject.obstacle_game.GameElements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

class AdventureManager extends Observable implements Manager{
    /**
     * The time need to be counted down before the game start.
     */
    private int startGameCountDown = 30 * 5;

    /**
     * The time need to be counted down before the game end.
     */
    private int endGameCountDown = 30 * 3;

    /**
     * The player controlled spaceShip
     */
    private List<SpaceShip> spaceShipList = new ArrayList<>();

    /**
     * List containing all obstacles in this adventure.
     */
    private List<Obstacle> spaceObstacles = new ArrayList<>();

    /**
     * List containing all obstacles to be deleted at the end of this update
     */
    private List<Obstacle> obstacleGarbageCart = new ArrayList<>();

    /**
     * List containing all spaceships to be deleted at the end of this update.
     */
    private List<SpaceShip> spaceshipGarbageCart = new ArrayList<>();

    /**
     * List containing all treasury boxes in this adventure.
     */
    private List<Obstacle> treasuryBoxList = new ArrayList<>();

    /**
     * List containing all treasury boxes to be deleted at the end of this update.
     */
    private List<Obstacle> treasuryBoxGarbageCart = new ArrayList<>();

    /**
     * The generator for obstacles.
     */
    private ItemGenerator<Obstacle> treasuryBoxGenerator;

    /**
     * the generator for obstacles
     */
    private ItemGenerator<Obstacle> obstacleGenerator;

    /**
     * Check whether the game is over
     */
    private boolean gameOver = false;

    /**
     * Constructs this AdventureManger by default.
     *
     * @param height height of obstacleGamePanel in unit.
     * @param width  width of obstacleGamePanel in unit.
     */
    AdventureManager(int width, int height, int difficulty) {
        obstacleGenerator = new ObstacleGenerator(width, height, difficulty);
        treasuryBoxGenerator = new TreasuryBoxGenerator(width, height, 3);
    }

    /**
     * Adds a spaceship to the game.
     *
     * @param s the spaceship to be added.
     */
    public void addSpaceShip(SpaceShip s) {
        spaceShipList.add(s);
    }

    /**
     * Gets the ship in this game.
     *
     * @return the player's ship.
     */
    List<SpaceShip> getSpaceShipList() {
        return spaceShipList;
    }

    /**
     * Gets the list of dead spaceships.
     * @return the list of dead spaceships.
     */
    List<SpaceShip> getSpaceshipGarbageCart() {
        return spaceshipGarbageCart;
    }

    /**
     * Gets the obstacles in this game.
     *
     * @return the list of SpaceObstacles.
     */
    List<Obstacle> getObstacles() {
        return spaceObstacles;
    }

    /**
     * Returns the game over boolean.
     *
     * @return game over boolean.
     */
    boolean getGameOver() {
        return gameOver;
    }

    /**
     * Gets the startGameCountDown time.
     *
     * @return the startGameCountDown time.
     */
    int getStartGameCountDown() {
        return startGameCountDown;
    }

    /**
     * Gets the endGameCountDown time.
     *
     * @return the endGameCountDown time.
     */
    int getEndGameCountDown() {
        return endGameCountDown;
    }

    /**
     * Responds to events of player input, when the player touches, make the spaceship with index i jump.
     *
     * @param i the index of spaceship which is supposed to jump.
     */
    void respondTouch(int i) {
        spaceShipList.get(i).jump();
    }

    /**
     * Gets the list of treasury boxes.
     *
     * @return the list of treasury boxes.
     */
    List<Obstacle> getTreasuryBoxList() {
        return treasuryBoxList;
    }

    /**
     * Gets a list containing the number of collections each spaceship gets.
     * precondition: Only call when game ends
     *
     * @return a list containing the number of collections each spaceship gets.
     */
    List<Integer> getCollections() {
        List<Integer> list = new ArrayList<>();
        for (SpaceShip s : spaceshipGarbageCart) {
            list.add(s.getCollection());
        }
        return list;
    }

    /**
     * Updates the information of all items in this adventure.
     */
    public void update() {
        // Check whether the game is start.
        if (!checkStartGameCountDown()) {
            setChanged();
            notifyObservers();
            return;
        }

        // check whether the game is over.
        if (spaceShipList.size() == 0) {
            // Sets the game to be end.
            gameOver = true;
            setChanged();
            notifyObservers();
            // Count Down the endGameCountDown time.
            checkEndGameCountDown();
            return;
        }

        // update conditions of every spaceship.
        updateSpaceShipList();

        // update conditions of every space obstacles.
        updateObstacles(spaceObstacles, obstacleGarbageCart);
        clearObstacleGarbage();
        updateObstacles(treasuryBoxList, treasuryBoxGarbageCart);
        clearTreasuryBoxGarbage();

        // check to see whether to generate new obstacles.
        checkGeneration(obstacleGenerator, spaceObstacles);

        // check to see whether to generate new treasury box.
        checkGeneration(treasuryBoxGenerator, treasuryBoxList);

        setChanged();
        notifyObservers();
    }


    /**
     * Checks the startGameCountDown ends or not.
     *
     * @return whether we can start the game.
     */
    private boolean checkStartGameCountDown() {
        if (startGameCountDown > 0) {
            startGameCountDown = startGameCountDown - 1;
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks whether the endGameCountDown time is over. If no then -1
     */
    private void checkEndGameCountDown() {
        if (endGameCountDown > 0) {
            endGameCountDown = endGameCountDown - 1;
        }
    }

    /**
     * Updates the condition of all spaceships.
     */
    private void updateSpaceShipList() {
        for (SpaceShip s : spaceShipList) {
            // update the position of spaceship.
            s.move();
            // check whether the spaceship hits a obstacle or not.
            checkHit(s);

            // Check whether the spaceship is out of Screen.
            s.outOfScreen();

            // Update the score of this spaceship.
            s.updateScore();

            // Check whether the adventure of this spaceship is over.
            s.checkGameOver();
            if (s.getGameOver()) {
                spaceshipGarbageCart.add(s);
            }
        }

        // clear dead spaceship.
        for (SpaceShip s : spaceshipGarbageCart) {
            spaceShipList.remove(s);
        }
    }

    /**
     * Checks whether the spaceship hits a space item.
     *
     * @param s the spaceship to be checked.
     */
    private void checkHit(SpaceShip s) {
        checkHitObstacle(s);
        checkHitTreasuryBox(s);
    }

    /**
     * Checks whether the spaceship hits an obstacle.
     * @param s the spaceship to be checked.
     */
    private void checkHitObstacle(SpaceShip s) {
        int invincibleTime = s.getInvincibleTime();
        // If it's in 3 seconds invincible time(after it hits an obstacle), then the spaceship doesn't hit any obstacle.
        if (invincibleTime == 0) {
            for (Obstacle obstacle : spaceObstacles) {
                if (s.checkHit(obstacle)) {
                    obstacleGarbageCart.add(obstacle);
                }
            }
        } else {
            s.setInvincibleTime(invincibleTime - 1);
        }
    }

    /**
     * Checks whether the spaceship hits a treasury box.
     * @param s the treasury box to be checked.
     */
    private void checkHitTreasuryBox(SpaceShip s) {
        for (Obstacle treasuryBox : treasuryBoxList) {
            if (s.checkGetTreasury(treasuryBox)) {
                treasuryBoxGarbageCart.add(treasuryBox);
            }
        }

        // If the spaceship gets a collection, then update the time to display this message.
        int collectionTime = s.getCollectionTime();
        if (collectionTime > 0) {
            s.setCollectionTime(collectionTime - 1);
        }
    }

    /**
     * Updates the condition of all space obstacles.
     */
    private void updateObstacles(List<Obstacle> storeList, List<Obstacle> garbageList) {
        for (Obstacle obstacle : storeList) {
            obstacle.move();

            if (obstacle.outOfScreen()) {
                garbageList.add(obstacle);
            }
        }

        // clear garbage obstacle(treasury box) in generation list.
        for (Obstacle obstacle : garbageList) {
            storeList.remove(obstacle);
        }
    }

    /**
     * Clears the obstacle garbage cart.
     */
    private void clearObstacleGarbage() {
        obstacleGarbageCart = new ArrayList<>();
    }

    /**
     * Clears treasury box garbage cart.
     */
    private void clearTreasuryBoxGarbage() {
        treasuryBoxGarbageCart = new ArrayList<>();
    }

    /**
     * Randomly generates a new obstacle.
     */
    private void checkGeneration(ItemGenerator generator, List<Obstacle> list) {
        if (generator != null) {
            Obstacle newObstacle = (Obstacle) generator.checkGeneration();
            if (newObstacle != null) {
                list.add(newObstacle);
                if (list.size() == 1) {
                    System.out.print(newObstacle.getSpeed());
                }
            }
        }
    }
}
