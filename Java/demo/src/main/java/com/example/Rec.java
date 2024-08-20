package com.example;

public class Rec {

    public static void main(String[] args) {

        double[] y = new double[] { 40061.7, 41107.4, 37587.4, 35292.4, 42439.1 }; // Medições
        double[] v = new double[] { 26052.96, 27807.41, 24511.02, 23239.58, 27791.63 }; // Desvios padrão

        double[][] A = new double[][] {
                { 1, -1, 0, 0, 0 }, // Relação entre os pontos 1-2 e 2-3
                { 0, 1, -1, 0, 0 }, // Relação entre os pontos 2-3 e 3-4
                { 0, 0, 1, -1, 0 }, // Relação entre os pontos 3-4 e 4-5
                { 0, 0, 0, 1, -1 }  // Relação entre os pontos 4-5 e 5-6
        };

        Reconciliation rec = new Reconciliation(y, v, A);

        // Imprimir os vetores e a matriz
        System.out.println("Raw Measurement (y):");
        rec.printArray(rec.getRawMeasurement());

        System.out.println("\nStandard Deviation (v):");
        rec.printArray(rec.getStandardDeviation());

        System.out.println("\nIncidence Matrix (A):");
        rec.printMatrix(rec.getIncidenceMatrix());

        System.out.println("\nVariance Matrix (V):");
        rec.printMatrix(rec.getVarianceMatrix());

        System.out.println("\nAdjustment:");
        rec.printArray(rec.getAdjustment());

        System.out.println("\nReconciled Flow (Y_hat):");
        rec.printArray(rec.getReconciledFlow());

        System.out.println("\nReconciled Standard Deviation:");
        double[] reconciledStdDev = rec.getReconciledStandardDeviation();
        for (double stddev : reconciledStdDev) {
            System.out.println(stddev);
        }
    }
}

