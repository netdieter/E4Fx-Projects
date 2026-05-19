package com.performance.analyzer.reporter;

import com.performance.analyzer.aspect.PerformanceMeasurementAspect;
import com.performance.analyzer.statistics.MethodStatistics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reporter-Klasse zur Generierung von Performance-Reports.
 * 
 * Diese Klasse ermöglicht es, die gesammelten Performance-Daten in verschiedenen
 * Formaten auszugeben, z.B. als Console-Report, CSV oder HTML.
 * 
 * @author Performance Analyzer Module
 */
public class PerformanceReporter {

    private static final Logger LOGGER = Logger.getLogger(PerformanceReporter.class.getName());
    private final PerformanceMeasurementAspect aspect;

    public PerformanceReporter(PerformanceMeasurementAspect aspect) {
        this.aspect = aspect;
    }

    /**
     * Gibt einen Performance-Report auf der Konsole aus.
     * 
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     */
    public void printConsoleReport(TimeUnit timeUnit) {
        System.out.println("=".repeat(80));
        System.out.println("PERFORMANCE ANALYSIS REPORT");
        System.out.println("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("=".repeat(80));
        System.out.println();

        ConcurrentHashMap<String, MethodStatistics> statistics = aspect.getStatisticsMap();
        
        if (statistics.isEmpty()) {
            System.out.println("No performance data collected yet.");
            return;
        }

        printHeader(timeUnit);
        
        statistics.entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue().getAverageNanos(), a.getValue().getAverageNanos()))
            .forEach(entry -> printMethodStats(entry.getKey(), entry.getValue(), timeUnit));
        
        System.out.println("=".repeat(80));
        System.out.println();
    }

    /**
     * Generiert einen CSV-Report und speichert ihn in einer Datei.
     * 
     * @param filePath Der Pfad zur Ausgabedatei
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt
     */
    public void generateCsvReport(String filePath, TimeUnit timeUnit) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // CSV Header
            writer.println("Method Name,Count,Average,Min,Max,StdDev,TimeUnit");
            
            ConcurrentHashMap<String, MethodStatistics> statistics = aspect.getStatisticsMap();
            
            statistics.forEach((methodName, stats) -> {
                double factor = 1.0 / timeUnit.toNanos(1);
                writer.printf("%s,%d,%.6f,%.6f,%.6f,%.6f,%s%n",
                    methodName,
                    stats.getCount(),
                    stats.getAverageNanos() * factor,
                    stats.getMinNanos() * factor,
                    stats.getMaxNanos() * factor,
                    stats.getStandardDeviationNanos() * factor,
                    timeUnit.toString().toLowerCase()
                );
            });
        }
        
        LOGGER.info("CSV report generated: " + filePath);
    }

    /**
     * Generiert einen HTML-Report und speichert ihn in einer Datei.
     * 
     * @param filePath Der Pfad zur Ausgabedatei
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt
     */
    public void generateHtmlReport(String filePath, TimeUnit timeUnit) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang=\"en\">");
            writer.println("<head>");
            writer.println("    <meta charset=\"UTF-8\">");
            writer.println("    <title>Performance Analysis Report</title>");
            writer.println("    <style>");
            writer.println("        body { font-family: Arial, sans-serif; margin: 20px; }");
            writer.println("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
            writer.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            writer.println("        th { background-color: #4CAF50; color: white; }");
            writer.println("        tr:nth-child(even) { background-color: #f2f2f2; }");
            writer.println("        tr:hover { background-color: #ddd; }");
            writer.println("        h1 { color: #333; }");
            writer.println("        .timestamp { color: #666; font-size: 0.9em; }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <h1>Performance Analysis Report</h1>");
            writer.println("    <p class=\"timestamp\">Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>");
            
            ConcurrentHashMap<String, MethodStatistics> statistics = aspect.getStatisticsMap();
            
            if (statistics.isEmpty()) {
                writer.println("    <p>No performance data collected yet.</p>");
            } else {
                writer.println("    <table>");
                writer.println("        <thead>");
                writer.println("            <tr>");
                writer.println("                <th>Method</th>");
                writer.println("                <th>Count</th>");
                writer.println("                <th>Average (" + timeUnit.toString().toLowerCase() + ")</th>");
                writer.println("                <th>Min (" + timeUnit.toString().toLowerCase() + ")</th>");
                writer.println("                <th>Max (" + timeUnit.toString().toLowerCase() + ")</th>");
                writer.println("                <th>StdDev (" + timeUnit.toString().toLowerCase() + ")</th>");
                writer.println("            </tr>");
                writer.println("        </thead>");
                writer.println("        <tbody>");
                
                double factor = 1.0 / timeUnit.toNanos(1);
                statistics.forEach((methodName, stats) -> {
                    writer.println("            <tr>");
                    writer.printf("                <td>%s</td>%n", escapeHtml(methodName));
                    writer.printf("                <td>%d</td>%n", stats.getCount());
                    writer.printf("                <td>%.6f</td>%n", stats.getAverageNanos() * factor);
                    writer.printf("                <td>%.6f</td>%n", stats.getMinNanos() * factor);
                    writer.printf("                <td>%.6f</td>%n", stats.getMaxNanos() * factor);
                    writer.printf("                <td>%.6f</td>%n", stats.getStandardDeviationNanos() * factor);
                    writer.println("            </tr>");
                });
                
                writer.println("        </tbody>");
                writer.println("    </table>");
            }
            
            writer.println("</body>");
            writer.println("</html>");
        }
        
        LOGGER.info("HTML report generated: " + filePath);
    }

    /**
     * Gibt die Top-N langsamsten Methoden aus.
     * 
     * @param n Anzahl der Methoden die ausgegeben werden sollen
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     */
    public void printTopSlowestMethods(int n, TimeUnit timeUnit) {
        System.out.println("\nTop " + n + " Slowest Methods (by average execution time):");
        System.out.println("-".repeat(60));
        
        ConcurrentHashMap<String, MethodStatistics> statistics = aspect.getStatisticsMap();
        
        statistics.entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue().getAverageNanos(), a.getValue().getAverageNanos()))
            .limit(n)
            .forEach(entry -> {
                double avg = entry.getValue().getAverageNanos() / (double) timeUnit.toNanos(1);
                System.out.printf("%s: %.3f %s%n", entry.getKey(), avg, timeUnit.toString().toLowerCase());
            });
        
        System.out.println();
    }

    private void printHeader(TimeUnit timeUnit) {
        System.out.printf("%-60s %8s %12s %12s %12s %12s%n",
            "Method", "Count", "Avg (" + timeUnit + ")", "Min (" + timeUnit + ")", "Max (" + timeUnit + ")", "StdDev (" + timeUnit + ")");
        System.out.println("-".repeat(120));
    }

    private void printMethodStats(String methodName, MethodStatistics stats, TimeUnit timeUnit) {
        double factor = 1.0 / timeUnit.toNanos(1);
        System.out.printf("%-60s %8d %12.6f %12.6f %12.6f %12.6f%n",
            truncate(methodName, 60),
            stats.getCount(),
            stats.getAverageNanos() * factor,
            stats.getMinNanos() * factor,
            stats.getMaxNanos() * factor,
            stats.getStandardDeviationNanos() * factor);
    }

    private String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return "..." + str.substring(str.length() - maxLength + 3);
    }

    private String escapeHtml(String str) {
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}
