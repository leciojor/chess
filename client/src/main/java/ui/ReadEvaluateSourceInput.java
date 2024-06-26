package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.ClientCommunicator;
import client.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ReadEvaluateSourceInput {

    private Scanner scanner = new Scanner(System.in);

    private String input;

    private static int currentGameId;

    private static ServerFacade clientCall = new ServerFacade(8080);

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private static ChessGame currentBoard;

    private static ChessGame.TeamColor currentColor;


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

        for (String inputLoop : inputsArray){
            if (inputLoop.charAt(0) == '-'){
                return false;
            }
        }


        try {
            int intValue = Integer.parseInt(inputsArray[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

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
                    clientCall.register(input);
                    if (!ServerFacade.returnedError){
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
                    clientCall.login(input);
                    if (!ServerFacade.returnedError){
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

    public static void printCurrentBoard(ChessGame game, ChessGame.TeamColor currentColor){
        ChessBoardUi.drawBoard(out, currentBoard, currentColor);

    }

    private void printHighlightedBoard(ArrayList<ChessMove> possibleMoves){
        ArrayList<ChessPosition> possiblePositions = new ArrayList<>();

        if (possibleMoves != null){
            for (ChessMove move : possibleMoves){
                possiblePositions.add(move.getEndPosition());
            }
        }
        else{
            possiblePositions = null;
        }
        ChessBoardUi.setAllowedPositions(possiblePositions);
        ChessBoardUi.highlight(out, currentBoard, currentColor);
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
                clientCall.logout();
                if (!ServerFacade.returnedError){
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
                    clientCall.create(input);
                }
            }
            else if (input.equals("list")){
                clientCall.list();
            }
            else if (input.equals("join")){
                input = readInput("Type desired game ID and PIECE COLOR (BLACK|WHITE): ", false);
                input = input.toLowerCase();
                String[] inputWords = input.split("\\s+");

                if (!checkInputSize(input, 2)){
                    System.out.println("You forgot some required information");
                }

                else if (!checkIdType(inputWords[0])){
                    System.out.println("Game ID has to be a positive number");
                }

                else if (Objects.equals(inputWords[1], "black") || Objects.equals(inputWords[1], "white")){
                    currentGameId = Integer.parseInt(inputWords[0]);

                    clientCall.join(inputWords[0], inputWords[1].toUpperCase());

                        currentColor = getUserColor(inputWords[1]);
                        clientCall.webSoc("join_player", new Object[]{currentGameId, currentColor, ClientCommunicator.currentAuthToken});
                        if (!ServerFacade.returnedError){
                            runGameplay();
                            break;
                        }

                }
                else{
                    System.out.println("Available sides to play: white, black");
                }
            }
            else if (input.equals("observe")){
                input = readInput("Type desired game ID: ", false);
                input = input.toLowerCase();
                String[] inputWords = input.split("\\s+");

                if (!checkInputSize(input, 1)){
                    System.out.println("You can only add the game's ID and nothing else");
                }

                else if (!checkIdType(input)){
                    System.out.println("Game ID has to be a positive number");
                }

                else{
                    currentGameId = Integer.parseInt(inputWords[0]);
                    clientCall.join(inputWords[0], "OBSERVER");
                    clientCall.webSoc("join_observer", new Object[]{Integer.parseInt(inputWords[0]), ClientCommunicator.currentAuthToken});
                    if (!ServerFacade.returnedError){
                        runGameplay();
                        break;
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
                printCurrentBoard(currentBoard, currentColor);
            }

            else if (input.equals("leave")){
                clientCall.webSoc("leave", new Object[] {currentGameId, ClientCommunicator.currentAuthToken});
                runPostLogin();
                break;
            }

            else if (input.equals("move")){
                String inputStartPosition = readInput("Type desired piece coordinates(row - col): ", false);
                String inputEndPosition = readInput("Type desired end coordinates(row - col): ", false);

                inputStartPosition = inputStartPosition.toLowerCase();
                inputEndPosition = inputEndPosition.toLowerCase();
                String[] startPositions = inputStartPosition.split("\\s+");
                String[] endPositions = inputEndPosition.split("\\s+");

                if (!checkInputSize(inputStartPosition, 2) || !checkInputSize(inputEndPosition,2)){
                    System.out.println("You forgot some required information or gave positions with wrong format");
                }

                else if (!checkIdType(startPositions[0]) || !checkIdType(startPositions[1]) || !checkIdType(endPositions[0]) || !checkIdType(endPositions[1] )){
                    System.out.println("The coordinates have to be have to be positive numbers");
                }
                else{
                    ChessPosition startPosition = new ChessPosition(Integer.parseInt(startPositions[0]), Integer.parseInt(startPositions[1]));
                    ChessPosition endPosition = new ChessPosition(Integer.parseInt(endPositions[0]), Integer.parseInt(endPositions[1]));

                    ChessMove userMove = new ChessMove(startPosition, endPosition, null);

                    clientCall.webSoc("make_move", new Object[] {currentGameId, userMove, ClientCommunicator.currentAuthToken});

                }

            }

            else if (input.equals("resign")){
                clientCall.webSoc("resign", new Object[] {currentGameId, ClientCommunicator.currentAuthToken});
                runPostLogin();
                break;
            }

            else if (input.equals("highlight")){
                String inputPiece = readInput("Type desired piece coordinates(row - col): ", false);
                String[] startPositions = inputPiece.split("\\s+");

                if (!checkInputSize(inputPiece, 2)){
                    System.out.println("You forgot some required information or gave positions with wrong format");
                }

                else if (!checkIdType(startPositions[0]) || !checkIdType(startPositions[1] )){
                    System.out.println("The coordinates have to be have to be positive numbers");
                }
                else{
                    ChessPosition piecePosition = new ChessPosition(Integer.parseInt(startPositions[0]), Integer.parseInt(startPositions[1]));
                    if (currentBoard != null){
                        printHighlightedBoard((ArrayList<ChessMove>) currentBoard.validMoves(piecePosition));
                    }

                }

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

    public static void setCurrentBoard(ChessGame currentBoard) {
        ReadEvaluateSourceInput.currentBoard = currentBoard;
    }

    public static ChessGame.TeamColor getCurrentColor() {
        return currentColor;
    }

}
