import java.util.ArrayList;
import java.util.Random;

public class RandomAgent {
    private GameState gameState;

    public RandomAgent(GameState gameState) {
        this.gameState = gameState;
    }

    public Move makeMove() {
        ArrayList<Move> nextMoves = gameState.getNextMoves();
        int index = new Random().nextInt(nextMoves.size());

        return nextMoves.get(index);
    }
}
