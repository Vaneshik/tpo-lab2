package ru.itmo.qa.lab2.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FunctionGraph extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(FunctionGraph.class.getName());
    private static final double TRIM_THRESHOLD = 100.0;

    private final JFreeChart chart;

    public FunctionGraph(String title, String seriesName, String csvFilePath, boolean trim) {
        super(title);
        XYSeries series = loadSeries(seriesName, csvFilePath, trim);
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        chart = ChartFactory.createXYLineChart(
                title, "x", "f(x)", dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        setContentPane(new ChartPanel(chart));
    }

    private XYSeries loadSeries(String name, String path, boolean trim) {
        XYSeries series = new XYSeries(name);
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                try {
                    double x = Double.parseDouble(parts[0]);
                    double y = Double.parseDouble(parts[1]);
                    if (!trim || (y >= -TRIM_THRESHOLD && y <= TRIM_THRESHOLD)) {
                        series.add(x, y);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Cannot read CSV: " + path, e);
        }
        return series;
    }

    public void saveAsPNG(String outputPath, int width, int height) throws IOException {
        ChartUtils.saveChartAsPNG(new File(outputPath), chart, width, height);
    }
}
