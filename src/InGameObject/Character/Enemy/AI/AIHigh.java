package InGameObject.Character.Enemy.AI;

import InGameObject.Character.Enemy.AI.Algorithm.DijkstraGraph;
import InGameObject.Character.Enemy.AI.Algorithm.Graph;

import java.util.Stack;

public class AIHigh extends AIAbstract {
    protected Graph graph;
    protected String[] _lineTiles;
    private int height, width;
    private int prevEnemyNode, prevPlayerNode;
    private int add;
    private Stack<Integer> locationPath;

    public AIHigh(String[] lineTiles) {
        _lineTiles = lineTiles;
        width = lineTiles[0].length();
        height = lineTiles.length;
        locationPath = new Stack<>();
        graph = new DijkstraGraph(_lineTiles, height, width);
        prevEnemyNode = prevPlayerNode = -1;
        add = -1;
    }

    @Override
    public int calculateDirection(int enemyNode, int playerNode) {
        //if (prevPlayerNode == -1) return random.nextInt(4);
        if (prevPlayerNode != playerNode) {
            /*
        if (_player == null)
        {
            return random.nextInt(4);
        }*/
            //update every single move
            System.out.println("update" + enemyNode + "-" + playerNode);
            locationPath.clear();
            //int playerNode = _player.getYTile()*width+_player.getXTile();
            //int enemyNode = _e.getYTile()*width+_e.getXTile();

            locationPath = graph.getShortestPath(enemyNode, playerNode);
            System.out.println("Path");
            if (locationPath.isEmpty()) System.out.println("Empty after update");
            else
                for (int i = 0; i < locationPath.size(); i++) {
                    System.out.print(locationPath.get(i) + "-");
                    if (i % 31 == 30) System.out.println();
                }
            System.out.println();
            prevPlayerNode = playerNode;
        }
        prevEnemyNode = enemyNode;
        //if locationPath have no solution the bot should stay
        if (locationPath.isEmpty()) {
            System.out.println("no solution");
            return -1;
        }
        if (enemyNode != locationPath.peek()) {
            System.out.println(enemyNode + "not yet to target" + locationPath.peek());
            return add;
        }
        System.out.println(enemyNode);
        //handling how to go to next location from path from currentLocation
        int currentLocation = locationPath.pop();
        //if enemy is touch player of course it will stay no more running
        if (locationPath.isEmpty()) {
            System.out.println("touched player");
            return -1;
        }
        if (currentLocation - locationPath.peek() >= width) add = UP;
        else if (currentLocation - locationPath.peek() >= 1) add = LEFT;
        else if (currentLocation - locationPath.peek() <= -width) add = DOWN;
        else if (currentLocation - locationPath.peek() <= -1) add = RIGHT;
        return add;
    }

}
