package GUI;

import Entities.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private JPanel contentPane;
    private JLabel label;
    private JTextField ticketField;
    private JTextField deviceField;
    private JTextField sourceField;
    private JTextField bufferField;
    private JTextField aField;
    private JTextField bField;
    private JTextField lambdaField;
    private JButton autoMode;
    private JButton steByStepMode;

    public MainMenu() {
        this.setContentPane(contentPane);
        this.setTitle("APS");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width / 2 - 250, dimension.height / 2 - 200, 500, 400);
        this.setVisible(true);

        autoMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller engine = new Controller(Integer.parseInt(ticketField.getText()),
                        Integer.parseInt(sourceField.getText()), Integer.parseInt(deviceField.getText()),
                        Integer.parseInt(bufferField.getText()), Double.parseDouble(aField.getText()),
                        Double.parseDouble(bField.getText()), Double.parseDouble(lambdaField.getText()));
                AutoMode autoMode = new AutoMode(engine);
            }
        });
        steByStepMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller engine = new Controller(Integer.parseInt(ticketField.getText()),
                        Integer.parseInt(sourceField.getText()), Integer.parseInt(deviceField.getText()),
                        Integer.parseInt(bufferField.getText()), Double.parseDouble(aField.getText()),
                        Double.parseDouble(bField.getText()), Double.parseDouble(lambdaField.getText()));
                StepByStepMode stepByStepMode = new StepByStepMode(engine);
            }
        });
    }
}
