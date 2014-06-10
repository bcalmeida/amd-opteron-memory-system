package extra;

import memory.MemorySystem;

import java.io.*;

public class Analysis {
    public static void main(String[] args) throws IOException {
        int[][] mHits1 = new int[7][7];
        int[][] mHits2 = new int[7][7];
        int[][] mHitsMain = new int[7][7];
        int[][] mTime = new int[7][7];
        int[][] mClocks = new int[4][7];
        int[][] mAssociative = new int[4][6];
        for (int i = 0, memL1 = 128; i < 7; i++, memL1*=2) {
            for (int j = 0, memL2 = 2048; j < 7; j++, memL2 *= 2) {
                BufferedReader bf = new BufferedReader(new FileReader("benchmark.txt"));
                MemorySystem mem = new MemorySystem(memL1, 2, 2, memL2, 16, 7, 140);
                for (String line = bf.readLine(); line != null; line = bf.readLine()) {
                    String[] aux = line.split(" ");
                    if (aux[1].equals("R")) {
                        mem.read(aux[0]);
                    } else {
                        mem.write(aux[0], "00000000");
                    }
                }
                mHits1[i][j] = mem.getL1Hit();
                mHits2[i][j] = mem.getL2Hit();
                mHitsMain[i][j] = mem.getMainMemHit();
                mTime[i][j] = mem.getTotalTime();
            }
        }
        for (int i = 0, timeCache1 = 1; i < 4; i++, timeCache1*=2) {
            for (int j = 0, timeCache2 = 1; j < 7; j++, timeCache2*=2) {
                if(timeCache2 == 8)
                    timeCache2--;
                BufferedReader bf = new BufferedReader(new FileReader("benchmark.txt"));
                MemorySystem mem = new MemorySystem(1024, 2, timeCache1, 16384, 16, timeCache2, 140);
                for (String line = bf.readLine(); line != null; line = bf.readLine()) {
                    String[] aux = line.split(" ");
                    if (aux[1].equals("R")) {
                        mem.read(aux[0]);
                    } else {
                        mem.write(aux[0], "00000000");
                    }
                }
                mClocks[i][j] = mem.getTotalTime();
            }
        }

        for (int i = 0, AssociativeCache1 = 1; i < 4; i++, AssociativeCache1 *= 2) {
            for (int j = 0, AssociativeCache2 = 2; j < 6; j++, AssociativeCache2 *= 2) {
                BufferedReader bf = new BufferedReader(new FileReader("benchmark.txt"));
                MemorySystem mem = new MemorySystem(1024, AssociativeCache1 , 2, 16384, AssociativeCache2, 7, 140);
                for (String line = bf.readLine(); line != null; line = bf.readLine()) {
                    String[] aux = line.split(" ");
                    if (aux[1].equals("R")) {
                        mem.read(aux[0]);
                    } else {
                        mem.write(aux[0], "00000000");
                    }
                }
                mAssociative[i][j] = mem.getL1Hit();
            }
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("hits1.txt"));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                bw.write(mHits1[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter("hits2.txt"));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                bw.write(mHits2[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter("hitsMain.txt"));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                bw.write(mHitsMain[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter("hitsTime.txt"));
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                bw.write(mTime[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter("Clocks.txt"));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                bw.write(mClocks[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();


        bw = new BufferedWriter(new FileWriter("Associative.txt"));
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                bw.write(mAssociative[i][j]+";");
            }
            bw.write("\n");
        }
        bw.close();
    }
}
