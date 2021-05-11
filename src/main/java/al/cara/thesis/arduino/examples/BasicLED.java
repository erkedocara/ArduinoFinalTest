package al.cara.thesis.arduino.examples;

import al.cara.thesis.arduino.ArduinoConnector;

import java.util.Scanner;

public class BasicLED {

    public static void main(String[] args) {

        Scanner ob = new Scanner(System.in);
        ArduinoConnector arduinoConnector = new ArduinoConnector("COM3", 9600);
        arduinoConnector.openConnection();
        System.out.println("Enter 1 to switch LED on and 0  to switch LED off");
        char input = ob.nextLine().charAt(0);
        while (input != 'n') {
            arduinoConnector.serialWrite(input);
            input = ob.nextLine().charAt(0);
        }
        ob.close();
        arduinoConnector.closeConnection();

    }

}
