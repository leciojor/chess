package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 10;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 26;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};
    private static Random rand = new Random();





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBox(out);
    }





    private static void drawBox(PrintStream out) {
        drawBorder(out);

//        for(int row = 0; row < BOARD_SIZE; row++){
//            for(int col = 0; col < BOARD_SIZE; col++){
//
//                if (row == 0) {
//                    out.print(BORDER_UP);
//                }
//
//                else if(row == BOARD_SIZE - 1){
//                    out.print(BORDER_DOWN);
//                }
//
//                else if (col == 0 || col == BOARD_SIZE - 1){
//                    drawBorder(out);
//                }
//
//                else {
//                    out.print(EMPTY);
//                    drawInside(out);
//                }
//
//            }
//            out.println();
//        }
    }



    private static void drawInside(PrintStream out) {

    }

    private static void drawBorder(PrintStream out){
        drawTopBottom(out);
        drawSides(out);
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

            for (int row = 0; row < BOARD_SIZE; row++){
                out.print(EMPTY);
            }

            out.print(digit);
            out.print(SIDE_COMPLIMENT);

            out.println();



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

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }


}
