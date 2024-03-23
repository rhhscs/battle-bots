package battle.bots.game;

import battle.bots.game.assets.BulletAssets;
import battle.bots.game.assets.ObstacleAssets;
import battle.bots.game.assets.PlayerAssets;
import battle.bots.game.assets.TileAssets;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class to allow for global access to assets (e.g. sprites and fonts) .
 * @author Tommy Shan
 * @version 1.0 - January 10th 2024
 */
public class Assets {
    /** Singleton instance */
    private static Assets instance;

    private final PlayerAssets player;
    private final TileAssets tile;
    private final BulletAssets bullet;
    private final ObstacleAssets obstacle;

    /**
     * Constructs assets with sub-asset classes.
     * @param player the player sub-assets
     * @param tile the tile sub-assets
     * @param bullet the gui sub-assets
     * @param obstacle the coin sub-assets
     */
    private Assets(PlayerAssets player, TileAssets tile, BulletAssets bullet, ObstacleAssets obstacle) {
        this.player = player;
        this.tile = tile;
        this.bullet = bullet;
        this.obstacle = obstacle;
    }

    /**
     * getPlayer
     * Gets the player sub-assets.
     * @return the player sub-assets
     */
    public PlayerAssets getPlayer() {
        return this.player;
    }

    /**
     * Gets the tile sub-assets.
     * @return the tile sub-assets
     */
    public TileAssets getTile() {
        return this.tile;
    }

    /**
     * Gets the bullet sub-assets.
     * @return the bullet sub-assets
     */
    public BulletAssets getBullet() {
        return this.bullet;
    }

    /**
     * Gets the obstacle sub-assets.
     * @return the obstacle sub-assets
     */
    public ObstacleAssets getObstacle() {
        return this.obstacle;
    }

    /**
     * Loads, initializes, and processes the assets,
     * and sets the instance to the {@link #instance} variable.
     * @param size the size to load the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    public static void initialize(int size) throws IOException {
        if (instance != null) {
            return;
        }

        instance = new Assets(
                new PlayerAssets(size),
                new TileAssets(size),
                new BulletAssets(size),
                new ObstacleAssets(size)
        );
    }

    /**
     * Gets the singleton instance of the class
     * @return the singleton {@link Assets} instance
     */
    public static Assets getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Sprites not initialized");
        }

        return instance;
    }

    /**
     * Converts an entry of directories to image paths into an entry of directory names to resized images.
     * @param entry the map entry
     * @param size the resized size of the images
     * @return the transformed entry
     */
    private static Map.Entry<String, Image[]> processImage(Map.Entry<Path, List<Path>> entry, int size) {
        String color = entry.getKey().toString();
        List<Path> files = entry.getValue();

        Image[] images = new Image[files.size()];

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i).toFile();

            try {
                images[i] = ImageIO.read(file).getScaledInstance(size, size, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new AbstractMap.SimpleEntry<>(color, images);
    }

    /**
     * Helper function which partially applies the
     * {@link Assets#processImage(Map.Entry, int)} method with a size parameter.
     * @param size the resized size of the images
     * @return a partially applied function which maps an
     * entry of directories to image paths into an
     * entry of directory names to resized images
     */
    public static Function<Map.Entry<Path, List<Path>>, Map.Entry<String, Image[]>> processImage(int size) {
        return (Map.Entry<Path, List<Path>> entry) -> processImage(entry, size);
    }

    /**
     * Loads all images within a root directory and
     * categorizes them by the name of their parent directory.
     * @param root the root directory
     * @param size the resized size of the images
     * @return a map of parent directory names to images
     */
    public static Map<String, Image[]> loadAndGroupImages(Path root, int size) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            // Read and process sprites with stream API
            return paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.groupingBy(path -> path.getName(path.getNameCount() - 2)))
                    .entrySet()
                    .stream()
                    .map(processImage(size))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }
}
