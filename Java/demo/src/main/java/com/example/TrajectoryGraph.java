package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TrajectoryGraph extends ApplicationFrame {

    public TrajectoryGraph(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Simulação", "Tempo (ms)",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Dados dos pontos de 1-2, 2-3, 3-4, 4-5, 5-6
        int[] p1_p2 = {86100, 86183, 44066, 43861, 30730, 30091, 21829, 22256, 17968, 17533};
        int[] p2_p3 = {90566, 90407, 45103, 45282, 29330, 29819, 23021, 22208, 17426, 17912};
        int[] p3_p4 = {81149, 81290, 40646, 40772, 28046, 28023, 20537, 21099, 16929, 17383};
        int[] p4_p5 = {76017, 76789, 38875, 39196, 26141, 25895, 19462, 19256, 16106, 15187};
        int[] p5_p6 = {92085, 91537, 46117, 46375, 32038, 31190, 23508, 23298, 19369, 18874};        

        // Adicionando dados ao dataset
        for (int i = 0; i < p1_p2.length; i++) {
            String simulation = "Simulação " + (i + 1);
            dataset.addValue(p1_p2[i], "Ponto 1-2", simulation);
            dataset.addValue(p2_p3[i], "Ponto 2-3", simulation);
            dataset.addValue(p3_p4[i], "Ponto 3-4", simulation);
            dataset.addValue(p4_p5[i], "Ponto 4-5", simulation);
            dataset.addValue(p5_p6[i], "Ponto 5-6", simulation);
        }

        return dataset;
    }

    public static void main(String[] args) {
        TrajectoryGraph chart = new TrajectoryGraph(
                "Análise de Trajetória",
                "Tempo Decorrido por Ponto");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
