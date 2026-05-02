package simulation.examples;

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link BirthdayCollisionSimulation}.
 */
@DisplayName("BirthdayCollisionSimulation")
class BirthdayCollisionSimulationTest {

    private static final double THEORETICAL_PROBABILITY_FOR_23_PEOPLE = 0.5073;
    private static final double TEN_THOUSAND_TRIAL_TOLERANCE = 0.03;

    @Test
    @DisplayName("constructor rejects invalid group size")
    void constructorRejectsInvalidGroupSize() {
        assertThrows(IllegalArgumentException.class,
            () -> new BirthdayCollisionSimulation(0, 100, new Random(1L)));
    }

    @Test
    @DisplayName("constructor rejects invalid trial count")
    void constructorRejectsInvalidTrialCount() {
        assertThrows(IllegalArgumentException.class,
            () -> new BirthdayCollisionSimulation(23, 0, new Random(1L)));
    }

    @Test
    @DisplayName("constructor rejects null random generator")
    void constructorRejectsNullRandomGenerator() {
        assertThrows(NullPointerException.class,
            () -> new BirthdayCollisionSimulation(23, 100, null));
    }

    @Test
    @DisplayName("hasSharedBirthday rejects invalid group size")
    void hasSharedBirthdayRejectsInvalidGroupSize() {
        assertThrows(IllegalArgumentException.class,
            () -> BirthdayCollisionSimulation.hasSharedBirthday(0, new Random(1L)));
    }

    @Test
    @DisplayName("hasSharedBirthday rejects null random generator")
    void hasSharedBirthdayRejectsNullRandomGenerator() {
        assertThrows(NullPointerException.class,
            () -> BirthdayCollisionSimulation.hasSharedBirthday(23, null));
    }

    @Test
    @DisplayName("one-person group cannot have a collision")
    void onePersonGroupCannotHaveCollision() {
        assertFalse(BirthdayCollisionSimulation.hasSharedBirthday(1, new Random(1L)));
    }

    @Test
    @DisplayName("group larger than possible birthdays must have a collision")
    void groupLargerThanPossibleBirthdaysMustHaveCollision() {
        assertTrue(BirthdayCollisionSimulation.hasSharedBirthday(
            BirthdayCollisionSimulation.DAYS_IN_YEAR + 1,
            new Random(1L)));
    }

    @Test
    @DisplayName("run completes the requested number of trials")
    void runCompletesRequestedNumberOfTrials() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 100, new Random(2026L));

        simulation.run();

        assertEquals(23, simulation.getGroupSize());
        assertEquals(100, simulation.getTrialCount());
        assertEquals(100, simulation.getCompletedTrials());
    }

    @Test
    @DisplayName("estimate is collisions divided by completed trials")
    void estimateIsCollisionsDividedByCompletedTrials() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 100, new Random(2026L));

        simulation.run();

        assertEquals(
            (double) simulation.getCollisionCount() / simulation.getCompletedTrials(),
            simulation.getEstimatedProbability(),
            1e-12);
    }

    @Test
    @DisplayName("estimate is zero before running trials")
    void estimateIsZeroBeforeRunningTrials() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 100, new Random(2026L));

        assertEquals(0.0, simulation.getEstimatedProbability());
    }

    @Test
    @DisplayName("100 trial estimate is a valid probability")
    void oneHundredTrialEstimateIsValidProbability() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 100, new Random(2026L));

        simulation.run();

        assertTrue(simulation.getEstimatedProbability() >= 0.0);
        assertTrue(simulation.getEstimatedProbability() <= 1.0);
    }

    @Test
    @DisplayName("1000 trial estimate is a valid probability")
    void oneThousandTrialEstimateIsValidProbability() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 1_000, new Random(2026L));

        simulation.run();

        assertTrue(simulation.getEstimatedProbability() >= 0.0);
        assertTrue(simulation.getEstimatedProbability() <= 1.0);
    }

    @Test
    @DisplayName("10000 trial estimate is close to theoretical value")
    void tenThousandTrialEstimateIsCloseToTheoreticalValue() {
        BirthdayCollisionSimulation simulation =
            new BirthdayCollisionSimulation(23, 10_000, new Random(2026L));

        simulation.run();

        assertEquals(
            THEORETICAL_PROBABILITY_FOR_23_PEOPLE,
            simulation.getEstimatedProbability(),
            TEN_THOUSAND_TRIAL_TOLERANCE);
    }
}
