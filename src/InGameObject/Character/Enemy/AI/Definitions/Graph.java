package InGameObject.Character.Enemy.AI.Definitions;

import InGameObject.Character.Enemy.AI.Algorithm.PathFinding.AStar;
import InGameObject.Character.Enemy.AI.Algorithm.PathFinding.PathFinding;

import java.util.ArrayList;
import java.util.Stack;

public class Graph {
    protected int n;

    protected ArrayList<Node<Integer>> graphNode, graphNodeClone;
    private PathFinding _pathFinding;
    protected int _height, _width;
    private String[] tiles;
    protected Stack<Integer> path; // store route for path finding
    public Graph(String[] lineTiles, int height, int width) {
        _height = height;
        _width = width;
        n = _height * _width;
        graphNode = new ArrayList<>();
        graphNodeClone = new ArrayList<>();
        update(lineTiles, height, width);
        path = new Stack<>();
        //_pathFinding = new BDS(graphNodeClone);
        _pathFinding = new AStar();
        //_pathFinding = new BFS();
        //_pathFinding = new IDDFS();
        //_pathFinding = new DFS();
    }

    public ArrayList<Node<Integer>> getGraphNode() {
        return graphNode;
    }

    public void update(String[] lineTiles, int height, int width) {
        tiles = lineTiles;
        graphNodeClone.clear();
        graphNode.clear();
        System.out.println("Graph update");
        for (int i = 0; i < height; i++) {
            System.out.println(lineTiles[i]);
            for (int j = 0; j < width; j++) {
                graphNode.add(new Node<>(new Vector2(j, i), i * width + j));
                graphNodeClone.add(new Node<>(new Vector2(j, i), i * width + j));
            }
        }
        //read input
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                if (lineTiles[i].charAt(j) != '#' && lineTiles[i].charAt(j) != '*' && lineTiles[i].charAt(j) != 'b') //~ node i*weight+j
                {
                    int[] dx = new int[]{-1, 0, 0, 1};
                    int[] dy = new int[]{0, -1, 1, 0};
                    for (int k = 0; k < 4; k++) {
                        int y = i + dy[k];
                        int x = j + dx[k];
                        if (lineTiles[y].charAt(x) != '#' && lineTiles[y].charAt(x) != '*' && lineTiles[y].charAt(x) != 'b') {
                            addEdge(graphNode.get(y * width + x),
                                    graphNode.get(i * width + j));
                            addEdge(graphNodeClone.get(y * width + x),
                                    graphNodeClone.get(i * width + j));
                        }

                    }
                }
            }
    }

    public void addEdge(Node x, Node y) {
        x.makeLink(y);
        y.makeLink(x);
        //graphNode[y].add(x);
    }

    public Stack<Integer> getShortestPath(int start, int end) {
        if (start < 0 || start > graphNode.size() - 1 || end < 0 || end > graphNode.size() - 1) return null;
        return _pathFinding.pathFinding(graphNode.get(start), graphNode.get(end));
    }
}