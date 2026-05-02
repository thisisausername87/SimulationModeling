package simulation.examples;

import java.util.Random;
import simulation.Event;
import simulation.Simulation;

/**
 * Estimates the birthday-collision probability with repeated random trials.
 *
 * <p>A birthday collision occurs when at least two people in a group share the
 * same birthday. This example assumes 365 equally likely birthdays, independent
 * birthdays, and no leap years.
 *
 * <p>Run it after compiling with:
 * <pre>{@code
 * java -cp build/classes simulation.examples.BirthdayCollisionSimulation 23
 * }</pre>
 */
public final class BirthdayCollisionSimulation extends Simulation {

    /** Number of possible birthdays when leap years are ignored. */
    public static final int DAYS_IN_YEAR = 365;

    private static final int DEFAULT_GROUP_SIZE = 23;
    private static final int[] DEFAULT_TRIAL_COUNTS = {100, 1_000, 10_000};

    private final int groupSize;
    private final int trialCount;
    private final Random random;

    private int completedTrials;
    private int collisionCount;

    /**
     * Creates a simulation with a new random number generator.
     *
     * @param groupSize number of people in each trial group
     * @param trialCount number of independent trials to run
     */
    public BirthdayCollisionSimulation(int groupSize, int trialCount) {
        this(groupSize, trialCount, java.util.concurrent.ThreadLocalRandom.current().nextLong());
    }

    /**
     * Creates a simulation with a specific random number generator seed.
     *
     * @param groupSize number of people in each trial group
     * @param trialCount number of independent trials to run
     * @param seed specific seed value for random source of random birthdays
     * @throws IllegalArgumentException if {@code groupSize} or {@code trialCount}
     *         is less than one
     */
    public BirthdayCollisionSimulation(int groupSize, int trialCount, long seed) {
        if (groupSize < 1) {
            throw new IllegalArgumentException("Group size must be at least one: " + groupSize);
        }
        if (trialCount < 1) {
            throw new IllegalArgumentException("Trial count must be at least one: " + trialCount);
        }

        this.groupSize = groupSize;
        this.trialCount = trialCount;
        this.random = new Random(seed);
    }

    /**
     * Runs estimates for 100, 1,000, and 10,000 trials.
     *
     * <p>The optional first argument sets the group size. If no argument is
     * provided, the classic group size of 23 is used.
     *
     * @param args optional group size
     */
    public static void main(String[] args) {
        int groupSize = parseGroupSize(args);

        for (int trials : DEFAULT_TRIAL_COUNTS) {
            BirthdayCollisionSimulation simulation =
                new BirthdayCollisionSimulation(groupSize, trials);
            simulation.run();
            System.out.printf(
                "k=%d, trials=%d, collisions=%d, estimate=%.4f%n",
                groupSize,
                trials,
                simulation.getCollisionCount(),
                simulation.getEstimatedProbability());
        }
    }

    /**
     * Returns whether one generated group contains a shared birthday.
     *
     * <p>Each person's birthday is generated independently as an integer from
     * {@code 0} through {@code 364}.
     *
     * @param groupSize number of birthdays to generate
     * @param random  the source of random birthdays
     * @return {@code true} when any birthday appears more than once
     * @throws IllegalArgumentException if {@code groupSize} is less than one
     * @throws NullPointerException if {@code random} is {@code null}
     */
    public static boolean hasSharedBirthday(int groupSize, Random random) {
        if (groupSize < 1) {
            throw new IllegalArgumentException("Group size must be at least one: " + groupSize);
        }

        boolean[] observedBirthdays = new boolean[DAYS_IN_YEAR];
        for (int person = 0; person < groupSize; person++) {
            int birthday = random.nextInt(DAYS_IN_YEAR);
            if (observedBirthdays[birthday]) {
                return true;
            }
            observedBirthdays[birthday] = true;
        }
        return false;
    }

    /**
     * Returns the number of people in each trial group.
     *
     * @return group size
     */
    public int getGroupSize() {
        return groupSize;
    }

    /**
     * Returns the requested number of trials.
     *
     * @return trial count
     */
    public int getTrialCount() {
        return trialCount;
    }

    /**
     * Returns the number of trials that have run.
     *
     * @return completed trial count
     */
    public int getCompletedTrials() {
        return completedTrials;
    }

    /**
     * Returns the number of trials in which a collision occurred.
     *
     * @return collision count
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Returns the estimated collision probability.
     *
     * @return collisions divided by completed trials, or {@code 0.0} before
     *         any trials have completed
     */
    public double getEstimatedProbability() {
        if (completedTrials == 0) {
            return 0.0;
        }
        return (double) collisionCount / completedTrials;
    }

    @Override
    protected void initialize() {
        scheduleEvent(new TrialEvent(0.0));
    }

    private void runTrial() {
        if (hasSharedBirthday(groupSize, this.random)) {
            collisionCount++;
        }
        completedTrials++;

        if (completedTrials >= trialCount) {
            stop();
        } else {
            scheduleEvent(new TrialEvent(getClock() + 1.0));
        }
    }

    private static int parseGroupSize(String... args) {
        if (args.length == 0) {
            return DEFAULT_GROUP_SIZE;
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: BirthdayCollisionSimulation [groupSize]");
        }
        return Integer.parseInt(args[0]);
    }

    private class TrialEvent extends Event {

        TrialEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulation sim) {
            runTrial();
        }
    }
}
