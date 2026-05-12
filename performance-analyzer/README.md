# Performance Analyzer Module

Ein Java-Modul zur Performance-Analyse von Anwendungen mittels Annotationen.

## Überblick

Dieses Modul ermöglicht die einfache Performance-Messung von Java-Methoden durch die Verwendung von Annotationen. Es basiert auf AspectJ und bietet folgende Funktionen:

- **Annotation-basierte Messung**: Einfache Markierung von Methoden oder ganzen Klassen
- **Automatische Statistik-Erfassung**: Count, Min, Max, Average, Standard Deviation
- **Schwellwert-Überwachung**: Konfigurierbare Warn- und Error-Schwellwerte
- **Mehrere Ausgabeformate**: Console, CSV, HTML Reports
- **Thread-safe**: Sicherer Einsatz in Multi-Threading-Umgebungen

## Installation

### Maven Dependency

Fügen Sie folgende Abhängigkeiten zu Ihrem `pom.xml` hinzu:

```xml
<dependency>
    <groupId>com.performance</groupId>
    <artifactId>performance-analyzer</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.9.22.1</version>
</dependency>
```

### AspectJ Konfiguration

Das Modul benötigt AspectJ für das Weaving. Fügen Sie den AspectJ Maven Plugin zu Ihrem Build-Prozess hinzu:

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>aspectj-maven-plugin</artifactId>
    <version>1.15.0</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Verwendung

### 1. Initialisierung

Initialisieren Sie den Performance Analyzer beim Start Ihrer Anwendung:

```java
import com.performance.analyzer.config.PerformanceAnalyzerConfig;

// Beim Anwendungsstart
PerformanceAnalyzerConfig.initialize();
```

### 2. Annotation verwenden

#### Auf Methodenebene:

```java
import com.performance.analyzer.annotation.MeasurePerformance;
import java.util.concurrent.TimeUnit;

public class MyService {
    
    @MeasurePerformance(name = "calculateMetrics", timeUnit = TimeUnit.MILLISECONDS)
    public void calculateMetrics() {
        // Ihre Logik hier
    }
}
```

#### Auf Klassenebene (alle öffentlichen Methoden werden gemessen):

```java
@MeasurePerformance(name = "CriticalService", detailedStats = true)
public class CriticalService {
    
    public void method1() { /* wird gemessen */ }
    public void method2() { /* wird gemessen */ }
}
```

### 3. Annotation Parameter

| Parameter | Typ | Standard | Beschreibung |
|-----------|-----|----------|--------------|
| `name` | String | "" | Optionale Bezeichnung für die Messung |
| `timeUnit` | TimeUnit | MILLISECONDS | Zeiteinheit für die Ausgabe |
| `warnThreshold` | long | 0 | Schwellwert für Warnungen |
| `errorThreshold` | long | 0 | Schwellwert für Fehler |
| `detailedStats` | boolean | false | Detaillierte Statistiken ausgeben |
| `logResults` | boolean | true | Ergebnisse loggen |

### 4. Reports generieren

```java
import com.performance.analyzer.config.PerformanceAnalyzerConfig;
import java.util.concurrent.TimeUnit;

// Console Report
PerformanceAnalyzerConfig.getReporter().printConsoleReport(TimeUnit.MILLISECONDS);

// CSV Report
PerformanceAnalyzerConfig.getReporter().generateCsvReport("report.csv", TimeUnit.MILLISECONDS);

// HTML Report
PerformanceAnalyzerConfig.getReporter().generateHtmlReport("report.html", TimeUnit.MILLISECONDS);

// Top N langsamste Methoden
PerformanceAnalyzerConfig.getReporter().printTopSlowestMethods(5, TimeUnit.MILLISECONDS);
```

## Beispiel

```java
import com.performance.analyzer.annotation.MeasurePerformance;
import com.performance.analyzer.config.PerformanceAnalyzerConfig;
import java.util.concurrent.TimeUnit;

@MeasurePerformance(name = "DataService", detailedStats = true)
public class DataService {
    
    @MeasurePerformance(name = "fetchData", warnThreshold = 100, timeUnit = TimeUnit.MILLISECONDS)
    public List<Data> fetchData() {
        // Datenbankabfrage
        return database.query();
    }
    
    @MeasurePerformance(name = "processData", errorThreshold = 500, timeUnit = TimeUnit.MILLISECONDS)
    public void processData(List<Data> data) {
        // Datenverarbeitung
    }
}

// Hauptprogramm
public class Application {
    public static void main(String[] args) {
        PerformanceAnalyzerConfig.initialize();
        
        DataService service = new DataService();
        
        // Anwendung ausführen
        for (int i = 0; i < 100; i++) {
            List<Data> data = service.fetchData();
            service.processData(data);
        }
        
        // Performance Report ausgeben
        PerformanceAnalyzerConfig.getReporter().printConsoleReport(TimeUnit.MILLISECONDS);
    }
}
```

## Architektur

Das Modul besteht aus folgenden Komponenten:

1. **@MeasurePerformance**: Annotation zur Markierung von Methoden/Klassen
2. **PerformanceMeasurementAspect**: AspectJ Aspect zur Interception der Methodenaufrufe
3. **MethodStatistics**: Thread-safe Statistik-Sammlung
4. **PerformanceReporter**: Generierung von Reports in verschiedenen Formaten
5. **PerformanceAnalyzerConfig**: Zentrale Konfigurationsklasse

## Build

Das Projekt kann mit Maven gebaut werden:

```bash
cd performance-analyzer
mvn clean install
```

## Tests

Die Testausführung erfolgt mit:

```bash
mvn test
```

## Lizenz

Dieses Projekt steht unter der MIT-Lizenz.

## Autor

Performance Analyzer Module - Ein Werkzeug zur einfachen Performance-Analyse von Java-Anwendungen.
