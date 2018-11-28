/**
 * Representation of the all powerful Queen Chess piece.
 * Fear her.
 * The Queen piece can move like a combination of the Bishop and Rook.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 * 
 */

package pieces;

import board.Board;

public class Queen extends Pieces{

	/**
	 * Construct to create instance of Queen piece.
	 * 
	 * @param name Should be "Queen"
	 * @param color "black" or "white"
	 * @param currentPosition Current position of piece on chess board
	 */
	public Queen(String name, String color, Position currentPosition) {
		super(name, color, currentPosition);
	}

	/**
	 * Test if Queen can hypothetically move to the given position.
	 * 
	 * @param np New position to move to
	 * @param board Current board
	 * @return true if valid move
	 */
	public boolean isValid(Position np, Board board) {
		
		//piece cannot leave the confines of the board, uses the super method instead of override
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
			
		//queen can move like a bishop and a rook piece
		if((np.getFile() == this.getPosition().getFile() || np.getRank() == this.getPosition().getRank())
				|| (Math.abs(np.getFile() - this.getPosition().getFile()) == Math.abs(np.getRank() - this.getPosition().getRank()))){			
			boolean test = testPosition(this, np, board);
			if(!test) {
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Moves queen to specified space.
	 * 
	 * @param np New position to move Queen
	 * @param board Current Board
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
