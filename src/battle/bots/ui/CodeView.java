package battle.bots.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Allows users to preview the source code of a selected file.
 * @author Harry Xu
 * @version 1.0 - March 14th 2024
 */
public class CodeView extends JPanel {
    /** Constructs a {@link CodeView} */
    public CodeView() {
        this.add(new JLabel("Preview Code here"));
    }
}
