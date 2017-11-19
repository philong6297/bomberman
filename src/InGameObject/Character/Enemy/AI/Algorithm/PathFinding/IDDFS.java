package InGameObject.Character.Enemy.AI.Algorithm.PathFinding;

import InGameObject.Character.Enemy.AI.Definitions.Node;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class IDDFS extends PathFinding {


    //Iterative deepening depth-first search
    @Override
    public Stack<Integer> pathFinding(Node<Integer> start, Node<Integer> end) {
        if (start == null || end == null)
            return null;
        Set<Node<Integer>> visitedSet = new LinkedHashSet<>();
        int depth = 0;
        boolean goalFound = false;
        while (goalFound == false) {
            visitedSet.clear();
            start.cameFrom = null;
            end.cameFrom = null;
            goalFound = visitDLS(start, end, depth, visitedSet);
            depth++;
        }
        return ReconstructPath(start, end);
    }

    //depth limit DFS
    private boolean visitDLS(Node<Integer> src, Node<Integer> goal, int maxDepth, Set visitedSet) {
        visitedSet.add(src);
        if (maxDepth == 0 && src == goal) {
            return true;
        }
        if (maxDepth > 0) {
            Iterator<Node> i = src.next.iterator();
            while (i.hasNext()) {
                Node w = i.next();
                if (!visitedSet.contains(w))  //if node w is not visited
                {
                    w.cameFrom = src; //record where w come from
                    if (visitDLS(w, goal, maxDepth - 1, visitedSet))
                        return true;
                }
            }
        }
        return false;
    }


}

            /*

            */