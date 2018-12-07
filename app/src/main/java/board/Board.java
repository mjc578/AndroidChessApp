/**
 * Representation of the chess board
 * Represented by an 8x8 array
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 */

package board;

import com.example.kmist.chessapp03.R;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Pieces;
import pieces.Position;
import pieces.Queen;
import pieces.Rook;

public class Board {

	/**
	 * Pieces 2-D array field that represents the Chess board
	 */
	private Pieces[][] board; 
	
	/**
	 * Constructor to create an instance of the board. Creates an 8x8 array for the chess board
	 */
	public Board() {
		this.board = new Pieces[8][8];
	}
	
	/**
	 * Method to return the chess board
	 * @return board
	 */
	public Pieces[][] getBoard() {
		return board;
	}
			
	/**
	 * Method to get the piece that is at the position on the board specified.
	 * 
	 * @param p Position specified
	 * @return Pieces piece at position parameter
	 */
	public Pieces atPosition(Position p) {
		int file = p.getFile();
		int rank = p.getRank();
		if(board[file][rank] == null) {
			return null;
		}
		else {
			return board[file][rank];
		}
	}
	
	/**
	 * Method to populate board with pieces at their correct starting positions.
	 */
	public void populate() {
		board[0][7] = new Rook("Rook", "black", new Position('a', 8), R.drawable.brook);
		board[1][7] = new Knight("Night", "black", new Position('b', 8), R.drawable.bknight);
		board[2][7] = new Bishop("Bishop", "black", new Position('c', 8), R.drawable.bbishop);
		board[3][7] = new Queen("Queen", "black", new Position('d', 8), R.drawable.bqueen);
		board[4][7] = new King("King", "black", new Position('e', 8), R.drawable.bking);
		board[5][7] = new Bishop("Bishop", "black", new Position('f', 8), R.drawable.bbishop);
		board[6][7] = new Knight("Night", "black", new Position('g', 8), R.drawable.bknight);
		board[7][7] = new Rook("Rook", "black", new Position('h', 8), R.drawable.brook);
		
		for(int i = 0; i < board.length; i++) {
			board[i][6] = new Pawn("pawn", "black", new Position(Position.toChar(i + 1), 7), R.drawable.bpawn);
		}
		
		//16 whites:
		
		board[0][0] = new Rook("Rook", "white", new Position('a', 1), R.drawable.wrook);
		board[1][0] = new Knight("Night", "white", new Position('b', 1), R.drawable.wknight);
		board[2][0] = new Bishop("Bishop", "white", new Position('c', 1), R.drawable.wbishop);
		board[3][0] = new Queen("Queen", "white", new Position('d', 1), R.drawable.wqueen);
		board[4][0] = new King("King", "white", new Position('e', 1), R.drawable.wking);
		board[5][0] = new Bishop("Bishop", "white", new Position('f', 1), R.drawable.wbishop);
		board[6][0] = new Knight("Night", "white", new Position('g', 1), R.drawable.wknight);
		board[7][0] = new Rook("Rook", "white", new Position('h', 1), R.drawable.wrook);
		
		for(int i = 0; i < board.length; i++) {
			board[i][1] = new Pawn("pawn", "white", new Position(Position.toChar(i + 1), 2), R.drawable.wpawn);
		}
	}
	
	/**
	 * Method which prints out the chess board.
	 */
	public void printBoard() {

		for(int j = board.length - 1; j >= -1; j--) {
			for(int i = 0; i <= 8; i++) {
				if((i != 8 && j != -1) && board[i][j] == null) {
					if(i % 2 == 0) {
						if(j % 2 == 0) {
							System.out.print("## ");
						}
						else {
							System.out.print("   ");
						}
					}
					else {
						if(j % 2 != 0) {
							System.out.print("## ");
						}
						else {
							System.out.print("   ");
						}
					}
				}
				else if((i != 8 && j != -1) && board[i][j] != null) {
					System.out.print(board[i][j].toString());
					System.out.print(" ");
				}
				else if(i == 8 || j == -1) {
					if(j == -1 && i + j != 7) {
						System.out.print(" ");
						System.out.print(Character.toChars(97 + i));
						System.out.print(" ");
					}
					else if(i == 8 && i + j != 7) {
						System.out.println(j + 1);
					}
					else if(i == 8 && j == -1) {
						System.out.println(" ");
					}
				}
			}
		}
	}

	/**
	 * Method to maintain all the pawn pieces and their justMovedTwo fields.
	 * This is necessary to execute after every time a player makes a move
	 * to turn off justMovedTwo fields after the opponent's turn if the pawn has just moved two up.
	 */
	public void maintainPawn() {
		for (int i = 0; i<8; i++) {
			for (int j = 0; j<8; j++) {
				if(board[i][j] != null) {
					if(board[i][j].getName().equals("pawn")) {
						Pawn p = (Pawn) board[i][j];
						if(p.getCount() == 0 && p.getJustMovedTwo()) {
							p.setCount(1);
						}
						else {
							p.setJustMovedTwo(false);
						}
					}
				}
			}
		}
	}	

	/**
	 * Checks if position is under attack by opponent of the player calling this method.
	 * Method mostly for checking if the King is in check.
	 * 
	 * @param piece Player's piece to refer to color to know who is opponent
	 * @param p Position to test if under attack
	 * @param board Current board
	 * @return true if the position is under attack
	 */
	public static boolean isUnderAttack(Pieces piece, Position p, Board board) {
		
		Board temp = new Board();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				temp.getBoard()[i][j] = board.getBoard()[i][j];
			}
		}
		if(board.getBoard()[p.getFile()][p.getRank()] == null) {
			temp.getBoard()[p.getFile()][p.getRank()] = new Pawn("", "", new Position('a', 1), R.drawable.wpawn);
		}			
		for(int i = 0; i < temp.getBoard().length; i++) {
			for(int j = 0; j < temp.getBoard().length; j++) {
				if(temp.getBoard()[i][j] != null) {
					if(piece.getColor().equals("white")) {
						if(temp.getBoard()[i][j].getColor().equals("black")) {
							if(temp.getBoard()[i][j].getName().equals("pawn")) {
								Pawn pawn = (Pawn) temp.getBoard()[i][j];
								if(pawn.canAttack(p, temp)) {
									return true;
								}
							}
							else {
								if(temp.getBoard()[i][j].isValid(p, temp)) {
									return true;
								}
							}
						}
					}
					else {
						if(temp.getBoard()[i][j].getColor().equals("white")) {
							if(temp.getBoard()[i][j].getName().equals("pawn")) {
								Pawn pawn = (Pawn) temp.getBoard()[i][j];
								if(pawn.canAttack(p, temp)) {
									return true;
								}
							}
							else {
								if(temp.getBoard()[i][j].isValid(p, temp)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/** 
	 * Gets position of king of same color as piece calling this methodical method.
	 * 
	 * @param color To refer to to get correct king
	 * @param board Current board
	 * @return Position of the King of interest
	 */
	public Position getPositionKing(String color, Board board) {
		Position p = null;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board.getBoard()[i][j] != null) {
					if(board.getBoard()[i][j].getName().equals("King") && board.getBoard()[i][j].getColor().equals(color)) {
						p = board.getBoard()[i][j].getPosition();
					}
				}
			}
		}
		return p;
	}
	
	/**
	 * Returns position corresponding to file and rank parameters.
	 * 
	 * @param file File for position
	 * @param rank Rank for position
	 * @return Position specified by file and rank
	 */
	public Position getBoardPosition(int file, int rank) {
		if(board[file][rank] == null) {
			return new Position(Position.toChar(file + 1), rank + 1);
		}
		else {
			return board[file][rank].getPosition();
		}
	}
	/**
	* Method to detect a stalemate with respect to the color passed in. Checks if all pieces of the
	* passed in color have legal moves. This method is only called if the player of the current color 
	* does not have their king in check.
	* 
	* @param color Used to check all piece's of a color to see if they have any legal moves
	* @return true if stalemate is detected
	*/
	public boolean isStalemate(String color) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j] != null && board[i][j].getColor().equals(color)) {
					Pieces p = board[i][j];
					for(int k = 0; k < 8; k++) {
						for(int l = 0; l < 8; l++) {
							if(p.isValid(getBoardPosition(k, l), this)) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	public Position[] makeRandomMove(String color){
		Position[] pp = new Position[2];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] != null && board[i][j].getColor().equals(color)){
					for(int m = 0; m < 8; m++){
						for(int n = 0; n < 8; n++){
							Position np = new Position(Position.toChar(m + 1), n + 1);
							if(board[i][j].move(np, this)){
								pp[0] = new Position(Position.toChar(i + 1), j + 1);
								pp[1] = np;
								return pp;
							}
						}
					}
				}
			}
		}
		return null;
	}
}
