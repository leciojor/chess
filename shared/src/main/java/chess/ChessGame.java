package chess;


import java.util.*;

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
        ArrayList<ChessMove> valid_moves = new ArrayList<ChessMove>();
        ChessBoard current_board = getBoard();
        //System.out.print(current_board);
        ChessPiece piece_to_move = current_board.getPiece(startPosition);


        if (piece_to_move == null){
            return null;
        }
        TeamColor piece_color = piece_to_move.getTeamColor();

        Collection<ChessMove> valid_moves_piece = piece_to_move.pieceMoves(current_board, startPosition);
        //System.out.print(valid_moves_piece);

        ChessBoard temp_copy = current_board.boardDeepCopy();
        ChessGame temp_game = new ChessGame();
        for (ChessMove move : valid_moves_piece) {
            //System.out.print(move);
            ChessPosition temp_position = move.getEndPosition();
            temp_copy.addPiece(move.getStartPosition(), null);
            temp_copy.addPiece(temp_position, piece_to_move);
            temp_game.setBoard(temp_copy);
            //System.out.print(!temp_game.isInCheck(piece_color));
            if (!temp_game.isInCheck(piece_color)) {
                valid_moves.add(move);
            }
            temp_copy = current_board.boardDeepCopy();
            temp_game.setBoard(temp_copy);

        }
        return valid_moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        if(valid != null){
            if (valid.contains(move)){
                ChessBoard current_board = getBoard();
                ChessPiece piece_to_move = current_board.getPiece(move.getStartPosition());
                setBoard(game_board.boardDeepCopy());
                getBoard().addPiece(move.getStartPosition(), null);
                getBoard().addPiece(move.getEndPosition(), piece_to_move);

            }
            else{
                //System.out.print(valid);
                //System.out.print(move);
                throw new InvalidMoveException("This move is not valid");
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(game_board, chessGame.game_board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(game_board, turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard current_board = getBoard();
        if (getKingPosition(teamColor) != null) {
            ChessPosition king_position = getKingPosition(teamColor);

            //System.out.println(king_position);
            HashSet<ArrayList<Object>> opposite_team_pieces = getOppositeTeamPieces(current_board, teamColor);
            //System.out.print(opposite_team_pieces);
            for (ArrayList<Object> pair : opposite_team_pieces) {

                ChessPiece piece = (ChessPiece) pair.get(0);
                //System.out.print(piece);
                ChessPosition position = (ChessPosition) pair.get(1);
                //System.out.print(position);
                Collection<ChessMove> valid_moves_piece = piece.pieceMoves(current_board, position);
                //System.out.print(valid_moves_piece);
                for (ChessMove move : valid_moves_piece) {
                    //System.out.print(move.getEndPosition());
                    //System.out.print(move.getEndPosition() == king_position);
                    //System.out.println(move.getEndPosition() + "al");
                    //System.out.println(king_position + "king");
                    if (move.getEndPosition().getRow() == king_position.getRow() && move.getEndPosition().getColumn() == king_position.getColumn()) {
                        return true;
                    }
                    //System.out.println(move.getEndPosition() + "al");
                    //System.out.println(king_position + "king");
                }

            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        java.util.ArrayList<ChessMove> valid_moves = new java.util.ArrayList<ChessMove>();
        if(isInCheck(teamColor)){
            ChessBoard current_board = getBoard();
            HashSet<ArrayList<Object>> team_pieces = getTeamPieces(current_board, teamColor);
            for (ArrayList<Object> pair : team_pieces){
                ChessPiece piece = (ChessPiece) pair.get(0);
                ChessPosition position = (ChessPosition) pair.get(1);
                Collection<ChessMove> all_moves = piece.pieceMoves(current_board, position);

                for (ChessMove move: all_moves){
                    ChessBoard temp_board = current_board.boardDeepCopy();
                    ChessGame temp_game = new ChessGame();
                    temp_board.addPiece(move.getStartPosition(), null);
                    temp_board.addPiece(move.getEndPosition(), piece);
                    temp_game.setBoard(temp_board);
                    if (!temp_game.isInCheck(teamColor)) {
                        valid_moves.add(move);
                    }
                    temp_board = current_board.boardDeepCopy();
                    temp_game.setBoard(temp_board);
                }



            }
            if (valid_moves.isEmpty()){
                return true;
            }


        }
        return false;
    }


    private ChessPosition getKingPosition(TeamColor color){
        ChessPosition king_position = null;
        ChessBoard current_board = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                ChessPiece temp_piece = current_board.getPiece(temp_position);
                if(temp_piece != null && temp_piece.getPieceType() == ChessPiece.PieceType.KING && temp_piece.getTeamColor() == color){
                    king_position = temp_position;
                }

            }
        }
        return king_position;
    }

    private ChessPiece getKingPiece(TeamColor color){
        ChessPiece king_piece = null;
        ChessBoard current_board = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                ChessPiece temp_piece = current_board.getPiece(temp_position);
                if(temp_piece != null && temp_piece.getPieceType() == ChessPiece.PieceType.KING && temp_piece.getTeamColor() == color){
                    king_piece = temp_piece;
                }

            }
        }
        return king_piece;
    }



    private HashSet<ArrayList<Object>> getOppositeTeamPieces(ChessBoard board, TeamColor current_color){
        HashSet<ArrayList<Object>> team_pieces = new HashSet<ArrayList<Object>>();
        TeamColor color;
        if(current_color == TeamColor.WHITE){
            color = TeamColor.BLACK;
        }
        else{
            color = TeamColor.WHITE;
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                ChessPiece temp_piece = board.getPiece(temp_position);
                if(temp_piece != null && temp_piece.getTeamColor() == color){
                    //System.out.print(temp_position);
                    ArrayList<Object> piece_pair = new ArrayList<Object>();
                    piece_pair.add(temp_piece);
                    piece_pair.add(temp_position);
                    team_pieces.add(piece_pair);
                }

            }
        }

        return team_pieces;

    }


    private HashSet<ArrayList<Object>> getTeamPieces(ChessBoard board, TeamColor current_color){
        HashSet<ArrayList<Object>> team_pieces = new HashSet<ArrayList<Object>>();
        TeamColor color;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                ChessPiece temp_piece = board.getPiece(temp_position);
                if(temp_piece != null && temp_piece.getTeamColor() == current_color){
                    //System.out.print(temp_position);
                    ArrayList<Object> piece_pair = new ArrayList<Object>();
                    piece_pair.add(temp_piece);
                    piece_pair.add(temp_position);
                    team_pieces.add(piece_pair);
                }

            }
        }

        return team_pieces;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */


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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition temp_position = new ChessPosition(i, j);
                if (current_board.getPiece(temp_position) != null){
                    if (current_board.getPiece(temp_position).getTeamColor() == teamColor && getTeamTurn() == teamColor){
                        //System.out.print(temp_position);
                        Collection<ChessMove> valid_moves = validMoves(temp_position);
                        moves.addAll(valid_moves);
                    }
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
