package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] structure;

    public ChessBoard() {
        this.structure = new ChessPiece[8][8];
    }

    public ChessPiece[][] getStructure(){
        return structure;
    }

    public void setStructure(ChessPiece[][] newStructure){
        structure = newStructure;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(structure, that.structure);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(structure);
    }


    @Override
    public String toString() {
        return "ChessBoard{" +
                "structure=" + Arrays.toString(structure) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.structure[position.getRow()-1][position.getColumn()-1] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.structure[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        ChessPiece[][] resetBoard = new ChessPiece[8][8];


        ChessPiece.PieceType[] pieceSequence = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 0; i < pieceSequence.length; i++){
            ChessPosition positionWhite = new ChessPosition(0, i);
            ChessPiece pieceWhite = new ChessPiece(ChessGame.TeamColor.WHITE, pieceSequence[i]);
            ChessPosition positionBlack = new ChessPosition(7, i);
            ChessPiece pieceBlack = new ChessPiece(ChessGame.TeamColor.BLACK, pieceSequence[i]);
            resetBoard[positionWhite.getRow()][positionWhite.getColumn()] = pieceWhite;
            resetBoard[positionBlack.getRow()][positionBlack.getColumn()] = pieceBlack;




            ChessPosition positionWhitePawn = new ChessPosition(1, i);
            ChessPiece pieceWhitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPosition positionBlackPawn = new ChessPosition(6, i);
            ChessPiece pieceBlackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            resetBoard[positionWhitePawn.getRow()][positionWhitePawn.getColumn()] = pieceWhitePawn;
            resetBoard[positionBlackPawn.getRow()][positionBlackPawn.getColumn()] = pieceBlackPawn;
            System.out.println(positionBlackPawn);
            System.out.println(pieceBlackPawn);
            System.out.println(positionWhitePawn);
            System.out.println(pieceWhitePawn);
        }


        this.structure = resetBoard;

    }


    public ChessBoard boardDeepCopy(){
        ChessBoard copy = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition tempPosition = new ChessPosition(i, j);
                ChessPiece tempPiece = this.getPiece(tempPosition);
                if (tempPosition != null && tempPiece != null){
                    copy.addPiece(tempPosition, tempPiece);
                }


            }
        }
        return copy;
    }




}
