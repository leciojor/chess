package chess;

import java.util.HashSet;

public class ChessValidMovesPawn{

    private int[][] addUpsWhite;

    private int[][] addUpsBlack;


    public ChessValidMovesPawn(int[][] positions_white, int[][] positions_black){
        this.addUpsWhite = positions_white;
        this.addUpsBlack = positions_black;

    };

    private void addingLogic(HashSet<ChessMove> validMoves, ChessPosition start_position, ChessPosition newValidPosition){
        ChessMove newMove = new ChessMove(start_position, newValidPosition, ChessPiece.PieceType.QUEEN);
        validMoves.add(newMove);
        newMove = new ChessMove(start_position, newValidPosition, ChessPiece.PieceType.ROOK);
        validMoves.add(newMove);
        newMove = new ChessMove(start_position, newValidPosition, ChessPiece.PieceType.BISHOP);
        validMoves.add(newMove);
        newMove = new ChessMove(start_position, newValidPosition, ChessPiece.PieceType.KNIGHT);
        validMoves.add(newMove);

    }


    public HashSet<ChessMove> getValidMoves(ChessPosition start_position, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();

        if (color == ChessGame.TeamColor.WHITE){
            for (int i = 0; i < this.addUpsWhite.length; i++) {
                int newRow = start_position.getRow() + this.addUpsWhite[i][0];
                int newCol = start_position.getColumn() + this.addUpsWhite[i][1];



                if ((newRow <= 8 && newRow > 0) && (newCol <= 8 && newCol > 0)) {
                    ChessPosition newValidPosition = new ChessPosition(newRow, newCol);
                    ChessPosition newValidPositionPlusTwoCase = new ChessPosition(newRow - 1, newCol);


                    if (this.addUpsWhite[i][0] == 2 && start_position.getRow() == 2 && board.getPiece(newValidPosition) == null && board.getPiece(newValidPositionPlusTwoCase) == null){
                        ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                        validMoves.add(newMove);
                    }

                    else if (board.getPiece(newValidPosition) == null && this.addUpsWhite[i][1] == 0 && this.addUpsWhite[i][0] != 2){
                        if (newValidPosition.getRow() == 8){
                            addingLogic(validMoves, start_position, newValidPosition);

                        }
                        else{
                            ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                            validMoves.add(newMove);
                        }

                    }

                    else if (board.getPiece(newValidPosition) != null && this.addUpsWhite[i][1] != 0 && board.getPiece(newValidPosition).getTeamColor() != color && this.addUpsWhite[i][0] != 2){
                        if (newValidPosition.getRow() == 8){
                            addingLogic(validMoves, start_position, newValidPosition);
                        }
                        else{
                            ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                            validMoves.add(newMove);
                        }

                    }


                }
            }
        }

        else {
            for (int i = 0; i < this.addUpsBlack.length; i++) {
                int newRow = start_position.getRow() + this.addUpsBlack[i][0];
                int newCol = start_position.getColumn() + this.addUpsBlack[i][1];


                if ((newRow <= 8 && newRow > 0) && (newCol <= 8 && newCol > 0)) {
                    ChessPosition newValidPosition = new ChessPosition(newRow, newCol);
                    ChessPosition newValidPositionPlusTwoCase = new ChessPosition(newRow + 1, newCol);


                    if (this.addUpsBlack[i][0] == -2 && start_position.getRow() == 7 && board.getPiece(newValidPosition) == null && board.getPiece(newValidPositionPlusTwoCase) == null){
                        ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                        validMoves.add(newMove);
                    }

                    else if (board.getPiece(newValidPosition) == null && this.addUpsBlack[i][1] == 0 && this.addUpsBlack[i][0] != -2){
                        if (newValidPosition.getRow() == 1){
                            addingLogic(validMoves, start_position, newValidPosition);
                        }
                        else{
                            ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                            validMoves.add(newMove);
                        }

                    }

                    else if (board.getPiece(newValidPosition) != null && this.addUpsBlack[i][1] != 0 && board.getPiece(newValidPosition).getTeamColor() != color && this.addUpsBlack[i][0] != -2){
                        if (newValidPosition.getRow() == 1){
                            addingLogic(validMoves, start_position, newValidPosition);
                        }
                        else{
                            ChessMove newMove = new ChessMove(start_position, newValidPosition, null);
                            validMoves.add(newMove);
                        }

                    }

                }
            }
        }
        return validMoves;
    }

    //normal cases (just walk one)
    //kill other piece case
    //+2 case
    //promotion case


}


