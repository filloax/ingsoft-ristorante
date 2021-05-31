package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public class DoublePair {
    private double x;
    private double y;

    public DoublePair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public DoublePair x(double x) {
        setX(x);
        return this;
    }

    public DoublePair y(double y) {
        setY(y);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DoublePair)) {
            return false;
        }
        DoublePair doublePair = (DoublePair) o;
        return x == doublePair.x && y == doublePair.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" +
            " x='" + getX() + "'" +
            ", y='" + getY() + "'" +
            "}";
    }

}
