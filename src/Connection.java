public class Connection {
    private MyPoint start;
    private MyPoint end;
    private int distance;

    public Connection(MyPoint start, MyPoint end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public MyPoint getStart() {
        return start;
    }
    public MyPoint getEnd() {
        return end;
    }
    public int getDistance() {
        return distance;
    }

}
