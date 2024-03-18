package ui;


import client.ServerFacade;

import java.util.Scanner;

public class ReadEvaluateSourceInput {

    private Scanner scanner = new Scanner(System.in);

    private String input;

    private ServerFacade client_call = new ServerFacade();



    private String readInput(String prompt, boolean formated){
        System.out.print(prompt);
        input = scanner.nextLine();
        if (formated){
            input = input.toLowerCase().trim();
        }
        return input;
    }

    private void runPreLogin(){
        while (true){
            input = readInput("""
                            Select one of the following options: 
                                - register (to register a new account)
                                - login (to get into the game)
                                - quit (to quit the game)
                                - help (for more info)""", true);

            if (input.equals("register")){
                input = readInput("Type desired USERNAME PASSWORD EMAIL: ", false);
                client_call.register(input);
            }
            else if (input.equals("login")){
                input = readInput("Type your USERNAME PASSWORD: ");
                client_call.login(input);
            }
            else if (input.equals("quit")){
                System.out.print("Thank you for playing");
                break;
            }
            else if (!input.equals("help")){
                System.out.print("Invalid input");
            }
        }
    }

    private void runPostLogin(){

    }

    public void run(){

        while (true){
            System.out.println("Press ENTER to START...");

            boolean keyPressed = scanner.hasNextLine();

            if (keyPressed){
                runPreLogin();
                break;
            }

        }

    }



}
