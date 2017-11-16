package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class IDDFSGraph extends Graph {
    Set<Integer> visitedSet;
    Stack<Integer> stack;

    public IDDFSGraph(String[] lineTiles, int height, int width) {
        super(lineTiles, height, width);
        visitedSet = new LinkedHashSet<>();
        stack = new Stack<>();
    }

    @Override
    public Stack<Integer> getShortestPath(int start, int end) {
        path.clear();
        int[] pathTo = new int[n];
        visitIDDFS(pathTo, start, end);
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

    //Iterative deepening depth-first search
    public Stack<Integer> visitIDDFS(int[] pathTo, int start, int end) {
        int depth = 0;
        boolean goalFound = false;
        while (goalFound == false) {
            visitedSet.clear();
            stack.clear();
            for (int i = 0; i < n; i++) pathTo[i] = -1;
            goalFound = visitDLS(start, end, depth, pathTo);
            depth++;
        }
        return stack;
    }

    //depth limit DFS
    private boolean visitDLS(int src, int goal, int maxDepth, int[] pathTo) {
        visitedSet.add(src);
        if (maxDepth == 0 && src == goal) {
            return true;
        }
        if (maxDepth > 0) {
            Iterator<Integer> i = graphNode[src].iterator();
            while (i.hasNext()) {
                int w = i.next();
                if (!visitedSet.contains(w))  //if node w is not visited
                {
                    pathTo[w] = src; //record where w come from
                    if (visitDLS(w, goal, maxDepth - 1, pathTo))
                        return true;
                }
            }
        }
        return false;
    }
}

            /*

            */