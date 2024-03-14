package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 4;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 26;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};
    private static Random rand = new Random();

    private static boolean alternate = false;





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBox(out);
    }





    private static void drawBox(PrintStream out) {
        drawBorders(out);

    }


    private static void drawBorders(PrintStream out){
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
        for(String digit : DIGITS_SIDES){
            out.print(SIDE_COMPLIMENT);
            out.print(digit);

            drawInside(out);

            out.print(digit);
            out.print(SIDE_COMPLIMENT);

            out.println();

        }
    }

    private static void drawInside(PrintStream out) {
        out.print(EMPTY);
        for (int row = 0; row < BOARD_SIZE; row++){
            if (!alternate){
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
        setRegular(out);
        out.print(EMPTY);

        if (alternate){
            alternate = false;
        }
        else{
            alternate = true;
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlue(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRegular(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }


}
