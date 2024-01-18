package chess;

import java.util.Collection;
import java.util.LinkedList;
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
        Collection<ChessMove> valid_moves = new LinkedList<>();

        if (this.type == PieceType.KING){
            int[][] add_ups = {
                    {1, 0}, {1, 1}, {1, -1},
                    {-1, 0}, {-1, 1}, {-1, -1},
                    {0, 1}, {0, -1}
            };

            for (int i = 0; i < add_ups.length; i++) {
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board);
                if (valid_New_Position != null) {
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,PieceType.KING);
                    valid_moves.add(move);
                }
            }

        }

        else if (this.type == PieceType.QUEEN){

        }

        else if (this.type == PieceType.PAWN){
            int[][] add_ups = {
                    {1, 0}, {1, 1}, {1, -1},
                    {-1, 0}, {-1, 1}, {-1, -1},
                    {0, 1}, {0, -1}
            };

            for (int i = 0; i < add_ups.length; i++) {
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board);
                if (valid_New_Position != null) {
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,PieceType.PAWN);
                    valid_moves.add(move);
                }
            }
            ChessPosition extra_valid_New_Position = myPosition.new_position(myPosition.getRow() + 2, myPosition.getColumn(), board);
            if (extra_valid_New_Position != null){
                ChessMove extra_move = new ChessMove(myPosition, extra_valid_New_Position,PieceType.PAWN);
                valid_moves.add(extra_move);
            }
            }

        else if (this.type == PieceType.BISHOP){

        }

        else if (this.type == PieceType.KNIGHT){

        }

        else if (this.type == PieceType.ROOK){
            int[] add_ups = {1, -1};

            };

            for (int i = 0; i < 4; i++) {
                int[][] add_ups = {
                        {1, 0}, {-1, 0}, {0, 1},
                        {0, -1}
                };
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board);

                while(valid_New_Position != null){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,PieceType.ROOK);
                    valid_moves.add(move);
                    int newRow_ = newRow + add_ups[i][0];
                    int newColumn_ = newColumn + add_ups[i][1];
                    valid_New_Position = myPosition.new_position(newRow_, newColumn_, board);
                }
                

            }

        }


    }
}
