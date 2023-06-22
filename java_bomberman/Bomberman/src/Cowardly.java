import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.List;
import java.util.Set;

/**
 * The type Cowardly. The extension of the enemy class that creates cowardly enemy that nonstop runs from the player
 */
public class Cowardly extends Enemy{
    /**
     * Instantiates a new Cowardly.
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
    public Cowardly(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        super(row, col, color, mobility, map, bomb, delay,board,playerPosition);
    }
    /**
     * The AI implementation for the Cowardly enemy. First of all it waits till it is updated by the board. The up-dated status is essential for
     * proper behaviour. This enemy checks where player is and finds the furthest possible tile. If the tile isn't reachable it finds the closest
     * tile to it and places there bomb. The process is repeated till the enemy is as far as possible. When it reaches it and player isn't near it waits.
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
            GraphPath<Integer,DefaultEdge> road;
            List<Integer> roadVertices;
            Integer whereIam = row * 100 + col;
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPathGlobal=
                    new BidirectionalDijkstraShortestPath<>( board );
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPath=
                    new BidirectionalDijkstraShortestPath<>( paths );
            Set<Integer> vertices = paths.vertexSet();
            Set<Integer> tiles = board.vertexSet();
            if(this.isSafe() && this.nearPlayer())
                this.enemyPlaceBomb();
            if(this.isSafe()) {
                int distance, destination = whereIam;
                double norm = 0;
                for(Integer x : tiles){
                    double tmp = euclideanNorm(x,playerPosition);
                    if(tmp >= norm){
                        norm = tmp;
                        destination = x;
                    }
                }
                if(destination != whereIam){
                    if(( road = shortestPathGlobal.getPath(whereIam,destination) ) != null){
                        roadVertices = road.getVertexList();
                        if(roadVertices.size() > 1 && !nearPlayer()){
                            if(isSafe(roadVertices.get(1))){
                                this.move(roadVertices.get(1));
                                continue;
                            }
                        }
                    }
                    Integer base = getRandomSetElement(vertices);
                    distance = Math.abs(destination - base);
                    for(Integer x : vertices){
                        int tmp = Math.abs(destination - x);
                        if(tmp <= distance){
                            distance = tmp;
                            base = x;
                        }
                    }
                    if(!whereIam.equals(base)){
                        if(( road = shortestPath.getPath(whereIam,base) ) != null) {
                            roadVertices = road.getVertexList();
                            if (roadVertices.size() > 1 && !nearPlayer()){
                                if(isSafe(roadVertices.get(1))){
                                    this.move(roadVertices.get(1));
                                    continue;
                                }
                            }
                        }
                    }else{
                        if(safeBombPlacement())
                            this.enemyPlaceBomb();
                    }
                }
            }else{
                Integer base = whereIam;
                for(Integer x : vertices){
                    if(isSafe(x)) {
                        base = x;
                        break;
                    }
                }
                if(!whereIam.equals(base)){
                    if(( road = shortestPath.getPath(whereIam,base) ) != null) {
                        roadVertices = road.getVertexList();
                        if (roadVertices.size() > 1 && !nearPlayer()){
                            this.move(roadVertices.get(1));
                            continue;
                        }
                        else
                            move();
                    }
                }
                else
                    move();
            }
            updated = false;
        }
    }
}
