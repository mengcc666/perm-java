package test;
import entity.Snake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnakeTest {

    private Snake snake;

    @BeforeEach
    void setUp() {
        snake = new Snake();
    }

    private int[] createSamplePath() {
        return new int[]{1,2,3,4};
    }
}
