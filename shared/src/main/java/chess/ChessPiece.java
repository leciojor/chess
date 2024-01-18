package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;



    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return this.pieceColor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> valid_moves;
        ChessMove[] all_moves = new ChessMove[10];

        if (this.type == PieceType.KING){
            //add another if statement that checks if its out of bounds and summarizes all of this with less code

            for (int i = 0; i < 8; i++){
                if (myPosition.row + 1 <= board.length){
                    ChessPosition end_position_0 = new ChessPosition(myPosition.row + 1, myPosition.col);
                    ChessMove move_0 = new ChessMove(myPosition, end_position_0, PieceType.KING);
                    all_moves[0] = move_0;
                }
                if (myPosition.row - 1 > 0){
                    ChessPosition end_position_1 = new ChessPosition(myPosition.row - 1, myPosition.col);
                    ChessMove move_1 = new ChessMove(myPosition, end_position_1,PieceType.KING);
                    all_moves[1] = move_1;
                }
            }

            ChessPosition end_position_2 = new ChessPosition(myPosition.row, myPosition.col + 1);
            ChessPosition end_position_3 = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
            ChessPosition end_position_4 = new ChessPosition(myPosition.row + 1, myPosition.col + 1);
            ChessPosition end_position_5 = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
            ChessPosition end_position_6 = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
            ChessPosition end_position_7 = new ChessPosition(myPosition.row - 1, myPosition.col + 1);



            ChessMove move_2 = new ChessMove(myPosition, end_position_2,PieceType.KING);
            ChessMove move_3 = new ChessMove(myPosition, end_position_3,PieceType.KING);
            ChessMove move_4 = new ChessMove(myPosition, end_position_4,PieceType.KING)
            ChessMove move_5 = new ChessMove(myPosition, end_position_5,PieceType.KING);
            ChessMove move_6 = new ChessMove(myPosition, end_position_6,PieceType.KING);
            ChessMove move_7 = new ChessMove(myPosition, end_position_7,PieceType.KING);

            all_moves[1] = move_1;
            all_moves[2] = move_2;
            all_moves[3] = move_3;
            all_moves[4] = move_4;
            all_moves[5] = move_5;
            all_moves[6] = move_6;
            all_moves[7] = move_7;

            valid_moves.add();
        }
        else if (this.type == PieceType.QUEEN){

        }
        else if (this.type == PieceType.PAWN){

        }
        else if (this.type == PieceType.BISHOP){

        }
        else if (this.type == PieceType.KNIGHT){

        }
        else if (this.type == PieceType.ROOK){

        }
    }
}
