import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomInputDialog extends JDialog {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton okButton;

    public CustomInputDialog(Frame parent) {
        super(parent, "输入参数", true);
        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("起点："));
        textField1 = new JTextField();
        panel.add(textField1);

        panel.add(new JLabel("终点："));
        textField2 = new JTextField();
        panel.add(textField2);

        panel.add(new JLabel("距离："));
        textField3 = new JTextField();
        panel.add(textField3);

        okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(okButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public String[] showDialog() {
        setVisible(true);
        return new String[]{textField1.getText(), textField2.getText(), textField3.getText()};
    }

}
