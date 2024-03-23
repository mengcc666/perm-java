import entity.Matching;
import entity.Neighbor;
import entity.Problem;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Snake snake = new Snake();
//        snake.initPath(problem);
//        snake.start(neighbor,problem);

        for(int i=4;i<=10;i++){
            System.out.print("n="+i+"\t");
            Problem problem=new Problem();
            problem.setN(i);
            Neighbor neighbor = new Neighbor(problem);
            Matching matching=new Matching(problem);
            // Experiment on SimpleRandom
//            matching.solveBySimpleRandomExp(problem,neighbor,100);
            // Experiment on RandomWalk
            matching.solveByRandomWalkExp(problem,neighbor,100,problem.getnP()/3);
        }
    }
}