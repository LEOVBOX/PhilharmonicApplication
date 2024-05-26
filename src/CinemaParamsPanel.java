import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CinemaParamsPanel extends JPanel {
    JTextField screenWidth;
    JTextField screenHeight;

    public int getScreenWidth() {
        return Integer.parseInt(screenWidth.getText());
    }

    public int getScreenHeight() {
        return Integer.parseInt(screenHeight.getText());
    }

    public CinemaParamsPanel(HashMap<String, String> buildingParams) {
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            JLabel screenWidthLabel = new JLabel("Ширина экрана");
            screenWidthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(screenWidthLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            screenWidth = new JTextField();
            if (buildingParams != null) {
                screenWidth.setText(buildingParams.get("screen_width"));
            }
            add(screenWidth, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            JLabel screenHeightLabel = new JLabel("Высота экрана");
            screenHeightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(screenHeightLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            screenHeight = new JTextField();
            if (buildingParams != null) {
                screenHeight.setText(buildingParams.get("screen_height"));
            }
            add(screenHeight, gbc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
