import java.util.*;

public class GameState {
    private char[][] gameBoard;
    private char player;

    public GameState(char[][] gameBoard, char player) {
        this.gameBoard = new char[16][];
        for(int i = 0; i < 16; i++)
            this.gameBoard[i] = gameBoard[i].clone();
        this.player = player;
    }

    public ArrayList<Location> getPlayerPieceLocations() {
        ArrayList<Location> playerPieceLocations = new ArrayList<>();

        for(int i = 0; i < 16; i++)
            for(int j = 0; j < 16; j++)
                if(gameBoard[i][j] == this.player)
                    playerPieceLocations.add(new Location(i, j));
        
        return playerPieceLocations;
    }

    public boolean isValidLocation(Location location) {
        if(location.getX() < 0 || location.getX() > 15 || location.getY() < 0
                || location.getY() > 15)
            return false;
        else
            return true;
    }

    public Location findOppositeLocation(Location location, Location reflector) {
        int reflectedX = location.getX() + (2 * (reflector.getX() - location.getX()));
        int reflectedY = location.getY() + (2 * (reflector.getY() - location.getY()));

        return new Location(reflectedX, reflectedY);
    }

    public ArrayList<Move> getImmediateMoves(Location location, char moveType) {
        ArrayList<Move> immediateMoves = new ArrayList<>();

        for(int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0)
                    continue;

                Location neighbor = new Location(location.getX() + i, location.getY() + j);

                if (isValidLocation(neighbor)) {
                    if (moveType == 'E' && gameBoard[neighbor.getX()][neighbor.getY()] == '.')
                        immediateMoves.add(new Move('E', new ArrayList<>(
                                Arrays.asList(location, neighbor))));

                    if (moveType == 'J' && gameBoard[neighbor.getX()][neighbor.getY()] != '.') {
                        Location oppositeLocation = findOppositeLocation(location, neighbor);

                        if (isValidLocation(oppositeLocation) &&
                                gameBoard[oppositeLocation.getX()][oppositeLocation.getY()] == '.')
                            immediateMoves.add(new Move('J', new ArrayList<>(
                                    Arrays.asList(location, oppositeLocation))));
                    }
                }
            }
        }

        return immediateMoves;
    }

    public boolean extremityOfMoveInHomeCamp(Move move, String extremity) {
        Location location;
        if(extremity.equals("START"))
            location = move.getMoveSequence().get(0);
        else
            location = move.getMoveSequence().get(move.getMoveSequence().size() - 1);

        if(player == 'B') {
            if(location.getX() > 4 || location.getY() > 4)
                return false;

            Location[] excludedLocations = new Location[] {
                    new Location(2, 4), new Location(3, 3), new Location(3, 4),
                    new Location(4, 2), new Location(4, 3), new Location(4, 4)};

            for(Location excludedLocation: excludedLocations)
                if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                    return false;

            return true;
        } else {
            if(location.getX() < 11 || location.getY() < 11)
                return false;

            Location[] excludedLocations = new Location[] {
                    new Location(11, 11), new Location(11, 12), new Location(11, 13),
                    new Location(12, 11), new Location(12, 12), new Location(13, 11)};

            for(Location excludedLocation: excludedLocations)
                if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                    return false;

            return true;
        }
    }

    public boolean extremityOfMoveInEnemyCamp(Move move, String extremity) {
        Location location;
        if(extremity.equals("START"))
            location = move.getMoveSequence().get(0);
        else
            location = move.getMoveSequence().get(move.getMoveSequence().size() - 1);

        if(player == 'W') {
            if(location.getX() > 4 || location.getY() > 4)
                return false;

            Location[] excludedLocations = new Location[] {
                    new Location(2, 4), new Location(3, 3), new Location(3, 4),
                    new Location(4, 2), new Location(4, 3), new Location(4, 4)};

            for(Location excludedLocation: excludedLocations)
                if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                    return false;

            return true;
        } else {
            if(location.getX() < 11 || location.getY() < 11)
                return false;

            Location[] excludedLocations = new Location[] {
                    new Location(11, 11), new Location(11, 12), new Location(11, 13),
                    new Location(12, 11), new Location(12, 12), new Location(13, 11)};

            for(Location excludedLocation: excludedLocations)
                if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                    return false;

            return true;
        }
    }

    public void removeIllegalMoves(ArrayList<Move> nextMoves) {
        ArrayList<Move> nextMovesCopy = new ArrayList<>();
        for(Move move: nextMoves)
            nextMovesCopy.add(move);

        for(Move move: nextMovesCopy) {
            if(extremityOfMoveInHomeCamp(move, "END") && !extremityOfMoveInHomeCamp(move, "START")) {
                // System.out.println("HOME " + move);
                nextMoves.remove(move);
            }
            if(extremityOfMoveInEnemyCamp(move, "START") && !extremityOfMoveInEnemyCamp(move, "END")) {
                // System.out.println("ENEMY " + move);
                nextMoves.remove(move);
            }
        }
    }

    public boolean pieceInHomeCamp(ArrayList<Location> pieceLocations) {
        boolean pieceInHomeCamp = false;

        for(Location location: pieceLocations) {
            if(player == 'B') {
                if(location.getX() > 4 || location.getY() > 4)
                    continue;

                Location[] excludedLocations = new Location[] {
                        new Location(2, 4), new Location(3, 3), new Location(3, 4),
                        new Location(4, 2), new Location(4, 3), new Location(4, 4)};

                for(Location excludedLocation: excludedLocations)
                    if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                        continue;

                pieceInHomeCamp = true;
                break;
            } else {
                if(location.getX() < 11 || location.getY() < 11)
                    continue;

                Location[] excludedLocations = new Location[] {
                        new Location(11, 11), new Location(11, 12), new Location(11, 13),
                        new Location(12, 11), new Location(12, 12), new Location(13, 11)};

                for(Location excludedLocation: excludedLocations)
                    if(location.getX() == excludedLocation.getX() && location.getY() == excludedLocation.getY())
                        continue;

                pieceInHomeCamp = true;
                break;
            }
        }

        return pieceInHomeCamp;
    }

    public ArrayList<Move> getNextMovesFromInHomeToOutOfHome(ArrayList<Move> nextMoves) {
        ArrayList<Move> nextMovesFromInHomeToOutOfHome = new ArrayList<>();
        for(Move move: nextMoves)
            if(extremityOfMoveInHomeCamp(move, "START") && !extremityOfMoveInHomeCamp(move, "END"))
                nextMovesFromInHomeToOutOfHome.add(move);

        return nextMovesFromInHomeToOutOfHome;
    }

    public int manhattanDistance(Location a, Location b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }

    public ArrayList<Move> getNextMovesFromInHomeToAwayFromHomeCorner(ArrayList<Move> nextMoves) {
        ArrayList<Move> nextMovesFromInHomeToAwayFromHomeCorner = new ArrayList<>();
        Location corner;
        if(player == 'B')
            corner = new Location(0, 0);
        else
            corner = new Location(15, 15);

        for(Move move: nextMoves)
            if(extremityOfMoveInHomeCamp(move, "START")) {
                Location start = move.getMoveSequence().get(0);
                Location end = move.getMoveSequence().get(move.getMoveSequence().size() - 1);

                if(manhattanDistance(start, corner) < manhattanDistance(end, corner))
                    nextMovesFromInHomeToAwayFromHomeCorner.add(move);
            }

        return nextMovesFromInHomeToAwayFromHomeCorner;
    }

    public ArrayList<Move> getNextMoves() {
        ArrayList<Location> pieceLocations = getPlayerPieceLocations();
        ArrayList<Move> moves = new ArrayList<>();

        for(Location location: pieceLocations) {
            moves.addAll(getImmediateMoves(location, 'E'));

            HashMap<Location, Move> visited = new HashMap<>();
            ArrayList<Location> frontier = new ArrayList<>();
            frontier.add(location);
            visited.put(location, new Move('J', new ArrayList<>(
                    Arrays.asList(location)
            )));

            // System.out.println("FRONTIER:\n" + frontier);
            // System.out.println("VISITED:\n" + visited);

            while(!frontier.isEmpty()) {
                Location current = frontier.remove(0);

                ArrayList<Move> immediateMoves = getImmediateMoves(current, 'J');
                for(Move move:immediateMoves) {
                    if(!visited.containsKey(move.getMoveSequence().get(1))) {
                        Move parentMove = visited.get(move.getMoveSequence().get(0));
                        Move newMove = new Move('J', parentMove.getMoveSequenceCopy());
                        newMove.getMoveSequence().add(move.getMoveSequence().get(1));

                        moves.add(newMove);
                        frontier.add(move.getMoveSequence().get(1));
                        visited.put(move.getMoveSequence().get(1), newMove);
                        // System.out.println("FRONTIER:\n" + frontier);
                        // System.out.println("VISITED:\n" + visited);
                    }
                }
            }
            // System.out.println();
        }

        removeIllegalMoves(moves);
        if(pieceInHomeCamp(pieceLocations)) {
            ArrayList<Move> nextMovesFromInHomeToOutOfHome = getNextMovesFromInHomeToOutOfHome(moves);
            if(!nextMovesFromInHomeToOutOfHome.isEmpty())
                return nextMovesFromInHomeToOutOfHome;
            else {
                ArrayList<Move> nextMovesFromInHomeToAwayFromHomeCorner =
                        getNextMovesFromInHomeToAwayFromHomeCorner(moves);
                if(!nextMovesFromInHomeToAwayFromHomeCorner.isEmpty())
                    return nextMovesFromInHomeToAwayFromHomeCorner;
            }
        }

        return moves;
    }

    public void makeMove(Move move) {
        Location start = move.getMoveSequence().get(0);
        Location end = move.getMoveSequence().get(move.getMoveSequence().size() - 1);

        gameBoard[start.getX()][start.getY()] = '.';
        gameBoard[end.getX()][end.getY()] = player;

        if(player == 'W')
            player = 'B';
        else
            player = 'W';
    }

    public char[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(char[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public char getPlayer() {
        return player;
    }

    public void setPlayer(char player) {
        this.player = player;
    }
}
