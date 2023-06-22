import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.shortestpath.*;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * The type Aggressive. The extension of the enemy class that creates aggressive enemy that nonstop attacks player
 */
public class Aggressive extends Enemy{

    /**
     * Instantiates a new Aggressive.
     *
     * @param row            the row
     * @param col            the col
     * @param color          the color
     * @param mobility       the mobility
     * @param map            the map
     * @param bomb           the bomb
     * @param delay          the delay
     * @param board          the board
     * @param playerPosition the player position
     * @see Enemy
     */
    public Aggressive(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        super(row, col, color, mobility, map, bomb,delay,board,playerPosition);
    }

    /**
     * The AI implementation for the Aggressive enemy. First of all it waits till it is updated by the board. The up-dated status is essential for
     * proper behaviour. This enemy checks where player is and finds the closest possible tile. When it reaches it and player isn't near it places bomb.
     * If the player is near it will try to kill him. It uses Dijkstra Algorithm for shortest pathfinding. This method is the essential implementation
     * of the thread based programming.
     * @see Runnable
     * @see Enemy
     * @see Board
     * @see BidirectionalDijkstraShortestPath
     */
    public void run(){
        while (this.status() && !Thread.currentThread().isInterrupted()){
            while (!updated) {
                Thread.onSpinWait();
            }
            Integer whereIam = row * 100 + col;
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPath=
                    new BidirectionalDijkstraShortestPath<>( paths );
            Set<Integer> vertices = paths.vertexSet();
            if(this.isSafe() && this.nearPlayer())
                this.enemyPlaceBomb();
            if(this.isSafe()) {
                Integer base = getRandomSetElement(paths.vertexSet());
                int distance = Math.abs(playerPosition - base);
                for(Integer x : vertices){
                    int tmp = Math.abs(playerPosition - x);
                    if(tmp <= distance){
                        distance = tmp;
                        base = x;
                    }
                }
                List<Integer> road
                        = shortestPath.getPath(whereIam, base).getVertexList();
                if(road.size() > 1 && !nearPlayer()){
                    if(isSafe(road.get(1)))
                        this.move(road.get(1));
                }
                else{
                    if(safeBombPlacement())
                        this.enemyPlaceBomb();
                }
            }else{
                Integer base = whereIam;
                for(Integer x : vertices){
                        if(this.isSafe(x)) {
                            base = x;
                            break;
                        }
                }
                if(!whereIam.equals(base)){
                    List<Integer> road
                            = shortestPath.getPath(whereIam, base).getVertexList();
                    if(road.size() > 1)
                        this.move(road.get(1));
                    else
                        move();
                }
            }
        updated = false;
        }
    }
}

