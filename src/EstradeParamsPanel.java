import javax.swing.*;
import java.awt.*;

public class EstradeParamsPanel extends JPanel {
    JTextField stageWidth;
    JTextField stageHeight;

    public int getStageWidth() {
        return Integer.parseInt(stageHeight.getText());
    }

    public int getStageHeight() {
        return Integer.parseInt(stageHeight.getText());
    }

    public EstradeParamsPanel() {
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            JLabel screenWidthLabel = new JLabel("Ширина сцены (м)");
            screenWidthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(screenWidthLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            stageWidth = new JTextField();
            add(stageWidth, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            JLabel screenHeightLabel = new JLabel("Глубина сцены (м)");
            screenHeightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(screenHeightLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            stageHeight = new JTextField();
            add(stageHeight, gbc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
