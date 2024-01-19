package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    private boolean killed_another_piece = false;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    public boolean GetKilledAnother() {
        return killed_another_piece;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public ChessPosition new_position(int change_row, int change_col, ChessBoard board, ChessGame.TeamColor color){
        //System.out.println(change_row + " " + change_col);

        ChessPosition position_new = new ChessPosition(change_row, change_col);

        //System.out.println(change_row <= 8);
        //System.out.println(change_row > 0 );
        //System.out.println(change_col <= 8 );
        //System.out.println(change_col > 0 );
        //System.out.println(board.getPiece(position_new) == null);

        if (change_row <= 8 && change_row > 0 && change_col <= 8 && change_col > 0){
            if (board.getPiece(position_new) == null) {
                return position_new;
            }
            else if (board.getPiece(position_new).getTeamColor() != color){
                killed_another_piece = true;
                return position_new;
            }
        }
        else{
            return null;
        }
        return null;
    }
}
