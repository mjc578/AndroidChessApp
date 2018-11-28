/**
 * Representation of castley Rook piece on the Chess Board.
 * The Rook can move any amount of spaces within its own file or rank.
 * The Rook may also castle with the King.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 * 
 */

package pieces;

import board.Board;

public class Rook extends Pieces{
	
	/**
	 * Boolean to keep track of if Rook has moved yet or not, for castling.
	 */
	private boolean firstMove = false;
	
	/**
	 * Constructor to create instance of Rook.
	 * 
	 * @param name Should be "Rook"
	 * @param color "black" or "white"
	 * @param position Current position on the Chess board
	 */
	public Rook(String name, String color, Position position) {
		super(name, color, position);
	}
	
	/**
	 * Sets first move to true.
	 */
	public void setFirstMove() {
		firstMove = true;
	}
	
	/**
	 * Get firstMove boolean field.
	 * 
	 * @return firstMove field
	 */
	public boolean getFirstMove() {
		return firstMove;
	}

	/**
	 * Tests if Rook can hypothetically move to the new position.
	 * 
	 * @param np New position
	 * @param board Current board
	 * @return true if piece can move to position
	 */
	public boolean isValid(Position np, Board board) {
		
		//piece may not leave the confines of the board
		if(!isOutOfBounds(np, board)) {
			return false;
		}
		
		//piece is not obstructed by any pieces
		if(!canMoveThrough(np, board)) {
			return false;
		}
		
		if(isTeammate(np, board)) {
			return false;
		}
		
		//rook can only move through a file or through a rank
		if(np.getFile() == this.getPosition().getFile() || np.getRank() == this.getPosition().getRank()) {
			boolean test = testPosition(this, np, board);
			if(!test) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Moves the Rook to the new position specified.
	 * Sets Rook's first move to true.
	 * 
	 * @param np New position
	 * @param board Current board
	 * @return true if move successful
	 */
	public boolean move(Position np, Board board) {
		//if rook tries to make an invalid move, prevent it from moving
		if(!isValid(np, board)){
			return false;
		}
		
		board.getBoard()[np.getFile()][np.getRank()] = this;
		board.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		//update position field
		this.setPosition(Position.toChar(np.getFile()), np.getRank());
		firstMove = true;
		return true;
	}
}
