package com.performance.analyzer.aspect;

import com.performance.analyzer.annotation.MeasurePerformance;
import com.performance.analyzer.statistics.MethodStatistics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aspect zur Performance-Messung von Methoden, die mit @MeasurePerformance annotiert sind.
 * 
 * Dieser Aspect fängt den Aufruf von annotierten Methoden ab, misst die Ausführungszeit
 * und gibt statistische Informationen aus.
 * 
 * @author Performance Analyzer Module
 */
@Aspect
public class PerformanceMeasurementAspect {

    private static final Logger LOGGER = Logger.getLogger(PerformanceMeasurementAspect.class.getName());
    
    // Statistik-Speicher für jede Methode
    private final ConcurrentHashMap<String, MethodStatistics> statisticsMap = new ConcurrentHashMap<>();

    /**
     * Pointcut für alle Methoden, die mit @MeasurePerformance annotiert sind.
     */
    @Pointcut("@annotation(com.performance.analyzer.annotation.MeasurePerformance)")
    public void measurePerformanceMethod() {}

    /**
     * Pointcut für Klassen, die mit @MeasurePerformance annotiert sind.
     * Alle öffentlichen Methoden der Klasse werden gemessen.
     */
    @Pointcut("@within(com.performance.analyzer.annotation.MeasurePerformance) && execution(public * *(..))")
    public void measurePerformanceClass() {}

    /**
     * Around-Advice zur Messung der Performance.
     * 
     * @param joinPoint Der JoinPoint der ausgeführten Methode
     * @return Das Ergebnis der Methodenaufrufs
     * @throws Throwable Falls die ursprüngliche Methode eine Exception wirft
     */
    @Around("measurePerformanceMethod() || measurePerformanceClass()")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        
        // Prüfen ob es sich um eine Methodenausführung handelt (nicht StaticInitializer etc.)
        if (!(signature instanceof MethodSignature)) {
            return joinPoint.proceed();
        }
        
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        
        // Annotation von der Methode oder der Klasse holen
        MeasurePerformance measurePerf = method.getAnnotation(MeasurePerformance.class);
        if (measurePerf == null) {
            // Wenn die Methode keine Annotation hat, aber die Klasse, hole sie von der Klasse
            measurePerf = method.getDeclaringClass().getAnnotation(MeasurePerformance.class);
        }
        
        if (measurePerf == null) {
            return joinPoint.proceed();
        }

        String methodName = getMethodName(joinPoint, measurePerf);
        long startTime = System.nanoTime();
        
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            // Statistik aktualisieren
            updateStatistics(methodName, duration, measurePerf);
            
            // Ergebnisse loggen wenn gewünscht
            if (measurePerf.logResults()) {
                logPerformance(methodName, duration, measurePerf);
            }
        }
    }

    /**
     * Ermittelt den Namen für die Performance-Messung.
     */
    private String getMethodName(ProceedingJoinPoint joinPoint, MeasurePerformance measurePerf) {
        if (!measurePerf.name().isEmpty()) {
            return measurePerf.name();
        }
        
        Signature signature = joinPoint.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    /**
     * Aktualisiert die Statistiken für eine Methode.
     */
    private void updateStatistics(String methodName, long durationNanos, MeasurePerformance measurePerf) {
        MethodStatistics stats = statisticsMap.computeIfAbsent(methodName, k -> new MethodStatistics());
        stats.recordExecution(durationNanos);
    }

    /**
     * Loggt die Performance-Ergebnisse.
     */
    private void logPerformance(String methodName, long durationNanos, MeasurePerformance measurePerf) {
        TimeUnit timeUnit = measurePerf.timeUnit();
        double duration = (double) durationNanos / TimeUnit.NANOSECONDS.toNanos(1);
        double convertedDuration = TimeUnit.NANOSECONDS.convert((long) duration, TimeUnit.NANOSECONDS) / 
                                   (double) timeUnit.toNanos(1);
        
        StringBuilder message = new StringBuilder();
        message.append(String.format("[PERFORMANCE] %s: %.3f %s", 
                                    methodName, convertedDuration, timeUnit.toString().toLowerCase()));
        
        // Prüfen ob Warn- oder Error-Schwellwert überschritten wurde
        Level logLevel = Level.INFO;
        
        if (measurePerf.errorThreshold() > 0 && durationNanos >= TimeUnit.NANOSECONDS.convert(measurePerf.errorThreshold(), timeUnit)) {
            message.append(String.format(" [ERROR: Überschreitet Error-Schwellwert von %d %s]", 
                                        measurePerf.errorThreshold(), timeUnit.toString().toLowerCase()));
            logLevel = Level.SEVERE;
        } else if (measurePerf.warnThreshold() > 0 && durationNanos >= TimeUnit.NANOSECONDS.convert(measurePerf.warnThreshold(), timeUnit)) {
            message.append(String.format(" [WARN: Überschreitet Warn-Schwellwert von %d %s]", 
                                        measurePerf.warnThreshold(), timeUnit.toString().toLowerCase()));
            logLevel = Level.WARNING;
        }
        
        LOGGER.log(logLevel, message.toString());
        
        // Detaillierte Statistiken ausgeben wenn gewünscht
        if (measurePerf.detailedStats()) {
            logDetailedStatistics(methodName, timeUnit);
        }
    }

    /**
     * Loggt detaillierte Statistiken für eine Methode.
     */
    private void logDetailedStatistics(String methodName, TimeUnit timeUnit) {
        MethodStatistics stats = statisticsMap.get(methodName);
        if (stats != null) {
            long count = stats.getCount();
            double avgNanos = stats.getAverageNanos();
            long minNanos = stats.getMinNanos();
            long maxNanos = stats.getMaxNanos();
            double stdDevNanos = stats.getStandardDeviationNanos();
            
            String detailedMessage = String.format(
                "[STATS] %s - Count: %d, Avg: %.3f %s, Min: %.3f %s, Max: %.3f %s, StdDev: %.3f %s",
                methodName,
                count,
                TimeUnit.NANOSECONDS.toNanos(1) > 0 ? avgNanos / timeUnit.toNanos(1) : avgNanos, timeUnit.toString().toLowerCase(),
                TimeUnit.NANOSECONDS.toNanos(1) > 0 ? (double) minNanos / timeUnit.toNanos(1) : minNanos, timeUnit.toString().toLowerCase(),
                TimeUnit.NANOSECONDS.toNanos(1) > 0 ? (double) maxNanos / timeUnit.toNanos(1) : maxNanos, timeUnit.toString().toLowerCase(),
                TimeUnit.NANOSECONDS.toNanos(1) > 0 ? stdDevNanos / timeUnit.toNanos(1) : stdDevNanos, timeUnit.toString().toLowerCase()
            );
            
            LOGGER.info(detailedMessage);
        }
    }

    /**
     * Gibt die gesammelten Statistiken für alle gemessenen Methoden zurück.
     * 
     * @return ConcurrentHashMap mit allen Statistiken
     */
    public ConcurrentHashMap<String, MethodStatistics> getStatisticsMap() {
        return statisticsMap;
    }

    /**
     * Setzt alle gesammelten Statistiken zurück.
     */
    public void resetStatistics() {
        statisticsMap.clear();
    }
}
