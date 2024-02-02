package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard game_board = new ChessBoard();
    private TeamColor turn;

    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
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
        java.util.ArrayList<ChessMove> valid_moves = new java.util.ArrayList<ChessMove>();
        ChessBoard current_board = getBoard();
        ChessPiece piece_to_move = current_board.getPiece(startPosition);
        TeamColor piece_color = piece_to_move.getTeamColor();
        if (piece_to_move == null){
            return null;
        }
        Collection<ChessMove> valid_moves_piece = piece_to_move.pieceMoves(current_board, startPosition);
        ChessBoard temp_copy = current_board;
        for (ChessMove move : valid_moves_piece) {
            ChessPosition temp_position = move.getEndPosition();
            temp_copy.addPiece(move.getStartPosition(), null);
            temp_copy.addPiece(temp_position, piece_to_move);
            // checkmate or just check
            if (!isInCheckmate(piece_color)) {
                valid_moves.add(move);
            }
            temp_copy = current_board;

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
        ChessBoard current_board = getBoard();
        ChessPosition king_position = getKingPosition(teamColor);
        Object[][] opposite_team_pieces = getOppositeTeamPieces(current_board, teamColor);

        for (int i = 0; i < opposite_team_pieces.length; i++){
            ChessPiece piece = (ChessPiece) opposite_team_pieces[i][0];
            ChessPosition position = (ChessPosition) opposite_team_pieces[i][1];
            Collection<ChessMove> valid_moves_piece = piece.pieceMoves(current_board, position);
            for (ChessMove move : valid_moves_piece){
                if (move.getEndPosition() == king_position){
                    return true;
                }
            }

        }
        return false;
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
        game_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
    }
}
