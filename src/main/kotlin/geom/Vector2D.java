package geom;

public class Vector2D {

    public double x;
    public double z;

    public Vector2D() {
    }

    public Vector2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public Vector2D(Vector2D v) {
        set(v);
    }

    public void set(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public void set(Vector2D v) {
        this.x = v.x;
        this.z = v.z;
    }

    public void setZero() {
        x = 0;
        z = 0;
    }

    public double[] getComponents() {
        return new double[]{x, z};
    }

    public double getLength() {
        return Math.sqrt(x * x + z * z);
    }

    public double getLengthSq() {
        return (x * x + z * z);
    }

    public double distanceSq(double vx, double vz) {
        vx -= x;
        vz -= z;
        return (vx * vx + vz * vz);
    }

    public double distanceSq(Vector2D v) {
        double vx = v.x - this.x;
        double vz = v.z - this.z;
        return (vx * vx + vz * vz);
    }

    public double distance(double vx, double vz) {
        vx -= x;
        vz -= z;
        return Math.sqrt(vx * vx + vz * vz);
    }

    public double distance(Vector2D v) {
        double vx = v.x - this.x;
        double vz = v.z - this.z;
        return Math.sqrt(vx * vx + vz * vz);
    }

    public double getAngle() {
        return Math.atan2(z, x);
    }

    public void normalize() {
        double magnitude = getLength();
        x /= magnitude;
        z /= magnitude;
    }

    public Vector2D getNormalized() {
        double magnitude = getLength();
        return new Vector2D(x / magnitude, z / magnitude);
    }

    public static Vector2D toCartesian(double magnitude, double angle) {
        return new Vector2D(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public void add(Vector2D v) {
        this.x += v.x;
        this.z += v.z;
    }

    public void add(double vx, double vz) {
        this.x += vx;
        this.z += vz;
    }

    public static Vector2D add(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x + v2.x, v1.z + v2.z);
    }

    public Vector2D getAdded(Vector2D v) {
        return new Vector2D(this.x + v.x, this.z + v.z);
    }

    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.z -= v.z;
    }

    public void subtract(double vx, double vy) {
        this.x -= vx;
        this.z -= vy;
    }

    public static Vector2D subtract(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.z - v2.z);
    }

    public Vector2D getSubtracted(Vector2D v) {
        return new Vector2D(this.x - v.x, this.z - v.z);
    }

    public void multiply(double scalar) {
        x *= scalar;
        z *= scalar;
    }

    public Vector2D getMultiplied(double scalar) {
        return new Vector2D(x * scalar, z * scalar);
    }

    public void divide(double scalar) {
        x /= scalar;
        z /= scalar;
    }

    public Vector2D getDivided(double scalar) {
        return new Vector2D(x / scalar, z / scalar);
    }

    public Vector2D getPerp() {
        return new Vector2D(-z, x);
    }

    public double dot(Vector2D v) {
        return (this.x * v.x + this.z * v.z);
    }

    public double dot(double vx, double vz) {
        return (this.x * vx + this.z * vz);
    }

    public static double dot(Vector2D v1, Vector2D v2) {
        return v1.x * v2.x + v1.z * v2.z;
    }

    public double cross(Vector2D v) {
        return (this.x * v.z - this.z * v.x);
    }

    public double cross(double vx, double vz) {
        return (this.x * vz - this.z * vx);
    }

    public static double cross(Vector2D v1, Vector2D v2) {
        return (v1.x * v2.z - v1.z * v2.x);
    }

    public double project(Vector2D v) {
        return (this.dot(v) / this.getLength());
    }

    public double project(double vx, double vz) {
        return (this.dot(vx, vz) / this.getLength());
    }

    public static double project(Vector2D v1, Vector2D v2) {
        return (dot(v1, v2) / v1.getLength());
    }

    public Vector2D getProjectedVector(Vector2D v) {
        return this.getNormalized().getMultiplied(this.dot(v) / this.getLength());
    }

    public Vector2D getProjectedVector(double vx, double vz) {
        return this.getNormalized().getMultiplied(this.dot(vx, vz) / this.getLength());
    }

    public static Vector2D getProjectedVector(Vector2D v1, Vector2D v2) {
        return v1.getNormalized().getMultiplied(Vector2D.dot(v1, v2) / v1.getLength());
    }

    public void rotateBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rx = x * cos - z * sin;
        z = x * sin + z * cos;
        x = rx;
    }

    public Vector2D getRotatedBy(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new Vector2D(x * cos - z * sin, x * sin + z * cos);
    }

    public void rotateTo(double angle) {
        set(toCartesian(getLength(), angle));
    }

    public Vector2D getRotatedTo(double angle) {
        return toCartesian(getLength(), angle);
    }

    public void reverse() {
        x = -x;
        z = -z;
    }

    public Vector2D getReversed() {
        return new Vector2D(-x, -z);
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector2D) {
            Vector2D v = (Vector2D) obj;
            return (x == v.x) && (z == v.z);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector2d[" + x + ", " + z + "]";
    }

}
