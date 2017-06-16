/* John Allen 3.31.2017 */
public class Main {

  public static void main(String[] args) {
	
    int totalRuns = 500, trials = 1;
    double TEMPERATURE = 100;
    do {
      ChessBoard game = new ChessBoard();
      LocalSearch.hillClimbing(game);	
      LocalSearch.simulatedAnnealing(game, TEMPERATURE);
    }  while (++trials <= totalRuns);
    
    float movesInBadSolutionHC = LocalSearch.getNumMovesHC() - LocalSearch.getMovesHCSuccess();
    float successRateHC =  (new Float(100) * LocalSearch.getHCSuccess()) / (totalRuns);
    float avgMovesBadRunsHC = movesInBadSolutionHC / (totalRuns - LocalSearch.getHCSuccess());
    float successTrialRoundsHC = LocalSearch.getMovesHCSuccess() / LocalSearch.getHCSuccess();
    
    System.out.print(String.format("%5.2f%%, %5.2f, %5.2f\n", successRateHC, successTrialRoundsHC, avgMovesBadRunsHC));	  
    
    float movesInBadSolutionSA = LocalSearch.getNumMovesSA() - LocalSearch.getMovesSASuccess();
    float successRateSA = (new Float(100) * LocalSearch.getSASuccess()) / totalRuns;
    float avgMovesBadRunsSA = movesInBadSolutionSA / (totalRuns - LocalSearch.getSASuccess());
    float successTrialRoundsSA = LocalSearch.getMovesSASuccess() / LocalSearch.getSASuccess();
    
    System.out.println(String.format("%5.2f%%, %5.2f, %5.2f\n", successRateSA, successTrialRoundsSA, avgMovesBadRunsSA));	
  }
}