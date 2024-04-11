package ui;

import chess.*;


import java.io.PrintStream;
import java.util.*;

import static ui.EscapeSequences.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 8;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 29;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    private static final Map<ChessPiece.PieceType, String> PIECES_WHITE = new HashMap<>() {{
        put(ChessPiece.PieceType.ROOK, WHITE_ROOK);
        put(ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT);
        put(ChessPiece.PieceType.BISHOP, WHITE_BISHOP);
        put(ChessPiece.PieceType.KING, WHITE_KING);
        put(ChessPiece.PieceType.QUEEN, WHITE_QUEEN);
        put(ChessPiece.PieceType.PAWN, WHITE_PAWN);
    }};

    private static final Map<ChessPiece.PieceType, String> PIECES_BLACK = new HashMap<>() {{
        put(ChessPiece.PieceType.ROOK, BLACK_ROOK);
        put(ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT);
        put(ChessPiece.PieceType.BISHOP, BLACK_BISHOP);
        put(ChessPiece.PieceType.KING, BLACK_KING);
        put(ChessPiece.PieceType.QUEEN, BLACK_QUEEN);
        put(ChessPiece.PieceType.PAWN, BLACK_PAWN);
    }};

    private static final String[] HEADER_LETTERS = { "𝐻", "𝒢", "𝐹", "𝐸", "𝒟", "𝒞", "𝐵", "𝒜"};

    private static Random rand = new Random();

    private static boolean alternateRow = true;
    private static boolean alternateCol = true;

    private static boolean highlight;

    private static ArrayList<ChessPosition> allowedPositions;




//add method that takes the current board and changes it with the necessary piece changes


    public static void setAllowedPositions(ArrayList<ChessPosition> positions){
        allowedPositions = positions;
    }


    public static void drawBoard(PrintStream out, ChessGame game, ChessGame.TeamColor currentColor) {
        ChessGame tempGameCopy = new ChessGame();
        ChessGame tempGameCopyTwo = new ChessGame();
        ChessBoard flippedWhite = flipWhite(game.getBoard().getStructure());
        ChessBoard flippedArray = flipArray(flippedWhite.getStructure());

        tempGameCopy.setBoard(flippedArray);
        tempGameCopyTwo.setBoard(flippedWhite);

        if (currentColor == ChessGame.TeamColor.BLACK){
            drawBorders(out, tempGameCopyTwo);
        }
        else{
            drawBorders(out, tempGameCopy);
        }

    }

    public static void highlight(PrintStream out, ChessGame game, ChessGame.TeamColor currentColor){
        highlight = true;
        drawBoard(out, game, currentColor);
        highlight = false;
    }


    private static void drawBorders(PrintStream out, ChessGame game){
        drawTopBottom(out);
        drawTextTopBottom(out);

        out.println();

        drawSides(out, game);

        drawTextTopBottom(out);
        out.println();
        drawTopBottom(out);
    }

    private static ChessBoard flipArray(ChessPiece[][] structure){
        ChessPiece[][] flippedArray = new ChessPiece[8][8];
        ChessBoard newBoard = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flippedArray[7 - i][7 - j] = structure[i][j];
            }
        }

        newBoard.setStructure(flippedArray);
        return newBoard;
    }

    private static ChessBoard flipWhite(ChessPiece[][] structure){
        ChessPiece[][] flippedArray = new ChessPiece[8][8];
        ChessBoard newBoard = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flippedArray[i][7 - j] = structure[i][j];
            }
        }

        newBoard.setStructure(flippedArray);
        return newBoard;
    }



    private static void drawTopBottom(PrintStream out){
        for(int col = 0; col < BOARD_SIZE_CHAR; col++){
            if (col == 0){
                out.print(EMPTY);

            }
            else{
                int randomIndex = rand.nextInt(DIGITS_SIDES.length);
                String value = DIGITS_SIDES[randomIndex];
                out.print(value);
            }
        }

        out.println();
    }



    private static void drawSides(PrintStream out, ChessGame game){

        for(int row = 0; row < DIGITS_SIDES.length; row++){
            out.print(SIDE_COMPLIMENT);
            out.print(DIGITS_SIDES[row]);

            drawInside(out, row, game);

            out.print(DIGITS_SIDES[row]);
            out.print(SIDE_COMPLIMENT);

            out.println();

        }
    }

    private static void drawInside(PrintStream out, int row, ChessGame game) {
        out.print(EMPTY);

        drawBoxes(out, row, game);

        setRegular(out);
        out.print(EMPTY);

        alternateRow = !alternateRow;
    }


    private static String addingBoxes(ChessGame.TeamColor color, ChessPiece chessPiece){
        String piece;
        if (color == ChessGame.TeamColor.WHITE){
            piece = PIECES_WHITE.get(chessPiece.getPieceType());
        }
        else{
            piece = PIECES_BLACK.get(chessPiece.getPieceType());
        }

        return piece;
    }

    private static boolean checkIfWithinAllowedOnes(int row, int col){
        ChessPosition currPosition = new ChessPosition(row + 1, col + 1);
        for (ChessPosition position : allowedPositions){
            if (currPosition.equals(position)){
                return true;
            }
        }
        return false;

    }

    private static void drawBoxes(PrintStream out, int row, ChessGame game){

        for (int col = 0; col < BOARD_SIZE; col++){
            ChessPosition position = new ChessPosition(row + 1, col + 1);
            ChessPiece chessPiece = game.getBoard().getPiece(position);
            if (highlight && allowedPositions != null && checkIfWithinAllowedOnes(row, col)){
                setBoxRed(out);
            }

            else if (chessPiece != null){
                ChessGame.TeamColor color = chessPiece.getTeamColor();
                String piece;
                piece = addingBoxes(color, chessPiece);

                addPieces(out, piece);
            }
            else{
                addPieces(out, null);
            }
;            alternateCol = !alternateCol;
        }

    }

    private static void addPieces(PrintStream out, String piece){


        if (!alternateRow && !alternateCol || alternateRow && alternateCol){
            setBoxWhite(out, piece);

        }

        else if(!alternateRow && alternateCol || alternateRow && !alternateCol){
            setBoxBlue(out, piece);
        }

    }

    private static void setBoxRed(PrintStream out){
        setRed(out);
        out.print(EMPTY);

    }

    private static void setBoxBlue(PrintStream out, String piece){
        setBlue(out);
        if (piece == null){
            out.print(EMPTY);
        }
        else{
            printPlayer(out, piece);
        }

    }


    private static void setBoxWhite(PrintStream out, String piece){
        setWhite(out);
        if (piece == null){
            out.print(EMPTY);
        }
        else{
            printPlayer(out, piece);
        }
    }


    private static void drawTextTopBottom(PrintStream out){
        out.print(EMPTY);
        for (int col = 0; col < HEADER_LETTERS.length; col++){
            out.print(EMPTY_HEADER);
            out.print(HEADER_LETTERS[col]);
        }

    }

    private static void setTextBlack(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
    }

    private static void setRegular(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        setTextBlack(out);
        out.print(player);
    }


}