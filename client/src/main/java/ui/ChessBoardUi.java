package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoardUi {

    private static final int BOARD_SIZE = 8;
    private static final int BOARD_SIZE_CHAR = BOARD_SIZE + 29;
    private static final String[] DIGITS_SIDES = {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT};

    private static final String[] PIECES_WHITE = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_KING, WHITE_QUEEN, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};

    private static final String[] PIECES_BLACK = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_KING, BLACK_QUEEN, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};

    private static final String[] HEADER_LETTERS = { "𝐻", "𝒢", "𝐹", "𝐸", "𝒟", "𝒞", "𝐵", "𝒜"};

    private static Random rand = new Random();

    private static boolean alternate = true;
    private static boolean orientationAlternate = false;





    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, "one");

        out.println();
        out.println();

        drawBoard(out, "two");
    }





    private static void drawBoard(PrintStream out, String orientation) {
            drawBorders(out, orientation);
    }


    private static void drawBorders(PrintStream out, String orientation){
        drawTopBottom(out);
        drawTextTopBottom(out);


        out.println();
        drawSides(out, orientation);


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



    private static void drawSides(PrintStream out, String orientation){
        for(int row = 0; row < DIGITS_SIDES.length; row++){
            out.print(SIDE_COMPLIMENT);
            out.print(DIGITS_SIDES[row]);

            drawInside(out, row, orientation);

            out.print(DIGITS_SIDES[row]);
            out.print(SIDE_COMPLIMENT);

            out.println();

        }
    }

    private static void drawInside(PrintStream out, int row, String orientation) {
        out.print(EMPTY);

        drawBoxes(out, row, orientation);

        setRegular(out);
        out.print(EMPTY);

        alternate = !alternate;
    }

    private static void drawBoxes(PrintStream out, int row, String orientation){

        for (int col = 0; col < BOARD_SIZE; col++){
            String[] temp_pieces_white = {PIECES_WHITE[col], PIECES_WHITE[col + 1]};
            String[] temp_pieces_black = {PIECES_BLACK[col], PIECES_BLACK[col + 1]};

            if (Objects.equals(orientation, "one")){
                addPieces(out, row, temp_pieces_white, temp_pieces_black, WHITE_PAWN, BLACK_PAWN);
            }
            else{
                addPieces(out, row, temp_pieces_black, temp_pieces_white, BLACK_PAWN, WHITE_PAWN);
            }
            col++;
        }

    }

    private static void addPieces(PrintStream out, int row, String[] pieceDown, String[] pieceUp, String pawnDown, String pawnUp){
        if (row == 0){
            setBoxBlue(out, pieceUp);
        }

        else if(row == 7){
            setBoxWhite(out, pieceDown);
        }

        else if (row == 1){
            setBoxWhite(out, pieceUp);
        }

        else if (row == 6){
            setBoxBlue(out, pieceDown);
        }

        else if (!alternate){
            setWhite(out);
            out.print(EMPTY);
            setBlue(out);
            out.print(EMPTY);
        }

        else{
            setBlue(out);
            out.print(EMPTY);
            setWhite(out);
            out.print(EMPTY);
        }
    }

    private static void setBoxBlue(PrintStream out, String[] piece){
        setBlue(out);
        printPlayer(out, piece[0]);
        setWhite(out);
        printPlayer(out, piece[1]);
    }

    private static void setBoxWhite(PrintStream out, String[] piece){
        setWhite(out);
        printPlayer(out, piece[0]);
        setBlue(out);
        printPlayer(out, piece[1]);
    }


    private static void drawTextTopBottom(PrintStream out){
        out.print("   ");
        for (int col = 0; col < HEADER_LETTERS.length; col++){
            out.print("  ");
            out.print(HEADER_LETTERS[col]);
            out.print(" ");
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