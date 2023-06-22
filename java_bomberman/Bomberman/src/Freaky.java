import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The type Freaky. The extension of the enemy that creates enemy that cannot be predicted.
 */
public class Freaky extends Enemy{

    private Integer goal;

    /**
     * Instantiates a new Freaky.
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
    public Freaky(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        super(row, col, color, mobility, map,  bomb, delay,board,playerPosition);
        goal = row*100+col;
    }

    /**
     * The AI implementation for the Freaky enemy. First of all it waits till it is updated by the board. The up-dated status is essential for
     * proper behaviour. This enemy randomly chooses goal on the map and tries to reach it. When it cannot do it, then it places randomly bomb
     * and run. If the player is near it will try to kill him, and then continue to reaching the goal. It uses Dijkstra Algorithm for shortest
     * pathfinding. This method is the essential implementation of the thread based programming.
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
            Random random = new Random();
            GraphPath<Integer,DefaultEdge> road;
            List<Integer> roadVertices;
            Integer whereIam = row * 100 + col;
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPathGlobal=
                    new BidirectionalDijkstraShortestPath<>( board );
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPath=
                    new BidirectionalDijkstraShortestPath<>( paths );
            Set<Integer> vertices = paths.vertexSet();
            if(this.isSafe() && this.nearPlayer())
                this.enemyPlaceBomb();
            if(this.isSafe()) {
                goal = makeGoal();
                if(!whereIam.equals(goal)){
                    if(( road = shortestPathGlobal.getPath(whereIam,goal) ) != null){
                        roadVertices = road.getVertexList();
                        if(roadVertices.size() > 1 && !nearPlayer()){
                            if(isSafe(roadVertices.get(1))){
                                this.move(roadVertices.get(1));
                                continue;
                            }
                        }
                    }
                    Integer base = getRandomSetElement(vertices);
                    int distance = Math.abs(goal - base);
                    for(Integer x : vertices){
                        int tmp = Math.abs(goal - x);
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
                        if(safeBombPlacement() && random.nextInt(9) != 0)
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


    /**
     * Method of random choose of the destination for the Freak. If the current goal is not reached it is not changed.
     * Else the new random aim is chosen
     * @return Integer - vertex that is a Freak's goal
     */
    private Integer makeGoal(){
        if( row*100 + col != goal )
            return goal;
        return getRandomSetElement(board.vertexSet());
    }

}
