package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;

    private ChessPiece.PieceType type_;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type_ = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type_ == that.type_;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type_);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type_ +
                '}';
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (this.type_ == PieceType.KING){
            int[][] add_ups = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
            String for_while_if = "for_if";

            ChessValidMovesGeneral validMoves = new ChessValidMovesGeneral(add_ups, for_while_if);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;
        }

        else if (this.type_ == PieceType.QUEEN){
            int[][] add_ups = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
            String for_while_if = "for_while_if";

            ChessValidMovesGeneral validMoves = new ChessValidMovesGeneral(add_ups, for_while_if);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;

        }

        else if (this.type_ == PieceType.BISHOP){
            int[][] add_ups = {{1,1},{1,-1},{-1,1},{-1,-1}};
            String for_while_if = "for_while_if";

            ChessValidMovesGeneral validMoves = new ChessValidMovesGeneral(add_ups, for_while_if);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;
        }

        else if (this.type_ == PieceType.ROOK){
            int[][] add_ups = {{1,0},{-1,0},{0,1},{0,-1}};
            String for_while_if = "for_while_if";

            ChessValidMovesGeneral validMoves = new ChessValidMovesGeneral(add_ups, for_while_if);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;
        }

        else if (this.type_ == PieceType.KNIGHT){
            int[][] add_ups = {{2,1},{2,-1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2}};
            String for_while_if = "for_if";

            ChessValidMovesGeneral validMoves = new ChessValidMovesGeneral(add_ups, for_while_if);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;
        }

        else if (this.type_ == PieceType.PAWN){
            int[][] add_ups_black = {{-1,0}, {-2,0}, {-1,1}, {-1,-1}};
            int[][] add_ups_white = {{1,0}, {2,0}, {1,1}, {1,-1}};

            ChessValidMovesPawn validMoves = new ChessValidMovesPawn(add_ups_white, add_ups_black);
            Collection<ChessMove> valid_moves = validMoves.getValidMoves(myPosition, board, this.color);
            return valid_moves;
        }

        return null;
    }
}
