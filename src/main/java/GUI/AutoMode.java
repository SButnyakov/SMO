package GUI;

import Entities.Controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AutoMode extends JFrame {
    private JPanel contentPane;
    private JLabel autoLabel;
    private JTable sourceTable;
    private JTable deviceTable;
    private JPanel sourcePanel;
    private JPanel devicePanel;

    public AutoMode(Controller engine) {
        engine.autoMode();
        createSourceTable(engine.getSourceStatus());
        createDeviceTable(engine.getDeviceStatus());
        this.setContentPane(contentPane);
        this.setTitle("Auto Mode");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds(dimension.width / 2 - 500, dimension.height / 2 - 300, 1000, 600);
        this.setVisible(true);
    }

    private void createSourceTable(Object[][] content) {
        sourcePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Entities.Source Table", TitledBorder.LEFT,
                TitledBorder.TOP));
        sourceTable.setModel(new DefaultTableModel(
                content,
                new String[]{"Number", "Generated requests", "Fail probability", "Average TimeInSystem", "Average TimeWaiting",
                        "Average TimeProcessing", "Dispersion TimeWaiting", "Dispersion TimeProcessing"}
        ));
    }

    private void createDeviceTable(Object[][] content) {
        devicePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Entities.Device Table", TitledBorder.LEFT,
                TitledBorder.TOP));
        deviceTable.setModel(new DefaultTableModel(
                content,
                new String[]{"Number", "Usage rate"}
        ));
    }
}
