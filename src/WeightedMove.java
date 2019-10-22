public class WeightedMove {
    private Move move;
    private double weight;

    public WeightedMove(Move move, double weight) {
        this.move = move;
        this.weight = weight;
    }

    public Move getMove() {
        return move;
    }

    public double getWeight() {
        return weight;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
