package at.edu.c02.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    /**
     * This is the main program entry point. TODO: add new commands when implementing additional features.
     */
    public static void main(String[] args) throws IOException, InterruptedException
    {
        LedController ledController = new LedControllerImpl(new ApiServiceImpl());

        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(!input.equalsIgnoreCase("exit"))
        {
            System.out.println("=== LED Controller ===");
            System.out.println("Enter 'demo' to send a demo request");
            System.out.println("Enter 'exit' to exit the program");
            input = reader.readLine();
            if(input.equalsIgnoreCase("demo"))
            {
                ledController.demo();
            }
            else if(input.equalsIgnoreCase("groupstatus"))
            {
                ledController.getGroupStatus();
            }
            else if(input.equalsIgnoreCase("setled"))
            {
                System.out.println("Which LED? (1-8)");
                Integer id = 45 + Integer.valueOf(reader.readLine()) ;
                System.out.println("Which color?");
                String color = reader.readLine();
                ledController.setLed(id, "#ff0");
                System.out.println("LED color set!");
            }
            else if(input.equalsIgnoreCase("turnoffall"))
            {
                ledController.turnOffAllLeds();
            }
            else if(input.equalsIgnoreCase("status"))
            {
                System.out.println("Please specify LED ID:");
                input = reader.readLine();
                int ledId = Integer.parseInt(input);
                ledController.getStatus(ledId);
                System.out.println();
            }
            else if(input.equalsIgnoreCase("spinningled"))
            {
                System.out.println("Which color? ");
                input = reader.readLine();
                String color = input;
                System.out.println("How many turns? ");
                input = reader.readLine();
                int turns = Integer.parseInt(input);
                System.out.println("Starting SpinningLed effect...");
                ledController.laufLicht(color, turns);
            }
        }
    }
}
