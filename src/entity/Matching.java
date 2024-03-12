package entity;

import util.PermUtil;
import util.PermutationRank;

import java.util.*;

public class Matching {


    private HashMap<Integer, int[]> permutationHashMap = new HashMap<>();


    private int[] pairs;
    private ArrayList<Integer> matched;

    private ArrayList<Integer> unmatched;
    private final PermUtil permUtil = new PermUtil();
    private final PermutationRank permRank = new PermutationRank();

    /*
        L: Sequence of random index of perm
     */
    private int[] L;

    public void setUnmatched(Problem problem) {
        unmatched=new ArrayList<>();
        for (int i=0;i<problem.getnP();i++){
            unmatched.add(i);
        }
    }

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
        matched=new ArrayList<>();
        setUnmatched(problem);
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

    public void findMatchingMM1(Problem problem,Neighbor neighbor){
        initL(problem.getnP());
        while (!unmatched.isEmpty()){
            Collections.shuffle(unmatched);
            ArrayList<Integer> neighbors=neighbor.getNeighbors(unmatched.get(0));
            Collections.shuffle(neighbors);
            for (int i=0;i<neighbors.size();i++){
                if (!matched.contains((neighbors.get(i)))){
                    matched.add(unmatched.get(0));
                    matched.add(neighbors.get(i));
                    unmatched.remove(0);
                    final int tempi=i;
                    unmatched.removeIf(s->s.equals(neighbors.get(tempi)));
                    break;
                }
                if (i==neighbors.size()-1){
                    int curr= unmatched.get(0),currNbr= neighbors.get(0),nbrPartner=0;
                    int previousPairIndex;
                    // Force pair
                    // Remove neighbors[0] and its partner from matched
                    for (int j=0;j<matched.size();j++){
                        if (matched.get(j)==neighbors.get(0)){
                            if (j%2==0){
                                previousPairIndex=j;
                                nbrPartner= matched.get(j+1);
                            }else {
                                previousPairIndex=j-1;
                                nbrPartner= matched.get(j-1);
                            }
                            matched.remove(previousPairIndex);
                            matched.remove(previousPairIndex);
                            matched.add(curr);
                            matched.add(currNbr);
                            unmatched.remove(0);
                            unmatched.add(nbrPartner);
                            break;
                        }
                    }
                }
            }
            System.out.println(matched+" len="+String.valueOf(matched.size()));

        }
        System.out.println(matched+" len="+String.valueOf(matched.size()));

    }
}
