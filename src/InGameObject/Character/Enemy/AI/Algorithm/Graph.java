package InGameObject.Character.Enemy.AI.Algorithm;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public abstract class Graph {
    protected int n;
    protected Set<Integer>[] graphNode;
    protected int _height, _width;

    protected Stack<Integer> path; // store route for path finding

    public Graph(String[] lineTiles, int height, int width) {
        _height = height;
        _width = width;
        n = _height * _width;
        graphNode = new Set[n];
        for (int i = 0; i < n; i++)
            graphNode[i] = new LinkedHashSet<Integer>();
        //read input
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (lineTiles[i].charAt(j) != '#' && lineTiles[i].charAt(j) != '*') //~ node i*weight+j
                {
                    int[] dx = new int[]{-1, 0, 0, 1};
                    int[] dy = new int[]{0, -1, 1, 0};
                    for (int k = 0; k < 4; k++) {
                        int y = i + dx[k];
                        int x = j + dy[k];
                        if (lineTiles[y].charAt(x) != '#' && lineTiles[y].charAt(x) != '*') {
                            addEdge(i * width + j, y * width + x);
                            addEdge(y * width + x, i * width + j);
                        }
                    }
                }
        path = new Stack<>();
    }

    public void AddEdgeTo(int x) {
        int[] dx = new int[]{-1, 1, _width, -_width};
        for (int i : dx) {
            addEdge(x, x + i);
        }
    }

    public void addEdge(int x, int y) {
        if (x < 0 || x > n - 1 || y < 0 || y > n - 1)
            return;

        if (!graphNode[x].contains(y)) graphNode[x].add(y);
        //graphNode[y].add(x);
    }

    public Stack<Integer> getShortestPath(int start, int end) {
        return path;
    }
}