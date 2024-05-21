import javax.swing.*;
import java.awt.*;

public class TheatreParamsPanel extends JPanel {
    JTextField parterCpacity;
    JTextField benoirCapacity;
    // Вместимость бельэтажа
    JTextField mezzanineCapacity;
    JTextField firstTierCapacity;
    JTextField secondTierCapacity;
    JTextField amphitheatreCapacity;
    JTextField galerkaCpacity;

    public int getParterCapacity() {
        return Integer.parseInt(parterCpacity.getText());
    }

    public int getBenoirCapacity() {
        return Integer.parseInt(benoirCapacity.getText());
    }

    public int getMezzanineCapacity() {
        return Integer.parseInt(mezzanineCapacity.getText());
    }

    public int getFirstTierCapacity() {
        return Integer.parseInt(firstTierCapacity.getText());
    }

    public int getSecondTierCapacity() {
        return Integer.parseInt(secondTierCapacity.getText());
    }

    public int getAmphitheatreCapacity() {
        return Integer.parseInt(amphitheatreCapacity.getText());
    }

    public int getGalerkaCapacity() {
        return Integer.parseInt(galerkaCpacity.getText());
    }

    public TheatreParamsPanel() {
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            JLabel parterCapacityLabel = new JLabel("Вместимость партера");
            parterCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(parterCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            parterCpacity = new JTextField();
            add(parterCpacity, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            JLabel benoirCapacityLabel = new JLabel("Вместимость бенуара");
            benoirCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(benoirCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            benoirCapacity = new JTextField();
            add(benoirCapacity, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel mezzanineCapacityLabel = new JLabel("Вместимость бельэтажа");
            mezzanineCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(mezzanineCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            mezzanineCapacity = new JTextField();
            add(mezzanineCapacity, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            JLabel firstTierCapacityLabel = new JLabel("Вместимость первого яруса");
            firstTierCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(firstTierCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            firstTierCapacity = new JTextField();
            add(firstTierCapacity, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            JLabel secondTierCapacityLabel = new JLabel("Вместимость второго яруса");
            secondTierCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(secondTierCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            secondTierCapacity = new JTextField();
            add(secondTierCapacity, gbc);

            gbc.gridy = 5;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel amphitheatreCapacityLabel = new JLabel("Вместимость амфитеарта");
            amphitheatreCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(amphitheatreCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            amphitheatreCapacity = new JTextField();
            add(amphitheatreCapacity, gbc);

            gbc.gridy = 6;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel galerkaCapacityLabel = new JLabel("Вместимость галерки");
            galerkaCapacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            add(galerkaCapacityLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            galerkaCpacity = new JTextField();
            add(galerkaCpacity, gbc);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
