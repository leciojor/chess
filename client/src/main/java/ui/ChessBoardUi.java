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
    private static Random rand = new Random();

    private static boolean alternate = false;
    private static boolean orientationAlternate = false;





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out);
    }





    private static void drawBoard(PrintStream out) {
        if (!orientationAlternate){
            drawBorders(out, "one");
        }
        else{
            drawBorders(out, "two");
        }



        orientationAlternate = !orientationAlternate;
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
            if (row == 0){
                drawInside(out, "top0");
            }
            else if(row == 1){
                drawInside(out, "top1");
            }
            else if(row == 6){
                drawInside(out, "down0");
            }
            else if(row == 7){
                drawInside(out, "down1");
            }

            out.print(DIGITS_SIDES[row]);
            out.print(SIDE_COMPLIMENT);

            out.println();

        }
    }

    private static void drawInside(PrintStream out, String piecesToInsert) {
        out.print(EMPTY);
        for (int col = 0; col < BOARD_SIZE; col++){
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

        alternate = !alternate;
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setTextBlack(PrintStream out) {
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
        setTextBlack(out);
        out.print(player);
    }


}
