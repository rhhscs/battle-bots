package battle.bots.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Allows users to preview the source code of a selected file.
 * @author Harry Xu
 * @version 1.0 - March 14th 2024
 */
public class CodeView extends JPanel {
    private final JTextArea textArea;

    /** Constructs a {@link CodeView} */
    public CodeView() {
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);

        this.setLayout(new GridLayout(1, 1));
        this.add(new JScrollPane(this.textArea), BorderLayout.CENTER);
    }

    /**
     * Updates the text of the text area .
     * @param newText the new text content
     */
    public void updateText(String newText) {
        this.textArea.setText(newText);
    }

    /**
     * Resets the text of the text area
     */
    public void resetText() {
        this.textArea.setText("Select file to preview.");
    }
}
