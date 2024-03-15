package battle.bots.loader;

import battle.bots.game.Player;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;

/**
 * Class for compiling, loading, and instantiating submitted {@link Player} classes.
 * @author Harry Xu
 * @version 1.0 - March 15th 2024
 * */
public class ObjectLoader {
    private final JavaCompiler compiler;
    private final File root;

    /**
     * Constructs an {@link ObjectLoader}.
     * @throws IOException if an I/O error occurs
     */
    public ObjectLoader() throws IOException {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.root = Files.createTempDirectory("players").toFile();
    }

    /**
     * Loads a single {@link Player} object with a player name and the code.
     * @param className the filename
     * @param code the bot class source code as a {@link String}
     * @return an instance of submitted {@link Player} class
     * @throws IOException if an I/O error occurs while writing to or creating the program file
     * @throws ObjectLoaderException if an error occurs while compiling, loading, or instantiating the {@link Player}
     */
    public Player load(String className, String code) throws IOException, ObjectLoaderException {
        // Create temporary source file
        File sourceFile = new File(this.root, className + ".java");
        Files.writeString(sourceFile.toPath(), code);

        OutputStream errStream = new ByteArrayOutputStream();

        int resultCode = this.compiler.run(null, null, errStream, sourceFile.getPath());

        if (resultCode != 0) {
            throw new ObjectLoaderException(errStream.toString());
        }

        // Attempt to load class file
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{ root.toURI().toURL() })) {
            Class<?> cls = Class.forName(className, true, classLoader);
            return (Player) cls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException |InvocationTargetException | InstantiationException |
                IllegalAccessException | NoSuchMethodException e) {
            throw new ObjectLoaderException(e);
        } catch (ClassCastException e) {
            throw new ObjectLoaderException("Could not cast class" + className + " to a Player instance.");
        }
    }
}