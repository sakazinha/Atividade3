package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.ui.UIUtils;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class DataComparisonChart extends ApplicationFrame {

    public DataComparisonChart(String title) {
        super(title);
    }

    // Método para criar um gráfico de linha com dois eixos Y e labels personalizados
    private JFreeChart criarGrafico(String title, int[] sim1, int[] sim2, String labelSim1, String labelSim2) {
        // Dataset para a primeira simulação
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (int i = 0; i < sim1.length; i++) {
            dataset1.addValue(sim1[i], labelSim1, "Ponto " + (i + 1));
        }

        // Dataset para a segunda simulação
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (int i = 0; i < sim2.length; i++) {
            dataset2.addValue(sim2[i], labelSim2, "Ponto " + (i + 1));
        }

        // Criação do gráfico para o primeiro dataset
        JFreeChart chart = ChartFactory.createLineChart(
                title,
                "", // Remove o título do eixo X nos gráficos individuais
                "Valores",
                dataset1,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Obtenção do plot e configuração do eixo Y à esquerda
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis1.setNumberFormatOverride(NumberFormat.getInstance());

        // Ajustar as margens superiores e inferiores dentro do gráfico para evitar cortes
        rangeAxis1.setUpperMargin(0.2);  // Aumenta a margem superior
        rangeAxis1.setLowerMargin(0.2);  // Aumenta a margem inferior

        // Configuração do renderizador para a primeira simulação
        LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setDefaultShapesVisible(true);
        renderer1.setDefaultItemLabelsVisible(true);
        renderer1.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        // Posicionar os rótulos da Simulação 1 acima da linha
        renderer1.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));

        plot.setRenderer(0, renderer1);

        // Criação do segundo eixo Y e adição do segundo dataset
        NumberAxis rangeAxis2 = new NumberAxis("Valores " + labelSim2);
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis2.setNumberFormatOverride(NumberFormat.getInstance());
        plot.setRangeAxis(1, rangeAxis2);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        // Ajustar as margens superiores e inferiores dentro do gráfico para evitar cortes
        rangeAxis2.setUpperMargin(0.2);  // Aumenta a margem superior
        rangeAxis2.setLowerMargin(0.2);  // Aumenta a margem inferior

        // Configuração do renderizador para a segunda simulação
        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setDefaultShapesVisible(true);
        renderer2.setDefaultItemLabelsVisible(true);
        renderer2.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());

        // Posicionar os rótulos da Simulação 2 abaixo da linha
        renderer2.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_CENTER));

        plot.setRenderer(1, renderer2);

        return chart;
    }

    // Método principal
    public static void main(String[] args) {
        // Configurar o frame do gráfico
        DataComparisonChart chart = new DataComparisonChart("Data Comparison Charts");

        // Configurar o layout para exibir os gráficos lado a lado
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2)); // 2 colunas, múltiplas linhas

        // Simulações para a Rota 1 - 1x
        int[] sim1_1x = {1, 87063, 148292, 246635, 340145, 431690};
        int[] sim2_1x = {0, 86427, 146752, 245906, 339730, 431275};

        // Criar o gráfico para a Velocidade 1x
        JFreeChart grafico1x = chart.criarGrafico("Comparação de Simulações - Velocidade 1x", sim1_1x, sim2_1x, "Simulação 1", "Simulação 2");
        ChartPanel panel1x = new ChartPanel(grafico1x);
        panel.add(panel1x);

        // Simulações para a Rota 1 - 2x
        int[] sim1_2x = {0, 44754, 75781, 124578, 172832, 218240};
        int[] sim2_2x = {2, 44400, 75571, 124156, 171335, 218677};

        // Criar o gráfico para a Velocidade 2x
        JFreeChart grafico2x = chart.criarGrafico("Comparação de Simulações - Velocidade 2x", sim1_2x, sim2_2x, "Simulação 1", "Simulação 2");
        ChartPanel panel2x = new ChartPanel(grafico2x);
        panel.add(panel2x);

        // Simulações para a Rota 1 - 3x
        int[] sim1_3x = {1, 30706, 50609, 83592, 116261, 147218};
        int[] sim2_3x = {0, 30400, 49978, 83616, 115455, 145988};

        // Criar o gráfico para a Velocidade 3x
        JFreeChart grafico3x = chart.criarGrafico("Comparação de Simulações - Velocidade 3x", sim1_3x, sim2_3x, "Simulação 1", "Simulação 2");
        ChartPanel panel3x = new ChartPanel(grafico3x);
        panel.add(panel3x);

        // Simulações para a Rota 1 - 4x
        int[] sim1_4x = {1, 21663, 38255, 61808, 87045, 110329};
        int[] sim2_4x = {0, 22880, 38033, 62346, 86924, 109801};

        // Criar o gráfico para a Velocidade 4x
        JFreeChart grafico4x = chart.criarGrafico("Comparação de Simulações - Velocidade 4x", sim1_4x, sim2_4x, "Simulação 1", "Simulação 2");
        ChartPanel panel4x = new ChartPanel(grafico4x);
        panel.add(panel4x);

        // Simulações para a Rota 1 - 5x
        int[] sim1_5x = {1, 17404, 29561, 49902, 69714, 88384};
        int[] sim2_5x = {0, 18107, 30176, 50438, 69740, 88788};

        // Criar o gráfico para a Velocidade 5x
        JFreeChart grafico5x = chart.criarGrafico("Comparação de Simulações - Velocidade 5x", sim1_5x, sim2_5x, "Simulação 1", "Simulação 2");
        ChartPanel panel5x = new ChartPanel(grafico5x);
        panel.add(panel5x);

        // Configurar e exibir a janela com os gráficos
        chart.setContentPane(panel);
        chart.pack();
        UIUtils.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}

