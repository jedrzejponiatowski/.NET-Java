import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.shortestpath.*;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Aggressive extends Enemy{

    public Aggressive(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, BiconnectivityInspector<Integer, DefaultEdge> inspector, Integer playerPosition) {
        super(row, col, color, mobility, map, bomb,delay,inspector,playerPosition);
    }

    public void run(){
        Random random = new Random();

        while (this.status() && !Thread.currentThread().isInterrupted()){
            Integer whereIam = row * 100 + col;
            BidirectionalDijkstraShortestPath<Integer,DefaultEdge> shortestPath=
                    new BidirectionalDijkstraShortestPath<>( paths );
            Set<Integer> vertices = paths.vertexSet();
            if(this.isSafe()) {
                Integer base = getRandomSetElement(paths.vertexSet());
                Integer distance = Math.abs(playerPosition - base);
                for(Integer x : vertices){
                    Integer tmp = Math.abs(playerPosition - x);
                    if(tmp <= distance){
                        distance = tmp;
                        base = x;
                    }
                }
                List<Integer> road
                        = shortestPath.getPath(whereIam, base).getVertexList();
                if(road.size() > 1 && isSafe(road.get(1)))
                    this.move(road.get(1));
//                else
//                    this.enemyPlaceBomb();
            }else{
                Integer base = whereIam;
                for(Integer x : vertices){
                        if(this.isSafe(x)) {
                            base = x;
                            break;
                        }
                }
                List<Integer> road
                        = shortestPath.getPath(whereIam, base).getVertexList();
                if(road.size() > 1)
                    this.move(road.get(1));
                else
                    this.enemyPlaceBomb();
            }

        }
    }
}

