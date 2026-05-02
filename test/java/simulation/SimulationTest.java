package simulation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Simulation}.
 */
@DisplayName("Simulation")
class SimulationTest {

    /**
     * Simple counter simulation: schedules N tick events spaced 1.0 apart,
     * each re-scheduling the next tick.
     */
    private static class CountingSimulation extends Simulation {
        private final int targetCount;
        private int count = 0;

        CountingSimulation(int targetCount) {
            this.targetCount = targetCount;
        }

        @Override
        protected void initialize() {
            scheduleEvent(new TickEvent(1.0, this));
        }

        void tick() {
            count++;
            if (count < targetCount) {
                scheduleEvent(new TickEvent(getClock() + 1.0, this));
            }
        }

        int getCount() { return count; }
    }

    private static class TickEvent extends Event {
        private final CountingSimulation owner;

        TickEvent(double time, CountingSimulation owner) {
            super(time);
            this.owner = owner;
        }

        @Override
        public void execute(Simulation sim) {
            owner.tick();
        }
    }

    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("clock starts at zero")
    void clockStartsAtZero() {
        CountingSimulation sim = new CountingSimulation(1);
        assertEquals(0.0, sim.getClock());
    }

    @Test
    @DisplayName("isRunning is false before run()")
    void isNotRunningBeforeRun() {
        CountingSimulation sim = new CountingSimulation(1);
        assertFalse(sim.isRunning());
    }

    @Test
    @DisplayName("isRunning is false after run() completes")
    void isNotRunningAfterRun() {
        CountingSimulation sim = new CountingSimulation(1);
        sim.run();
        assertFalse(sim.isRunning());
    }

    @Test
    @DisplayName("run processes all scheduled events")
    void runProcessesAllEvents() {
        CountingSimulation sim = new CountingSimulation(5);
        sim.run();
        assertEquals(5, sim.getCount());
    }

    @Test
    @DisplayName("clock advances to last event time")
    void clockAdvancesToLastEventTime() {
        CountingSimulation sim = new CountingSimulation(3);
        sim.run();
        assertEquals(3.0, sim.getClock(), 1e-9);
    }

    @Test
    @DisplayName("scheduleEvent rejects past times")
    void scheduleRejectsPastTime() {
        CountingSimulation sim = new CountingSimulation(1);
        sim.run(); // clock is now 1.0
        assertThrows(IllegalArgumentException.class,
            () -> sim.scheduleEvent(new TickEvent(0.5, sim)));
    }

    @Test
    @DisplayName("shouldStop halts the simulation early")
    void shouldStopHaltsEarly() {
        // Simulation that stops after 3 events even though 10 are scheduled
        Simulation sim = new Simulation() {
            private int processed = 0;

            @Override
            protected void initialize() {
                for (int i = 1; i <= 10; i++) {
                    scheduleEvent(new Event(i) {
                        @Override
                        public void execute(Simulation s) {
                            processed++;
                        }
                    });
                }
            }

            @Override
            protected boolean shouldStop() {
                return processed >= 3;
            }

            int getProcessed() { return processed; }
        };

        sim.run();
        // At most 3 events should have executed before shouldStop returned true
        // (shouldStop is checked before dequeuing, so exactly 3 execute)
        assertTrue(sim.getClock() <= 3.0 + 1e-9);
    }
}
