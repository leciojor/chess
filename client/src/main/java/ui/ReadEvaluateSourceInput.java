package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.ClientCommunicator;
import client.ServerFacade;

import java.net.URISyntaxException;


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

    private int current_game_id;

    private ServerFacade client_call = new ServerFacade(8080);

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private String readInput(String prompt, boolean formatted){
        System.out.println(prompt);
        input = scanner.nextLine();
        if (formatted){
            input = input.toLowerCase().trim();
        }
        return input;
    }

    private ChessGame.TeamColor getUserColor(String color){
        if (Objects.equals(color, "white")){
            return ChessGame.TeamColor.WHITE;
        }

        return ChessGame.TeamColor.BLACK;
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



    private void runPreLogin() throws Exception {
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

    public static void printCurrentBoard(ChessGame game){
        System.out.println();
        ChessBoardUi.drawBoard(out, "one");
        out.println();
        out.println();
        ChessBoardUi.drawBoard(out, "two");
        System.out.println();
    }

    private void printHighlightedBoard(){

    }

    private void runPostLogin() throws Exception {
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
                    System.out.println();
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
                input = readInput("Type desired game ID and PIECE COLOR (BLACK|WHITE|OBSERVE): ", false);
                input = input.toLowerCase();
                String[] input_words = input.split("\\s+");

                if (!checkInputSize(input, 2)){
                    System.out.println("You forgot some required information");
                }

                else if (!checkIdType(input)){
                    System.out.println("Game ID has to be a number");
                }

                else if (Objects.equals(input_words[1], "black") || Objects.equals(input_words[1], "white")){
                    current_game_id = Integer.parseInt(input_words[0]);
                    client_call.join(input);
                    if (ServerFacade.returned_error){
                        System.out.println("Server Error, try again");
                    }
                    else{
                        ChessGame.TeamColor color = getUserColor(input_words[0]);

                        client_call.webSoc("join_player", new Object[]{current_game_id, color, ClientCommunicator.current_auth_token});
                        if (!ServerFacade.returned_error){
                            runGameplay();
                            break;
                        }
                    }
                }
                else{
                    System.out.println("Available sides to play: white, black");
                }
            }
            else if (input.equals("observe")){
                input = readInput("Type desired game ID: ", false);
                input = input.toLowerCase();
                String[] input_words = input.split("\\s+");

                if (!checkInputSize(input, 1)){
                    System.out.println("You can only add the game's ID and nothing else");
                }
                else{
                    client_call.join(input);
                    if (ServerFacade.returned_error){
                        System.out.println("Server Error, try again");
                    }
                    else{
                        client_call.webSoc("join_observer", new Object[]{current_game_id, ClientCommunicator.current_auth_token});
                        if (!ServerFacade.returned_error){
                            runGameplay();
                            break;
                        }
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

    private void runGameplay() throws Exception {
        while(true){
            System.out.println();
            input = readInput("""
                            Select one of the following options: 
                                - Help (available actions)
                                - Redraw (redraws the chess board)
                                - Leave (leaves game)
                                - Move (makes game move)
                                - Resign (to leave and finish the game)
                                - Highlight (shows legal moves)""", true);

            input = input.toLowerCase();
            if (input.equals("help")){
                System.out.println("""                      
                                - Redraw -> will update your chess board to the current one
                                - Leave -> will leave the current game leaving the spot open for another player
                                - Move -> will move some piece to the requested stop (if it is a legal move)
                                - Resign -> leaves and finishes the current game
                                - Highlight -> displays the legal moves allowed based on the selected piece""");
            }

            else if (input.equals("redraw")){
//                printCurrentBoard();
            }

            else if (input.equals("leave")){
                client_call.webSoc("leave", new Object[] {current_game_id, ClientCommunicator.current_auth_token});
                runPostLogin();
                break;
            }

            else if (input.equals("move")){
                String input_start_position = readInput("Type desired piece coordinates(row - col): ", false);
                String input_end_position = readInput("Type desired end coordinates(row - col): ", false);

                input_start_position = input_start_position.toLowerCase();
                input_end_position = input_end_position.toLowerCase();
                String[] start_positions = input_start_position.split("\\s+");
                String[] end_positions = input_end_position.split("\\s+");

                if (!checkInputSize(input_start_position, 2) || !checkInputSize(input_end_position,2)){
                    System.out.println("You forgot some required information or gave positions with wrong format");
                }

                else if (!checkIdType(start_positions[0]) || !checkIdType(start_positions[1]) || !checkIdType(end_positions[0]) || !checkIdType(end_positions[1] )){
                    System.out.println("The coordinates have to be have to be a numbers. It is still your turn");
                }
                else{
                    ChessPosition start_position = new ChessPosition(Integer.parseInt(start_positions[0]), Integer.parseInt(start_positions[1]));
                    ChessPosition end_position = new ChessPosition(Integer.parseInt(end_positions[0]), Integer.parseInt(end_positions[1]));

                    ChessMove user_move = new ChessMove(start_position, end_position, null);

                    client_call.webSoc("make_move", new Object[] {current_game_id, user_move, ClientCommunicator.current_auth_token});

                }

            }

            else if (input.equals("resign")){
                client_call.webSoc("resign", new Object[] {current_game_id, ClientCommunicator.current_auth_token});
                runPostLogin();
                break;
            }

            else if (input.equals("highlight")){
                printHighlightedBoard();
            }

            else if (!input.equals("help")){
                System.out.print("Invalid input");
                System.out.println();
            }

        }
    }

    public void run() throws Exception {

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
