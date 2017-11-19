package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.*;

public class AStar extends PathFinding {

    @Override
    public Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end) {
        if (start == null || end == null)
            return null;
        Set<Node> closedSet = new LinkedHashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>();

        start.gValue = 0;
        start.hValue = start.chebyshevHeuristic(end);
        start.fValue = start.hValue + start.gValue;

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.peek();
            for (Node iNode : openSet) {
                if (iNode.fValue < current.fValue)
                    current = iNode;
            }
            if (current == end) {
                return ReconstructPath(start, end);
            }
            openSet.remove(current);
            closedSet.add(current);

            Iterator<Node> i = current.next.iterator();
            while (i.hasNext()) {
                Node neighbor = i.next();
                if (closedSet.contains(neighbor)) continue;
                double tempCurrentGValue = current.gValue + current.distance(neighbor);
                if (!openSet.contains(neighbor) || tempCurrentGValue < neighbor.gValue) {
                    neighbor.cameFrom = current;
                    neighbor.gValue = tempCurrentGValue;
                    neighbor.hValue = neighbor.chebyshevHeuristic(end);
                    neighbor.fValue = neighbor.gValue + neighbor.hValue;
                    if (!openSet.contains(neighbor))
                        openSet.add(neighbor);
                }
            }

        }
        return null;
    }
}
