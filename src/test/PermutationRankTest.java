package test;

import org.junit.jupiter.api.Test;
import util.PermutationRank;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermutationRankTest {

    @Test
    public void testUnrank() {
        int[][] permutations = {
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3}
        };

        int[][] expectedResults = {
                {1, 2, 3, 0},
                {3, 2, 0, 1},
                {1, 3, 0, 2}
        };

        PermutationRank permRank = new PermutationRank();

        for (int i = 0; i < permutations.length; i++) {
            permRank.unrank(4, i, permutations[i]);
            assertArrayEquals(expectedResults[i], permutations[i], "Unrank function error.");
        }
    }

    @Test
    public void testRank() {
        int[][] permutations = {
                {1, 2, 3, 0},
                {3, 2, 0, 1},
                {1, 3, 0, 2}
        };

        PermutationRank permRank = new PermutationRank();

        for (int i = 0; i < permutations.length; i++) {
            int expected = permRank.rank(4, permutations[i], permRank.getInversePermutation(permutations[i]));
            assertEquals(i, expected, "Rank function error.");
        }
    }
}
