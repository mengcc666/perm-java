package util;

import entity.Problem;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {


    public void writePathToFile(int[] path,Problem problem) {
        int N = problem.getnP();
        int n = problem.getN();
        FileWriter fileWriter=null;
        try {
            fileWriter = new FileWriter(n + ".txt");
            fileWriter.write("Path: [");
            for (int i = 0; i < N; i++) {
                fileWriter.write(Integer.toString(path[i]));
                if (i != N - 1) {
                    fileWriter.write("-");
                }
            }
            fileWriter.write("]\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
