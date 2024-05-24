import javax.swing.*;
import java.awt.*;

public class ScrollablePanel extends JPanel {
    private final JPanel containerPanel;

    public ScrollablePanel() {
        // Устанавливаем BorderLayout для основного JPanel
        super(new BorderLayout());

        // Создаем контейнер JPanel с BoxLayout для вертикального расположения
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

        // Создаем JScrollPane и добавляем в него containerPanel
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Добавляем JScrollPane в основной JPanel
        this.add(scrollPane, BorderLayout.CENTER);
    }

    // Метод для добавления панелей в containerPanel
    @Override
    public Component add(Component component) {
        setAlignmentY(Component.TOP_ALIGNMENT);
        containerPanel.add(component);
        containerPanel.revalidate();  // Обновляем контейнер после добавления панели
        containerPanel.repaint();
        return component;
    }

    public void remove(int index) {
        containerPanel.remove(index);
        containerPanel.revalidate();
        containerPanel.repaint();
    }
}
