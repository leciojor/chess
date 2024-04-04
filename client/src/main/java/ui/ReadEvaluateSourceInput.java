package ui;


import client.ClientCommunicator;
import client.ServerFacade;

import java.net.URISyntaxException;
import java.util.HashMap;


import org.eclipse.jetty.websocket.api.Session;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class ReadEvaluateSourceInput {

    private Scanner scanner = new Scanner(System.in);

    private String input;

    private ServerFacade client_call = new ServerFacade(8080);

    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private String readInput(String prompt, boolean formatted){
        System.out.println(prompt);
        input = scanner.nextLine();
        if (formatted){
            input = input.toLowerCase().trim();
        }
        return input;
    }

    private boolean checkInputSize(String input, int requiredSize){
        String[] inputsArray = input.split(" ");

        if (inputsArray.length != requiredSize || Objects.equals(inputsArray[0], "")){
            return false;
        }
        return true;
    }

    private boolean checkIdType(String input){
        String[] inputsArray = input.split(" ");

        try {
            int intValue = Integer.parseInt(inputsArray[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

// for future generalization of run methods
//    private void mainEndpointsGeneralization(String prompt, boolean formatted, int requiredSize, lamba) throws IOException {
//        input = readInput("Type desired USERNAME PASSWORD EMAIL: ", false);
//        if (!checkInputSize(input, 3)){
//            System.out.println("You forgot some required information");
//        }
//        else{
//            client_call.clientEndpointMethod(input);
//            if (!ServerFacade.returned_error){
//                runPostLogin();
//                break;
//            }
//        }
//    }



    private void runPreLogin() throws IOException, DeploymentException, URISyntaxException {
        while (true){
            System.out.println();
            input = readInput("""
                            Select one of the following options: 
                                - register (to register a new account)
                                - login (to get into the game)
                                - quit (to quit the game)
                                - help (for more info)""", true);

            if (input.equals("register")){
                input = readInput("Type desired USERNAME PASSWORD EMAIL: ", false);
                if (!checkInputSize(input, 3)){
                    System.out.println("You forgot some required information");
                }
                else{
                    client_call.register(input);
                    if (!ServerFacade.returned_error){
                        runPostLogin();
                        break;
                    }
                }
            }
            else if (input.equals("login")){
                input = readInput("Type your USERNAME PASSWORD: ", false);
                if (!checkInputSize(input, 2)){
                    System.out.println("You forgot some required information");
                }
                else{
                    client_call.login(input);
                    if (!ServerFacade.returned_error){
                        runPostLogin();
                        break;
                    }
                }
            }
            else if (input.equals("quit")){
                System.out.print("Thank you for playing");
                break;
            }
            else if (!input.equals("help")){
                System.out.println();
                System.out.print("Invalid input");
                System.out.println();
            }
        }
    }

    private void printCurrentBoard(){
        System.out.println();
        ChessBoardUi.drawBoard(out, "one");
        out.println();
        out.println();
        ChessBoardUi.drawBoard(out, "two");
        System.out.println();
    }

    private void printHighlightedBoard(){

    }

    private void runPostLogin() throws IOException, DeploymentException, URISyntaxException {
        while(true){
            System.out.println();
            input = readInput("""
                            Select one of the following options: 
                                - logout (to leave current account)
                                - create (to create a game)
                                - list (to list all games created)
                                - join (to join a game)
                                - observe (to join game with watch only mode)
                                - help (for more info)""", true);

            if (input.equals("logout")){
                client_call.logout();
                if (!ServerFacade.returned_error){
                    runPreLogin();
                    break;
                }
            }
            else if (input.equals("create")){
                input = readInput("Type desired game NAME: ", false);
                System.out.print(input);
                if (!checkInputSize(input, 1)){
                    System.out.println("A game's name can only have one word");
                }
                else{
                    client_call.create(input);
                }
            }
            else if (input.equals("list")){
                client_call.list();
            }
            else if (input.equals("join")){
                input = readInput("Type desired game ID and PIECE COLOR (BLACK|WHITE|NONE): ", false);
                input = input.toLowerCase();
                String[] input_words = input.split("\\s+");

                if (!checkInputSize(input, 2)){
                    System.out.println("You forgot some required information");
                }

                else if (!checkIdType(input)){
                    System.out.println("Game ID has to be a number");
                }

                else if (input_words[1] != "black" || input_words[1] != "white"){
                    client_call.join(input);
                    client_call.webSoc("join_player");

                    if (!ServerFacade.returned_error){
                        printCurrentBoard();
                        runGameplay();
                    }
                }

                else{
                    client_call.join(input);
                    client_call.webSoc("join_observer");

                    if (!ServerFacade.returned_error){
                        printCurrentBoard();
                        runGameplay();
                    }
                }
            }
            else if (input.equals("observe")){
                input = readInput("Type desired game ID: ", false);
                if (!checkInputSize(input, 1)){
                    System.out.println("You can only add the game's ID and nothing else");
                }
                else{
                    client_call.join(input);
                    if (!ServerFacade.returned_error){
                        printCurrentBoard();
                    }
                }
            }
            else if (input.equals("quit")){
                System.out.print("Thank you for playing");
                break;
            }
            else if (!input.equals("help")){
                System.out.print("Invalid input");
                System.out.println();
            }
        }

    }

    private void runGameplay() throws IOException, DeploymentException, URISyntaxException {
        while(true){
            System.out.println();
            input = readInput("""
                            Select one of the following options: 
                                - Help (available actions)
                                - Redraw (redraws the chess board)
                                - Move (makes game move)
                                - Resign (to leave and finish the game)
                                - Highlight (shows legal moves)""", true);

            input = input.toLowerCase();
            if (input.equals("help")){
                System.out.println("""                      
                                - Redraw -> will update your chess board to the current one
                                - Move -> will move some piece to the requested stop (if it is a legal move)
                                - Resign -> leaves and finishes the current game
                                - Highlight -> displays the legal moves allowed based on the selected piece""");
            }

            else if (input.equals("redraw")){
                printCurrentBoard();
            }

            else if (input.equals("move")){
                client_call.webSoc("make_move");
            }

            else if (input.equals("resign")){
                client_call.webSoc("resign");
            }

            else if (input.equals("highlight")){
                printHighlightedBoard();
            }

        }
    }

    public void run() throws IOException, DeploymentException, URISyntaxException {

        while (true){
            System.out.println("Press ENTER to START...");

            boolean keyPressed = scanner.hasNextLine();
            input = scanner.nextLine();

            if (keyPressed){
                runPreLogin();
                break;
            }

        }

    }



}
