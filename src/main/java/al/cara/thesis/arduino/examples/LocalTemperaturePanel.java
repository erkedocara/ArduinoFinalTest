package al.cara.thesis.arduino.examples;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import javax.swing.LayoutStyle.ComponentPlacement;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * TemperaturePanel that can read and display temperatures from a local COM port.
 * @author F. van Slooten
 *
 */
public class LocalTemperaturePanel extends JPanel {

    // variables to communicate with Arduino:
    String readline = "";
    String comPort = "COM3";

    public void setLabelTemp(String temperature) {
        labelTemp.setText(temperature);
    }
    public void setLabelHumidity(String humidity) {
        lblStationName.setText(humidity);
    }

    private JLabel labelTemp;

    private JLabel lblStationName;

    /**
     * Create the panel.
     */
    public LocalTemperaturePanel() {
        labelTemp = new JLabel("TEMP...");
        labelTemp.setFont(new Font("Tahoma", Font.PLAIN, 48));
        labelTemp.setHorizontalAlignment(SwingConstants.CENTER);



        lblStationName = new JLabel("Humidity...");
        lblStationName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblStationName.setHorizontalAlignment(SwingConstants.CENTER);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelTemp, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                .addGap(39)
                                .addComponent(lblStationName, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addGap(36))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelTemp, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(lblStationName, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                .addGap(68))
        );
        setLayout(groupLayout);

        // setup comport:
        /*
         * Setup an eventlistener to respond to data send from Arduino.
         * Based on:
         * jSerialComm Event-Based Reading Usage Example
         * https://github.com/Fazecast/jSerialComm/wiki/Event-Based-Reading-Usage-Example
         */
        // comPort = SerialPort.getCommPorts()[0];
        SerialPort port = SerialPort.getCommPort(comPort);
        port.openPort();
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[port.bytesAvailable()];
                int numRead = port.readBytes(newData, newData.length);
                // System.out.println("Read " + numRead + " bytes.");
                if (numRead > 0) {
                    for (int i = 0; i < newData.length; ++i) {
                        if ((char)newData[i]=='\n'||(char)newData[i]=='\r') {
                            readline=readline.trim();
                            if (readline.length()>0) receive(readline);
                            readline="";
                        }
                        else
                            readline=readline+(char)newData[i];
                    }
                }
            }
        });

        // setup output stream to allow sending to the Arduino:
        //comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_SEMI_BLOCKING, 100, 0);
        //out = comPort.getOutputStream();
        //send("Hello world");

		/*// get a list of available ports:
		SerialPort ports[] = SerialPort.getCommPorts();
		for (int i = 0; i < ports.length; ++i) {
			System.out.println(ports[i].getDescriptivePortName());
		}*/

    }

    /**
     * Get the temperature from the label and return it as a double
     * @return temperature
     */
    public double getTemp() {
        return Double.parseDouble(labelTemp.getText());
    }

    /**
     * If a line of text is received, process it (get the temperature from it)
     * @param line
     */
    protected void receive(String line) {
        System.out.println(line);
        if (readline.startsWith("Temperature:")) {
            String[] words = readline.split(" ");
            String str = words[1].replaceAll("[^\\.0123456789]",""); // remove all non-digits
            double temp = Double.parseDouble(str);
            if (temp>25) setForeground(Color.RED);
            String formattedTemp = String.format(Locale.getDefault(), "%.1f", temp);
            setLabelTemp("TEMP:"+formattedTemp+ "°C");
        }
        else if (readline.startsWith("Humidity:")) {
            String[] words = readline.split(" ");
            String str = words[1].replaceAll("[^\\.0123456789]",""); // remove all non-digits
            double humidity = Double.parseDouble(str);
            String formattedHumidity = String.format(Locale.getDefault(), "%.1f", humidity);
            setLabelHumidity("HUMIDITY:"+formattedHumidity+ "%");
        }
    }
}

