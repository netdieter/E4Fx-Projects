package com.performance.analyzer.example;

import com.performance.analyzer.annotation.MeasurePerformance;
import com.performance.analyzer.config.PerformanceAnalyzerConfig;

import java.util.concurrent.TimeUnit;

/**
 * Beispielklasse zur Demonstration der Performance-Messung.
 * 
 * Diese Klasse zeigt verschiedene Anwendungsfälle der @MeasurePerformance Annotation:
 * - Messung auf Methodenebene
 * - Messung auf Klassenebene
 * - Verwendung von Schwellwerten
 * - Detaillierte Statistiken
 */
@MeasurePerformance(name = "CriticalService", detailedStats = true, timeUnit = TimeUnit.MILLISECONDS)
public class CriticalService {

    /**
     * Eine schnelle Methode
     */
    @MeasurePerformance(name = "fastOperation")
    public void fastOperation() {
        try {
            Thread.sleep(5); // 5ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Eine mittlere Methode mit Warn-Schwellwert
     */
    @MeasurePerformance(name = "mediumOperation", warnThreshold = 50, timeUnit = TimeUnit.MILLISECONDS)
    public void mediumOperation() {
        try {
            Thread.sleep(30); // 30ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Eine langsame Methode mit Error-Schwellwert
     */
    @MeasurePerformance(name = "slowOperation", warnThreshold = 100, errorThreshold = 200, timeUnit = TimeUnit.MILLISECONDS)
    public void slowOperation() {
        try {
            Thread.sleep(150); // 150ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Eine Methode die aufgrund des Klassen-Annotations auch gemessen wird
     */
    public void classLevelMeasuredOperation() {
        try {
            Thread.sleep(20); // 20ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Hauptmethode zur Demonstration
     */
    public static void main(String[] args) throws Exception {
        // Performance Analyzer initialisieren
        PerformanceAnalyzerConfig.initialize();

        CriticalService service = new CriticalService();

        System.out.println("Starting performance measurement demo...\n");

        // Mehrere Aufrufe für aussagekräftige Statistiken
        for (int i = 0; i < 10; i++) {
            System.out.println("--- Iteration " + (i + 1) + " ---");
            service.fastOperation();
            service.mediumOperation();
            service.slowOperation();
            service.classLevelMeasuredOperation();
            Thread.sleep(100); // Pause zwischen Iterationen
        }

        // Report ausgeben
        System.out.println("\n");
        PerformanceAnalyzerConfig.getReporter().printConsoleReport(TimeUnit.MILLISECONDS);

        // Top 3 langsamste Methoden
        PerformanceAnalyzerConfig.getReporter().printTopSlowestMethods(3, TimeUnit.MILLISECONDS);

        // CSV Report generieren
        try {
            PerformanceAnalyzerConfig.getReporter().generateCsvReport("performance-report.csv", TimeUnit.MILLISECONDS);
            System.out.println("CSV report generated: performance-report.csv");
        } catch (Exception e) {
            System.err.println("Error generating CSV report: " + e.getMessage());
        }

        // HTML Report generieren
        try {
            PerformanceAnalyzerConfig.getReporter().generateHtmlReport("performance-report.html", TimeUnit.MILLISECONDS);
            System.out.println("HTML report generated: performance-report.html");
        } catch (Exception e) {
            System.err.println("Error generating HTML report: " + e.getMessage());
        }
    }
}
