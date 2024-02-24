package chess;

import java.lang.String;
import java.util.HashSet;

public class ChessValidMovesGeneral{
    private int[][] addUps;

    private String forIfWhile;
    //for if
    //for while if

    public ChessValidMovesGeneral(int[][] positions, String forIfWhile){
        this.addUps = positions;
        this.forIfWhile = forIfWhile;
    }


    public HashSet<ChessMove> getValidMoves(ChessPosition startPosition, ChessBoard board, ChessGame.TeamColor color){
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();

        if (this.forIfWhile == "for_if"){
            for (int i = 0; i < this.addUps.length; i++){
                int newRow = startPosition.getRow() + this.addUps[i][0];
                int newCol = startPosition.getColumn() + this.addUps[i][1];
                ChessPosition newPosition = startPosition.validPosition(newRow, newCol, board, color);


                if(newPosition != null){
                    ChessMove newMove = new ChessMove(startPosition, newPosition, null);
                    validMoves.add(newMove);
                }
            }

        }



        else if (this.forIfWhile == "for_while_if"){
            for (int i = 0; i < this.addUps.length; i++){
                int newRow = startPosition.getRow() + this.addUps[i][0];
                int newCol = startPosition.getColumn() + this.addUps[i][1];
                ChessPosition newPosition = startPosition.validPosition(newRow, newCol, board, color);


                while(newPosition != null && (!newPosition.getKilled())){
                    //System.out.println(newPosition);
                    ChessMove newMove = new ChessMove(startPosition, newPosition, null);
                    validMoves.add(newMove);

                    newRow = newPosition.getRow() + this.addUps[i][0];
                    newCol = newPosition.getColumn() + this.addUps[i][1];
                    ChessPosition updatedPosition = newPosition.validPosition(newRow, newCol, board, color);
                    newPosition = updatedPosition;
                }

                if (newPosition != null && newPosition.getKilled()){
                    ChessMove exceptionMove = new ChessMove(startPosition, newPosition, null);
                    validMoves.add(exceptionMove);
                }
            }


        }

        return validMoves;
    }

}
