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

    public ChessPosition validPosition(int positionRow, int positionCol, ChessBoard board, ChessGame.TeamColor color){

        ChessPosition newValidPosition = new ChessPosition(positionRow, positionCol);


        if ((positionRow <= 8 && positionRow > 0)&&(positionCol <= 8 && positionCol > 0)){
            if (board.getPiece(newValidPosition) == null){
                return newValidPosition;
            }

            else if(board.getPiece(newValidPosition) != null && board.getPiece(newValidPosition).getTeamColor() != color){
                newValidPosition.killed = true;
                return newValidPosition;
            }
        }
        else{
            return null;
        }

        return null;
    }
}

