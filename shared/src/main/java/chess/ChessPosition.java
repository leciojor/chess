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

    private boolean killed;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
        this.killed = false;
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

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return this.row;
    }

    public boolean getKilled(){
        return this.killed;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public ChessPosition valid_position(int position_row, int position_col, ChessBoard board, ChessGame.TeamColor color){
        //if out of bounds
        //if no one here
        //if else someone but enemy

        ChessPosition new_valid_position = new ChessPosition(position_row, position_col);


        if ((position_row <= 8 && position_row > 0)&&(position_col <= 8 && position_col > 0)){
            if (board.getPiece(new_valid_position) == null){
                return new_valid_position;
            }

            else if(board.getPiece(new_valid_position) != null && board.getPiece(new_valid_position).getTeamColor() != color){
                new_valid_position.killed = true;
                return new_valid_position;
            }
        }
        else{
            return null;
        }

        return null;
    }
}

