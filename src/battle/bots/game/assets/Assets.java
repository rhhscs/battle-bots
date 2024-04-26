package battle.bots.game.assets;

import battle.bots.game.Const;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class to allow for global access to assets (e.g. sprites and fonts) .
 * @author Harry Xu
 * @version 1.0 - March 23rd 2024
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
     * @throws IOException if an I/O error occur while reading the files
     */
    public static void initialize() throws IOException {
        if (instance != null) {
            return;
        }

        instance = new Assets(
                new PlayerAssets(Const.TILE_SIZE),
                new TileAssets(Const.TILE_SIZE),
                new BulletAssets(Const.TILE_SIZE),
                new ObstacleAssets(Const.TILE_SIZE)
        );
    }

    /**
     * Gets the singleton instance of the class
     * @return the singleton {@link Assets} instance
     */
    public static Assets getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The 'assets' instance is not initialized.");
        }

        return instance;
    }

    /**
     * Converts an entry of directories to image paths into an entry of directory names to resized images.
     * @param entry the map entry
     * @param size the resized size of the images
     * @return the transformed entry
     */
    private static Map.Entry<String, Image[]> processImage(Map.Entry<Path, List<Path>> entry, int size, boolean isJar) {
        String color = entry.getKey().toString();
        List<Path> resources = entry.getValue();

        Image[] images = new Image[resources.size()];

        for (int i = 0; i < resources.size(); i++) {
            Path resource = resources.get(i);

            InputStream stream;

            if (isJar) {
                stream = Assets.class.getResourceAsStream(resource.toString());
            } else {
                try {
                    stream = Files.newInputStream(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (stream == null) {
                throw new IllegalArgumentException("Resource '" + resource + "' cannot be found.");
            }

            try {
                images[i] = ImageIO.read(stream).getScaledInstance(size, size, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new AbstractMap.SimpleEntry<>(color, images);
    }

    /**
     * Helper function which partially applies the
     * {@link Assets#processImage(Map.Entry, int, boolean)} method with a size parameter.
     * @param size the resized size of the images
     * @param isJar if to process the image in a jar file or not
     * @return a partially applied function which maps an
     * entry of directories to image paths into an
     * entry of directory names to resized images
     */
    public static Function<Map.Entry<Path, List<Path>>, Map.Entry<String, Image[]>> processImage(int size, boolean isJar) {
        return (Map.Entry<Path, List<Path>> entry) -> processImage(entry, size, isJar);
    }

    /**
     * Loads all images within a root directory and
     * categorizes them by the name of their parent directory.
     * @param root the root resource directory
     * @param size the resized size of the images
     * @return a map of parent directory names to images
     */
    public static Map<String, Image[]> loadAndGroupImages(String root, int size) throws IOException, URISyntaxException {
        URL url = Assets.class.getResource(root);

        if (url == null) {
            throw new IllegalStateException("Resource root '" + root + "' cannot be found");
        }

        URI uri = url.toURI();

        if (uri.getScheme().equals("jar")) {
            String systemRoot = uri.toString().split("!")[0];
            URI systemRootURI = URI.create(systemRoot);

            try (FileSystem fileSystem = FileSystems.newFileSystem(systemRootURI, Collections.emptyMap())) {
                Path resourcePath = fileSystem.getPath("/sprites");

                try (Stream<Path> paths = Files.walk(resourcePath)) {
                    // Read and process sprites with stream API
                    return toImageMap(paths, size, true);
                }
            }
        }

        try (Stream<Path> paths = Files.walk(Paths.get(uri))) {
            // Read and process sprites with stream API
            return toImageMap(paths, size, false);
        }
    }

    public static Map<String, Image[]> toImageMap(Stream<Path> paths, int size, boolean isJar) {
        return paths
            .filter(Files::isRegularFile)
            .collect(Collectors.groupingBy(path -> path.getName(path.getNameCount() - 2)))
            .entrySet()
            .stream()
            .map(processImage(size, isJar))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
