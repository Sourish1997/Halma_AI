import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static String gameMode;
    public static char player;
    public static double timeRemaining;
    public static char[][] gameBoard;

    public static void main(String args[]) {
        long startTime = System.currentTimeMillis();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/input.txt"));
            gameMode = reader.readLine();
            player = reader.readLine().charAt(0);
            timeRemaining = Double.parseDouble(reader.readLine());
            gameBoard = new char[16][16];

            for(int i = 0; i < 16; i++) {
                String[] gameBoardRow = reader.readLine().split("");
                for(int j = 0; j < 16; j++)
                    gameBoard[i][j] = gameBoardRow[j].charAt(0);
            }
        } catch(IOException e) {}

        GameState gameState = new GameState(gameBoard, player);

        /* ArrayList<Move> nextMoves = gameState.getNextMoves();
        for(Move move: nextMoves) {
            System.out.println(move);
            System.out.println();
        }*/

        MinimaxAgent minimaxAgent = new MinimaxAgent(gameState, 3);
        System.out.println(minimaxAgent.makeMove());

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
