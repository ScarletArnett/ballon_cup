import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepiocheFrame extends JFrame {

    private JLabel label;
    private JSpinner spinner;
    private JButton button;

    public static Component notMaximized(Component component) {
        JPanel panel = new JPanel();
        panel.add(component);
        return panel;
    }

    public static JLabel centered(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    public static Component withSize(Component component, int width, int height) {
        JPanel panel = new JPanel();
        component.setPreferredSize(new Dimension(width, height));
        panel.setPreferredSize(new Dimension(width, height));
        panel.add(component);
        return panel;
    }

    public RepiocheFrame() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 20, 0));

        label = new JLabel("<html>" +
                "Vos cartes de vous permettent pas de jouer.<br/>" +
                "Combien de cartes souhaitez-vous jeter ?" +
                "</html>");
        panel.add(centered(label));
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        panel.add(withSize(spinner, 100, 40));
        button = new JButton("Jeter");
        panel.add(notMaximized(button));

        add(panel);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button) {
                    onButtonClicked();
                }
            }
        });


        setTitle("Repiocher");
        setSize(400, 150);
        setVisible(true);
    }

    private void onButtonClicked() {
        firePropertyChange("value", -1, spinner.getValue());
        setVisible(false);
        dispose();
    }

}
