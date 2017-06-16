/* John Allen 4.1.2017 */

public class Queen {
/*
 * The essence of a queen object is a position on a ChessBoard 
 * and a score the represent the amount of other Queens is striking
 * distance  
 */
  private int row, column, score;
	
  public Queen(int column, int row) {
    this.column = column;
    this.row = row;
  }	
  
  public int getRow() {	return this.row;	}
  public int getColumn() {  return this.column;	}
  /*  setScore() is defined in ChessBoard.java  */
  public void changeScore(int score) {  this.score = score;  }
  public int getScore() {  return this.score;	}
  
  public boolean equals(Queen that) {	return this.getRow() == that.getRow() && this.getColumn() == that.getColumn();	}
  public String toString() {  return String.format("(%d, %d)", column, row);  }
}