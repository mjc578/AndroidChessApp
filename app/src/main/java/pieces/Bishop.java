/**
 * Representation of the pious Bishop Chess piece.
 * The Bishop piece may only move in diagonals, as far as it wants.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 */

package pieces;

import board.Board;

public class Bishop extends Pieces{
	
	/**
	 * Constructor to create instance of the Bishop Chess piece.
	 * 
	 * @param name Should be "Bishop"
	 * @param color "black" or "white"
	 * @param position Current position on the board
	 */
	public Bishop(String name, String color, Position position) {
		super(name, color, position);
	}

	/**
	 * Method to check if Bishop can hypothetically move to new position.
	 * 
	 * @param np New position
	 * @param board Current board
	 * @return true if hypothetical move is valid
	 */
	public boolean isValid(Position np, Board board) {
		
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
		
		if(Math.abs(np.getFile() - this.getPosition().getFile()) == Math.abs(np.getRank() - this.getPosition().getRank())) {
			boolean test = testPosition(this, np, board);
			if(!test) {
				return false;
			}
			return true;
		}
		
		return false;	
	}
	
	/**
	 * Method to move Bishop to new position.
	 * 
	 * @param np New position to move to
	 * @param board Current board
	 * @return true if move successful
	 */
	public boolean move(Position np, Board board) {
		
		if(!isValid(np, board)) {
			return false;
		}
		
		board.getBoard()[np.getFile()][np.getRank()] = this;
		board.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		//update position field
		this.setPosition(Position.toChar(np.getFile()), np.getRank());
		return true;
	}
}
