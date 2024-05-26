import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

// Implements drop-down menu for {Integer id: String value} options
public class Selector {
    // String - value, Integer - id in table
    private final String selectorLabel;
    private final HashMap<String, Integer> map;
    private final JComboBox<String> selector;

    public Selector(String selectorLabel, HashMap<String, Integer> map) {
        this.selectorLabel = selectorLabel;
        this.map = map;
        selector = new JComboBox<>(map.keySet().toArray(new String[0]));
    }

    public Integer getSelectedID() {
        return map.get((String) selector.getSelectedItem());
    }

    public String getSelectorLabel() {
        return selectorLabel;
    }

    public String getSelectedName() {
        return (String)selector.getSelectedItem();
    }

    public JPanel getPanel() {
        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.X_AXIS));
        selectorPanel.add(Box.createHorizontalGlue());

        JLabel selectorLabel = new JLabel(getSelectorLabel());
        selectorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectorPanel.add(selectorLabel);

        selectorPanel.add(selector);

        return selectorPanel;
    }

    // Функция для получения ключа по значению из HashMap<String, Integer>
    public static String getKeyByValue(HashMap<String, Integer> map, Integer value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; // Возвращаем null, если ключ не найден
    }

    public void setSelectedID(Integer id) {
        selector.setSelectedItem(getKeyByValue(map, id));
    }
}
