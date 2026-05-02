package simulation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Event}.
 */
@DisplayName("Event")
class EventTest {

    /** Concrete subclass used for testing. */
    private static class TestEvent extends Event {
        boolean executed = false;

        TestEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulation sim) {
            executed = true;
        }
    }

    @Test
    @DisplayName("constructor stores the scheduled time")
    void constructorStoresTime() {
        Event e = new TestEvent(3.14);
        assertEquals(3.14, e.getTime(), 1e-9);
    }

    @Test
    @DisplayName("constructor accepts time zero")
    void constructorAcceptsZero() {
        Event e = new TestEvent(0.0);
        assertEquals(0.0, e.getTime());
    }

    @Test
    @DisplayName("constructor rejects negative time")
    void constructorRejectsNegativeTime() {
        assertThrows(IllegalArgumentException.class, () -> new TestEvent(-1.0));
    }

    @Test
    @DisplayName("compareTo: earlier event is less than later event")
    void compareToEarlierIsLess() {
        Event early = new TestEvent(1.0);
        Event late  = new TestEvent(2.0);
        assertTrue(early.compareTo(late) < 0);
    }

    @Test
    @DisplayName("compareTo: later event is greater than earlier event")
    void compareToLaterIsGreater() {
        Event early = new TestEvent(1.0);
        Event late  = new TestEvent(2.0);
        assertTrue(late.compareTo(early) > 0);
    }

    @Test
    @DisplayName("compareTo: equal times compare as zero")
    void compareToEqualIsZero() {
        Event a = new TestEvent(5.0);
        Event b = new TestEvent(5.0);
        assertEquals(0, a.compareTo(b));
    }

    @Test
    @DisplayName("toString contains the class name and time")
    void toStringContainsInfo() {
        Event e = new TestEvent(2.5);
        String s = e.toString();
        assertTrue(s.contains("TestEvent"), "should contain class name");
        assertTrue(s.contains("2.5") || s.contains("2.5000"), "should contain time");
    }
}
