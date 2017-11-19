package InGameObject.Character.Enemy.AI.Definitions;

import java.util.LinkedHashSet;
import java.util.Set;

public class Node<T> implements Comparable<Node> {

    public Vector2 _position;
    public Set<Node> next;
    public Node cameFrom = null;
    public double gValue = Integer.MAX_VALUE;
    public double hValue = 0;
    public double fValue = Integer.MAX_VALUE;
    protected T _data;

    public Node(Vector2 pos, T data) {
        _data = data;
        _position = pos;
        next = new LinkedHashSet<>();
    }

    public Set<Node> getNext() {
        return next;
    }

    public T get_data() {
        return _data;
    }

    public Vector2 get_position() {
        return _position;
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public double getgValue() {
        return gValue;
    }

    public double gethValue() {
        return hValue;
    }

    public double getfValue() {
        return fValue;
    }

    public void makeLink(Node node) {
        this.next.add(node);
        //node.next.add(this);
    }

    public void destroyLink(Node node) {
        this.next.remove(node);
        //node.next.remove(this);
    }

    public double distance(Node node) {
        return Vector2.vector2dDistance(this._position, node._position);
    }

    public double manhattanHeuristic(Node node) {
        return Vector2.manhattanDistance(this._position, node._position);
    }

    public double chebyshevHeuristic(Node node) {
        return Vector2.chebyshevDistance(this._position, node._position);
    }

    @Override
    public int compareTo(Node o) {
        if (fValue < o.fValue)
            return -1;
        if (fValue > o.fValue)
            return 1;
        return 0;
    }
}
