package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.*;

public class BFSGraph extends Graph {
    private Set<Integer> visitedSet;
    private Queue<Integer> queue;

    public BFSGraph(String[] lineTiles, int height, int width) {
        super(lineTiles, height, width);
        visitedSet = new LinkedHashSet<>();
        queue = new LinkedList<Integer>();
    }

    @Override
    public Stack<Integer> getShortestPath(int start, int end) {
        path.clear();
        visitedSet.clear();
        int[] pathTo = new int[n]; //pathTo save where node come from : pathTo[w] = v means node w is come from v
        for (int i = 0; i < n; i++) {
            pathTo[i] = -1; // initialize pathTo in node i was visited right after nowhere!
        }
        queue.clear();
        visitBFS(pathTo, start, end); // path finding from start to end and store to pathTo


        //get shortest path from startNode to endNode : start with pathTo[endNode]
        // and backtrack until reach startNode
        int currentNode = end;
        //if there is no path to end return empty stack;
        if (pathTo[currentNode] == -1)
            return path;
        while (pathTo[currentNode] != -1) {
            path.add(currentNode);
            currentNode = pathTo[currentNode];
        }
        path.add(start);
        return path;
    }

    private void visitBFS(int[] pathTo, int startNode, int endNode) {
        visitedSet.add(startNode);
        queue.add(startNode);

        while (!queue.isEmpty()) {
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
    }
}
