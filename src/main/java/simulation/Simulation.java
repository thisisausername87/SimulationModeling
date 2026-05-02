package simulation;

/**
 * Abstract base class for discrete-event simulations.
 *
 * <p>A {@code Simulation} maintains a clock and an {@link EventQueue}.
 * Subclasses populate the queue with initial events in
 * {@link #initialize()} and may optionally override
 * {@link #shouldStop()} to implement an early-termination condition.
 *
 * <p>The simulation loop advances the clock to the time of each event
 * in chronological order and invokes {@link Event#execute(Simulation)},
 * which may in turn schedule further events.  This pattern—known as
 * <em>next-event time advance</em>—is a classic application of
 * priority-queue algorithms.
 *
 * <p>Minimal example:
 * <pre>{@code
 * public class PingSimulation extends Simulation {
 *     private int count = 0;
 *
 *     \u0040Override
 *     protected void initialize() {
 *         scheduleEvent(new PingEvent(1.0, this));
 *     }
 *
 *     \u0040Override
 *     protected boolean shouldStop() {
 *         return count >= 5;
 *     }
 *
 *     public void incrementCount() { count++; }
 * }
 * }</pre>
 */
public abstract class Simulation {

    private final EventQueue eventQueue;
    private double clock;
    private boolean running;

    /**
     * Creates a new simulation with an empty event queue and clock at 0.
     */
    public Simulation() {
        eventQueue = new EventQueue();
        clock = 0.0;
        running = false;
    }

    /**
     * Schedules an event for future processing.
     *
     * <p>The event's time must be greater than or equal to the current
     * clock value to preserve causality.
     *
     * @param event the event to enqueue
     * @throws IllegalArgumentException if the event's time precedes the
     *         current simulation clock
     */
    public void scheduleEvent(Event event) {
        if (event.getTime() < clock) {
            throw new IllegalArgumentException(
                String.format("Cannot schedule event at time %.4f before current clock %.4f",
                    event.getTime(), clock));
        }
        eventQueue.schedule(event);
    }

    /**
     * Returns the current simulation clock value.
     *
     * @return elapsed simulation time
     */
    public double getClock() {
        return clock;
    }

    /**
     * Returns {@code true} if the simulation is currently executing.
     *
     * @return {@code true} while {@link #run()} is processing events
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Populates the event queue with the initial set of events.
     *
     * <p>Called once at the start of {@link #run()} before any events
     * are processed.  Subclasses must implement this method.
     */
    protected abstract void initialize();

    /**
     * Returns {@code true} when the simulation should terminate.
     *
     * <p>The default implementation returns {@code false}, so the
     * simulation runs until the event queue is empty.  Override to
     * implement time limits, event-count limits, or other stopping
     * criteria.
     *
     * @return {@code true} to stop processing further events
     */
    protected boolean shouldStop() {
        return false;
    }

    /**
     * Runs the simulation from start to finish.
     *
     * <p>Calls {@link #initialize()}, then repeatedly dequeues the
     * next event, advances the clock, and executes the event—until
     * the queue is empty or {@link #shouldStop()} returns {@code true}.
     */
    public void run() {
        initialize();
        running = true;
        while (running && !eventQueue.isEmpty()) {
            if (shouldStop()) {
                break;
            }
            Event event = eventQueue.nextEvent();
            clock = event.getTime();
            event.execute(this);
        }
        running = false;
    }

    /**
     * Requests that the simulation stop after the current event finishes.
     *
     * <p>This may be called from within an event's
     * {@link Event#execute(Simulation)} method to halt the simulation
     * mid-run.
     */
    public void stop() {
        running = false;
    }
}
