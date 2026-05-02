package simulation;

/**
 * Accumulates observations and computes descriptive statistics.
 *
 * <p>Maintains a running count, sum, sum-of-squares, minimum, and
 * maximum so that mean, variance, and standard deviation can be
 * computed in O(1) time after any number of observations.  This
 * implementation uses Bessel's correction (divides by n-1) for the
 * sample variance.
 *
 * <p>Typical usage in a simulation:
 * <pre>{@code
 * Statistics waitTime = new Statistics("wait time");
 * // ... as each customer departs:
 * waitTime.observe(customer.getDepartureTime() - customer.getArrivalTime());
 * // ... at end of simulation:
 * System.out.println(waitTime);
 * }</pre>
 */
public class Statistics {

    private final String name;
    private long count;
    private double sum;
    private double sumSquares;
    private double min;
    private double max;

    /**
     * Creates a named statistics collector with no observations.
     *
     * @param name a descriptive label for this statistic
     */
    public Statistics(String name) {
        this.name = name;
        reset();
    }

    /**
     * Records one observation.
     *
     * @param value the observed value
     */
    public void observe(double value) {
        if (count == 0) {
            min = value;
            max = value;
        } else {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        count++;
        sum += value;
        sumSquares += value * value;
    }

    /**
     * Resets all accumulators, discarding previous observations.
     */
    public void reset() {
        count = 0;
        sum = 0.0;
        sumSquares = 0.0;
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
    }

    /**
     * Returns the number of observations recorded.
     *
     * @return observation count
     */
    public long getCount() {
        return count;
    }

    /**
     * Returns the sum of all observations.
     *
     * @return sum, or {@code 0.0} if no observations have been made
     */
    public double getSum() {
        return sum;
    }

    /**
     * Returns the smallest observed value.
     *
     * @return minimum value, or {@link Double#NaN} if no observations
     */
    public double getMin() {
        return count > 0 ? min : Double.NaN;
    }

    /**
     * Returns the largest observed value.
     *
     * @return maximum value, or {@link Double#NaN} if no observations
     */
    public double getMax() {
        return count > 0 ? max : Double.NaN;
    }

    /**
     * Returns the arithmetic mean of all observations.
     *
     * @return mean, or {@link Double#NaN} if no observations
     */
    public double getMean() {
        return count > 0 ? sum / count : Double.NaN;
    }

    /**
     * Returns the sample variance using Bessel's correction (divides by n-1).
     *
     * @return sample variance, or {@link Double#NaN} if fewer than 2 observations
     */
    public double getVariance() {
        if (count < 2) {
            return Double.NaN;
        }
        return (sumSquares - (sum * sum) / count) / (count - 1);
    }

    /**
     * Returns the sample standard deviation.
     *
     * @return sample standard deviation, or {@link Double#NaN} if fewer than 2 observations
     */
    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    /**
     * Returns the descriptive label assigned to this collector.
     *
     * @return statistic name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a human-readable summary of the collected statistics.
     *
     * @return formatted summary string
     */
    @Override
    public String toString() {
        if (count == 0) {
            return String.format("Statistics[%s: no observations]", name);
        }
        return String.format(
            "Statistics[%s: n=%d, mean=%.4f, stddev=%.4f, min=%.4f, max=%.4f]",
            name, count, getMean(), getStdDev(), getMin(), getMax());
    }
}
