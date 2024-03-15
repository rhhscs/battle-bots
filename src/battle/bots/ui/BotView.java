package battle.bots.ui;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Composite {@link javax.swing.JComponent} which contains a list for users to
 * view and select files, as well as a button to upload Java source files.
 * @author Harry Xu
 * @version 1.0 - March 14th 2024
 */
public class BotView extends JPanel {
    private final JList<String> botList;
    private final Function<File[], List<String>> onUpload;
    private final Consumer<String> onSelect;

    /**
     * Constructs a {@link BotView} with a callback function.
     * @param onUpload called when files are uploaded through the user interface
     * @param onSelect called when a file is selected through the user interface
     * */
    public BotView(
        Function<File[], List<String>> onUpload,
        Consumer<String> onSelect
    ) {
        this.botList = new JList<>();
        this.onUpload = onUpload;
        this.onSelect = onSelect;

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Bot list
        ListSelectionModel model = this.botList.getSelectionModel();
        model.addListSelectionListener(new BotListSelectionListener());

        JScrollPane scrollPane = new JScrollPane(this.botList);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        this.add(scrollPane, constraints);

        // Upload button
        constraints.gridy = 1;
        constraints.weighty = 0;

        JButton botUploadButton = new JButton("Upload Bot");
        botUploadButton.addActionListener(new OnUploadListener());

        this.add(botUploadButton, constraints);
    }

    /**
     * Listener invoked when the file upload button is clicked
     * @author Harry Xu
     * @version 1.0 - March 14th 2024
     */
    private class OnUploadListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new JavaFileFilter());
            fileChooser.setMultiSelectionEnabled(true);

            int returnValue = fileChooser.showOpenDialog(BotView.this);

            // Pass selected files to callback and update UI
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                List<String> files = onUpload.apply(selectedFiles);

                String[] filesArray = new String[files.size()];
                files.toArray(filesArray);

                botList.setListData(filesArray);
            }
        }
    }

    /**
     * Filters results for the {@link JFileChooser}.
     * @author Harry Xu
     * @version 1.0 - March 14th 2024
     */
    private static class JavaFileFilter extends FileFilter {
        /**
         * Whether the given file is accepted by this filter.
         *
         * @param f the File to test
         * @return true if the file is to be accepted
         */
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            // Gets file extension
            Optional<String> optional =
                    Optional.of(f.getName())
                    .map(String::toLowerCase)
                    .filter(name -> name.contains("."))
                    .map(name -> name.substring(name.lastIndexOf(".") + 1));

            return optional.isPresent() && optional.get().equals("java");
        }

        /**
         * The description of this filter. For example: "JPG and GIF Images"
         * @return the description of this filter
         * @see FileView#getName
         */
        @Override
        public String getDescription() {
            return "Java Source Files (.java)";
        }
    }

    private class BotListSelectionListener implements ListSelectionListener {
        /**
         * Called whenever the value of the selection changes.
         *
         * @param e the event that characterizes the change.
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel model = (ListSelectionModel) e.getSource();

            if (!e.getValueIsAdjusting()) {
                if (model.isSelectionEmpty()) {
                    onSelect.accept(null);
                } else {
                    onSelect.accept(botList.getSelectedValue());
                }
            }
        }
    }
}
