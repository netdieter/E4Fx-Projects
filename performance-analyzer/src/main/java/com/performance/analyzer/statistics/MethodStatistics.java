package com.performance.analyzer.statistics;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Klasse zur Sammlung und Berechnung von Performance-Statistiken für eine Methode.
 * 
 * Diese Klasse ist thread-safe und sammelt Daten über mehrere Ausführungen hinweg,
 * um statistische Kennzahlen wie Durchschnitt, Minimum, Maximum und Standardabweichung zu berechnen.
 * 
 * @author Performance Analyzer Module
 */
public class MethodStatistics {

    private final LongAdder count = new LongAdder();
    private final AtomicLong totalNanos = new AtomicLong(0);
    private final AtomicLong minNanos = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxNanos = new AtomicLong(Long.MIN_VALUE);
    
    // Für die Berechnung der Standardabweichung verwenden wir die Welford-Methode
    private final AtomicLong m2Nanos = new AtomicLong(0);
    private final AtomicLong meanNanos = new AtomicLong(0);

    /**
     * Zeichnet eine neue Ausführung mit der gegebenen Dauer auf.
     * 
     * @param durationNanos Die Dauer der Ausführung in Nanosekunden
     */
    public void recordExecution(long durationNanos) {
        count.increment();
        
        // Total für Durchschnittsberechnung
        totalNanos.addAndGet(durationNanos);
        
        // Minimum aktualisieren
        minNanos.updateAndGet(current -> Math.min(current, durationNanos));
        
        // Maximum aktualisieren
        maxNanos.updateAndGet(current -> Math.max(current, durationNanos));
        
        // Welford's online algorithm for variance calculation
        long currentCount = count.sum();
        long prevMean = meanNanos.get();
        long newMean = prevMean + (int) ((durationNanos - prevMean) / currentCount);
        meanNanos.set(newMean);
        
        long delta = durationNanos - prevMean;
        long delta2 = durationNanos - newMean;
        m2Nanos.addAndGet(delta * delta2);
    }

    /**
     * Gibt die Anzahl der aufgezeichneten Ausführungen zurück.
     * 
     * @return Anzahl der Ausführungen
     */
    public long getCount() {
        return count.sum();
    }

    /**
     * Gibt die durchschnittliche Ausführungszeit in Nanosekunden zurück.
     * 
     * @return Durchschnittliche Ausführungszeit in Nanosekunden
     */
    public double getAverageNanos() {
        long c = count.sum();
        if (c == 0) {
            return 0.0;
        }
        return (double) totalNanos.get() / c;
    }

    /**
     * Gibt die minimale Ausführungszeit in Nanosekunden zurück.
     * 
     * @return Minimale Ausführungszeit in Nanosekunden
     */
    public long getMinNanos() {
        long min = minNanos.get();
        return (min == Long.MAX_VALUE) ? 0 : min;
    }

    /**
     * Gibt die maximale Ausführungszeit in Nanosekunden zurück.
     * 
     * @return Maximale Ausführungszeit in Nanosekunden
     */
    public long getMaxNanos() {
        long max = maxNanos.get();
        return (max == Long.MIN_VALUE) ? 0 : max;
    }

    /**
     * Gibt die Standardabweichung der Ausführungszeiten in Nanosekunden zurück.
     * 
     * @return Standardabweichung in Nanosekunden
     */
    public double getStandardDeviationNanos() {
        long c = count.sum();
        if (c < 2) {
            return 0.0;
        }
        return Math.sqrt((double) m2Nanos.get() / (c - 1));
    }

    /**
     * Setzt alle Statistiken zurück.
     */
    public void reset() {
        count.reset();
        totalNanos.set(0);
        minNanos.set(Long.MAX_VALUE);
        maxNanos.set(Long.MIN_VALUE);
        m2Nanos.set(0);
        meanNanos.set(0);
    }

    /**
     * Gibt eine Zusammenfassung der Statistiken als String zurück.
     * 
     * @return String-Repräsentation der Statistiken
     */
    @Override
    public String toString() {
        return String.format(
            "MethodStatistics{count=%d, avg=%.3f ns, min=%d ns, max=%d ns, stddev=%.3f ns}",
            getCount(),
            getAverageNanos(),
            getMinNanos(),
            getMaxNanos(),
            getStandardDeviationNanos()
        );
    }
}
