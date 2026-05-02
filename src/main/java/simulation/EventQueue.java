package simulation;

import java.util.PriorityQueue;

/**
 * A chronologically ordered queue of simulation {@link Event}s.
 *
 * <p>Events are stored in a min-heap (priority queue) keyed on
 * {@link Event#getTime()}, so the next event to be processed is
 * always the one with the smallest scheduled time.  This is the
 * standard data structure at the heart of a discrete-event simulation
 * engine and provides an excellent illustration of heap-based priority
 * queues in an algorithms course.
 *
 * <p>Typical usage:
 * <pre>{@code
 * EventQueue eq = new EventQueue();
 * eq.schedule(new ArrivalEvent(1.5));
 * eq.schedule(new ArrivalEvent(0.3));
 * Event next = eq.nextEvent();  // returns the t=0.3 event
 * }</pre>
 */
public class EventQueue {

    private final PriorityQueue<Event> queue;

    /**
     * Creates an empty event queue.
     */
    public EventQueue() {
        queue = new PriorityQueue<>();
    }

    /**
     * Adds an event to the queue.
     *
     * @param event the event to schedule; must not be {@code null}
     * @throws NullPointerException if {@code event} is {@code null}
     */
    public void schedule(Event event) {
        if (event == null) {
            throw new NullPointerException("Cannot schedule a null event");
        }
        queue.offer(event);
    }

    /**
     * Removes and returns the earliest scheduled event.
     *
     * @return the event with the smallest time, or {@code null} if the
     *         queue is empty
     */
    public Event nextEvent() {
        return queue.poll();
    }

    /**
     * Returns {@code true} if no events are pending.
     *
     * @return {@code true} when the queue contains no events
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the number of pending events.
     *
     * @return current queue size
     */
    public int size() {
        return queue.size();
    }

    /**
     * Removes all pending events from the queue.
     */
    public void clear() {
        queue.clear();
    }
}
