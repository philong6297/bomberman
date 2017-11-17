package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.*;

public class DijkstraGraph extends Graph {
    private Set<Integer> closedSet;
    private Queue<Integer> openSet;

    public DijkstraGraph(String[] lineTiles, int height, int width) {
        super(lineTiles, height, width);
        closedSet = new LinkedHashSet<>();
        openSet = new LinkedList<>();
    }

    @Override
    public Stack<Integer> getShortestPath(int start, int end) {
        path.clear();
        closedSet.clear();
        openSet.clear();
        openSet.add(start);
        int[] cameFrom = new int[n];
        int[] gScore = new int[n];
        int[] fScore = new int[n];
        for (int i = 0; i < n; i++) {
            cameFrom[i] = -1;
            gScore[i] = Integer.MAX_VALUE;
            fScore[i] = 0;
        }
        gScore[start] = 0;

        while (!openSet.isEmpty()) {
            int current = openSet.peek();
            if (current == end)
                break;
            openSet.remove(current);
            closedSet.add(current);

            Iterator<Integer> i = graphNode[current].iterator();
            while (i.hasNext()) {
                int neighbor = i.next();
                if (closedSet.contains(neighbor))
                    continue;
                int tentative_gScore = gScore[current] + 1; // 1 = distance between current and neighbor
                if (!openSet.contains(neighbor) || tentative_gScore < gScore[neighbor]) {
                    cameFrom[neighbor] = current;
                    gScore[neighbor] = tentative_gScore;
                    fScore[neighbor] = gScore[neighbor] + 0; //heuristic = 0
                    if (!openSet.contains(neighbor))
                        openSet.add(neighbor);
                }
            }
        }
        int currentNode = end;
        //if there is no path to end return empty stack;
        if (cameFrom[currentNode] == -1)
            return path;
        while (cameFrom[currentNode] != -1) {
            path.add(currentNode);
            currentNode = cameFrom[currentNode];
        }
        path.add(start);
        return path;
    }

}
