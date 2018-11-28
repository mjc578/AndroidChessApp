/**
 * Representation of the chivalrous Knight Chess piece.
 * The Knight has a special movement: it moves in "L's."
 * The knight is also the only piece that can jump over other pieces, excluding
 * when King castles with a Rook.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 *
 */

package pieces;

import board.Board;

public class Knight extends Pieces{
	
	/**
	 * Constructor to create an instance of the Knight Chess piece.
	 * 
	 * @param name Should be "Night" (for that toString method in pieces)
	 * @param color "black" or "white"
	 * @param currentPosition Current position on board
	 */
	public Knight(String name, String color, Position currentPosition) {
		super(name, color, currentPosition);
	}
	
	/**
	 * Test if Knight can hypothetically move to the given position.
	 * Does not need to test for pieces being in the way.
	 * 
	 * @param np New position to move to
	 * @param board Current board
	 * @return true if valid move
	 */
	public boolean isValid(Position np, Board board) {
		
		if(!isOutOfBounds(np, board)) {
			return false;
		}
		
		if(isTeammate(np, board)) {
			return false;
		}
				
		if (Math.abs(np.getFile() - this.getPosition().getFile()) == 2 && Math.abs(np.getRank() - this.getPosition().getRank()) == 1 
			|| Math.abs(np.getFile() - this.getPosition().getFile()) == 1 && Math.abs(np.getRank() - this.getPosition().getRank()) == 2){
			boolean test = testPosition(this, np, board);
			if(!test) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}

	/**
	 * Moves Knight to specified space.
	 * 
	 * @param np New position to move Knight
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
