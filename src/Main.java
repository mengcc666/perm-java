import entity.Neighbor;
import entity.Snake;

public class Main {
    public static void main(String[] args) {
        int n = 4;
        Neighbor neighbor = new Neighbor(n);
        Snake snake = new Snake();
        int N = neighbor.getTotal();
        snake.initPath(new int[N]);
        snake.start(neighbor);
    }
}