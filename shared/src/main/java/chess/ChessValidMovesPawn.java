package chess;

import java.util.HashSet;

public class ChessValidMovesPawn{

    private int[][] add_ups_white;

    private int[][] add_ups_black;


    public ChessValidMovesPawn(int[][] positions_white, int[][] positions_black){
        this.add_ups_white = positions_white;
        this.add_ups_black = positions_black;

    };


    public HashSet<ChessMove> getValidMoves(ChessPosition start_position, ChessBoard board, ChessGame.TeamColor color) {
        HashSet<ChessMove> valid_moves = new HashSet<ChessMove>();

        if (color == ChessGame.TeamColor.WHITE){
            for (int i = 0; i < this.add_ups_white.length; i++) {
                int new_row = start_position.getRow() + this.add_ups_white[i][0];
                int new_col = start_position.getColumn() + this.add_ups_white[i][1];



                if ((new_row <= 8 && new_row > 0) && (new_col <= 8 && new_col > 0)) {
                    ChessPosition new_valid_position = new ChessPosition(new_row, new_col);
                    ChessPosition new_valid_position_plus_two_case = new ChessPosition(new_row - 1, new_col);


                    if (this.add_ups_white[i][0] == 2 && start_position.getRow() == 2 && board.getPiece(new_valid_position) == null && board.getPiece(new_valid_position_plus_two_case) == null){
                        ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                        valid_moves.add(new_move);
                    }

                    else if (board.getPiece(new_valid_position) == null && this.add_ups_white[i][1] == 0 && this.add_ups_white[i][0] != 2){
                        if (new_valid_position.getRow() == 8){
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.QUEEN);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.ROOK);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.BISHOP);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.KNIGHT);
                            valid_moves.add(new_move);

                        }
                        else{
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                            valid_moves.add(new_move);
                        }

                    }

                    else if (board.getPiece(new_valid_position) != null && this.add_ups_white[i][1] != 0 && board.getPiece(new_valid_position).getTeamColor() != color && this.add_ups_white[i][0] != 2){
                        if (new_valid_position.getRow() == 8){
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.QUEEN);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.ROOK);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.BISHOP);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.KNIGHT);
                            valid_moves.add(new_move);
                        }
                        else{
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                            valid_moves.add(new_move);
                        }

                    }


                }
            }
        }

        else {
            for (int i = 0; i < this.add_ups_black.length; i++) {
                int new_row = start_position.getRow() + this.add_ups_black[i][0];
                int new_col = start_position.getColumn() + this.add_ups_black[i][1];


                if ((new_row <= 8 && new_row > 0) && (new_col <= 8 && new_col > 0)) {
                    ChessPosition new_valid_position = new ChessPosition(new_row, new_col);
                    ChessPosition new_valid_position_plus_two_case = new ChessPosition(new_row + 1, new_col);


                    if (this.add_ups_black[i][0] == -2 && start_position.getRow() == 7 && board.getPiece(new_valid_position) == null && board.getPiece(new_valid_position_plus_two_case) == null){
                        ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                        valid_moves.add(new_move);
                    }

                    else if (board.getPiece(new_valid_position) == null && this.add_ups_black[i][1] == 0 && this.add_ups_black[i][0] != -2){
                        if (new_valid_position.getRow() == 1){
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.QUEEN);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.ROOK);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.BISHOP);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.KNIGHT);
                            valid_moves.add(new_move);
                        }
                        else{
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                            valid_moves.add(new_move);
                        }

                    }

                    else if (board.getPiece(new_valid_position) != null && this.add_ups_black[i][1] != 0 && board.getPiece(new_valid_position).getTeamColor() != color && this.add_ups_black[i][0] != -2){
                        if (new_valid_position.getRow() == 1){
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.QUEEN);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.ROOK);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.BISHOP);
                            valid_moves.add(new_move);
                            new_move = new ChessMove(start_position, new_valid_position, ChessPiece.PieceType.KNIGHT);
                            valid_moves.add(new_move);
                        }
                        else{
                            ChessMove new_move = new ChessMove(start_position, new_valid_position, null);
                            valid_moves.add(new_move);
                        }

                    }

                }
            }
        }
        return valid_moves;
    }

    //normal cases (just walk one)
    //kill other piece case
    //+2 case
    //promotion case


}


