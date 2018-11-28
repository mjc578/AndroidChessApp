/**
 * Representation for the lowly pawn piece on the Chess board.
 * The pawn piece may only move forward one position at a time.
 * On its first move, it may move two positions, but it does not have to.
 * Pawn may only attack diagonally and it can execute an en passant if an enemy pawn had just moved to 
 * its right or left file on the same rank.
 * Pawn may also promote to any piece except King or pawn upon reaching its final rank.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 */

package pieces;

import board.Board;

public class Pawn extends Pieces{
	
	/**
	 * firstMove boolean to track if pawn has made its first move or not, for purposes of two jump move.
	 * justMovedTwo boolean to track if pawn just jumped two spaces, for en passant.
	 * count int to for the pawn maintenance method.
	 */
	private boolean firstMove = false;
	private boolean justMovedTwo = false;
	private int count = 0;

	/**
	 * Constructor for creating a pawn instance.
	 * 
	 * @param name Name is "pawn"
	 * @param color Either "black" or "white"
	 * @param currentPosition Current position on the board
	 * 
	 */
	public Pawn(String name, String color, Position currentPosition) {
		super(name, color, currentPosition);
	}
	
	/**
	 * Returns firstMove private field.
	 * @return firstMove boolean field
	 * 
	 */
	public boolean getFirstMove() {
		return firstMove;
	}
	
	/**
	 * Return justMovedTwo private field.
	 * @return justMovedTwo boolean field
	 * 
	 */
	public boolean getJustMovedTwo() {
		return justMovedTwo;
	}
	
	/**
	 * Sets the JustMovedTwo boolean. 
	 * @param justMovedTwo true or false
	 * 
	 */
	public void setJustMovedTwo(boolean justMovedTwo) {
		this.justMovedTwo = justMovedTwo;
	}
	
	/**
	 * Returns count field.
	 * @return count int field
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Sets count field.
	 * @param count Passed any number but 0
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Method to check if piece can hypothetically move to given position.
	 * 
	 * @param np Position to test if can move to
	 * @param board Current board
	 * @return true if piece can hypothetically move to new space
	 */
	public boolean isValid(Position np, Board board) {		
		//move must remain in confines of the board, does not depend on color
		if(!isOutOfBounds(np, board)) {
			return false;
		}
		
		//piece is not obstructed by any pieces (in this case, when the pawn moves two)
		if(!canMoveThrough(np, board)) {
			return false;
		}
		
		if(isTeammate(np, board)) {
			return false;
		}
		
		//white pawns can only move up through the ranks, so each move must have a positive rank2 - rank1
		if(this.getColor().equals("white")){
			if(canAttack(np, board)) {
				boolean test = testPosition(this, np, board);
				if(!test) {
					return false;
				}
				return true;
			}
			//moved up one or two...
			if(np.getFile() == this.getPosition().getFile()) {
				//piece moved up two
				if(np.getRank() - this.getPosition().getRank() == 2 && firstMove == false && board.atPosition(np) == null) {
					boolean test = testPosition(this, np, board);
					if(!test) {
						return false;
					}
					return true;
				}
				//piece moved up one
				else if(np.getRank() - this.getPosition().getRank() == 1 && board.atPosition(np) == null) {
					boolean test = testPosition(this, np, board);
					if(!test) {
						return false;
					}
					return true;
				}
			}
		}
		//pawn is black, may only move down
		else{
			if(canAttack(np, board)) {
				boolean test = testPosition(this, np, board);
				if(!test) {
					return false;
				}
				return true;
			}
			if(np.getFile() == this.getPosition().getFile()) {
				if(np.getRank() - this.getPosition().getRank() == -2 && firstMove == false && board.atPosition(np) == null) {
					boolean test = testPosition(this, np, board);
					if(!test) {
						return false;
					}
					return true;
				}
				else if(np.getRank() - this.getPosition().getRank() == -1 && board.atPosition(np) == null) {
					boolean test = testPosition(this, np, board);
					if(!test) {
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Tests if pawn can attack the position desired or if the new position results in an en passant.
	 * 
	 * @param np New position to test
	 * @param board Current board
	 * @return true if pawn can attack or en passant
	 */
	public boolean canAttack(Position np, Board board) {
		//we already checked if the destination spot has a teammate so this spot, if occupied, is guaranteed to have an enemy piece
		//therefore, this is an attempted capture
		//pawn is moving diagonally, therefore the pawn is either attempting en passant or capturing a diagonal enemy piece
		if(this.getColor().equals("white")) {
			//pawn is moving diagonally, therefore the pawn is either attempting en passant or capturing a diagonal enemy piece
			if(Math.abs(np.getFile() - this.getPosition().getFile()) == 1 && np.getRank() - this.getPosition().getRank() == 1) {
				//piece is attempting a capture, we already checked if where it wants to go is occupied by teammate, so it is guaranteed to be an enemy
				if(board.atPosition(np) != null) {
					return true; 
				}
				//piece is attempting en passant, so an enemy pawn must be either on its right file or left file and have JUST moved two squares on its first move
				else {
					//get the position right under where pawn wants to go to see if it is a pawn who just moved 2
					Position p = new Position(Position.toChar(np.getFile() + 1), this.getPosition().getRank() + 1);
					//is the position under occupied?
					if(board.atPosition(p) != null){
						//is position a pawn?
						if(board.atPosition(p).getName().equals("pawn")){
							Pawn wpawn = (Pawn) board.atPosition(p);
							//DID IT JUST MOVE TWO?
							if(wpawn.getJustMovedTwo()) {
								//DENIED, get en passanteddddd
								board.getBoard()[p.getFile()][p.getRank()] = null;
								return true;
							}
						}	
					}
				}
			}
		}
		else {
			if(Math.abs(np.getFile() - this.getPosition().getFile()) == 1 && np.getRank() - this.getPosition().getRank() == -1) {
				if(board.atPosition(np) != null) {
					return true; 
				}
				else {
					Position p = new Position(Position.toChar(np.getFile() + 1), this.getPosition().getRank() + 1);
					if(board.atPosition(p) != null){
						if(board.atPosition(p).getName().equals("pawn")){
							Pawn wpawn = (Pawn) board.atPosition(p);
							if(wpawn.getJustMovedTwo()) {
								board.getBoard()[p.getFile()][p.getRank()] = null;
								return true;
							}
						}	
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Moves the pawn piece to new space. Also includes auto promotion to Queen 
	 * if no promotion piece specified and pawn lands on its last rank.
	 * 
	 * @param np New Position to move to
	 * @param board Current board
	 * @return true if move successful
	 * 
	 */
	public boolean move(Position np, Board board) {
		if(!isValid(np, board)) {
			return false;
		}
		
		if(Math.abs(this.getPosition().getRank() - np.getRank()) == 2) {
			justMovedTwo = true;
		}
		else {
			justMovedTwo = false;
		}
		firstMove = true;
		
		board.getBoard()[np.getFile()][np.getRank()] = this;
		if(np.getRank() == 7 || np.getRank() == 0) {
			board.getBoard()[np.getFile()][np.getRank()] = new Queen("Queen", this.getColor(), np);
		}
		
		board.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		if(np.getRank() != 7 && np.getRank() != 0) {
			this.setPosition(Position.toChar(np.getFile()), np.getRank());
		}
		return true;
	}
	
	/**
	 * If the player specified a piece to promote to, this method will replace current pawn
	 * with the piece that they specified.
	 * 
	 * @param promotion Letter of piece player wants to promote to
	 * @param np New position to move to
	 * @param board Current board
	 * @return true if move and promotion successful
	 */
	public boolean promotionMove(String promotion, Position np, Board board) {
		
		if(np.getRank() != 7 && np.getRank() != 0) {
			return false;
		}
		
		if(!isValid(np, board)) {
			return false;
		}
		
		boolean test = testPosition(this, np, board);
		if(!test) {
			return false;
		}
		
		if(promotion.equals("N")) {
			board.getBoard()[np.getFile()][np.getRank()] = new Knight("Night", this.getColor(), np);
		}
		else if(promotion.equals("Q")) {
			board.getBoard()[np.getFile()][np.getRank()] = new Queen("Queen", this.getColor(), np);
		}
		else if(promotion.equals("R")) {
			board.getBoard()[np.getFile()][np.getRank()] = new Rook("Rook", this.getColor(), np);
		}
		else if(promotion.equals("B")) {
			board.getBoard()[np.getFile()][np.getRank()] = new Bishop("Bishop", this.getColor(), np);
		}
		else {
			//entered a piece that was invalid
			return false;
		}
		board.getBoard()[this.getPosition().getFile()][this.getPosition().getRank()] = null;
		return true;
	}
}