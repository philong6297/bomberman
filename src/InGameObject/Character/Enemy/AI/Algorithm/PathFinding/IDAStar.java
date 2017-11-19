package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class IDAStar extends PathFinding {
    @Override
    public Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end) {
        if (start == null || end == null)
            return null;
        Set<Node<Integer>> visitedSet = new LinkedHashSet<>();
        boolean goalFound = false;
        double bound = start.manhattanHeuristic(end);
        double result = Integer.MAX_VALUE;
        while (goalFound == false) {
            start.gValue = 0;
            start.hValue = start.manhattanHeuristic(end);
            start.fValue = start.hValue + start.gValue;
            visitedSet.clear();
            end.cameFrom = null;
            visitedSet.add(start);
            result = AStar(start, end, bound, visitedSet);
            if (result <= bound) return ReconstructPath(start, end);
            if (result == Integer.MAX_VALUE) return null;
            bound = result;
        }
        return null;
    }

    private double AStar(Node<Integer> start, Node<Integer> end, double bound, Set visitedSet) {
        start.fValue = start.gValue + start.manhattanHeuristic(end);
        if (start.fValue > bound) {
            return start.fValue;
        }
        if (start == end) {
            return bound;
        }
        //visitedSet.add(start);
        double min = Integer.MAX_VALUE;
        Iterator<Node> i = start.next.iterator();
        while (i.hasNext()) {
            Node neighbor = i.next();
            if (!visitedSet.contains(neighbor)) {
                visitedSet.add(neighbor);
                neighbor.cameFrom = start;
                neighbor.gValue = start.gValue + neighbor.distance(start);
                double result = AStar(neighbor, end, bound, visitedSet);
                if (result <= bound) return result;
                if (result < min) min = result;
                visitedSet.remove(neighbor);
                neighbor.cameFrom = null;
            }
        }
        return min;
    }
}
