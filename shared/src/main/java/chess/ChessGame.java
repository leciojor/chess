package chess;


import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameboard = new ChessBoard();
    private TeamColor turn;

    private static boolean isOver;

    public ChessGame() {
        turn = TeamColor.WHITE;
    }

    public void setIsOver(boolean state){
        isOver = state;
    }

    public boolean getIsOver(){
        return isOver;
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
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessBoard currentBoard = getBoard();
        ChessPiece pieceToMove = currentBoard.getPiece(startPosition);

        if (pieceToMove == null){
            return null;
        }

        TeamColor pieceColor = pieceToMove.getTeamColor();

        Collection<ChessMove> validMovesPiece = pieceToMove.pieceMoves(currentBoard, startPosition);

        ChessBoard tempCopy = currentBoard.boardDeepCopy();
        ChessGame tempGame = new ChessGame();
        for (ChessMove move : validMovesPiece) {
            ChessPosition tempPosition = move.getEndPosition();
            tempCopy.addPiece(move.getStartPosition(), null);
            tempCopy.addPiece(tempPosition, pieceToMove);
            tempGame.setBoard(tempCopy);
            if (!tempGame.isInCheck(pieceColor)) {
                validMoves.add(move);
            }
            tempCopy = currentBoard.boardDeepCopy();
            tempGame.setBoard(tempCopy);

        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        ChessBoard currentBoard = getBoard();
        ChessPiece pieceToMove = currentBoard.getPiece(move.getStartPosition());
        //issue is probably that valid is null
        if(valid != null && pieceToMove != null){
            if (valid.contains(move) && pieceToMove.getTeamColor() == turn){
                TeamColor color = pieceToMove.getTeamColor();
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN){
                    pieceToMove = new ChessPiece(color,ChessPiece.PieceType.QUEEN );
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK){
                    pieceToMove = new ChessPiece(color,ChessPiece.PieceType.ROOK );
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT){
                    pieceToMove = new ChessPiece(color,ChessPiece.PieceType.KNIGHT );
                }
                else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP){
                    pieceToMove = new ChessPiece(color,ChessPiece.PieceType.BISHOP );
                }
                setBoard(currentBoard.boardDeepCopy());
                getBoard().addPiece(move.getStartPosition(), null);
                getBoard().addPiece(move.getEndPosition(), pieceToMove);
                if (color == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                }
                else{
                    setTeamTurn(TeamColor.WHITE);
                }

            }
            else{
                throw new InvalidMoveException("This move is not valid");
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(gameboard, chessGame.gameboard) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameboard, turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard currentBoard = getBoard();
        if (getKingPosition(teamColor) != null) {
            ChessPosition kingPosition = getKingPosition(teamColor);

            HashSet<ArrayList<Object>> oppositeTeamPieces = getOppositeTeamPieces(currentBoard, teamColor);
            for (ArrayList<Object> pair : oppositeTeamPieces) {

                ChessPiece piece = (ChessPiece) pair.get(0);
                ChessPosition position = (ChessPosition) pair.get(1);
                Collection<ChessMove> validMovesPiece = piece.pieceMoves(currentBoard, position);
                for (ChessMove move : validMovesPiece) {
                    if (move.getEndPosition().getRow() == kingPosition.getRow() && move.getEndPosition().getColumn() == kingPosition.getColumn()) {
                        return true;
                    }

                }

            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        java.util.ArrayList<ChessMove> validMoves = new java.util.ArrayList<ChessMove>();
        if(isInCheck(teamColor)){
            ChessBoard currentBoard = getBoard();
            HashSet<ArrayList<Object>> teamPieces = getTeamPieces(currentBoard, teamColor);
            for (ArrayList<Object> pair : teamPieces){
                ChessPiece piece = (ChessPiece) pair.get(0);
                ChessPosition position = (ChessPosition) pair.get(1);
                Collection<ChessMove> allMoves = piece.pieceMoves(currentBoard, position);

                for (ChessMove move: allMoves){
                    ChessBoard tempBoard = currentBoard.boardDeepCopy();
                    ChessGame tempGame = new ChessGame();
                    tempBoard.addPiece(move.getStartPosition(), null);
                    tempBoard.addPiece(move.getEndPosition(), piece);
                    tempGame.setBoard(tempBoard);
                    if (!tempGame.isInCheck(teamColor)) {
                        validMoves.add(move);
                    }
                    tempBoard = currentBoard.boardDeepCopy();
                    tempGame.setBoard(tempBoard);
                }
            }
            if (validMoves.isEmpty()){
                return true;
            }


        }
        return false;
    }


    private ChessPosition getKingPosition(TeamColor color){
        ChessPosition kingPosition = null;
        ChessBoard currentBoard = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = currentBoard.getPiece(tempPosition);
                if(tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == color){
                    kingPosition = tempPosition;
                }

            }
        }
        return kingPosition;
    }



    private HashSet<ArrayList<Object>> getOppositeTeamPieces(ChessBoard board, TeamColor currentColor){
        HashSet<ArrayList<Object>> teamPieces = new HashSet<ArrayList<Object>>();
        TeamColor color;
        if(currentColor == TeamColor.WHITE){
            color = TeamColor.BLACK;
        }
        else{
            color = TeamColor.WHITE;
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null && tempPiece.getTeamColor() == color){
                    ArrayList<Object> piecePair = new ArrayList<Object>();
                    piecePair.add(tempPiece);
                    piecePair.add(tempPosition);
                    teamPieces.add(piecePair);
                }

            }
        }

        return teamPieces;

    }


    private HashSet<ArrayList<Object>> getTeamPieces(ChessBoard board, TeamColor currentColor){
        HashSet<ArrayList<Object>> teamPieces = new HashSet<ArrayList<Object>>();
        TeamColor color;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null && tempPiece.getTeamColor() == currentColor){
                    ArrayList<Object> piecePair = new ArrayList<Object>();
                    piecePair.add(tempPiece);
                    piecePair.add(tempPosition);
                    teamPieces.add(piecePair);
                }
            }
        }

        return teamPieces;

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
        HashSet<ChessMove> moves = new HashSet<>();
        ChessBoard currentBoard = getBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                if (currentBoard.getPiece(tempPosition) != null){
                    if (currentBoard.getPiece(tempPosition).getTeamColor() == teamColor){
                        Collection<ChessMove> validMoves = validMoves(tempPosition);
                        moves.addAll(validMoves);
                    }
                }

            }

        }
        return moves.isEmpty();



    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameboard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameboard;
    }
}
