package entity;

import util.PermUtil;

public class Problem {
    private int n;
    private int nP;

    private final PermUtil permUtil = new PermUtil();


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
