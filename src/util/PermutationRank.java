package util;

public class PermutationRank {


    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public int[] getInversePermutation(int[] permutation) {
        int[] inversePermutation = new int[permutation.length];
        for (int i = 0; i < permutation.length; i++) {
            inversePermutation[permutation[i]] = i;
        }
        return inversePermutation;
    }

    public void unrank(int n, int rank, int[] p) {
        if (n > 0) {
            swap(p, n - 1, rank % n);
            unrank(n - 1, rank / n, p);
        }
    }

    /*
        piInv[pi[i]]=i
     */
    public int rank(int n, int[] pi, int[] piInv) {
        if (n == 1) {
            return 0;
        }
        int s = pi[n - 1];
        swap(pi, n - 1, piInv[n - 1]);
        swap(piInv, s, n - 1);
        return s + n * rank(n - 1, pi, piInv);
    }
}

