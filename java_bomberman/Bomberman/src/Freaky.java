import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Freaky extends Enemy{

    private Integer goal;
    public Freaky(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        super(row, col, color, mobility, map,  bomb, delay,board,playerPosition);
        goal = row*100+col;
    }

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



    private Integer makeGoal(){
        if( row*100 + col != goal )
            return goal;
        return getRandomSetElement(board.vertexSet());
    }

}