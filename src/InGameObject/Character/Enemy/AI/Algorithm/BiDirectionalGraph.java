package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.*;

public class BiDirectionalGraph extends Graph {
    //visited set for BFS started from start and end ( front and backward BFS) for keep tracking on visited nodes
    private LinkedList<Integer> pathInList;
    private Set<Integer> visitedSetA, visitedSetB;
    private Queue<Integer> queueA, queueB; // queue for front and backward BFS

    public BiDirectionalGraph(String[] lineTiles, int height, int width) {
        super(lineTiles, height, width);
        visitedSetA = new LinkedHashSet<>();
        visitedSetB = new LinkedHashSet<>();
        queueA = new LinkedList<>();
        queueB = new LinkedList<>();
        pathInList = new LinkedList<>();
    }

    private void visitBFS(Queue<Integer> queue, Set visitedSet, int[] pathTo) {
        int v = queue.poll(); // pop head of queue for visit

        //iterate all node that v can go to
        Iterator<Integer> i = graphNode[v].iterator();
        while (i.hasNext()) {
            int w = i.next();
            if (!visitedSet.contains(w))  //if node w is not visited
            {
                visitedSet.add(w); // set visited node w
                pathTo[w] = v; //record where w come from
                queue.add(w);//add node w to queue
            }
        }
    }

    private int getIntersectingNode(Set visitedSetA, Set visitedSetB) {
        int intersectNode = -1;
        for (int i = 0; i < n; i++) {
            if (visitedSetA.contains(i) && visitedSetB.contains(i))
                return i;
        }
        return -1;
    }

    @Override
    public Stack<Integer> getShortestPath(int start, int end) {
        //necessary initialization
        path.clear();
        pathInList.clear();
        visitedSetA.clear();
        visitedSetB.clear();
        queueA.clear();
        queueB.clear();

        queueA.add(start);
        queueB.add(end);
        visitedSetA.add(start);
        visitedSetB.add(end);

        //pathTo save where node come from : pathTo[w] = v means node w is come from v
        int[] pathToA = new int[n];
        int[] pathToB = new int[n];
        // initialize pathTo in node i was visited right after nowhere!
        for (int i = 0; i < n; i++)
            pathToA[i] = pathToB[i] = -1;

        //variable for checking intersect node in 2 visited Set
        int intersectNode = -1;

        while (!queueA.isEmpty() && !queueB.isEmpty()) {
            //do BFS from start and from end node
            visitBFS(queueA, visitedSetA, pathToA);
            visitBFS(queueB, visitedSetB, pathToB);

            //check for intersecting node
            intersectNode = getIntersectingNode(visitedSetA, visitedSetB);

            //if intersecting node is found get the path
            if (intersectNode != -1) {
                //pathInList.push(intersectNode);
                int i = intersectNode;
                while (i != end) {
                    pathInList.push(pathToB[i]);
                    i = pathToB[i];
                }
                while (!pathInList.isEmpty())
                    path.push(pathInList.poll());
                i = intersectNode;
                while (pathToA[i] != -1) {
                    path.push(i);
                    i = pathToA[i];
                }
                path.push(start);
                return path;
            }
        }
        return path;
    }
}
