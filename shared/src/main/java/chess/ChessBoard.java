package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] structure;

    public ChessBoard() {
        this.structure = new ChessPiece[8][8];
    }

    public ChessPiece[][] getStructure() {
        return this.structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(structure, that.structure);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(structure);
    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "structure=" + Arrays.toString(structure) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.structure[position.getRow() - 1][position.getColumn() - 1] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.structure[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //ChessBoard reset_board = new ChessBoard();

        ChessPiece[][] reset_board = new ChessPiece[8][8];


        ChessPiece.PieceType[] piece_sequence = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 0; i < piece_sequence.length; i++) {
            ChessPosition position_white = new ChessPosition(0, i);
            ChessPiece piece_white = new ChessPiece(ChessGame.TeamColor.WHITE, piece_sequence[i]);
            ChessPosition position_black = new ChessPosition(7, i);
            ChessPiece piece_black = new ChessPiece(ChessGame.TeamColor.BLACK, piece_sequence[i]);
            reset_board[position_white.getRow()][position_white.getColumn()] = piece_white;
            reset_board[position_black.getRow()][position_black.getColumn()] = piece_black;


            ChessPosition position_white_pawn = new ChessPosition(1, i);
            ChessPiece piece_white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition position_black_pawn = new ChessPosition(6, i);
            ChessPiece piece_black_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            reset_board[position_white_pawn.getRow()][position_white_pawn.getColumn()] = piece_white_pawn;
            reset_board[position_black_pawn.getRow()][position_black_pawn.getColumn()] = piece_black_pawn;


            this.structure = reset_board;

        }
    }

}
