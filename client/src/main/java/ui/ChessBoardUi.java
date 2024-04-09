package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;
import static ui.TestFactory.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 8;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 29;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    private static Map<ChessPiece.PieceType, String> PIECES_WHITE = new HashMap<>() {{
        put(ChessPiece.PieceType.ROOK, WHITE_ROOK);
        put(ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT);
        put(ChessPiece.PieceType.BISHOP, WHITE_BISHOP);
        put(ChessPiece.PieceType.KING, WHITE_KING);
        put(ChessPiece.PieceType.QUEEN, WHITE_QUEEN);
        put(ChessPiece.PieceType.PAWN, WHITE_PAWN);
    }};

    private static Map<ChessPiece.PieceType, String> PIECES_BLACK = new HashMap<>() {{
        put(ChessPiece.PieceType.ROOK, BLACK_ROOK);
        put(ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT);
        put(ChessPiece.PieceType.BISHOP, BLACK_BISHOP);
        put(ChessPiece.PieceType.KING, BLACK_KING);
        put(ChessPiece.PieceType.QUEEN, BLACK_QUEEN);
        put(ChessPiece.PieceType.PAWN, BLACK_PAWN);
    }};

    private static final String[] HEADER_LETTERS = { "𝐻", "𝒢", "𝐹", "𝐸", "𝒟", "𝒞", "𝐵", "𝒜"};

    private static Random rand = new Random();

    private static boolean alternate_row = true;
    private static boolean alternate_col = true;

    private static ChessGame current_game;




//add method that takes the current board and changes it with the necessary piece changes

    public static void main(String[] args) {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                | | | | | | |q| |
                | | | | |n| | | |
                | | | | | | | | |
                | | | | | | |p| |
                | | | | | | | | |
                | | | | | | | |R|
                | | | | | | | | |
                |K|B| | | | | | |
                """));
        game.setTeamTurn(ChessGame.TeamColor.BLACK);

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, game, ChessGame.TeamColor.BLACK);

    }





    public static void drawBoard(PrintStream out, ChessGame game, ChessGame.TeamColor color) {
        current_game = game;
        drawBorders(out, game, color);
    }


    private static void drawBorders(PrintStream out, ChessGame game, ChessGame.TeamColor color){
        drawTopBottom(out);
        drawTextTopBottom(out);
        if (color == ChessGame.TeamColor.WHITE){
            Map<ChessPiece.PieceType, String> temp_black = PIECES_BLACK;
            PIECES_BLACK = PIECES_WHITE;
            PIECES_WHITE = temp_black;
        }


        out.println();
        drawSides(out);


        drawTextTopBottom(out);
        out.println();
        drawTopBottom(out);
    }

    private static void drawTopBottom(PrintStream out){
        for(int col = 0; col < BOARD_SIZE_CHAR; col++){
            if (col == 0){
                out.print(EMPTY);

            }
            else{
                int random_index = rand.nextInt(DIGITS_SIDES.length);
                String value = DIGITS_SIDES[random_index];
                out.print(value);
            }
        }

        out.println();
    }



    private static void drawSides(PrintStream out){

        for(int row = 0; row < DIGITS_SIDES.length; row++){
            out.print(SIDE_COMPLIMENT);
            out.print(DIGITS_SIDES[row]);

            drawInside(out, row);

            out.print(DIGITS_SIDES[row]);
            out.print(SIDE_COMPLIMENT);

            out.println();

        }
    }

    private static void drawInside(PrintStream out, int row) {
        out.print(EMPTY);

        drawBoxes(out, row);

        setRegular(out);
        out.print(EMPTY);

        alternate_row = !alternate_row;
    }

    private static void drawBoxes(PrintStream out, int row){

        for (int col = 0; col < BOARD_SIZE; col++){

            ChessPosition position = new ChessPosition(row + 1, col + 1);
            ChessPiece chess_piece = current_game.getBoard().getPiece(position);
            if (chess_piece != null){
                ChessGame.TeamColor color = chess_piece.getTeamColor();
                String piece;
                if (color == ChessGame.TeamColor.WHITE){
                    piece = PIECES_WHITE.get(chess_piece.getPieceType());
                }
                else{
                    piece = PIECES_BLACK.get(chess_piece.getPieceType());
                }
                addPieces(out, piece);
            }
            else{

                addPieces(out, null);
            }
            alternate_col = !alternate_col;
        }

    }

    private static void addPieces(PrintStream out, String piece){


        if (!alternate_row && !alternate_col || alternate_row && alternate_col){
            setBoxWhite(out, piece);

        }

        else if(!alternate_row && alternate_col || alternate_row && !alternate_col){
            setBoxBlue(out, piece);
        }

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

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
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

    private static void setRegular(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        setTextBlack(out);
        out.print(player);
    }


}