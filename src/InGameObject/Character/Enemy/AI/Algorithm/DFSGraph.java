package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class DFSGraph extends Graph {
    Set<Integer> visitedSet;
    Stack<Integer> stack;

    public DFSGraph(String[] lineTiles, int height, int width) {
        super(lineTiles, height, width);
        visitedSet = new LinkedHashSet<>();
        stack = new Stack<>();
    }

    public Stack<Integer> getShortestPath(int start, int end) {
        path.clear();
        visitedSet.clear();
        int[] pathTo = new int[n]; //pathTo save where node come from : pathTo[w] = v means node w is come from v
        for (int i = 0; i < n; i++) {
            pathTo[i] = -1; // initialize pathTo in node i was visited right after nowhere!
        }
        stack.clear();
        visitDFSrecursive(start, end, pathTo); // path finding from start to end and store to pathTo
        //visitDFS(pathTo,start,end);

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

    private boolean visitDFSrecursive(int start, int end, int pathTo[]) {
        visitedSet.add(start);
        if (start == end) return true;
        Iterator<Integer> i = graphNode[start].iterator();
        while (i.hasNext()) {
            int w = i.next();
            if (!visitedSet.contains(w))  //if node w is not visited
            {
                pathTo[w] = start; //record where w come from
                if (visitDFSrecursive(w, end, pathTo))
                    return true;
            }
        }
        return false;
    }
    private void visitDFS(int pathTo[], int startNode, int endNode) {
        stack.add(startNode);
        int prevMove = -1;
        while (!stack.isEmpty()) {
            int v = stack.pop(); // pop head of stack for visit
            //if v is not visited
            if (!visitedSet.contains(v)) {
                //visit v
                visitedSet.add(v);
                if (pathTo[v] == -1) {
                    pathTo[v] = prevMove;
                    prevMove = v;
                    if (v == endNode) return;
                }
                //iterate all node that v can go to
                Iterator<Integer> i = graphNode[v].iterator();
                while (i.hasNext()) {
                    int w = i.next();
                    if (!visitedSet.contains(w))  //if node w is not visited
                    {
                        pathTo[w] = v; //record where w come from
                        stack.add(w);//add node w to queue
                    }
                }
            }
        }
    }
}
