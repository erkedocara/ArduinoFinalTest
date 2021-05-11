package al.cara.thesis.arduino;

import java.util.Scanner;


public class BasicLEDExample {

    public static void main(String[] args) throws InterruptedException {

        Scanner ob = new Scanner(System.in);
        String ArduinoPort = "COM3"; //Your port name here
        int BAUD_RATE = 9600;
        ArduinoConnector arduino = new ArduinoConnector(ArduinoPort, BAUD_RATE);
        arduino.openConnection();
        arduino.serialWrite('1'); //serialWrite is an overridden method, allowing both characters and strings.//its second parameter even allows delays. more details can be found in the documentation.
        char input = ob.nextLine().charAt(0);
        while(input != 'n'){
            arduino.serialWrite(input,20);
            input = ob.nextLine().charAt(0);
        }
        ob.close();
        arduino.closeConnection();


    }

}
