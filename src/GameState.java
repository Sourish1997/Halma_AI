import java.util.*;

public class GameState {
    private char[][] game_board;
    private char player;

    public GameState(char[][] game_board, char player) {
        this.game_board = new char[game_board.length][];
        for(int i = 0; i < game_board.length; i++)
            this.game_board[i] = game_board[i].clone();
        this.player = player;
    }

    public ArrayList<Location> getPlayerPieceLocations() {
        ArrayList<Location> playerPieceLocations = new ArrayList<>();

        for(int i = 0; i < game_board.length; i++)
            for(int j = 0; j < game_board.length; j++)
                if(game_board[i][j] == this.player)
                    playerPieceLocations.add(new Location(i, j));
        
        return playerPieceLocations;
    }

    public boolean isValidLocation(Location location) {
        if(location.getX() < 0 || location.getX() > (game_board.length - 1) || location.getY() < 0
                || location.getY() > (game_board.length - 1))
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
                    if (moveType == 'E' && game_board[neighbor.getX()][neighbor.getY()] == '.')
                        immediateMoves.add(new Move('E', new ArrayList<>(
                                Arrays.asList(location, neighbor))));

                    if (moveType == 'J' && game_board[neighbor.getX()][neighbor.getY()] != '.') {
                        Location oppositeLocation = findOppositeLocation(location, neighbor);

                        if (isValidLocation(oppositeLocation) &&
                                game_board[oppositeLocation.getX()][oppositeLocation.getY()] == '.')
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
                System.out.println("HOME " + move);
                nextMoves.remove(move);
            }
            if(extremityOfMoveInEnemyCamp(move, "START") && !extremityOfMoveInEnemyCamp(move, "END")) {
                System.out.println("ENEMY " + move);
                nextMoves.remove(move);
            }
        }
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

            System.out.println("FRONTIER:\n" + frontier);
            System.out.println("VISITED:\n" + visited);

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
                        System.out.println("FRONTIER:\n" + frontier);
                        System.out.println("VISITED:\n" + visited);
                    }
                }
            }
            System.out.println();
        }

        removeIllegalMoves(moves);

        return moves;
    }

    public char[][] getGame_board() {
        return game_board;
    }

    public void setGame_board(char[][] game_board) {
        this.game_board = game_board;
    }

    public char getPlayer() {
        return player;
    }

    public void setPlayer(char player) {
        this.player = player;
    }
}
