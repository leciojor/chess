package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 4;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 33;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    private static final String[] PIECES_WHITE = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_KING, WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};

    private static final String[] PIECES_BLACK = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_KING, BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};

    private static Random rand = new Random();

    private static boolean alternate = false;
    private static boolean orientationAlternate = false;





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, "one");
    }





    private static void drawBoard(PrintStream out, String orientation) {
            drawBorders(out, orientation);
    }


    private static void drawBorders(PrintStream out, String orientation){
        drawTopBottom(out);
        out.println();
        drawSides(out);
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
        for (int col = 0; col < BOARD_SIZE; col++){
            addPieces(out, row);
        }
        setRegular(out);
        out.print(EMPTY);

        alternate = !alternate;
    }

    private static void addPieces(PrintStream out, int row){
        if (row == 0){
            setBlue(out);
            printPlayer(out, WHITE_BISHOP);
            setWhite(out);
            printPlayer(out, WHITE_BISHOP);
        }

        else if (row == 1){
            setWhite(out);
            printPlayer(out, WHITE_BISHOP);
            setBlue(out);
            printPlayer(out, WHITE_BISHOP);
        }

        else if (!alternate){
            setBlue(out);
            out.print(EMPTY);
            setWhite(out);
            out.print(EMPTY);
        }
        else{
            setWhite(out);
            out.print(EMPTY);
            setBlue(out);
            out.print(EMPTY);
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