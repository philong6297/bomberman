package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.*;

public class BDS extends PathFinding {

    private ArrayList<Node<Integer>> _graphNode;

    public BDS(ArrayList<Node<Integer>> graphNode) {
        _graphNode = graphNode;
    }

    private void visitBFS(Queue<Node> queue, Set visitedSet) {
        Node current = queue.poll(); // pop head of queue for visit

        //iterate all node that v can go to
        Iterator<Node> neighbor = current.next.iterator();
        while (neighbor.hasNext()) {
            Node<Integer> w = neighbor.next();
            if (!visitedSet.contains(w))  //if node w is not visited
            {
                visitedSet.add(w); // set visited node w
                w.cameFrom = current; //record where w come from
                queue.add(w);//add node w to queue
            }
        }
    }

    private Node[] getIntersectingNode(Set visitedSetA, Set visitedSetB) {
        Iterator<Node>[] iterator = new Iterator[2];
        Node[] ret = new Node[2];
        iterator[0] = visitedSetA.iterator();
        while (iterator[0].hasNext()) {
            ret[0] = iterator[0].next();
            iterator[1] = visitedSetB.iterator();
            while (iterator[1].hasNext()) {
                ret[1] = iterator[1].next();
                if (ret[0].get_data() == ret[1].get_data())
                    return ret;
            }
        }
        return null;
    }

    @Override
    public Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end) {
        if (start == null || end == null)
            return null;
        //visited set for BFS started from start and end ( front and backward BFS) for keep tracking on visited nodes
        Set visitedSetFront, visitedSetBackward;
        Queue queueFront, queueBackward; // queue for front and backward BFS
        Stack<Integer> path = new Stack<>();
        //necessary initialization
        visitedSetFront = new LinkedHashSet<Node<Integer>>();
        visitedSetBackward = new LinkedHashSet<Node<Integer>>();
        queueFront = new LinkedList<Node<Integer>>();
        queueBackward = new LinkedList<Node<Integer>>();

        visitedSetFront.clear();
        visitedSetBackward.clear();
        queueFront.clear();
        queueBackward.clear();
        //create clone of end to traverse backward
        Node endClone = _graphNode.get(end.get_data());
        queueFront.add(start);
        queueBackward.add(endClone);
        visitedSetFront.add(start);
        visitedSetBackward.add(endClone);

        //Node<Integer> endClone = new Node(end);
        //variable for checking intersect node in 2 visited Set
        Node[] intersectNode;

        while (!queueFront.isEmpty() && !queueBackward.isEmpty()) {
            //do BFS from start and from end node
            visitBFS(queueFront, visitedSetFront);
            visitBFS(queueBackward, visitedSetBackward);

            //check for intersecting node
            intersectNode = getIntersectingNode(visitedSetFront, visitedSetBackward);

            //if intersecting node is found get the path
            if (intersectNode != null) {
                //pathInList.push(intersectNode);
                Stack<Integer> temp = ReconstructPath(endClone, intersectNode[1]);
                while (!temp.isEmpty())
                    path.push(temp.pop());
                path.pop();
                temp = ReconstructPath(start, intersectNode[0]);
                path.addAll(temp);
                return path;
            }
        }
        return null;
    }
}
