package util;

import java.util.Random;

public class PermUtil {
    public int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public void shuffle(int[] array) {
        Random random = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            // Swap array[i] and array[index]
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }

    public void printArray(int[] array,String desc) {
        System.out.print(desc+": [");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public void rotateArray(int[] arr, int i, int j) {
        while (i < j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            i++;
            j--;
        }
    }

    public int[] initPerm(int n) {
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }
        return p;
    }

    public boolean checkNeighbor(int[] p1,int[] p2){
        int diff = 0;
        int countSame = 0;

        for (int i = 0; i < p1.length; i++) {
            if (p1[i] == p2[i]) {
                countSame++;
                if (countSame == p1.length) {
                    return false;
                }
            }

            if (p1[i] != p2[i]) {
                if (diff > 0) return false;
                diff = 1;

                if (p1[i] != p2[i + 1] || p2[i] != p1[i + 1]) {
                    return false;
                }

                i++;
            }
        }

        return true;
    }
}
