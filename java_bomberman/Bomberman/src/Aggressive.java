import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.BiconnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.alg.shortestpath.*;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Aggressive extends Enemy{

    public Aggressive(int row, int col, Color color, int mobility, int[][] map, List<Bomb> bomb, int delay, Graph<Integer,DefaultEdge> board, Integer playerPosition) {
        super(row, col, color, mobility, map, bomb,delay,board,playerPosition);
    }


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
                List<Integer> road
                        = shortestPath.getPath(whereIam, base).getVertexList();
                if(road.size() > 1)
                    this.move(road.get(1));
                else
                    move();
            }
        updated = false;
        }
    }
}

