package com.example.gameproject.obstacle_game.GameController;

/**
 * an interface for generic drawer that receives updates from backend data and draws on surface T
 *
 * @param <T> the surface to draw
 */
public interface Drawer<T> {
    void draw(T surface);
}
