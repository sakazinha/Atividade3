package com.example;

import org.ejml.simple.SimpleMatrix;

public class Reconciliation {

    private SimpleMatrix reconciledFlow;
    private SimpleMatrix adjustment;
    private SimpleMatrix rawMeasurement;
    private SimpleMatrix standardDeviation;
    private SimpleMatrix varianceMatrix;
    private SimpleMatrix incidenceMatrix;
    private SimpleMatrix varianceMatrixReconciled;

    public Reconciliation(double[] y, double[] v, double[][] A) {
        // Inicializar os dados
        this.rawMeasurement = new SimpleMatrix(y.length, 1, true, y);
        this.standardDeviation = new SimpleMatrix(v.length, 1, true, v);
        this.varianceMatrix = new SimpleMatrix(v.length, v.length);
        this.incidenceMatrix = new SimpleMatrix(A);

        // Calcular a matriz de variância
        for (int i = 0; i < v.length; i++) {
            this.varianceMatrix.set(i, i, v[i] * v[i]);
        }

        // Calcular A * V * A^T
        SimpleMatrix AVAT = incidenceMatrix.mult(varianceMatrix).mult(incidenceMatrix.transpose());

        // Calcular o inverso de A * V * A^T
        SimpleMatrix AVATInverse = AVAT.invert();

        // Calcular o ajuste
        this.adjustment = varianceMatrix.mult(incidenceMatrix.transpose()).mult(AVATInverse).mult(incidenceMatrix).mult(rawMeasurement);

        // Calcular o fluxo reconciliado (y_hat)
        this.reconciledFlow = rawMeasurement.minus(adjustment);

        // Calcular a matriz de variância reconciliada (V_hat)
        this.varianceMatrixReconciled = varianceMatrix.minus(varianceMatrix.mult(incidenceMatrix.transpose()).mult(AVATInverse).mult(incidenceMatrix).mult(varianceMatrix));
    }

    // Método para obter o desvio padrão reconciliado
    public double[] getReconciledStandardDeviation() {
        int n = varianceMatrixReconciled.numRows();
        double[] reconciledStdDev = new double[n];
        for (int i = 0; i < n; i++) {
            reconciledStdDev[i] = Math.sqrt(varianceMatrixReconciled.get(i, i));
        }
        return reconciledStdDev;
    }

    // Getter para o fluxo reconciliado
    public SimpleMatrix getReconciledFlow() {
        return reconciledFlow;
    }

    // Getter para o ajuste
    public SimpleMatrix getAdjustment() {
        return adjustment;
    }

    public SimpleMatrix getRawMeasurement() {
        return rawMeasurement;
    }

    public SimpleMatrix getStandardDeviation() {
        return standardDeviation;
    }

    public SimpleMatrix getVarianceMatrix() {
        return varianceMatrix;
    }

    public SimpleMatrix getIncidenceMatrix() {
        return incidenceMatrix;
    }

    public void printMatrix(SimpleMatrix matrix) {
        matrix.print();
    }

    public void printArray(SimpleMatrix vector) {
        vector.print();
    }
}
