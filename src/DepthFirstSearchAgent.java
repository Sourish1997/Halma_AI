import java.util.ArrayList;

public class DepthFirstSearchAgent {
    private GameState gameState;
    private int depth;

    private Location[] blackHomeCampLocations;
    private Location[] whiteHomeCampLocations;

    private final double MAX_HEURISTIC_VALUE = 0;

    public DepthFirstSearchAgent(GameState gameState, int depth) {
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

    public boolean gameOver(GameState gameState) {
        boolean foundEmpty = false, foundWhite = false;
        for(Location location: blackHomeCampLocations) {
            char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
            if(pieceAtLocation == 'W')
                foundWhite = true;
            else if(pieceAtLocation == '.')
                foundEmpty = true;
        }

        if(foundEmpty == false && foundWhite == true)
            return true;

        foundEmpty = false;
        boolean foundBlack = false;
        for(Location location: whiteHomeCampLocations) {
            char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
            if(pieceAtLocation == 'B')
                foundBlack = true;
            else if(pieceAtLocation == '.')
                foundEmpty = true;
        }

        if(foundEmpty == false && foundBlack == true)
            return true;

        return false;
    }

    public double euclideanDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    public double heuristic(GameState gameState, char playerToMax) {
        double heuristicValue = 0;

        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                if(gameState.getGameBoard()[i][j] == 'B' && playerToMax == 'B') {
                    Location pieceLocation = new Location(i, j);
                    double maxValue = Double.NEGATIVE_INFINITY;
                    for(Location location: whiteHomeCampLocations) {
                        char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
                        if(pieceAtLocation != 'B') {
                            double value = euclideanDistance(pieceLocation, location);
                            if (value > maxValue)
                                maxValue = value;
                        }
                    }

                    heuristicValue -= maxValue;
                } else if(gameState.getGameBoard()[i][j] == 'W' && playerToMax == 'W') {
                    Location pieceLocation = new Location(i, j);
                    double maxValue = Double.NEGATIVE_INFINITY;
                    for(Location location: blackHomeCampLocations) {
                        char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
                        if(pieceAtLocation != 'W') {
                            double value = euclideanDistance(pieceLocation, location);
                            if (value > maxValue)
                                maxValue = value;
                        }
                    }

                    heuristicValue -= maxValue;
                }
            }
        }

        return heuristicValue;
    }

    public GameState generateGameState(GameState gameState, Move move) {
        GameState newGameState = new GameState(gameState.getGameBoard(), gameState.getPlayer());
        newGameState.makeMove(move);
        newGameState.setPlayer(gameState.getPlayer());
        return newGameState;
    }

    public WeightedMove minimax(GameState gameState, int depth) {
        if(gameOver(gameState))
            return new WeightedMove(null, MAX_HEURISTIC_VALUE, depth);

        ArrayList<Move> nextMoves = gameState.removeBackwardMoves(gameState.getNextMoves());

        if(depth == 0 || nextMoves.isEmpty()) {
            double value = heuristic(gameState, gameState.getPlayer());
            return new WeightedMove(null, value, depth);
        }

        double value = Double.NEGATIVE_INFINITY;
        WeightedMove bestMove = new WeightedMove(null, value, depth);

        for(Move move: nextMoves) {
            GameState childGameState = generateGameState(gameState, move);
            WeightedMove weightedMove = minimax(childGameState, depth - 1);

            if((weightedMove.getWeight() > value)
                    || (Double.toString(weightedMove.getWeight()).equals(Double.toString(value))
                    && weightedMove.getDepth() > bestMove.getDepth())) {
                value = weightedMove.getWeight();
                bestMove.setMove(move);
                bestMove.setWeight(value);
                bestMove.setDepth(weightedMove.getDepth());
            }
        }

        return bestMove;
    }

    public Move makeMove() {
        return minimax(gameState, depth).getMove();
    }
}
