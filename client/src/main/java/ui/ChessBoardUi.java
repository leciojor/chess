package ui;

import chess.*;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    private static boolean highlight;

    private static ArrayList<ChessPosition> allowed_positions;




//add method that takes the current board and changes it with the necessary piece changes

    public static void main(String[] args) {
        var game = getNewGame();
        game.setBoard(loadBoard("""
                        |r|n|b|q|k|b|n|r|
                        |p|p|p|p|p|p|p|p|
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        | | | | | | | | |
                        |P|P|P|P|P|P|P|P|
                        |R|N|B|Q|K|B|N|R|
                        """));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawBoard(out, game, ChessGame.TeamColor.BLACK);

        ArrayList<ChessMove> allowed = (ArrayList<ChessMove>) game.validMoves(new ChessPosition(8,8));
//        System.out.println(allowed.isEmpty());
        for (ChessMove move : allowed){
            allowed_positions.add(move.getEndPosition());
        }

        //testing highlight
        highlight(out, game, ChessGame.TeamColor.BLACK);


    }

    public static void setAllowedPositions(ArrayList<ChessPosition> positions){
        allowed_positions = positions;
    }


    public static void drawBoard(PrintStream out, ChessGame game, ChessGame.TeamColor currentColor) {
        ChessGame temp_game_copy = new ChessGame();
        ChessGame temp_game_copy_two = new ChessGame();
        ChessBoard flipped_white = flipWhite(game.getBoard().getStructure());
        ChessBoard flipped_array = flipArray(flipped_white.getStructure());

        temp_game_copy.setBoard(flipped_array);
        temp_game_copy_two.setBoard(flipped_white);

        if (currentColor == ChessGame.TeamColor.BLACK){
            drawBorders(out, temp_game_copy_two);
        }
        else{
            drawBorders(out, temp_game_copy);
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
        ChessPiece[][] flipped_array = new ChessPiece[8][8];
        ChessBoard new_board = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flipped_array[7 - i][7 - j] = structure[i][j];
            }
        }

        new_board.setStructure(flipped_array);
        return new_board;
    }

    private static ChessBoard flipWhite(ChessPiece[][] structure){
        ChessPiece[][] flipped_array = new ChessPiece[8][8];
        ChessBoard new_board = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                flipped_array[i][7 - j] = structure[i][j];
            }
        }

        new_board.setStructure(flipped_array);
        return new_board;
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

        alternate_row = !alternate_row;
    }


    private static String addingBoxes(ChessGame.TeamColor color, ChessPiece chess_piece){
        String piece;
        if (color == ChessGame.TeamColor.WHITE){
            piece = PIECES_WHITE.get(chess_piece.getPieceType());
        }
        else{
            piece = PIECES_BLACK.get(chess_piece.getPieceType());
        }

        return piece;
    }

    private static boolean checkIfWithinAllowedOnes(int row, int col){
        ChessPosition curr_position = new ChessPosition(row, col);
        for (ChessPosition position : allowed_positions){
            if (curr_position.equals(position)){
                return true;
            }
        }
        return false;

    }

    private static void drawBoxes(PrintStream out, int row, ChessGame game){

        for (int col = 0; col < BOARD_SIZE; col++){
            ChessPosition position = new ChessPosition(row + 1, col + 1);
            ChessPiece chess_piece = game.getBoard().getPiece(position);
            if (highlight && allowed_positions != null && checkIfWithinAllowedOnes(row, col)){
                setBoxRed(out);
            }

            else if (chess_piece != null){
                ChessGame.TeamColor color = chess_piece.getTeamColor();
                String piece;
                piece = addingBoxes(color, chess_piece);

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