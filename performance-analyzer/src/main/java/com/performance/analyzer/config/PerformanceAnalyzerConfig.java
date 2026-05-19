package com.performance.analyzer.config;

import com.performance.analyzer.aspect.PerformanceMeasurementAspect;
import com.performance.analyzer.reporter.PerformanceReporter;

import java.util.concurrent.TimeUnit;

/**
 * Zentrale Konfigurationsklasse für das Performance Analyzer Module.
 * 
 * Diese Klasse bietet einen einfachen Einstiegspunkt zur Nutzung des Moduls
 * und ermöglicht den Zugriff auf den Aspect und Reporter.
 * 
 * @author Performance Analyzer Module
 */
public class PerformanceAnalyzerConfig {

    private static volatile PerformanceMeasurementAspect aspect;
    private static volatile PerformanceReporter reporter;

    /**
     * Initialisiert das Performance Analyzer Module.
     * 
     * Diese Methode muss einmalig beim Start der Anwendung aufgerufen werden,
     * um die benötigten Komponenten zu initialisieren.
     * 
     * @return Die PerformanceAnalyzerConfig Instanz
     */
    public static synchronized PerformanceAnalyzerConfig initialize() {
        if (aspect == null) {
            aspect = new PerformanceMeasurementAspect();
            reporter = new PerformanceReporter(aspect);
        }
        return new PerformanceAnalyzerConfig();
    }

    /**
     * Gibt den PerformanceMeasurementAspect zurück.
     * 
     * @return Der PerformanceMeasurementAspect
     */
    public static PerformanceMeasurementAspect getAspect() {
        if (aspect == null) {
            throw new IllegalStateException("PerformanceAnalyzer not initialized. Call initialize() first.");
        }
        return aspect;
    }

    /**
     * Gibt den PerformanceReporter zurück.
     * 
     * @return Der PerformanceReporter
     */
    public static PerformanceReporter getReporter() {
        if (reporter == null) {
            throw new IllegalStateException("PerformanceAnalyzer not initialized. Call initialize() first.");
        }
        return reporter;
    }

    /**
     * Gibt einen Performance-Report auf der Konsole aus.
     * 
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     */
    public void printReport(TimeUnit timeUnit) {
        if (reporter != null) {
            reporter.printConsoleReport(timeUnit);
        }
    }

    /**
     * Gibt einen Performance-Report auf der Konsole aus (Standard: Millisekunden).
     */
    public void printReport() {
        printReport(TimeUnit.MILLISECONDS);
    }

    /**
     * Generiert einen CSV-Report.
     * 
     * @param filePath Der Pfad zur Ausgabedatei
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     * @throws Exception Wenn ein Fehler beim Schreiben der Datei auftritt
     */
    public void generateCsvReport(String filePath, TimeUnit timeUnit) throws Exception {
        if (reporter != null) {
            reporter.generateCsvReport(filePath, timeUnit);
        }
    }

    /**
     * Generiert einen HTML-Report.
     * 
     * @param filePath Der Pfad zur Ausgabedatei
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     * @throws Exception Wenn ein Fehler beim Schreiben der Datei auftritt
     */
    public void generateHtmlReport(String filePath, TimeUnit timeUnit) throws Exception {
        if (reporter != null) {
            reporter.generateHtmlReport(filePath, timeUnit);
        }
    }

    /**
     * Gibt die Top-N langsamsten Methoden aus.
     * 
     * @param n Anzahl der Methoden die ausgegeben werden sollen
     * @param timeUnit Die Zeiteinheit für die Ausgabe
     */
    public void printTopSlowestMethods(int n, TimeUnit timeUnit) {
        if (reporter != null) {
            reporter.printTopSlowestMethods(n, timeUnit);
        }
    }

    /**
     * Setzt alle gesammelten Statistiken zurück.
     */
    public void resetStatistics() {
        if (aspect != null) {
            aspect.resetStatistics();
        }
    }
}
