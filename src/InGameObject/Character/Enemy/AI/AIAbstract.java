package InGameObject.Character.Enemy.AI;

import InGameObject.Character.Enemy.AI.Definitions.Graph;

import java.util.Random;
import java.util.Stack;

public abstract class AIAbstract {

    protected final int UP = 0;
    protected final int RIGHT = 1;
    protected final int DOWN = 2;
    protected final int LEFT = 3;
    protected Random random = new Random();

    protected Graph graph;
    protected String[] _lineTiles;
    protected int height, width;
    protected int prevEnemyNode, prevPlayerNode;
    protected int add;
    protected Stack<Integer> locationPath;

    public Graph getGraph() {
        return graph;
    }

    public abstract int calculateDirection(int start, int end);

    public void updateGraph(String[] lineTiles) {
        _lineTiles = lineTiles;
        width = lineTiles[0].length();
        height = lineTiles.length;
        graph.update(lineTiles, height, width);
    }
}
