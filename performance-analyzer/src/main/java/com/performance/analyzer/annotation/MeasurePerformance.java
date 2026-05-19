package com.performance.analyzer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation zur Performance-Messung von Methoden.
 * 
 * Diese Annotation kann auf Methoden angewendet werden, um deren AusfÃ¼hrungszeit zu messen.
 * Die Messergebnisse werden automatisch geloggt.
 * 
 * @author Performance Analyzer Module
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MeasurePerformance {
    
    /**
     * Optionale Bezeichnung fÃ¼r die Messung.
     * Wird verwendet, um die Messung im Log zu identifizieren.
     * Wenn leer, wird der Methodennamen verwendet.
     */
    String name() default "";
    
    /**
     * Zeiteinheit fÃ¼r die Ausgabe der Messergebnisse.
     * Standard ist Millisekunden.
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
    
    /**
     * Schwellwert, ab dem eine Warnung ausgegeben wird.
     * Wenn die AusfÃ¼hrungszeit diesen Wert Ã¼berschreitet, wird eine WARN-Level-Nachricht geloggt.
     * Der Wert wird in der angegebenen timeUnit interpretiert.
     * Standard ist 0 (keine Warnung).
     */
    long warnThreshold() default 0;
    
    /**
     * Schwellwert, ab dem ein Fehler ausgegeben wird.
     * Wenn die AusfÃ¼hrungszeit diesen Wert Ã¼berschreitet, wird eine ERROR-Level-Nachricht geloggt.
     * Der Wert wird in der angegebenen timeUnit interpretiert.
     * Standard ist 0 (kein Fehler).
     */
    long errorThreshold() default 0;
    
    /**
     * Gibt an, ob detaillierte Statistiken ausgegeben werden sollen.
     * Dazu gehÃ¶ren Minimum, Maximum, Durchschnitt und Standardabweichung.
     */
    boolean detailedStats() default false;
    
    /**
     * Logging-Level fÃ¼r die Ausgabe der Ergebnisse.
     * TRUE: Ergebnisse werden geloggt.
     * FALSE: Ergebnisse werden nicht geloggt (kann fÃ¼r Tests oder spezielle FÃ¤lle nÃ¼tzlich sein).
     */
    boolean logResults() default true;
}
