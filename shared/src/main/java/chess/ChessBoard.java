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


        if (position.getRow() <= 8 && position.getRow() > 0 && position.getColumn() <= 8 && position.getColumn() > 0){
            //System.out.println((position.getRow()) + " " + (position.getColumn()) );
            this.structure[position.getRow()-1][position.getColumn()-1] = piece;
            //System.out.println(this.structure[position.getRow()][position.getColumn()].getPieceType());
        }

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

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //  System.out.println(position.getRow());
        return this.structure[position.getRow()-1][position.getColumn()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece[][] reset_board = new ChessPiece[8][8];
        this.structure = reset_board;

        ChessPiece.PieceType[] piece_types = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 1; i <= 8; i++){
            ChessPiece pawn_white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition position_white_pawn = new ChessPosition(2, i);
            ChessPiece pawn_black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPosition position_black_pawn = new ChessPosition(7,i);
            addPiece(position_white_pawn, pawn_white);
            addPiece(position_black_pawn, pawn_black);
            ChessPiece piece_white = new ChessPiece(ChessGame.TeamColor.WHITE, piece_types[i-1]);
            ChessPosition position_white = new ChessPosition(1, i);
            ChessPiece piece_black = new ChessPiece(ChessGame.TeamColor.BLACK, piece_types[i-1]);
            ChessPosition position_black = new ChessPosition(8, i);
            addPiece(position_white, piece_white);
            addPiece(position_black, piece_black);
            //System.out.print(position_black.getRow());
        }

    }

    public ChessPiece[][] getBoardStructure(){

        return this.structure;

    }
}
