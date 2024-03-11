package entity;

import util.PermUtil;
import util.PermutationRank;

import java.util.*;

public class Matching {


    private HashMap<Integer, int[]> permutationHashMap = new HashMap<>();


    private int[] pairs;
    private final PermUtil permUtil = new PermUtil();
    private final PermutationRank permRank = new PermutationRank();

    /*
        L: Sequence of random index of perm
     */
    private int[] L;

    public void initL(int N) {
        L = new int[N];
        for (int i = 0; i < N; i++) {
            L[i] = i;
        }
        permUtil.shuffle(L);
    }

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


    public void findMatchingM1(Problem problem) {
        int i, j, k, nL, minL = 100000000;
        int[] neighbors = new int[20];
        int nn; // nn - number of neighbors
        int[] p, q; // current permutations
        int maxTime = problem.getMaxTimeForMatching();
        for (int time = 0; time < maxTime; time++) {
            HashMap<Integer, int[]> permHashMap = permutationHashMap;
            initL(problem.getnP());
            i = 0;
            int nPairs = 0;
            nL = problem.getnP();
            while (i < nL) { // loop
                p = permHashMap.get(L[i]);
                nn = 0; // neighbors
                for (j = i + 1; j < nL; j++) {
                    q = permHashMap.get(L[j]);
                    if (permUtil.checkNeighbor(p, q)) {
                        neighbors[nn++] = j;
                    }
                }

                if (nn == 0) {
                    i++;
                    continue;
                }
                // new pair
                int qRandom = new Random().nextInt(nn);
                pairs[L[i]] = L[neighbors[qRandom]];
                pairs[L[neighbors[qRandom]]] = L[i];
                nL -= 2;

                int offset = 1;
                for (k = i; k < nL; k++) {
                    if (L[k + 1] == L[neighbors[qRandom]]) {
                        offset = 2;
                    }
                    L[k] = L[k + offset];
                }
            }

            if (minL > nL) {
                minL = nL;
                System.out.println(" minN " + minL);
                if (nL == 0) {
                    permUtil.printArray(pairs, " Pairs ");
                    break;
                }

            }
        }
    }
}
