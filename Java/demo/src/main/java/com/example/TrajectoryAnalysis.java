package com.example;

public class TrajectoryAnalysis {

    public static void main(String[] args) {
        // Dados da tabela fornecida
        double[][] data = {
            {86100, 86183, 44066, 43861, 30730, 30091, 21829, 22256, 17968, 17533}, // 1 - 2 (ms)
            {90566, 90407, 45103, 45282, 29330, 29819, 23021, 22208, 17426, 17912}, // 2 - 3 (ms)
            {81149, 81290, 40646, 40772, 28046, 28023, 20537, 21099, 16929, 17383}, // 3 - 4 (ms)
            {76017, 76789, 38875, 39196, 26141, 25895, 19462, 19256, 16106, 15187}, // 4 - 5 (ms)
            {92085, 91537, 46117, 46375, 32038, 31190, 23508, 23298, 19369, 18874}  // 5 - 6 (ms)
        };        

        // Calculando os valores para cada coluna (1-2, 2-3, etc.)
        for (int i = 0; i < data.length; i++) {
            double[] values = data[i];
            double mean = calculateMean(values);
            double stdDev = calculateStandardDeviation(values, mean);
            double bias = calculateBias(values, mean);
            double precision = stdDev; // In this context, precision is often represented by the standard deviation
            double uncertainty = calculateUncertainty(stdDev, values.length);

            System.out.println("Segmento " + (i + 1) + " - " + (i + 2) + " (ms):");
            System.out.println("Média: " + mean);
            System.out.println("Desvio Padrão: " + stdDev);
            System.out.println("Polarização (Bias): " + bias);
            System.out.println("Precisão: " + precision);
            System.out.println("Incerteza: " + uncertainty);
            System.out.println();
        }
    }

    // Método para calcular a média
    public static double calculateMean(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    // Método para calcular o desvio padrão
    public static double calculateStandardDeviation(double[] values, double mean) {
        double sum = 0;
        for (double value : values) {
            sum += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sum / (values.length - 1));
    }

    // Método para calcular a polarização (bias)
    public static double calculateBias(double[] values, double mean) {
        double trueValue = mean; // Assumindo que o valor verdadeiro é a média
        return trueValue - mean;
    }

    // Método para calcular a incerteza
    public static double calculateUncertainty(double stdDev, int n) {
        return stdDev / Math.sqrt(n);
    }
}
