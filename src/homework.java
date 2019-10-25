import java.io.*;

public class homework {
    public static String gameMode;
    public static char player;
    public static double timeRemaining;
    public static char[][] gameBoard;

    public static void main(String args[]) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/input.txt"));
            gameMode = reader.readLine();
            player = reader.readLine().charAt(0);
            timeRemaining = Double.parseDouble(reader.readLine());
            gameBoard = new char[16][16];

            for (int i = 0; i < 16; i++) {
                String[] gameBoardRow = reader.readLine().split("");
                for (int j = 0; j < 16; j++)
                    gameBoard[i][j] = gameBoardRow[j].charAt(0);
            }

            reader.close();
        } catch (IOException e) {}

        GameState gameState = new GameState(gameBoard, player);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/output.txt"));

            if (gameMode.equals("SINGLE")) {
                writer.write(gameState.getNextMoves().get(0).toString());
            } else {
                OptimizedMinimaxAgent minimaxAgent = new OptimizedMinimaxAgent(gameState, timeRemaining);
                Move move = minimaxAgent.makeMove();
                writer.write(move.toString());
            }

            writer.close();
        } catch (IOException e) {}
    }
}
