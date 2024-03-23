package battle.bots.game;

import battle.bots.game.objects.Bot;
import battle.bots.game.objects.GameObject;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private final GameObject[][] map;
    private final Image[][] mapTiles;

    private final Timer gameLoop;

    private int currentCycle;
    private final int gridSize;

    public GamePanel(List<Bot> bots, Dimension panelSize) {
        // Game map dimensions
        int gridHeight = (int) Math.floor(
                Math.sqrt(
                        bots.size() * Const.TILES_PER_PLAYER * panelSize.height / (double) panelSize.width
                )
        );

        if (gridHeight < Const.MIN_HEIGHT) {
            gridHeight = Const.MIN_HEIGHT;
        }

        if (gridHeight > Const.MAX_HEIGHT) {
            gridHeight = Const.MAX_HEIGHT;
        }

        this.gridSize = panelSize.height / gridHeight;
        int gridWidth = panelSize.width / this.gridSize;
        this.map = new GameObject[gridHeight - 2][gridWidth];
        this.mapTiles = new Image[gridHeight - 2][gridWidth];
        this.currentCycle = 1;

        this.gameLoop = new Timer();
    }

    /**
     * start
     * Starts the game.
     */
    public void start() {
        this.gameLoop.schedule(new GameLoopTask(), 0, Const.MS_PER_TICK);
    }

    /**
     * runTick
     * Runs an animation frame (a tick).
     * An animation frame is NOT the same as an update cycle.
     * Animation frames occur more often to create a smoother animation.
     */
    public void runTick() {
        for (GameObject[] gameObjects : this.map) {
            for (GameObject currentObject : gameObjects) {
                // TODO: Implement
            }
        }

        repaint();
    }

    /**
     * runUpdate
     * Runs an update cycle on the map
     */
    public void runUpdate() {
        // TODO: Implement
    }

    /**
     * Task responsible for animating (ticks) and running update cycles .
     * @author Harry Xu
     * @version 1.0 - March 23rd 2024
     */
    public class GameLoopTask extends TimerTask {
        private int ticks;

        public GameLoopTask() {
            this.ticks = 0;
        }

        @Override
        public void run() {
            this.ticks++;


            if (ticks % Const.TICKS_PER_UPDATE == 0) {
                runUpdate();
            }

            runTick();
        }
    }
    /**
     * Paints the game using the {@link Graphics} objects
     * @param g the graphics object
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLUE);

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                int xCoord = x * this.gridSize;
                int yCoord = y * this.gridSize;

                g.drawRect(xCoord, yCoord, this.gridSize, this.gridSize);
            }
        }
    }
}
