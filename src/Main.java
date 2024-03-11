import entity.Matching;
import entity.Neighbor;
import entity.Problem;
import entity.Snake;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Problem problem=new Problem();
        problem.setMaxTimeForMatching(1000000);
        problem.setN(4);
        Neighbor neighbor = new Neighbor(problem);
        neighbor.getNeighbors(0);
//        Snake snake = new Snake();
//        snake.initPath(problem);
//        snake.start(neighbor,problem);
        Matching matching=new Matching(problem);
        matching.findMatchingM1(problem);
    }
}