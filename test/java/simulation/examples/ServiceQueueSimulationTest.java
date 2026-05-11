/*
*@author Toren Kochman
*/

package simulation.examples;

import simulation.Event;
import simulation.Simulation;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/*
 * Service Queue Simulation Example
 *
 * Features:
 * Adjustable number of trials
 * Adjustable randomized likelihoods
 * Deterministic random seed support
 * Clearly printed results
 * Simulation logic separated from output formatting
 */

public class ServiceQueueSimulation extends Simulation {
    
    // CONFIGURATION PARAMETERS

    private final int timeSteps;
    private final double arrivalProbability;
    private final double serviceProbability;
    private final int numberOfTrials;
    private final Long randomSeed;

    public ServiceQueueSimulation(
            int timeSteps,
            double arrivalProbability,
            double serviceProbability,
            int numberOfTrials,
            Long randomSeed
    ) {
        this.timeSteps = timeSteps;
        this.arrivalProbability = arrivalProbability;
        this.serviceProbability = serviceProbability;
        this.numberOfTrials = numberOfTrials;
        this.randomSeed = randomSeed;
    }

    /*
     * Runs all trials and returns averaged statistics.
     */
    public SimulationResult runSimulation() {

        double totalAverageQueueLength = 0.0;
        double totalAverageWaitTime = 0.0;
        double totalIdleFraction = 0.0;
        int overallMaxQueueLength = 0;

        Random masterRandom;

        if (randomSeed != null) {
            masterRandom = new Random(randomSeed);
        } else {
            masterRandom = new Random();
        }

        for (int trial = 0; trial < numberOfTrials; trial++) {

            long trialSeed = masterRandom.nextLong();

            SimulationResult result = runSingleTrial(trialSeed);

            totalAverageQueueLength += result.averageQueueLength;
            totalAverageWaitTime += result.averageWaitTime;
            totalIdleFraction += result.idleFraction;

            overallMaxQueueLength = Math.max(
                    overallMaxQueueLength,
                    result.maxQueueLength
            );
        }

        return new SimulationResult(
                totalAverageQueueLength / numberOfTrials,
                overallMaxQueueLength,
                totalAverageWaitTime / numberOfTrials,
                totalIdleFraction / numberOfTrials
        );
    }

    /*
     * Runs one trial of the queue simulation.
     */
    private SimulationResult runSingleTrial(long seed) {

        Random random = new Random(seed);

        Queue<Integer> queue = new LinkedList<>();

        int totalQueueLength = 0;
        int maxQueueLength = 0;

        int idleTimeSteps = 0;

        long totalWaitTime = 0;
        int customersServed = 0;

        for (int currentTime = 0; currentTime < timeSteps; currentTime++) {

            // CUSTOMER ARRIVAL

            if (random.nextDouble() < arrivalProbability) {
                queue.add(currentTime);
            }

            // SERVICE COMPLETION

            if (!queue.isEmpty()) {

                if (random.nextDouble() < serviceProbability) {

                    int arrivalTime = queue.remove();

                    int waitTime = currentTime - arrivalTime;

                    totalWaitTime += waitTime;

                    customersServed++;
                }

            } else {
                idleTimeSteps++;
            }

            // TRACK STATISTICS

            int currentQueueLength = queue.size();

            totalQueueLength += currentQueueLength;

            maxQueueLength = Math.max(
                    maxQueueLength,
                    currentQueueLength
            );
        }

        double averageQueueLength =
                (double) totalQueueLength / timeSteps;

        double averageWaitTime =
                customersServed > 0
                        ? (double) totalWaitTime / customersServed
                        : 0.0;

        double idleFraction =
                (double) idleTimeSteps / timeSteps;

        return new SimulationResult(
                averageQueueLength,
                maxQueueLength,
                averageWaitTime,
                idleFraction
        );
    }

    /*
     * Prints results clearly.
     */
    public void printResults(SimulationResult result) {

        System.out.println("======================================");
        System.out.println(" SERVICE QUEUE SIMULATION RESULTS ");
        System.out.println("======================================");

        System.out.println("Time Steps:           " + timeSteps);
        System.out.println("Arrival Probability: " + arrivalProbability);
        System.out.println("Service Probability: " + serviceProbability);
        System.out.println("Trials:               " + numberOfTrials);

        if (randomSeed != null) {
            System.out.println("Random Seed:          " + randomSeed);
        } else {
            System.out.println("Random Seed:          None");
        }

        System.out.println();

        System.out.printf("Average Queue Length: %.4f%n",
                result.averageQueueLength);

        System.out.println("Maximum Queue Length: "
                + result.maxQueueLength);

        System.out.printf("Average Wait Time:    %.4f%n",
                result.averageWaitTime);

        System.out.printf("Server Idle Fraction: %.4f%n",
                result.idleFraction);

        System.out.println();

        if (arrivalProbability < serviceProbability) {
            System.out.println("System Status: Likely Stable");
        } else if (arrivalProbability > serviceProbability) {
            System.out.println("System Status: Likely Unstable");
        } else {
            System.out.println("System Status: Near Critical Balance");
        }

        System.out.println("======================================");
    }

    /*
     * Required override from Simulation base class.
     * Placeholder implementation since this simulation
     * is time-step based rather than event-driven.
     */
    @Override
    public Event[] run() {
        return new Event[0];
    }

    /*
     * Stores simulation statistics.
     */
    static class SimulationResult {

        double averageQueueLength;
        int maxQueueLength;
        double averageWaitTime;
        double idleFraction;

        public SimulationResult(
                double averageQueueLength,
                int maxQueueLength,
                double averageWaitTime,
                double idleFraction
        ) {
            this.averageQueueLength = averageQueueLength;
            this.maxQueueLength = maxQueueLength;
            this.averageWaitTime = averageWaitTime;
            this.idleFraction = idleFraction;
        }
    }

    /*
     * Test class in the same package as required.
     */
    public static class TestSimulation {

        public static void main(String[] args) {

            // PARAMETERS

            int timeSteps = 100000;

            double arrivalProbability = 0.55;

            double serviceProbability = 0.60;

            int numberOfTrials = 10;

            Long randomSeed = 42L;

            // CREATE AND RUN SIMULATION

            ServiceQueueSimulation simulation =
                    new ServiceQueueSimulation(
                            timeSteps,
                            arrivalProbability,
                            serviceProbability,
                            numberOfTrials,
                            randomSeed
                    );

            SimulationResult result =
                    simulation.runSimulation();

            simulation.printResults(result);
        }
    }
}
