package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.Stack;

public abstract class PathFinding {
    static Stack<Integer> ReconstructPath(Node<Integer> s, Node<Integer> t) {
        Stack<Integer> path = new Stack<Integer>();
        path.clear();
        Node<Integer> tmp = t;
        while (tmp != s) {
            path.push(tmp.get_data());
            tmp = tmp.cameFrom;
        }
        path.add(s.get_data());
        return path;
    }

    public abstract Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end);
}
