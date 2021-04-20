package examples;

import arduino.Arduino;
import arduino.PortDropdownMenu;

import javax.swing.*;
import java.awt.*;

public class LEDWithGUI {
    static Arduino arduino;
    static JFrame frame = new JFrame("An Led Controller");
    static JButton btnOn = new JButton("ON");
    static JButton btnOff = new JButton("OFF");
    static JButton btnRefresh;

    public static void main(String[] args) {
        setUpGUI();

        frame.setResizable(false);

        btnOn.addActionListener(e -> arduino.serialWrite('1'));

        btnOff.addActionListener(e -> arduino.serialWrite('0'));

    }

    public static void populateMenu() { //gets the list of available ports and fills the dropdown menu
        final PortDropdownMenu portList = new PortDropdownMenu();
        portList.refreshMenu();
        final JButton connectButton = new JButton("Connect");
        ImageIcon refresh = new ImageIcon("C:\\Users\\cara\\Desktop\\projectArduino\\Java-Arduino-Communication-Library\\src\\examples\\refresh.png");
        btnRefresh = new JButton(refresh);
        JPanel topPanel = new JPanel();
        btnRefresh.addActionListener(e -> portList.refreshMenu());
        topPanel.add(portList);
        topPanel.add(btnRefresh);
        topPanel.add(connectButton);
        // populate the drop-down box

        connectButton.addActionListener(e -> {
            if (connectButton.getText().equals("Connect")) {
                arduino = new Arduino(portList.getSelectedItem().toString(), 9600);
                if (arduino.openConnection()) {
                    connectButton.setText("Disconnect");
                    portList.setEnabled(false);
                    btnOn.setEnabled(true);
                    btnOff.setEnabled(true);
                    btnRefresh.setEnabled(false);
                    frame.pack();
                }
            } else {
                arduino.closeConnection();
                connectButton.setText("Connect");
                portList.setEnabled(true);
                btnOn.setEnabled(false);
                btnRefresh.setEnabled(true);
                btnOff.setEnabled(false);
            }
        });
        topPanel.setBackground(Color.gray);
        frame.add(topPanel, BorderLayout.NORTH);
    }

    public static void setUpGUI() {
        JLabel jTextField = new JLabel("LAMB PORT 13");
        frame.setSize(600, 600);
        frame.setBackground(Color.black);
        frame.setForeground(Color.black);
        frame.setPreferredSize(new Dimension(600, 120));
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        btnOn.setForeground(Color.GREEN);
        btnOn.setEnabled(false);
        btnOff.setForeground(Color.RED);
        btnOff.setEnabled(false);
        JPanel pane = new JPanel();
        pane.setBackground(Color.gray);
        pane.add(jTextField);
        pane.add(btnOn);
        pane.add(btnOff);
        frame.add(pane, BorderLayout.CENTER);
        populateMenu();
        frame.pack();
        frame.getContentPane();
        frame.setVisible(true);

    }
}
