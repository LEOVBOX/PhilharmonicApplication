import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class EstradeParamsPanel extends JPanel {
    JTextField stageWidth;
    JTextField stageDepth;

    public int getStageWidth() {
        return Integer.parseInt(stageWidth.getText());
    }

    public int getStageDepth() {
        return Integer.parseInt(stageDepth.getText());
    }

    public EstradeParamsPanel(HashMap<String, String> buildingParams) {
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
            if (buildingParams != null) {
                stageWidth.setText(buildingParams.get("stage_width"));
            }
            add(stageWidth, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            JLabel screenHeightLabel = new JLabel("Глубина сцены (м)");
            screenHeightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(screenHeightLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            stageDepth = new JTextField();
            if (buildingParams != null) {
                stageDepth.setText(buildingParams.get("stage_depth"));
            }
            add(stageDepth, gbc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
