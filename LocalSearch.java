import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

public class LocalSearch {
	
  private static float 	hillClimbingSuccess;
  private static float 	movesHillClimbing;
  private static float 	movesHillClimbingSuccess;
  private static float 	simulatedAnnealingSuccess;
  private static float 	movesSimulatedAnnealing;
  private static float 	movesSimulatedAnnealingSuccess;
  private static double	initialTemperature;

  public static void hillClimbing(ChessBoard game) { 
  // calls a recursive function that performs search operations
	localSearch(game, null, game.getBoardScore(), 0, false, 0);	
  }	// FALSE = HILLCLIMBING
  
  public static void simulatedAnnealing(ChessBoard game, double startTemp) { 
  // calls a recursive function that performs search operations	
    LocalSearch.initialTemperature = startTemp;
    localSearch(game, null, game.getBoardScore(), 0, true, startTemp);
  }	// TRUE = SIMULATED ANNEALING                                 										
  
  
  
  
  private static void localSearch(ChessBoard game, ChessBoard lastState, int score, 
		  				int numMovesOfGame, boolean search, double temperature) {
/*
 *  recursive function that performs HillClimbing and SimulatedAnnealing, 
 * called once for each move made.
 * what this function does:
 *  1. keeps track of how many moves each current game is
 *  2. decrements temperature between moves in simulated annealing.
 *  3. keeps tack of moves made in each search
 *  4. records successful trials
 * returns when: 
 *	1.	Heuristic function = 0
 *	2.	Stuck in Hill Climbing.
 *  3.	Stuck in Simulated Annealing.
 */
	ChessBoard nextState;
	lastState = game;
	
	nextState = !search ? nextState = nextMove(game) : nextMove(game, search, temperature);
	
	if (nextState.equals(lastState)) {	// if only condition for return
	  if (score == 0) {					// if Goal State found
	    if (!search) {
	      LocalSearch.hillClimbingSuccess++;
	      LocalSearch.movesHillClimbingSuccess += numMovesOfGame;
	    }	else {
	      LocalSearch.simulatedAnnealingSuccess++;
	      LocalSearch.movesSimulatedAnnealingSuccess += numMovesOfGame;
	    }
	  }
	  return;
	}	
	if (!search) {	
	  LocalSearch.movesHillClimbing++;
	  localSearch(nextState, lastState, nextState.getBoardScore(), ++numMovesOfGame, search, 0);
	}	else {
	  LocalSearch.movesSimulatedAnnealing++;
	  localSearch(nextState, lastState, nextState.getBoardScore(), ++numMovesOfGame, search, --temperature);
	} 	
  }
  
  private static ChessBoard nextMove(ChessBoard originalState) {
    return nextMove(originalState, false, 0);
  }
  
  private static ChessBoard nextMove(ChessBoard originalState, boolean simulatedAnnealing, double temperature) {
  	
	HashMap<Integer ,ChessBoard> betterMoves = new HashMap<>();
	Integer bestScore = originalState.getBoardScore();	// stores score of best board at the moment
	betterMoves.put(bestScore, originalState);			// starts with a reference to the original state
	ArrayList<Queen> list = new ArrayList<>();
		
	if (simulatedAnnealing) {
		  ChessBoard  random = LocalSearch.randomMove(originalState); 
		  if (random.getBoardScore() < originalState.getBoardScore())	return random;
		  if (takeRandomMove(temperature))								return random;
	}

	for (Queen x: originalState.getQueensList())	list.add(x);	
	// the two list's start the same
	for (Queen current: originalState.getQueensList()) {		
	// current is the queen the is currently being moved around
	  list.remove(current);				// remove the current node from the list of queens
	  int currentColumn = current.getColumn(), currentRow = current.getRow();
	  
/* DIAGONAL CHECK */
	  
	  while (++currentColumn < 8 && --currentRow > 0) {	// check diagonal going up to right
		Queen tempQueen = new Queen(currentColumn, currentRow);
	    list.add(tempQueen);
	    if (!tempQueen.equals(current)) {
	      if (!skip(tempQueen, originalState.getQueensList())) {	
	        ChessBoard tempBoard = new ChessBoard(list);// creates board that is tested against current optimal board  
			if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
			  bestScore = tempBoard.getBoardScore();	// record value of current bestScore
		      betterMoves.put(bestScore, tempBoard);	// saves state of board when a better move is found 
			}
		  }
		  list.remove(tempQueen);	// remove test scenario queen from test list
		}
	  }
		
	  currentColumn = current.getColumn();
	  currentRow = current.getRow();	
	  
/* DIAGONAL CHECK */
	  
	  while (--currentColumn > 0 && ++currentRow < 8) {	// check diagonal going down to left	
	    Queen tempQueen = new Queen(currentColumn, currentRow);
    	list.add(tempQueen);
	    if (!tempQueen.equals(current)) {
	      if (!skip(tempQueen, originalState.getQueensList())) {	
	        ChessBoard tempBoard = new ChessBoard(list);// creates board that is tested against current optimal board  
	   	    if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
		      bestScore = tempBoard.getBoardScore();	// record value of current bestScore
		      betterMoves.put(bestScore, tempBoard);	// saves state of board when a better move is found 
	     	}
		  }
		  list.remove(tempQueen);// remove test scenario queen from test list
		}
	  }
		
	  currentColumn = current.getColumn();
	  currentRow = current.getRow();	
	  
/* DIAGONAL CHECK */		
	  
	  while (--currentColumn > 0 && --currentRow > 0) {	// check diagonal going up to left
        Queen tempQueen = new Queen(currentColumn, currentRow);
 		list.add(tempQueen);
		if (!tempQueen.equals(current)) {
		  if (!skip(tempQueen, originalState.getQueensList())) {	
	        ChessBoard tempBoard = new ChessBoard(list);// creates board that is tested against current optimal board  
		    if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
		      bestScore = tempBoard.getBoardScore();	// record value of current bestScore
			  betterMoves.put(bestScore, tempBoard);	// saves state of board when a better move is found 
		  	}
		  }
		  list.remove(tempQueen);	// remove test scenario queen from test list
		}
	  }
	  currentColumn = current.getColumn();
	  currentRow = current.getRow();	
		  
/* DIAGONAL CHECK */
	  
	  while (++currentColumn < 8 && ++currentRow < 8) {	//check diagonal going down to right
	    Queen tempQueen = new Queen(currentColumn, currentRow);
		list.add(tempQueen);
	    if (!tempQueen.equals(current)) {
	      if (!skip(tempQueen, originalState.getQueensList())) {	
	        ChessBoard tempBoard = new ChessBoard(list);// creates board that is tested against current optimal board  
			if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
	    	  bestScore = tempBoard.getBoardScore();	// record value of current bestScore
			  betterMoves.put(bestScore, tempBoard);	// saves state of board when a better move is found 
		  	}
		  }
		  list.remove(tempQueen);	// remove test scenario queen from test list  
		}	
	  }
	  int testRow = current.getRow();	// used for placing alternative queens in the same row
	  int testColumn = current.getColumn();	// used for placing alternative queen's in the same column
	  
/************************************ Starting HORIZONTAL CHECKS  *****************************/
	  for (currentColumn=0; currentColumn<8; currentColumn++) {	
		Queen tempQueen = new Queen(currentColumn , testRow);	// Create a new Queen that is used for moving around
		list.add(tempQueen);	// add new Queen to testing scenario
		if (!tempQueen.equals(current)) {
		  if (!skip(tempQueen, originalState.getQueensList())) {
		    ChessBoard tempBoard = new ChessBoard(list);// creates board that is tested against current optimal board  
		    if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
		      bestScore = tempBoard.getBoardScore();	// record value of current bestScore
		      betterMoves.put(bestScore, tempBoard);	// saves state of board when a better move is found 	      
		    }
		  }
		}
		list.remove(tempQueen);		// remove test scenario queen from test list			
	  }  
/************************************* STARTING VERTICAL CHECKS ****************************************/
	  for (currentRow=0; currentRow<8; currentRow++) {	
		Queen tempQueen = new Queen(testColumn , currentRow);	// Create a new Queen that is used for moving around
		list.add(tempQueen);			// add new Queen to testing scenario
		if (!tempQueen.equals(current)) {
		  if (!skip(tempQueen, originalState.getQueensList())) {
		    ChessBoard tempBoard = new ChessBoard(list);	// creates board that is tested against current optimal board  
		    if (tempBoard.getBoardScore() < betterMoves.get(bestScore).getBoardScore()) {
		      bestScore = tempBoard.getBoardScore();// record value of current bestScore
		      betterMoves.put(bestScore, tempBoard);// saves state of board when a better move is found 
		    }
		  }
		}
		list.remove(tempQueen);	// remove test scenario queen from test list		
	  }
	  list.add(current); 			// add the originally removed queen back to the list before choosing a new one
	} 	
	if (simulatedAnnealing && Math.random() > 0.1 && temperature > 0) 
	/* if the game would end otherwise, give algorithm a  better chance to fix itself
	 * as long as temperature is not 0 	                */
	  if (betterMoves.get(bestScore) == originalState && betterMoves.get(bestScore).getBoardScore() != 0)	
	    return LocalSearch.randomMove(originalState); 	
	
	return betterMoves.get(bestScore);	// return the most updated value of best score using HillClimbing	
  }
  
  private static ChessBoard randomMove(ChessBoard game) {
    ArrayList<Queen> list =  new ArrayList<>(game.getQueensList());
	Queen removedQueen = list.remove(new Random().nextInt(8));
	return makeNewBoard(list, removedQueen);
  }
  
  private static boolean takeRandomMove(double temperature) {
  /*decides whether to take a move that results in a lower state */
    if (temperature <= 0)	return false;
    double random1 = Math.random();
    double random2 = Math.random();
	double control = LocalSearch.initialTemperature*1.4;
	double takeRandom = temperature * random1;
	double dontTakeRandom = control * random2;
	return takeRandom > dontTakeRandom ? true : false;
  }
  
  private static ChessBoard makeNewBoard(ArrayList<Queen> finalPos, Queen moving) { 
  /* moving is the Queen from the board at the beginning of the move that is moving */ 
	while (finalPos.size() == 7) {
	  Queen temp = new Queen(new Random().nextInt(8), new Random().nextInt(8));
	  /* generate a queen in a random row and column */
	  if (temp.equals(moving) || finalPos.contains(temp))	continue;
	  /* skip considering if this queen is equal to a queen in original configuration */
	  int rowDifference = Math.abs(moving.getRow() - temp.getRow());
      int columnDifference = Math.abs(moving.getColumn() - temp.getColumn());  
	  if (temp.getColumn() == moving.getColumn())	finalPos.add(temp);
	  if (temp.getRow() == moving.getRow())			finalPos.add(temp);
	  if (rowDifference == columnDifference)		finalPos.add(temp);
	  /*  Check if the new queen is in a spot that the old queen can get to in
	   *  a single move														*/
    }
    return new ChessBoard(finalPos);
  }
    
  private static boolean skip(Queen newQueen, ArrayList<Queen> currentQueens) {
  // skip testing for conditions where two queen's are on the same spot
	for (Queen x: currentQueens)	if (newQueen.equals(x))	return true;
    return false;
  }
  
  /********** Series of functions that return static class variables *****************/
  public static float getHCSuccess() {		return LocalSearch.hillClimbingSuccess;				}
  public static float getMovesHCSuccess() {	return LocalSearch.movesHillClimbingSuccess;		}
  public static float getNumMovesHC() {		return LocalSearch.movesHillClimbing;				}
  public static float getSASuccess() {		return LocalSearch.simulatedAnnealingSuccess;		}
  public static float getMovesSASuccess() {	return LocalSearch.movesSimulatedAnnealingSuccess;	}
  public static float getNumMovesSA() {		return LocalSearch.movesSimulatedAnnealing;			}
}