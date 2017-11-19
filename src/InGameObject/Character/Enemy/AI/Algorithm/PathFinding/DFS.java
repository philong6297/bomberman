package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class DFS extends PathFinding {

    @Override
    public Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end) {
        if (start == null || end == null)
            return null;
        Set<Node> visitedSet = new LinkedHashSet<>();
        Stack<Node> stack = new Stack<>();

        stack.add(start);
        while (!stack.isEmpty()) {
            Node current = stack.pop(); // pop head of queue for visit

            if (current == end)
                return ReconstructPath(start, end);
            //iterate all node that current can go to
            Iterator<Node> i = current.next.iterator();
            while (i.hasNext()) {
                Node neighbor = i.next();
                if (!visitedSet.contains(neighbor))  //if node w is not visited
                {
                    visitedSet.add(neighbor); // set visited node w
                    neighbor.cameFrom = current; //record where w come from
                    stack.add(neighbor);//add node w to queue
                }
            }
        }
        return null;
    }
}
