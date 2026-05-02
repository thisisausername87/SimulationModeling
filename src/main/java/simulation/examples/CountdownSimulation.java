package simulation.examples;

import simulation.Event;
import simulation.Simulation;

/**
 * A tiny runnable simulation that schedules one event per tick.
 *
 * <p>Run it after compiling with:
 * <pre>{@code
 * java -cp build/classes simulation.examples.CountdownSimulation
 * }</pre>
 */
public class CountdownSimulation extends Simulation {

    private static final int DEFAULT_START = 5;

    private int remaining;

    /**
     * Creates a countdown simulation.
     *
     * @param start the first number to print
     */
    public CountdownSimulation(int start) {
        remaining = start;
    }

    /**
     * Runs the example from the command line.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        new CountdownSimulation(DEFAULT_START).run();
    }

    @Override
    protected void initialize() {
        scheduleEvent(new TickEvent(0.0));
    }

    private void tick() {
        System.out.printf("time %.1f: %d%n", getClock(), remaining);
        remaining--;

        if (remaining <= 0) {
            stop();
        } else {
            scheduleEvent(new TickEvent(getClock() + 1.0));
        }
    }

    private class TickEvent extends Event {

        TickEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulation sim) {
            tick();
        }
    }
}
