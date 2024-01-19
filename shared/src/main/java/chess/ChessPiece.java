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
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;




    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
        Collection<ChessMove> valid_moves = new HashSet<>();

        if (this.type == PieceType.KING){
            int[][] add_ups = {
                    {1, 0}, {1, 1}, {1, -1},
                    {-1, 0}, {-1, 1}, {-1, -1},
                    {0, 1}, {0, -1}
            };

            for (int i = 0; i < add_ups.length; i++) {
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);
                if (valid_New_Position != null) {
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,null);
                    valid_moves.add(move);
                }
            }

        }

        else if (this.type == PieceType.QUEEN){
            for (int i = 0; i < 4; i++) {
                int[][] add_ups = {
                        {1, 0}, {-1, 0}, {0, 1},
                        {0, -1},{1, 1}, {1, -1}, {-1, 1},
                        {-1, -1}
                };
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);

                while(valid_New_Position != null && !(valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,null);
                    valid_moves.add(move);
                    newRow = newRow + add_ups[i][0];
                    newColumn = newColumn + add_ups[i][1];
                    valid_New_Position = valid_New_Position.new_position(newRow, newColumn, board, this.pieceColor);
                }
                if (valid_New_Position != null && (valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position, null);
                    valid_moves.add(move);
                }


            }
        }

        else if (this.type == PieceType.PAWN){
            int[][] add_ups = {
                    {1, 0}, {2, 0},
                    {-1, 0},
            };

            for (int i = 0; i < add_ups.length; i++) {
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);
                if (valid_New_Position != null && !(valid_New_Position.GetKilledAnother())) {
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,null);
                    valid_moves.add(move);
                }
            }
            ChessPosition extra_valid_New_Position = myPosition.new_position(myPosition.getRow() + 2, myPosition.getColumn(), board, this.pieceColor);
            if (extra_valid_New_Position != null){
                ChessMove extra_move = new ChessMove(myPosition, extra_valid_New_Position,null);
                valid_moves.add(extra_move);
            }
        }

        else if (this.type == PieceType.BISHOP){
            for (int i = 0; i < 4; i++) {
                int[][] add_ups = {
                        {1, 1}, {1, -1}, {-1, 1},
                        {-1, -1}
                };
                int newRow = myPosition.getRow() + add_ups[i][0];

                int newColumn = myPosition.getColumn() + add_ups[i][1];

                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);
                //System.out.println(valid_New_Position.GetKilledAnother());
                while(valid_New_Position != null && !(valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position, null);
                    valid_moves.add(move);
                    newRow = newRow + add_ups[i][0];
                    newColumn = newColumn + add_ups[i][1];
                    valid_New_Position = valid_New_Position.new_position(newRow, newColumn, board, this.pieceColor);

                }
                if (valid_New_Position != null && (valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position, null);
                    valid_moves.add(move);
                }


            }
        }

        else if (this.type == PieceType.KNIGHT){
            int[][] add_ups = {
                    {2, -1}, {2, 1}, {-2, -1},
                    {-2, 1}, {1, -2}, {-1, -2},
                    {1, 2}, {-1, 2}
            };

            for (int i = 0; i < add_ups.length; i++) {
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);
                if (valid_New_Position != null) {
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,null);
                    valid_moves.add(move);
                }
            }


        }

        else if (this.type == PieceType.ROOK){


            for (int i = 0; i < 4; i++) {
                int[][] add_ups = {
                        {1, 0}, {-1, 0}, {0, 1},
                        {0, -1}
                };
                int newRow = myPosition.getRow() + add_ups[i][0];
                int newColumn = myPosition.getColumn() + add_ups[i][1];
                ChessPosition valid_New_Position = myPosition.new_position(newRow, newColumn, board, this.pieceColor);

                while(valid_New_Position != null && !(valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position,null);
                    valid_moves.add(move);
                    newRow = newRow + add_ups[i][0];
                    newColumn = newColumn + add_ups[i][1];
                    valid_New_Position = valid_New_Position.new_position(newRow, newColumn, board, this.pieceColor);
                }
                if (valid_New_Position != null && (valid_New_Position.GetKilledAnother())){
                    ChessMove move = new ChessMove(myPosition, valid_New_Position, null);
                    valid_moves.add(move);
                }

            }

        }

        return valid_moves;
    }
}
