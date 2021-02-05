package com.example.gameproject.obstacle_game.GameElements;

import android.graphics.Rect;

class SpaceItem {
    /**
     * The hitbox of this item
     */
    private Rect hitBox;

    /**
     * Constructs a new space item.
     *
     * @param hitBox the hitBox of this item
     */
    SpaceItem(Rect hitBox) {
        this.hitBox = hitBox;
    }

    /**
     * set the hitBox to be centered at (x, y)
     *
     * @param x the first coordinate of this space item.
     * @param y the second coordinate of this space item.
     */
    void setHitBoxTo(int x, int y) {
        hitBox.offsetTo(x - hitBox.width() / 2, y - hitBox.height() / 2);
    }

    /**
     * Gets the first coordinate of this space item's hit box's central point
     *
     * @return the first coordinate of this space item's hit box's central point
     */
    public int getX() {
        return hitBox.centerX();
    }

    /**
     * Gets this space item's second coordinate.
     *
     * @return the second coordinate of this space item.
     */
    public int getY() {
        return hitBox.centerY();
    }

    /**
     * get the hitBox of this item
     *
     * @return the hitBox
     */
    public Rect getHitBox() {
        return hitBox;
    }

    /**
     * Moves this space item.
     */
    public void move() {}
}
