/**
 * Abstract class to represent the common Methods and fields of a Chess piece
 * Purposefully abstract as this class should never be instantiated.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 */

package pieces;

import board.Board;

public abstract class Pieces{
	
	/**
	 * String field for piece's name, final as we do not want it changed.
	 * String field for piece' color, final as it will not change.
	 * Position field for piece's current position.
	 */
	final private String name;
	final private String color;
	private Position currentPosition;
	
	/**
	 * Constructor to be used by Pieces subclasses to initialize common fields.
	 * 
	 * @param name The piece's name
	 * @param color The piece's color
	 * @param currentPosition The piece's current chess board position
	 */
	public Pieces(String name, String color, Position currentPosition) {
		this.name = name;
		this.color = color;
		this.currentPosition = currentPosition;
	}
	
	/**
	 * Method to get piece's name.
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to get piece's current position.
	 * @return currentPosition
	 */
	public Position getPosition() {
		return currentPosition;
	}
	
	/**
	 * Method to get piece's color.
	 * @return color
	 */
	public String getColor() {
		return color;
	}
	
	/** Method to set piece's current position when it changes places.
	 * 
	 * @param c for the file
	 * @param r for the rank
	 */
	public void setPosition(char c, int r) {
		currentPosition.setFile(c);
		currentPosition.setRank(r);
	}
	
	/**
	 * Checks if piece is leaving confines of the board.
	 * 
	 * @param np new position
	 * @param board Current board
	 * @return true if in bounds
	 */
	public boolean isOutOfBounds(Position np, Board board) {
		if(np.getFile() < 8 && np.getFile() >= 0 && np.getRank() < 8 && np.getRank() >= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Abstract method that depends on the piece. Each piece has their own method of
	 * moving and this method checks if their projected move is valid.
	 * 
	 * @param np new position
	 * @param board Current board
	 * @return returns true if can make valid move to position
	 */
	public abstract boolean isValid(Position np, Board board);
	
	/**
	 * Moves piece from current position to new position.
	 * 
	 * @param np new position
	 * @param board Current board
	 * @return true if move is successful
	 */
	public abstract boolean move(Position np, Board board);
	
	/**
	 * Method that returns true if positions between the current piece's position and its desired 
	 * position are empty, false otherwise.
	 * Returns true for knight piece as it can jump over anything.
	 * 
	 * @param np New position
	 * @param board Current board
	 * @return true if piece may move through the positions
	 */
	public boolean canMoveThrough(Position np, Board board) {
		int pf1 = currentPosition.getFile(); int pr1 = currentPosition.getRank();
		int pf2 = np.getFile(); int pr2 = np.getRank();
		
		//if a knight, don't need to calculate at all
		if(getName().equals("Night")) {
			return true;
		}
		//the piece is moving up the board or down the board in the same file
		if(pf1 == pf2) {
			//piece only moved up or down one
			if(Math.abs(pr1 - pr2) == 1) {
					return true;
			}
			//the piece is moving up
			if(pr1 < pr2) {
				for(int i = pr1 + 1; i < pr2; i++) {
					//position starts at position above current position and ends right before end position
					//if there is a piece in any of the spots the current piece wants to pass through, return FALSE SHOOT EM DOWN BOY
					if(board.atPosition(new Position(Position.toChar(pf1 + 1) , i + 1)) != null) {
						return false;
					}
				}
			}
			//piece is moving on down
			else {
				for(int i = pr1 - 1; i > pr2; i--) {
					if(board.atPosition(new Position(Position.toChar(pf1 + 1) , i + 1)) != null) {
						return false;
					}
				}
			}
		}
		//piece is moving left or right through the same rank
		else if(pr1 == pr2) {
			//piece only moved left or right one
			if(Math.abs(pf1 - pf2) == 1) {
				return true;
			}
			//the piece is moving right
			if(pf1 < pf2) {
				for(int i = pf1 + 1; i < pf2; i++) {
					if(board.atPosition(new Position(Position.toChar(i + 1) , pr1 + 1)) != null) {
						return false;
					}
				}
			}
			//piece is moving on left
			else {
				for(int i = pf1 - 1; i > pf2; i--) {
					if(board.atPosition(new Position(Position.toChar(i + 1) , pr1 + 1)) != null) {
						return false;
					}
				}
			}
		}
		else if(pf1 != pf2 && pr1 != pr2) {
			//piece only moved one space
			if(Math.abs(pf1 - pf2) == 1 && Math.abs(pr1 - pr2) == 1) {
				return true;
			}
			int i, j;
			//piece moving northeast 
			if(pf1 < pf2 && pr1 < pr2) {
				i = pf1 + 1;
				j = pr1 + 1;
				while(i != pf2 && j != pr2) {
					if(board.atPosition(new Position(Position.toChar(i + 1), j + 1)) != null) {
						return false;
					}
					i++; j++;
				}
			}
			//piece moving southeast 
			else if(pf1 < pf2 && pr1 > pr2) {
				i = pf1 + 1;
				j = pr1 - 1;
				while(i != pf2 && j != pr2) {
					if(board.atPosition(new Position(Position.toChar(i + 1), j + 1)) != null) {
						return false;
					}
					i++; j--;
				}
			}//piece moving southwest 
			else if(pf1 > pf2 && pr1 > pr2) {
				i = pf1 - 1;
				j = pr1 - 1;
				while(i != pf2 && j != pr2) {
					if(board.atPosition(new Position(Position.toChar(i + 1), j + 1)) != null) {
						return false;
					}
					i--; j--;
				}
			}//piece moving northwest 
			else {
				i = pf1 - 1;
				j = pr1 + 1;
				while(i != pf2 && j != pr2) {
					if(board.atPosition(new Position(Position.toChar(i + 1), j + 1)) != null) {
						return false;
					}
					i--; j++;
				}
			}		
		}
		//Nothing was in the way of DOMINATION YEAH!
		return true;
	}

	/**
	 * Method that returns false if the destination spot has a teammate, as a piece may not take a teammate.
	 * 
	 * @param np new position to check
	 * @param board Current board
	 * @return true if position is enemy piece or empty
	 */
	public boolean isTeammate(Position np, Board board) {
		if(board.atPosition(np) == null) {
			return false;
		}
		else if(board.atPosition(np).getColor().equals(color)) {
			return true;
		}
		return false;
	}
	
	/**
	 * toString method override from object class.
	 * Prints out the first letter of the class's color and 
	 * then first letter of name (which is why Knight's name is Night).
	 */
	public String toString() {
		return "" + color.charAt(0) + name.charAt(0);
	}
	
	/**
	 * Method that tests if a move puts king into check with a dummy board.
	 * 
	 * @param piece piece that is making move
	 * @param np position piece wants to move to
	 * @param board Current board
	 * @return true if position does not put king into check
	 */
	public boolean testPosition(Pieces piece, Position np, Board board) {
		Board temp = new Board();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				temp.getBoard()[i][j] = board.getBoard()[i][j];
			}
		}
		
		temp.getBoard()[np.getFile()][np.getRank()] = piece;
		temp.getBoard()[piece.getPosition().getFile()][piece.getPosition().getRank()] = null;
		King k = (King) board.atPosition(board.getPositionKing(piece.getColor(), temp));
		if(k.isInCheck(temp)) {
			return false;
		}
		return true;
	}

}