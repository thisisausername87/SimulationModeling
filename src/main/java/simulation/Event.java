package simulation;

import java.util.Objects;

/**
 * Abstract base class for discrete-event simulation events.
 *
 * <p>Each {@code Event} has a scheduled time at which it occurs.
 * Events are ordered chronologically via {@link Comparable}, making
 * them suitable for storage in a priority queue.
 *
 * <p>Subclasses implement {@link #execute(Simulation)} to define the
 * effect of the event on the simulation state.
 *
 * <p>Example usage:
 * <pre>{@code
 * public class ArrivalEvent extends Event {
 *     public ArrivalEvent(double time) { super(time); }
 *
 *     \u0040Override
 *     public void execute(Simulation sim) {
 *         // handle arrival logic
 *     }
 * }
 * }</pre>
 */
public abstract class Event implements Comparable<Event> {

    private final double time;

    /**
     * Creates an event scheduled to occur at the given simulation time.
     *
     * @param time the non-negative simulation time at which this event occurs
     * @throws IllegalArgumentException if {@code time} is negative
     */
    public Event(double time) {
        if (time < 0) {
            throw new IllegalArgumentException("Event time must be non-negative: " + time);
        }
        this.time = time;
    }

    /**
     * Returns the simulation time at which this event is scheduled.
     *
     * @return the event's scheduled time
     */
    public double getTime() {
        return time;
    }

    /**
     * Executes this event within the given simulation.
     *
     * <p>Implementations typically update simulation state and may
     * schedule new events via {@link Simulation#scheduleEvent(Event)}.
     *
     * @param sim the simulation in which this event executes
     */
    public abstract void execute(Simulation sim);

    /**
     * Compares this event to another by scheduled time (ascending).
     *
     * @param other the other event to compare
     * @return a negative, zero, or positive integer as this event's
     *         time is less than, equal to, or greater than the other's
     */
    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }

    /**
     * Compares events by concrete event type and scheduled time.
     *
     * @param obj the object to compare with this event
     * @return {@code true} when both events have the same type and time
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        return Double.compare(time, other.time) == 0;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return hash code for this event
     */
    @Override
    public int hashCode() {
        return Objects.hash(getClass(), time);
    }

    /**
     * Returns a string representation showing the event time.
     *
     * @return human-readable description of this event
     */
    @Override
    public String toString() {
        return String.format("%s[time=%.4f]", getClass().getSimpleName(), time);
    }
}
