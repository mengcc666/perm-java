package entity;

import util.FileUtil;
import util.PermUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Snake {


    private int[] path;
    private final PermUtil permUtil = new PermUtil();

    private final FileUtil fileUtil = new FileUtil();

    private Problem problem = new Problem();

    private int predecessorIndex;
    private int headIndex;

    public int getHeadIndex() {
        return headIndex;
    }

    public void setHeadIndex(int headIndex) {
        this.headIndex = headIndex;
    }

    public void initPath(Problem problem) {
        int N = problem.getnP();
        this.path = new int[N];
        this.path[0] = 0;
        setHeadIndex(0);
    }

    public void start(Neighbor neighborObject,Problem problem) throws IOException {
        while (true) {
            System.out.println("[" + (headIndex + 1) + "/" + neighborObject.getTotal() + "]");
            if (headIndex + 1 == neighborObject.getTotal()) {
                permUtil.printArray(path, "Snake path");
                fileUtil.writePathToFile(this.path,problem);
                break;
            }
            predecessorIndex = -1;
            if (headIndex > 0) {
                predecessorIndex = headIndex - 1;
            }
            ArrayList<Integer> neighbors = neighborObject.getNeighbors(path[headIndex]);
            Collections.shuffle(neighbors);
//            permUtil.shuffle(neighbors);
            int nextNodeIndexInPath = -1;
            for (int i = 0; i < neighbors.size(); i++) {
                if (predecessorIndex >= 0) {
                    if (neighbors.get(i) == path[predecessorIndex]) {
                        continue;
                    }
                }
                nextNodeIndexInPath = findIndex(neighbors.get(i));
                if (nextNodeIndexInPath == -1) {
                    path[headIndex + 1] = neighbors.get(i);
                    headIndex++;
                    break;
                }
                if (i == neighbors.size() - 1) {
                    permUtil.rotateArray(path, nextNodeIndexInPath + 1, headIndex);
                }
            }
        }
    }

    private int findIndex(int neighbor) {
        for (int i = 0; i < headIndex; i++) {
            if (path[i] == neighbor) {
                return i;
            }
        }
        return -1;
    }
}
