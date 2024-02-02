package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    //A move is illegal if the chess piece cannot move there(use chessPiece pieceMoves), if the move leaves the team’s king in danger(use isInCheck), or if it’s not the corresponding team's turn.
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> valid_moves = new HashSet<ChessMove>();
        ChessBoard current_board = getBoard();
        ChessPiece piece_to_move = current_board.getPiece(startPosition);
        if (piece_to_move == null){
            return null;
        }
        Collection<ChessMove> valid_moves_piece = piece_to_move.pieceMoves(current_board, startPosition);
        for (int i = 0; i < valid_moves_piece.size(); i++){
            if(){

            }
        }

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        if (valid.contains(move)){
            ChessBoard current_board = getBoard();
            ChessPiece piece_to_move = current_board.getPiece(move.getStartPosition());
            //removes piece from current position
            current_board.addPiece(move.getStartPosition(), null);
            //adds piece to new position
            current_board.addPiece(move.getEndPosition(), piece_to_move);
        }
        else{
            throw new InvalidMoveException("This move is not valid");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        ChessBoard current_board = getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                if (current_board.getPiece(temp_position).getTeamColor() == teamColor){
                    Collection<ChessMove> valid_moves = validMoves(temp_position);
                    moves.addAll(valid_moves);
                }

            }

        }
        if (moves.isEmpty()){
            return true;
        }
        return false;


    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        throw new RuntimeException("Not implemented");
    }
}
