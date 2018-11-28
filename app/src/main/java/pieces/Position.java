/**
 * Representation of a position on the chess board.
 * 
 * @author Michael Chapman
 * @author Krishna Mistry
 * 
 */

package pieces;

public class Position {
	
	/**
	 * File int field for position file (converted from char).
	 * Rank int field for position rank.
	 */
	private int file;
	private int rank;

	/**
	 * Constructor to create an instance of a position.
	 * Subtraction by one from both fields to account for array representation of Chess board.
	 * 
	 * @param file field for position file
	 * @param rank field for position rank
	 */
	public Position(char file, int rank) {
		this.file = fromChar(file) - 1;
		this.rank = rank - 1;
	}
	
	/**
	 * Returns file field.
	 * @return file field
	 */
	public int getFile() {
		return file;
	}
	
	/**
	 * Returns rank field.
	 * @return rank field
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Sets file field.
	 * @param file char for file
	 */
	public void setFile(char file) {
		this.file = fromChar(file);
	}
	
	/**
	 * Sets rank field.
	 * @param rank int for rank
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/**
	 * Converts a character to corresponding numerical value.
	 * 
	 * @param c Character entered
	 * @return int representation of char
	 */
	public static int fromChar(char c) {
		switch(c) {
		
		case 'a': return 1;
		case 'b': return 2;
		case 'c': return 3;
		case 'd': return 4;
		case 'e': return 5;
		case 'f': return 6;
		case 'g': return 7;
		case 'h': return 8;
		default: return 0;
		
		}
	}
	
	/**
	 * Converts an integer to corresponding character.
	 * 
	 * @param c Integer entered
	 * @return char representation of num
	 */
	public static char toChar(int c) {
		switch(c) {
		
		case 1: return 'a';
		case 2: return 'b';
		case 3: return 'c';
		case 4: return 'd';
		case 5: return 'e';
		case 6: return 'f';
		case 7: return 'g';
		case 8: return 'h';
		default: return 0;
		
		}
	}
	
}
