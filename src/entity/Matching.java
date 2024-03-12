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
        unmatched = new ArrayList<>();
        for (int i = 0; i < problem.getnP(); i++) {
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
        matched = new ArrayList<>();
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

    public void findMatchingMM1(Problem problem, Neighbor neighbor) {
        initL(problem.getnP());
        while (!unmatched.isEmpty()) {
            Collections.shuffle(unmatched);
            int currNode = unmatched.get(0);
            ArrayList<Integer> neighborsOfCurrNode = neighbor.getNeighbors(currNode);
            Collections.shuffle(neighborsOfCurrNode);
            int nextNode = neighborsOfCurrNode.get(0);
            // Next node is unmatched, just add (currNode, nextNode) to matched and remove them from unmatched
            if (unmatched.contains(nextNode)) {
                matched.add(currNode);
                matched.add(nextNode);
                unmatched.remove(0);// Remove current node
                final int finalNextNode = nextNode;
                unmatched.removeIf(s -> s.equals(finalNextNode));
            } else {
                // Next node is already matched, do random walk
                ArrayList<Integer> visited = new ArrayList<>();
                boolean deadEnd;
                do {
                    deadEnd=true;
                    visited.add(currNode);
                    if (visited.size()%2!=0){
                        for(int nbr:neighbor.getNeighbors(currNode)){
                            if (!visited.contains(nbr)){
                                currNode=nbr;deadEnd=false;break;
                            }
                        }
                    }else {
                        currNode=findPartner(currNode);
                        deadEnd=false;
                    }
//                    System.out.println(visited);
                    if (unmatched.contains(currNode) && visited.size()%2==1){
                        visited.add(currNode);
                        for (int v:visited){
                            unmatched.removeIf(s->s.equals(v));
                            matched.removeIf(s->s.equals(v));
                        }
                        matched.addAll(visited);
                        break;
                    }
                } while (!deadEnd);
            }
//            System.out.println(String.valueOf(matched.size())+matched);
            System.out.println(matched.size());
        }

    }

    private int findPartner(int nodeIndex) {
        for (int i = 0; i < matched.size(); i++) {
            if (matched.get(i) == nodeIndex) {
                if (i % 2 == 0) {
                    return matched.get(i + 1);
                } else {
                    return matched.get(i - 1);
                }
            }
        }
        return -1;
    }
}
