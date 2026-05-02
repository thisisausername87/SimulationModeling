package simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link EventQueue}.
 */
@DisplayName("EventQueue")
class EventQueueTest {

    private EventQueue queue;

    /** Minimal concrete event for testing. */
    private static class NoOpEvent extends Event {
        NoOpEvent(double time) { super(time); }

        @Override
        public void execute(Simulation sim) { /* no-op */ }
    }

    @BeforeEach
    void setUp() {
        queue = new EventQueue();
    }

    @Test
    @DisplayName("new queue is empty")
    void newQueueIsEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    @DisplayName("schedule increases size")
    void scheduleIncreasesSize() {
        queue.schedule(new NoOpEvent(1.0));
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    @DisplayName("nextEvent returns events in chronological order")
    void nextEventChronologicalOrder() {
        queue.schedule(new NoOpEvent(3.0));
        queue.schedule(new NoOpEvent(1.0));
        queue.schedule(new NoOpEvent(2.0));

        assertEquals(1.0, queue.nextEvent().getTime(), 1e-9);
        assertEquals(2.0, queue.nextEvent().getTime(), 1e-9);
        assertEquals(3.0, queue.nextEvent().getTime(), 1e-9);
    }

    @Test
    @DisplayName("nextEvent on empty queue returns null")
    void nextEventEmptyReturnsNull() {
        assertNull(queue.nextEvent());
    }

    @Test
    @DisplayName("schedule rejects null")
    void scheduleRejectsNull() {
        assertThrows(NullPointerException.class, () -> queue.schedule(null));
    }

    @Test
    @DisplayName("clear empties the queue")
    void clearEmptiesQueue() {
        queue.schedule(new NoOpEvent(1.0));
        queue.schedule(new NoOpEvent(2.0));
        queue.clear();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    @DisplayName("schedule and nextEvent with same time")
    void scheduleSameTime() {
        queue.schedule(new NoOpEvent(1.0));
        queue.schedule(new NoOpEvent(1.0));
        assertEquals(2, queue.size());
        assertNotNull(queue.nextEvent());
        assertNotNull(queue.nextEvent());
        assertTrue(queue.isEmpty());
    }
}
