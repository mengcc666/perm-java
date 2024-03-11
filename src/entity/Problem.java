package entity;

import util.PermUtil;

public class Problem {
    private int n;
    private int nP;

    private int maxTimeForMatching;

    private final PermUtil permUtil = new PermUtil();

    public int getMaxTimeForMatching() {
        return maxTimeForMatching;
    }

    public void setMaxTimeForMatching(int maxTimeForMatching) {
        this.maxTimeForMatching = maxTimeForMatching;
    }

    public int getnP() {
        return nP;
    }

    public void setnP(int nP) {
        this.nP = nP;
    }


    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        this.nP=permUtil.factorial(n);
    }
}
