/**
 * Representation of the royal King piece on the Chess board.
 * The King may only move one position at a time in any direction and may
 * also castle with either Rook, so long as they have not moved yet.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 * 
 */

package pieces;

import board.Board;

public class King extends Pieces{
	
	/**
	 * Parameter to track if King has made it first move or no for castling.
	 */
	private boolean firstMove = false;
	
	/**
	 * Constructor to create a King instance.
	 * 
	 * @param name Name is "King"
	 * @param color Color either black or white
	 * @param currentPosition Current position on the board
	 * 
	 */
	public King(String name, String color, Position currentPosition) {
		super(name, color, currentPosition);
	}
	
	/**
	 * Takes in a position and tests if the piece can hypothetically move there.
	 * Method also accounts for castling.
	 * 
	 * @param np Position to test if can move to
	 * @param board Current board
	 * @return true if piece can hypothetically move to new position
	 * 
	 */
	public boolean isValid(Position np, Board board) {
		
		if(!isOutOfBounds(np, board)) {
			return false;
		}
		
		if(isTeammate(np, board)) {
			return false;
		}
		
		//king may move one spot in any direction		
		if(Math.abs(this.getPosition().getFile() - np.getFile()) <= 1 && Math.abs(this.getPosition().getRank() - np.getRank()) <= 1) {
			//check if new position is under attack
			boolean test = testKing(np, board);
			if(!test) { 
				return false;
			}
			if(!Board.isUnderAttack(this, np, board)) {
				return true;
			}
		}
		//attempts castling
		else if(Math.abs(this.getPosition().getFile() - np.getFile()) == 2) {
			//must be in same rank for castlage
			if(this.getPosition().getRank() != np.getRank()) {
				return false;
			}
			if(!castle(np, board)) {
				return false;
			}
			return true;
		}
		return false;
	}

	
	/**
	 * Moves the King to the position if the resulting spot is a valid move.
	 * 
	 * @param np Position to move King to
	 * @param board Current board
	 * 
	 * @return true if move is valid
	 * 
	 */
	public boolean move(Position np, Board board) {
		
		if(!isValid(np, board)) {
			return false;
		}
		
		board.getBoard()[np.getFile()][np.getRank()] = this;
		board.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		//update position field
		this.setPosition(Position.toChar(np.getFile()), np.getRank());
		firstMove = true;
		return true;
		
	}
	
	/**
	 * If player attempts castling, returns true if castling is currently valid
	 * King cant castle if in check, spaces in between rook are in check or if 
	 * rook or king have moved.
	 * 
	 * @param np This is the position king is moving to
	 * @param board Current board
	 * @return true if castling is valid
	 * 
	 */
	private boolean castle(Position np, Board board) {
		
		if(this.firstMove == true) {
			return false;
		}
		
		if(Board.isUnderAttack(this, this.getPosition(), board)) {
			return false;
		}
		int rank = this.getPosition().getRank();
		
		if(this.getPosition().getFile() - np.getFile() == -2) {
			if(board.getBoard()[5][rank] != null || Board.isUnderAttack(this, new Position('f', rank + 1), board)) {
				return false;
			}
			if(board.getBoard()[6][rank] != null || Board.isUnderAttack(this, new Position('g', rank + 1), board)) {
				return false;
			}
			if(board.getBoard()[7][rank] == null) {
				return false;
			}
			else {
				if(board.getBoard()[7][rank].getName().equals("Rook")) {
					Rook r = (Rook) board.getBoard()[7][rank];
					if(r.getFirstMove() == false) {
						board.getBoard()[5][rank] = board.getBoard()[7][rank];
						board.getBoard()[7][rank] = null;
						r.setFirstMove();
						r.setPosition('e', rank);
						firstMove = true;
						return true;
					}
				}
			}
		}
		if(this.getPosition().getFile() - np.getFile() == 2) {
			if(board.getBoard()[3][rank] != null || Board.isUnderAttack(this, new Position('d', rank + 1), board)) {
				return false;
			}
			if(board.getBoard()[2][rank] != null || Board.isUnderAttack(this, new Position('c', rank + 1), board)) {
				return false;
			}
			if(board.getBoard()[1][rank] != null) {
				return false;
			}
			if(board.getBoard()[0][rank] == null) {
				return false;
			}
			else {
				if(board.getBoard()[0][rank].getName().equals("Rook")) {
					Rook r = (Rook) board.getBoard()[0][rank];
					if(r.getFirstMove() == false) {
						board.getBoard()[3][rank] = board.getBoard()[0][rank];
						board.getBoard()[0][rank] = null;
						r.setPosition('c', rank);
						r.setFirstMove();
						firstMove = true;
						return true;
					}
				}
			}
		}
		return false;
	}


	/**
	 * Checks if a spot is under attack by any pieces of the other color, with
	 * respect to the piece that called it.
	 * 
	 * @param board Current board
	 * @return true if spot is under attack
	 * 
	 */
	public boolean isInCheck(Board board) {
		
		if(Board.isUnderAttack(this, this.getPosition(), board)) {
			return true;
		}
		return false;
		
	}
	
	/**
	* Sees if player is in checkmate by testing all players pieces if they can make any valid moves which
	* place king out of check, returns true if none can.
	*
	*@param board Current board
	*@return true if king is in checkmate
	*
	*/
	public boolean isCheckmated(Board board) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Pieces p = board.getBoard()[i][j];
				if(p != null && p.getColor().equals(this.getColor())) {
					for(int k = 0; k < 8; k++) {
						for(int l = 0; l < 8; l++) {
							if(board.getBoard()[k][l] != null && board.getBoard()[k][l].getColor().equals(p.getColor())) {
								continue;
							}
							else if(board.getBoard()[k][l] == null || !board.getBoard()[k][l].getColor().equals(p.getColor())) {
								if(p.getName().equals("pawn")) {
									Pawn pp = (Pawn) p;
									if(pp.isValid(board.getBoardPosition(k, l), board)){
										return false;
									}
								}
								else {
									if(p.isValid(board.getBoardPosition(k, l), board)) {
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Method to test if the king may move to the desired position without putting itself in check.
	 * 
	 * @param np Position to move to and test
	 * @param board Current board
	 * @return true if position does not put King into check
	 */
	public boolean testKing(Position np, Board board) {
		Board temp = new Board();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				temp.getBoard()[i][j] = board.getBoard()[i][j];
			}
		}
		
		King tempKing = new King(this.getName(), this.getColor(), np);
		temp.getBoard()[np.getFile()][np.getRank()] = tempKing;
		temp.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		if(tempKing.isInCheck(temp)) {
			return false;
		}
		return true;
	}
}