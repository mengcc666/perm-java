package entity;

import util.PermUtil;
import util.PermutationRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Neighbor {
    private HashMap<Integer, ArrayList<Integer>> neighborMap = new HashMap<>();
    PermutationRank permRank = new PermutationRank();
    private PermUtil permUtil = new PermUtil();

    private Problem problem=new Problem();
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Neighbor(Problem problem) {
        int n= problem.getN();
        int N = problem.getnP();
        total=N;
        for (int i = 0; i < N; i++) {
            int[] p = permUtil.initPerm(n);
            permRank.unrank(n, i, p);

            ArrayList<Integer> indicesSet = new ArrayList<>();

            for (int j = 0; j < n - 1; j++) {
                int[] temp = Arrays.copyOf(p, n);
                permRank.swap(temp, j, j + 1);
                int[] ipi = permRank.getInversePermutation(temp);
                int index = permRank.rank(n, temp, ipi);
                indicesSet.add(index);
            }

            this.neighborMap.put(i, indicesSet);
        }
    }



    public ArrayList<Integer> getNeighbors(int i) {
        ArrayList<Integer> neighbors = neighborMap.get(i);

        //            System.out.println("Neighbors for node " + i + ": " + neighbors);
        //            System.out.println("No neighbors found for node " + i);
        // Return an empty array if no neighbors found
        return Objects.requireNonNullElseGet(neighbors, ArrayList::new);
    }
}
