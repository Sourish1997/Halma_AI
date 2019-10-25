public class WeightedMove {
    private Move move;
    private double weight;
    private int depth;

    public WeightedMove(Move move, double weight, int depth) {
        if(move == null)
            this.move = null;
        else
            this.move = new Move(move.getMoveType(), move.getMoveSequenceCopy());
        this.weight = weight;
        this.depth = depth;
    }

    public Move getMove() {
        return move;
    }

    public double getWeight() {
        return weight;
    }

    public void setMove(Move move) {
        if(move == null)
            this.move = null;
        else
            this.move = new Move(move.getMoveType(), move.getMoveSequenceCopy());
        this.move = move;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
