import java.util.Arrays;

public class Location {
    private int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString()
    {
        return y + "," + x;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        Location location = (Location) obj;
        if(x == location.getX() && y == location.getY())
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int[] position = new int[] {x, y};
        return Arrays.hashCode(position);
    }
}