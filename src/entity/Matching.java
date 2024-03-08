package entity;

import util.PermUtil;
import util.PermutationRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Matching {


    private HashMap<Integer, int[]> permutationHashMap = new HashMap<>();


    private int[] pairs;
    private final PermUtil permUtil = new PermUtil();
    private final PermutationRank permRank = new PermutationRank();

    public Matching(Problem problem) {
        setPermutationHashMap(problem);
        initPairs(problem);
    }

    public void initPairs(Problem problem) {
        int N = problem.getnP();
        this.pairs = new int[N];
        for (int i = 0; i < N; i++) {
            pairs[i] = -1;
        }
    }

    public void setPermutationHashMap(Problem problem) {
        int n = problem.getN();
        int N = problem.getnP();
        for (int i = 0; i < N; i++) {
            int[] perm = permUtil.initPerm(n);
            permRank.unrank(n, i, perm);
            this.permutationHashMap.put(i, perm);
        }
    }

    public void findPairs(Problem problem) {
        HashMap<Integer, int[]> permutationHashMapTemp = permutationHashMap;
        int N = problem.getnP();
        for (int i = 0; i < N - 1; i++) {
            int[] pi = permutationHashMapTemp.get(i);
            if (pi == null) {
                continue;
            }
            HashMap<Integer, int[]> currentNeighbors = new HashMap<>();
            for (int j = i + 1; j < N; j++) {
                int[] pj = permutationHashMapTemp.get(j);
                if (pj == null) {
                    continue;
                }
                if (permUtil.checkNeighbor(pi, pj)) {
                    currentNeighbors.put(j, pj);
                }
            }
            int countOfNeighbors = currentNeighbors.size();
            if (countOfNeighbors > 1) {
                Random random = new Random();
                List<Integer> keysList = new ArrayList<>(currentNeighbors.keySet());
                int randomIndex = random.nextInt(keysList.size());
                pairs[i] = randomIndex;
                pairs[randomIndex] = i;
                permutationHashMapTemp.remove(i);
                permutationHashMapTemp.remove(randomIndex);
            }

        }
        permUtil.printArray(pairs,"Pairs");
    }
}
