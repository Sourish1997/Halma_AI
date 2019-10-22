import java.util.ArrayList;

public class AlphaBetaMinimaxAgent {
    private GameState gameState;
    private int depth;

    private Location[] blackHomeCampLocations;
    private Location[] whiteHomeCampLocations;

    public AlphaBetaMinimaxAgent(GameState gameState, int depth) {
        this.gameState = gameState;
        this.depth = depth;

        blackHomeCampLocations = new Location[19];
        whiteHomeCampLocations = new Location[19];

        int count = 0;
        for(int i = 0; i < 5; i++) {
            int row = i;
            int col = 0;
            while(row >= 0) {
                blackHomeCampLocations[count] = new Location(row, col);
                row -= 1;
                col += 1;
                count += 1;
            }
        }

        int row = 4, col = 1;
        for(int i = 0; i < 4; i++) {
            blackHomeCampLocations[count] = new Location(row, col);
            row -= 1;
            col += 1;
            count += 1;
        }

        count = 0;
        for(int i = 11; i < 16; i++) {
            row = i;
            col = 15;
            while(row <= 15) {
                whiteHomeCampLocations[count] = new Location(row, col);
                row += 1;
                col -= 1;
                count += 1;
            }
        }

        row = 11; col = 14;
        for(int i = 0; i < 4; i++) {
            whiteHomeCampLocations[count] = new Location(row, col);
            row += 1;
            col -= 1;
            count += 1;
        }
    }

    public double euclideanDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public double heuristic(GameState gameState, char playerToMax) {
        double heuristicValue = 0;

        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                if(gameState.getGameBoard()[i][j] == 'B') {
                    Location pieceLocation = new Location(i, j);
                    double maxValue = Double.NEGATIVE_INFINITY;
                    boolean foundEmpty = false, foundBlack = false;
                    for(Location location: whiteHomeCampLocations) {
                        char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
                        if(pieceAtLocation != 'B') {
                            if(pieceAtLocation == '.')
                                foundEmpty = true;

                            double value = euclideanDistance(pieceLocation, location);
                            if (value > maxValue)
                                maxValue = value;
                        } else {
                            foundBlack = true;
                        }
                    }

                    if(!foundEmpty && foundBlack)
                        maxValue = -20;

                    heuristicValue -= maxValue;
                } else if(gameState.getGameBoard()[i][j] == 'W') {
                    Location pieceLocation = new Location(i, j);
                    double maxValue = Double.NEGATIVE_INFINITY;
                    boolean foundEmpty = false, foundWhite = false;
                    for(Location location: blackHomeCampLocations) {
                        char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
                        if(pieceAtLocation != 'W') {
                            if(pieceAtLocation == '.')
                                foundEmpty = true;

                            double value = euclideanDistance(pieceLocation, location);
                            if (value > maxValue)
                                maxValue = value;
                        } else {
                            foundWhite = true;
                        }
                    }

                    if(!foundEmpty && foundWhite)
                        maxValue = -20;

                    heuristicValue += maxValue;
                }
            }
        }

        if(playerToMax == 'W')
            heuristicValue *= -1;

        return heuristicValue;
    }

    public GameState generateGameState(GameState gameState, Move move) {
        GameState newGameState = new GameState(gameState.getGameBoard(), gameState.getPlayer());
        newGameState.makeMove(move);
        return newGameState;
    }

    public WeightedMove minimax(GameState gameState, char playerToMax, boolean maxing, int depth,
                                double alpha, double beta) {
        ArrayList<Move> nextMoves = gameState.getNextMoves();

        if(depth == 0 || nextMoves.isEmpty()) {
            double value = heuristic(gameState, playerToMax);
            return new WeightedMove(null, value);
        }

        double value = maxing? Double.NEGATIVE_INFINITY: Double.POSITIVE_INFINITY;
        WeightedMove bestMove = new WeightedMove(null, value);

        for(Move move: nextMoves) {
            GameState childGameState = generateGameState(gameState, move);
            WeightedMove weightedMove = minimax(childGameState, playerToMax, !maxing, depth - 1, alpha, beta);

            if((maxing && weightedMove.getWeight() > value) || (!maxing && weightedMove.getWeight() < value)) {
                value = weightedMove.getWeight();
                bestMove.setMove(move);
                bestMove.setWeight(value);
            }

            if(maxing && weightedMove.getWeight() > alpha)
                alpha = weightedMove.getWeight();
            else if(!maxing && weightedMove.getWeight() < beta)
                beta = weightedMove.getWeight();

            if(alpha >= beta)
                return bestMove;
        }

        return bestMove;
    }

    public Move makeMove() {
        return minimax(gameState, gameState.getPlayer(), true, depth,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getMove();
    }
}
