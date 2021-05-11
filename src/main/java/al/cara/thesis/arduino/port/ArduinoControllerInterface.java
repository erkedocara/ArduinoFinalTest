package al.cara.thesis.arduino.port;

import com.fazecast.jSerialComm.SerialPort;

public interface ArduinoControllerInterface {
    String getPortDescription();

    String serialRead();

    SerialPort getSerialPort();

    String serialRead(int limit);

    void serialWrite(String s);

    void serialWrite(String s, int noOfChars, int delay);

    void serialWrite(char c);

    void serialWrite(char c, int delay);

}
