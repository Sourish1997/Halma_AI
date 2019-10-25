import java.io.*;

public class Main {
    public static String gameMode;
    public static char player;
    public static double timeRemaining1, timeRemaining2;
    public static char[][] gameBoard;

    private static Location[] blackHomeCampLocations;
    private static Location[] whiteHomeCampLocations;

    public static boolean gameOver(GameState gameState) {
        boolean foundEmpty = false, foundWhite = false;
        for(Location location: blackHomeCampLocations) {
            char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
            if(pieceAtLocation == 'W')
                foundWhite = true;
            else if(pieceAtLocation == '.')
                foundEmpty = true;
        }

        if(foundEmpty == false && foundWhite == true) {
            if(player == 'W')
                System.out.println("Agent 1 Wins!");
            else
                System.out.println("Agent 2 Wins!");

            return true;
        }

        foundEmpty = false;
        boolean foundBlack = false;
        for(Location location: whiteHomeCampLocations) {
            char pieceAtLocation = gameState.getGameBoard()[location.getX()][location.getY()];
            if(pieceAtLocation == 'B')
                foundBlack = true;
            else if(pieceAtLocation == '.')
                foundEmpty = true;
        }

        if(foundEmpty == false && foundBlack == true) {
            if(player == 'W')
                System.out.println("Agent 2 Wins!");
            else
                System.out.println("Agent 1 Wins!");

            return true;
        }

        return false;
    }

    public static void printGameBoard(GameState gameState) {
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                System.out.print(gameState.getGameBoard()[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String args[]) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/input.txt"));
            gameMode = reader.readLine();
            player = reader.readLine().charAt(0);
            timeRemaining1 = Double.parseDouble(reader.readLine());
            timeRemaining2 = timeRemaining1;
            gameBoard = new char[16][16];

            for(int i = 0; i < 16; i++) {
                String[] gameBoardRow = reader.readLine().split("");
                for(int j = 0; j < 16; j++)
                    gameBoard[i][j] = gameBoardRow[j].charAt(0);
            }

            reader.close();
        } catch(IOException e) {}

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

        GameState gameState = new GameState(gameBoard, player);

        AlphaBetaMinimaxAgent agent1;
        OptimizedMinimaxAgent agent2;
        boolean agent1Turn = true;

        int move_no = 0;

        while(!gameOver(gameState)) {
            if(agent1Turn && timeRemaining1 <= 0) {
                System.out.println("Agent 1 has run out of time. Agent 2 Wins!");
                break;
            }

            if(!agent1Turn && timeRemaining2 <= 0) {
                System.out.println("Agent 2 has run out of time. Agent 1 Wins!");
                break;
            }

            if(agent1Turn) {
                long startTime = System.currentTimeMillis();
                agent1 = new AlphaBetaMinimaxAgent(gameState, 2);
                Move move = agent1.makeMove();
                gameState.makeMove(move);
                long endTime = System.currentTimeMillis();
                timeRemaining1 -= (double)(endTime - startTime) / 1000;
                move_no += 1;
                System.out.println("Agent 1 Move No " + move_no + ":");
                System.out.println("Time Remaining: " + timeRemaining1 + "s");
                System.out.println(move + "\n");
                printGameBoard(gameState);
                agent1Turn = false;

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/time.txt", true));
                    writer.write(timeRemaining1 + "\n");
                    writer.close();
                } catch(IOException e) {}
            } else {
                long startTime = System.currentTimeMillis();
                agent2 = new OptimizedMinimaxAgent(gameState, timeRemaining2);
                Move move = agent2.makeMove();
                gameState.makeMove(move);
                long endTime = System.currentTimeMillis();
                timeRemaining2 -= (double)(endTime - startTime) / 1000;
                System.out.println("Agent 2 Move No " + move_no + ":");
                System.out.println("Time Remaining: " + timeRemaining2 + "s");
                System.out.println(move + "\n");
                printGameBoard(gameState);
                agent1Turn = true;
            }
        }
    }
}
