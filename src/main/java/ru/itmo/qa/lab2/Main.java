package ru.itmo.qa.lab2;

import ru.itmo.qa.lab2.function.AbstractFunction;
import ru.itmo.qa.lab2.log.BaseNLogarithm;
import ru.itmo.qa.lab2.log.NaturalLogarithm;
import ru.itmo.qa.lab2.trig.*;
import ru.itmo.qa.lab2.util.CSVGraphWriter;
import ru.itmo.qa.lab2.util.FunctionGraph;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.math.RoundingMode.HALF_EVEN;

public class Main {

    private static String outputDir = System.getProperty("user.dir") + File.separator + "plots" + File.separator;

    private static final BigDecimal PRECISION = new BigDecimal("0.0000001");
    private static final BigDecimal POSITIVE_END = new BigDecimal(10).setScale(7, HALF_EVEN);
    private static final BigDecimal NEGATIVE_END = POSITIVE_END.negate();
    private static final BigDecimal STEP = new BigDecimal("0.01");

    public static void main(String[] args) {
        try {
            new File(outputDir).mkdirs();
            generateAllCSV();
            saveAllPNG();
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static void setOutputDir(String path) {
        outputDir = path.endsWith(File.separator) ? path : path + File.separator;
    }

    private static void generateAllCSV() throws IOException {
        new CSVGraphWriter(new Sine(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new Cosine(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new Secant(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new Cosecant(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new Tangent(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new Cotangent(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new NaturalLogarithm(), outputDir).write(new BigDecimal("0.01"), POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new BaseNLogarithm(3), outputDir).write(new BigDecimal("0.01"), POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new BaseNLogarithm(5), outputDir).write(new BigDecimal("0.01"), POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new BaseNLogarithm(10), outputDir).write(new BigDecimal("0.01"), POSITIVE_END, STEP, PRECISION);
        new CSVGraphWriter(new EquationSystem(), outputDir).write(NEGATIVE_END, POSITIVE_END, STEP, PRECISION);
    }

    private static void saveAllPNG() throws IOException {
        List<String> trimmed = List.of("tan(x)", "cot(x)", "sec(x)", "csc(x)", "f(x)");

        Map<AbstractFunction, String> functions = new LinkedHashMap<>();
        functions.put(new Sine(), "sin(x)");
        functions.put(new Cosine(), "cos(x)");
        functions.put(new Secant(), "sec(x)");
        functions.put(new Cosecant(), "csc(x)");
        functions.put(new Tangent(), "tan(x)");
        functions.put(new Cotangent(), "cot(x)");
        functions.put(new NaturalLogarithm(), "ln(x)");
        functions.put(new BaseNLogarithm(3), "log3(x)");
        functions.put(new BaseNLogarithm(5), "log5(x)");
        functions.put(new BaseNLogarithm(10), "log10(x)");
        functions.put(new EquationSystem(), "f(x)");

        for (Map.Entry<AbstractFunction, String> entry : functions.entrySet()) {
            String csvPath = outputDir + entry.getKey().getClass().getSimpleName() + ".csv";
            String pngPath = outputDir + entry.getKey().getClass().getSimpleName() + ".png";
            FunctionGraph graph = new FunctionGraph(
                    "График: " + entry.getValue(),
                    entry.getValue(),
                    csvPath,
                    trimmed.contains(entry.getValue()));
            graph.saveAsPNG(pngPath, 800, 600);
            System.out.println("Saved: " + pngPath);
        }
    }
}
