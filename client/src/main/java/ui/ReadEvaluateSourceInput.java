package ui;


import chess.ChessBoard;
import client.ServerFacade;
import ui.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ReadEvaluateSourceInput {

    private Scanner scanner = new Scanner(System.in);

    private String input;

    private ServerFacade client_call = new ServerFacade();

    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private String readInput(String prompt, boolean formatted){
        System.out.println(prompt);
        input = scanner.nextLine();
        if (formatted){
            input = input.toLowerCase().trim();
        }
        return input;
    }

    private void runPreLogin() throws IOException {
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
                client_call.register(input);
                runPostLogin();
                break;
            }
            else if (input.equals("login")){
                input = readInput("Type your USERNAME PASSWORD: ", false);
                client_call.login(input);
                runPostLogin();
                break;
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

    private void printBoards(){
        System.out.println();
        ChessBoardUi.drawBoard(out, "one");
        out.println();
        out.println();
        ChessBoardUi.drawBoard(out, "two");
        System.out.println();
    }

    private void runPostLogin() throws IOException {
        while(true){
            System.out.println();
            input = readInput("""
                            Select one of the following options: 
                                - logout (to leave current account)
                                - create (to create a game)
                                - list (to list all games created)
                                - join (to join a game)
                                - observe (to join game with watch only mode)
d                               - help (for more info)""", true);

            if (input.equals("logout")){
                client_call.logout();
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
                input = readInput("Type desired game ID and PIECE COLOR (BLACK|WHITE|NONE): ", false);
                client_call.join(input);

                printBoards();
            }
            else if (input.equals("observe")){
                input = readInput("Type desired game ID: ", false);
                client_call.join(input);

                printBoards();
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

    public void run() throws IOException {

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
