package chess;

import java.lang.String;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessValidMovesGeneral{
    private int[][] add_ups;

    private String for_if_while;
    //for if
    //for while if

    public ChessValidMovesGeneral(int[][] positions, String for_if_while){
        this.add_ups = positions;
        this.for_if_while = for_if_while;
    }

    public String getForIfWhile(){
        return this.for_if_while;
    }

    public int[][] getPositions(){
        return this.add_ups;
    }

    public HashSet<ChessMove> getValidMoves(ChessPosition start_position, ChessBoard board, ChessGame.TeamColor color){
        HashSet<ChessMove> valid_moves = new HashSet<ChessMove>();

        if (this.for_if_while == "for_if"){
            for (int i = 0; i < this.add_ups.length; i++){
                int new_row = start_position.getRow() + this.add_ups[i][0];
                int new_col = start_position.getColumn() + this.add_ups[i][1];
                ChessPosition new_position = start_position.valid_position(new_row, new_col, board, color);


                if(new_position != null){
                    ChessMove new_move = new ChessMove(start_position, new_position, null);
                    valid_moves.add(new_move);
                }
            }

        }



        else if (this.for_if_while == "for_while_if"){
            for (int i = 0; i < this.add_ups.length; i++){
                int new_row = start_position.getRow() + this.add_ups[i][0];
                int new_col = start_position.getColumn() + this.add_ups[i][1];
                ChessPosition new_position = start_position.valid_position(new_row, new_col, board, color);


                while(new_position != null && (!new_position.getKilled())){
                    //System.out.println(new_position);
                    ChessMove new_move = new ChessMove(start_position, new_position, null);
                    valid_moves.add(new_move);

                    new_row = new_position.getRow() + this.add_ups[i][0];
                    new_col = new_position.getColumn() + this.add_ups[i][1];
                    ChessPosition updated_position = new_position.valid_position(new_row, new_col, board, color);
                    new_position = updated_position;
                }

                if (new_position != null && new_position.getKilled()){
                    ChessMove exception_move = new ChessMove(start_position, new_position, null);
                    valid_moves.add(exception_move);
                }
            }


        }

        return valid_moves;
    }

}
