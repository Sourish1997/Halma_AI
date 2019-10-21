import java.util.ArrayList;

public class Move {
    private char moveType;
    private ArrayList<Location> moveSequence;

    public Move(char moveType, ArrayList<Location> moveSequence) {
        this.moveType = moveType;
        this.moveSequence = moveSequence;
    }

    public char getMoveType() {
        return moveType;
    }

    public ArrayList<Location> getMoveSequence() {
        return moveSequence;
    }

    public ArrayList<Location> getMoveSequenceCopy() {
        ArrayList<Location> moveSequenceCopy = new ArrayList<>();
        for(Location location: moveSequence)
            moveSequenceCopy.add(location);

        return moveSequenceCopy;
    }

    @Override
    public String toString()
    {
        if(moveSequence.size() == 1)
            return "J " + moveSequence.get(0).toString();

        String repr = "";
        String prev = "";

        for(Location location:moveSequence) {
            if(prev.equals("")) {
                prev = location.toString();
                continue;
            }

            repr += moveType + " " + prev + " " + location.toString() + "\n";
            prev = location.toString();
        }

        return repr.substring(0, repr.length() - 1);
    }
}