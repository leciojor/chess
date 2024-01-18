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
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;



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

            for (int i = 0; i < 8; i++){
                ChessPosition valid_new_position = myPosition.new_position(myPosition.getRow() + i, myPosition.getColumn() + j, board);
            }
            ChessPosition valid_new_position = myPosition.new_position(myPosition.getRow() + 1, myPosition.getColumn(), board);
            ChessPosition valid_new_position_1 = myPosition.new_position(myPosition.getRow() + 1, myPosition.getColumn() + 1, board);
            ChessPosition valid_new_position_2 = myPosition.new_position(myPosition.getRow() + 1, myPosition.getColumn() - 1, board);
            ChessPosition valid_new_position_3 = myPosition.new_position(myPosition.getRow() - 1, myPosition.getColumn(), board);
            ChessPosition valid_new_position_4 = myPosition.new_position(myPosition.getRow() - 1, myPosition.getColumn() + 1, board);
            ChessPosition valid_new_position_5 = myPosition.new_position(myPosition.getRow() - 1, myPosition.getColumn() - 1, board);
            ChessPosition valid_new_position_6 = myPosition.new_position(myPosition.getRow(), myPosition.getColumn() + 1, board);
            ChessPosition valid_new_position_7 = myPosition.new_position(myPosition.getRow(), myPosition.getColumn() - 1, board);

            for (int i = 0; i < 8; i++){
                if ()

            }

            ChessPosition end_position_2 = new ChessPosition(myPosition.row, myPosition.col + 1);
            ChessPosition end_position_5 = new ChessPosition(myPosition.row + 1, myPosition.col - 1);
            ChessPosition end_position_6 = new ChessPosition(myPosition.row - 1, myPosition.col + 1);
            ChessPosition end_position_7 = new ChessPosition(myPosition.row - 1, myPosition.col + 1);



            ChessMove move_2 = new ChessMove(myPosition, end_position_2,PieceType.KING);
            ChessMove move_4 = new ChessMove(myPosition, end_position_4,PieceType.KING);
            ChessMove move_5 = new ChessMove(myPosition, end_position_5,PieceType.KING);
            ChessMove move_6 = new ChessMove(myPosition, end_position_6,PieceType.KING);
            ChessMove move_7 = new ChessMove(myPosition, end_position_7,PieceType.KING);

            all_moves[2] = move_2;
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
