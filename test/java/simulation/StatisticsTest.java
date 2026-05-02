package simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Statistics}.
 */
@DisplayName("Statistics")
class StatisticsTest {

    private Statistics stats;

    @BeforeEach
    void setUp() {
        stats = new Statistics("test");
    }

    @Test
    @DisplayName("new statistics has zero count")
    void initialCountIsZero() {
        assertEquals(0, stats.getCount());
    }

    @Test
    @DisplayName("mean is NaN with no observations")
    void meanNaNWithNoObservations() {
        assertTrue(Double.isNaN(stats.getMean()));
    }

    @Test
    @DisplayName("min is NaN with no observations")
    void minNaNWithNoObservations() {
        assertTrue(Double.isNaN(stats.getMin()));
    }

    @Test
    @DisplayName("max is NaN with no observations")
    void maxNaNWithNoObservations() {
        assertTrue(Double.isNaN(stats.getMax()));
    }

    @Test
    @DisplayName("variance is NaN with fewer than two observations")
    void varianceNaNWithOneObservation() {
        stats.observe(5.0);
        assertTrue(Double.isNaN(stats.getVariance()));
    }

    @Test
    @DisplayName("observe increments count")
    void observeIncrementsCount() {
        stats.observe(1.0);
        stats.observe(2.0);
        assertEquals(2, stats.getCount());
    }

    @Test
    @DisplayName("mean of {2, 4, 6} is 4")
    void meanOfThreeValues() {
        stats.observe(2.0);
        stats.observe(4.0);
        stats.observe(6.0);
        assertEquals(4.0, stats.getMean(), 1e-9);
    }

    @Test
    @DisplayName("min tracks smallest value")
    void minTracksSmallest() {
        stats.observe(3.0);
        stats.observe(1.0);
        stats.observe(2.0);
        assertEquals(1.0, stats.getMin(), 1e-9);
    }

    @Test
    @DisplayName("max tracks largest value")
    void maxTracksLargest() {
        stats.observe(3.0);
        stats.observe(1.0);
        stats.observe(5.0);
        assertEquals(5.0, stats.getMax(), 1e-9);
    }

    @Test
    @DisplayName("variance of {2, 4, 6} is 4 (sample)")
    void varianceOfThreeValues() {
        stats.observe(2.0);
        stats.observe(4.0);
        stats.observe(6.0);
        assertEquals(4.0, stats.getVariance(), 1e-9);
    }

    @Test
    @DisplayName("stddev of {2, 4, 6} is 2")
    void stdDevOfThreeValues() {
        stats.observe(2.0);
        stats.observe(4.0);
        stats.observe(6.0);
        assertEquals(2.0, stats.getStdDev(), 1e-9);
    }

    @Test
    @DisplayName("reset clears all observations")
    void resetClearsObservations() {
        stats.observe(10.0);
        stats.observe(20.0);
        stats.reset();
        assertEquals(0, stats.getCount());
        assertTrue(Double.isNaN(stats.getMean()));
    }

    @Test
    @DisplayName("getName returns the name provided at construction")
    void getNameReturnsName() {
        assertEquals("test", stats.getName());
    }

    @Test
    @DisplayName("toString contains name and statistics when observations exist")
    void toStringWithObservations() {
        stats.observe(1.0);
        stats.observe(3.0);
        String s = stats.toString();
        assertTrue(s.contains("test"));
        assertTrue(s.contains("n=2"));
    }

    @Test
    @DisplayName("toString indicates no observations when empty")
    void toStringNoObservations() {
        String s = stats.toString();
        assertTrue(s.contains("no observations"));
    }
}
