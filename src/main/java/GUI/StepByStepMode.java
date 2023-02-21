package GUI;

import Entities.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepByStepMode extends JFrame {
    private JPanel contentPane;
    private JLabel label;
    private JTable calendarTable;
    private JTable bufferTable;
    private JButton button;
    private JPanel calendarPanel;
    private JPanel buferPanel;
    private int calendarIndex;

    public StepByStepMode(Controller engine) {
        Object[][] result = new Object[(int) (engine.getAmountOfRequests() * 3)][5];
        calendarIndex = 0;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (engine.getAllNumberOfGeneratedRequests() < engine.getAmountOfRequests()) {
                    engine.stepByStepMode();
                    result[calendarIndex] = engine.getCalendarStatus(calendarIndex);
                    createCalendarTable(result);
                    createBufferTable(engine.getBufferStatus(calendarIndex));
                    calendarIndex++;
                } else {
                    result[calendarIndex] = new Object[]{"End of modulation", engine.getSystemTime()};
                    createCalendarTable(result);
                }
            }

        });
        this.setContentPane(contentPane);
        this.setTitle("Step by step Mode");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width / 2 - 300, dimension.height / 2 - 400, 600, 800);
        this.setVisible(true);


    }

    public void createCalendarTable(Object[][] content) {
        calendarPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Calendar Table", TitledBorder.LEFT,
                TitledBorder.TOP));
        calendarTable.setModel(new DefaultTableModel(
                content,
                new String[]{"Event", "Time", "Type", "Generated requests", "Failed requests"}
        ));
    }

    public void createBufferTable(Object[][] content) {
        buferPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Entities.Buffer Table", TitledBorder.LEFT,
                TitledBorder.TOP));
        bufferTable.setModel(new DefaultTableModel(
                content,
                new String[]{"Number", "Time", "Entities.Request"}
        ));
    }
}
