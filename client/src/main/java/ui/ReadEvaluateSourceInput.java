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
                runPostLogin();
                break;
            }
            else if (input.equals("login")){
                input = readInput("Type your USERNAME PASSWORD: ");
                client_call.login(input);
                runPostLogin();
                break;
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
        while(true){
            input = readInput("""
                            Select one of the following options: 
                                - logout (to leave current account)
                                - create (to create a game)
                                - list (to list all games created)
                                - join (to join a game)
                                - join observer (to join a game as observer)
                                - help (for more info)""", true);

            if (input.equals("logout")){
                client_call.logout(input);
                runPreLogin();
                break;
            }
            else if (input.equals("create")){
                input = readInput("Type desired game NAME: ", false);
                client_call.create(input);
            }
            else if (input.equals("list")){
                client_call.list(input);
            }
            else if (input.equals("join")){
                input = readInput("Type desired game PIECE COLOR (BLACK|WHITE|NONE): ", false);
                client_call.join(input);
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
