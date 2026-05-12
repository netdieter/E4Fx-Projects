package com.performance.analyzer;

import com.performance.analyzer.annotation.MeasurePerformance;
import com.performance.analyzer.config.PerformanceAnalyzerConfig;
import com.performance.analyzer.statistics.MethodStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den Performance Analyzer.
 */
public class PerformanceAnalyzerTest {

    @BeforeEach
    public void setUp() {
        // Initialisiere den Performance Analyzer vor jedem Test
        PerformanceAnalyzerConfig.initialize();
        // Reset statistics before each test
        PerformanceAnalyzerConfig.getAspect().resetStatistics();
    }

    /**
     * Testklasse mit annotierten Methoden für Tests
     */
    public static class TestService {

        @MeasurePerformance(name = "fastMethod")
        public void fastMethod() {
            // Sehr schnelle Operation
            int x = 1 + 1;
        }

        @MeasurePerformance(name = "slowMethod", warnThreshold = 50, timeUnit = TimeUnit.MILLISECONDS)
        public void slowMethod() throws InterruptedException {
            Thread.sleep(60); // 60ms - sollte Warnung auslösen
        }

        @MeasurePerformance(name = "verySlowMethod", errorThreshold = 100, timeUnit = TimeUnit.MILLISECONDS)
        public void verySlowMethod() throws InterruptedException {
            Thread.sleep(150); // 150ms - sollte Error auslösen
        }
    }

    @Test
    public void testFastMethodExecution() throws Exception {
        TestService service = new TestService();
        
        // Methode mehrmals aufrufen für bessere Statistiken
        for (int i = 0; i < 5; i++) {
            service.fastMethod();
        }

        // Überprüfen dass Statistiken gesammelt wurden
        MethodStatistics stats = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("fastMethod");

        assertNotNull(stats, "Statistiken sollten für fastMethod existieren");
        assertEquals(5, stats.getCount(), "Es sollten 5 Ausführungen gezählt worden sein");
        assertTrue(stats.getAverageNanos() > 0, "Durchschnittliche Zeit sollte > 0 sein");
    }

    @Test
    public void testSlowMethodWithWarning() throws Exception {
        TestService service = new TestService();
        
        service.slowMethod();

        MethodStatistics stats = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("slowMethod");

        assertNotNull(stats, "Statistiken sollten für slowMethod existieren");
        assertTrue(stats.getAverageNanos() >= TimeUnit.MILLISECONDS.toNanos(50), 
                   "Ausführungszeit sollte mindestens 50ms betragen");
    }

    @Test
    public void testVerySlowMethodWithError() throws Exception {
        TestService service = new TestService();
        
        service.verySlowMethod();

        MethodStatistics stats = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("verySlowMethod");

        assertNotNull(stats, "Statistiken sollten für verySlowMethod existieren");
        assertTrue(stats.getAverageNanos() >= TimeUnit.MILLISECONDS.toNanos(100), 
                   "Ausführungszeit sollte mindestens 100ms betragen");
    }

    @Test
    public void testStatisticsReset() throws Exception {
        TestService service = new TestService();
        
        service.fastMethod();
        service.fastMethod();

        MethodStatistics statsBefore = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("fastMethod");

        assertNotNull(statsBefore);
        assertEquals(2, statsBefore.getCount());

        // Statistics resetten
        PerformanceAnalyzerConfig.getAspect().resetStatistics();

        MethodStatistics statsAfter = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("fastMethod");

        assertNull(statsAfter, "Statistiken sollten nach Reset null sein");
    }

    @Test
    public void testMultipleExecutionsStatistics() throws Exception {
        TestService service = new TestService();
        
        // Methode 10 mal aufrufen
        for (int i = 0; i < 10; i++) {
            service.fastMethod();
        }

        MethodStatistics stats = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("fastMethod");

        assertNotNull(stats);
        assertEquals(10, stats.getCount());
        assertTrue(stats.getMinNanos() > 0);
        assertTrue(stats.getMaxNanos() > 0);
        assertTrue(stats.getAverageNanos() > 0);
        
        // Min sollte <= Average <= Max sein
        assertTrue(stats.getMinNanos() <= stats.getAverageNanos());
        assertTrue(stats.getAverageNanos() <= stats.getMaxNanos());
    }
    
    @Test
    public void testMethodStatisticsCalculation() throws Exception {
        TestService service = new TestService();
        
        // Mehrere Aufrufe für statistische Berechnungen
        for (int i = 0; i < 20; i++) {
            service.fastMethod();
        }

        MethodStatistics stats = PerformanceAnalyzerConfig.getAspect()
            .getStatisticsMap()
            .get("fastMethod");

        assertNotNull(stats);
        assertEquals(20, stats.getCount());
        
        // toString sollte funktionieren
        String statsString = stats.toString();
        assertNotNull(statsString);
        assertTrue(statsString.contains("count=20"));
    }
}
