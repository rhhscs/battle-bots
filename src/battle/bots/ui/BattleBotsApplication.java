package battle.bots.ui;

import battle.bots.Main;
import battle.bots.game.assets.Assets;
import battle.bots.game.objects.GamePanel;
import battle.bots.game.objects.Bot;
import battle.bots.loader.ObjectLoader;
import battle.bots.loader.ObjectLoaderException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Entry point for the RHHS Computer Science Club's Battle Bots software
 * @author Harry Xu
 * @version 1.0 - March 14th 2024
 */
public class BattleBotsApplication {
    private final JFrame frame;
    private final JPanel mainPanel;
    private final Map<String, String> botRegistry;
    private final ObjectLoader objectLoader;

    private final CodeView codeView;

    /** Constructs a {@link BattleBotsApplication}. */
    public BattleBotsApplication() throws IOException {
        this.frame = new JFrame();
        this.botRegistry = new LinkedHashMap<>();
        this.objectLoader = new ObjectLoader();

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 600);
        GridBagConstraints constraints = new GridBagConstraints();

        // Main Panel
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new GridBagLayout());
        this.mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // BotView
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);

        BotView botView = new BotView(this::onUpload, this::onSelect);
        this.mainPanel.add(botView, constraints);

        // CodeView
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;

        this.codeView = new CodeView();
        this.mainPanel.add(this.codeView, constraints);

        // Start
        constraints.gridx = 1;
        constraints.gridy = 1;
        JButton startButton = new JButton("Start!");
        startButton.addActionListener(new OnStartListener());
        this.mainPanel.add(startButton, constraints);

        this.frame.add(this.mainPanel, BorderLayout.CENTER);
    }

    /** Starts the application. */
    public void start() {
        this.frame.setVisible(true);
    }

    /**
     * Handles the file upload.
     * @param files the files being uploaded
     * @return the updated list of files to display
     */
    private List<String> onUpload(File[] files) {
        for (File file : files) {
            String filename = file.getName();

            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String content = new String(bytes, StandardCharsets.UTF_8);
                this.botRegistry.put(filename, content);
            } catch (IOException e) {
                System.out.println("Exception occurred whilst trying to read file '" + filename + "'.");
            }
        }

        return new ArrayList<>(this.botRegistry.keySet());
    }

    /**
     * Handles the file list selection.
     * @param filename the name of the selected file,
     *                 or {@code null} to reset the text
     */
    private void onSelect(String filename) {
        if (filename == null) {
            this.codeView.resetText();
        }

        String text = this.botRegistry.get(filename);

        if (text == null) {
            this.codeView.resetText();
        } else {
            this.codeView.updateText(text);
        }
    }

    /**
     * Listener invoked when the start button is pressed
     * @author Harry Xu
     * @version 1.0 - March 15th 2024
     */
    private class OnStartListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Bot> bots = new ArrayList<>();

            for (Map.Entry<String, String> entry : botRegistry.entrySet()) {
                // Remove .java extension
                String className = entry.getKey().substring(0, entry.getKey().indexOf("."));

                try {
                    Bot loadedBot = objectLoader.load(className, entry.getValue());
                    bots.add(loadedBot);
                } catch (IOException | ObjectLoaderException ex) {
                    codeView.updateText(ex.getMessage());
                    return;
                }
            }

            System.out.println(bots);

            frame.remove(mainPanel);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            GamePanel gamePanel = new GamePanel(bots);

            frame.add(gamePanel, BorderLayout.CENTER);
            frame.setSize(screenSize);
            frame.revalidate();
            frame.repaint();

            gamePanel.start();
        }
    }
}
