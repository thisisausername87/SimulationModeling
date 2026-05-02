# SimulationModeling

A Java scaffolding project for discrete-event simulation, designed for
college students in an **Algorithms and Algorithm Analysis** course.

## Prerequisites

| Tool | Version |
|------|---------|
| JDK | 17 or higher |
| Apache Ant | 1.10.6 or higher |

Verify with:
```
java -version
ant -version
```

## Project Layout

```
SimulationModeling/
├── build.xml              # Ant build script
├── build.properties       # Configurable build settings
├── lib/                   # JUnit 5 JARs (committed; see resolve target)
├── src/
│   └── main/java/
│       └── simulation/
│           ├── Event.java         # Abstract base for simulation events
│           ├── EventQueue.java    # Min-heap priority queue of events
│           ├── Simulation.java    # Abstract discrete-event engine
│           └── Statistics.java    # Online statistics collector
├── test/
│   └── java/
│       └── simulation/
│           ├── EventTest.java
│           ├── EventQueueTest.java
│           ├── SimulationTest.java
│           └── StatisticsTest.java
└── docs/                  # Generated Javadoc (not committed)
```

## Build Targets

| Target | Description |
|--------|-------------|
| `ant compile` | Compile main sources |
| `ant test` | Compile and run JUnit 5 tests |
| `ant javadoc` | Generate API docs in `docs/` |
| `ant jar` | Package classes into `build/simulation.jar` |
| `ant all` | Full pipeline: clean → compile → test → javadoc → jar |
| `ant clean` | Remove all generated files |
| `ant resolve` | Download JUnit 5 JARs to `lib/` (needed only if lib/ is absent) |

## Quick Start

```bash
# 1. Clone the repository
git clone https://github.com/MetroCS/SimulationModeling.git
cd SimulationModeling

# 2. Compile and run tests
ant test

# 3. Build everything (clean + compile + test + javadoc + jar)
ant all

# 4. Open the generated API documentation
open docs/index.html   # macOS
xdg-open docs/index.html  # Linux
```

## Extending the Simulation Framework

Subclass `Simulation` and `Event` to model your problem:

```java
import simulation.Event;
import simulation.Simulation;
import simulation.Statistics;

public class QueueingSimulation extends Simulation {
    private final Statistics waitTime = new Statistics("wait time");
    private int arrivals = 0;

    @Override
    protected void initialize() {
        scheduleEvent(new ArrivalEvent(exponential(1.0), this));
    }

    @Override
    protected boolean shouldStop() {
        return arrivals >= 1000;
    }

    // ... domain-specific methods
}
```

## Optional Build Tools

`build.xml` contains commented-out targets for:

| Tool | Purpose |
|------|---------|
| **Checkstyle** | Enforce coding style |
| **SpotBugs** | Static bug analysis |
| **PMD** | Source code analysis |
| **JaCoCo** | Test coverage reporting |

To enable any of these, download the corresponding JAR(s) to `lib/` and
uncomment the relevant `<target>` in `build.xml`.

## Running Tests Independently

To re-download JUnit 5 JARs (e.g., after a fresh clone where `lib/` is absent):

```bash
ant resolve
```
