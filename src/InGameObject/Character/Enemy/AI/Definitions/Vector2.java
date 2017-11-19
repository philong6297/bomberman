package InGameObject.Character.Enemy.AI.Definitions;

public class Vector2 {
    public double _x;
    public double _y;

    public Vector2(double x, double y) {
        _x = x;
        _y = y;
    }

    public Vector2() {
        this(0, 0);
    }

    public static double vector2dDistance(Vector2 a, Vector2 b) {
        double v0 = b._x - a._x;
        double v1 = b._y - a._y;
        return Math.sqrt(v0 * v0 + v1 * v1);
    }

    public static double chebyshevDistance(Vector2 a, Vector2 b) {
        double dx = Math.abs(b._x - a._x);
        double dy = Math.abs(b._y - a._x);
        return Math.max(dx, dy);
    }

    public static double manhattanDistance(Vector2 a, Vector2 b) {
        double dx = Math.abs(b._x - a._x);
        double dy = Math.abs(b._y - a._x);
        return dx + dy;
    }
    public boolean isEquals(Vector2 other) {
        return (_x == other._x && _y == other._y);
    }
}
