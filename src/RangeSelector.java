import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RangeSelector {
    JTextField startField;
    JTextField endField;
    JPanel panel;

    public RangeSelector(String labelText) {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Чтобы метка занимала две ячейки
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER; // Центрируем метку

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, gbc);

        // Метка "от"
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Сбрасываем ширину до одной ячейки
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE; // Не растягиваем метку
        gbc.anchor = GridBagConstraints.EAST; // Выравниваем метку вправо
        JLabel fromLabel = new JLabel("от:");
        panel.add(fromLabel, gbc);

        // Поле startField
        gbc.gridx = 1;
        gbc.weightx = 0.5; // Распределяем свободное место между полями
        gbc.fill = GridBagConstraints.HORIZONTAL; // Растягиваем поле по горизонтали
        gbc.anchor = GridBagConstraints.CENTER; // Центрируем компонент по умолчанию
        startField = new JTextField();
        panel.add(startField, gbc);

        // Метка "до"
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE; // Не растягиваем метку
        gbc.anchor = GridBagConstraints.EAST; // Выравниваем метку вправо
        JLabel toLabel = new JLabel("до:");
        panel.add(toLabel, gbc);

        // Поле endField
        gbc.gridx = 1;
        gbc.weightx = 0.5; // Распределяем свободное место между полями
        gbc.fill = GridBagConstraints.HORIZONTAL; // Растягиваем поле по горизонтали
        gbc.anchor = GridBagConstraints.CENTER; // Центрируем компонент по умолчанию
        endField = new JTextField();
        panel.add(endField, gbc);
    }

    public JPanel getPanel() {
        return panel;
    }

    public Integer getStartIntValue() {
        return Integer.parseInt(startField.getText());
    }

    public Integer getEndIntValue() {
        return Integer.parseInt(endField.getText());
    }

    public void setStartField(String value) {
        startField.setText(value);
    }

    public void setEndField(String value) {
        endField.setText(value);
    }

    private Date getDate(JTextField field) {
        Date sqlDate = null;
        try {
            // Создание объекта SimpleDateFormat с нужным форматом
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            // Преобразование строки в java.util.Date
            java.util.Date parsedDate = dateFormat.parse(field.getText());
            // Преобразование java.util.Date в java.sql.Date
            sqlDate = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Неверный формат даты\n дата должна вводиться в формате дд.мм.гггг"
                    + e.getMessage());
        }
        return sqlDate;
    }

    public Date getStartDate() {
        return getDate(startField);
    }

    public Date getEndDate() {
        return getDate(endField);
    }


}