import java.util.HashSet;
import java.util.Set;

public class MaxCutNaive {

    public static void main(String[] args) {
        // Example graph represented as an adjacency matrix
        int[][] graph = {
                {0, 3, 1, 6},
                {3, 0, 5, 2},
                {1, 5, 0, 4},
                {6, 2, 4, 0}
        };

        // Record the start time for the naive approach
        long startTimeNaive = System.currentTimeMillis();

        // Call the naive max cut method
        int maxCutNaive = maxCutNaive(graph);

        // Record the end time for the naive approach
        long endTimeNaive = System.currentTimeMillis();

        System.out.println("Naive Approach:");
        System.out.println("Maximum Cut Value: " + maxCutNaive);
        System.out.println("Start Time: " + startTimeNaive + " ms");
        System.out.println("End Time: " + endTimeNaive + " ms");
        System.out.println("Execution Time: " + (endTimeNaive - startTimeNaive) + " ms");

        // Run the Kernighan-Lin algorithm
        System.out.println("\nKernighan-Lin Approach:");
        KernighanLinMaxCut kl = new KernighanLinMaxCut();
        kl.runKernighanLin(graph);
    }

    public static int maxCutNaive(int[][] graph) {
        int numNodes = graph.length;
        int maxCutValue = 0;

        // Iterate through all possible partitions using bit masking
        int totalPartitions = (1 << numNodes); // 2^n partitions
        for (int mask = 0; mask < totalPartitions; mask++) {
            Set<Integer> A = new HashSet<>();
            Set<Integer> B = new HashSet<>();

            // Partition the nodes based on the current bitmask
            for (int i = 0; i < numNodes; i++) {
                if ((mask & (1 << i)) != 0) A.add(i); // Node i in set A
                else B.add(i);                       // Node i in set B
            }

            // Calculate the cut value for this partition
            int cutValue = calculateCutValue(A, B, graph);
            maxCutValue = Math.max(maxCutValue, cutValue);
        }

        return maxCutValue;
    }

    private static int calculateCutValue(Set<Integer> A, Set<Integer> B, int[][] graph) {
        int cutValue = 0;
        for (int a : A) {
            for (int b : B) {
                cutValue += graph[a][b];
            }
        }
        return cutValue;
    }
}

// Kernighan-Lin Algorithm (Single-Class Implementation)
class KernighanLinMaxCut {
    public void runKernighanLin(int[][] graph) {
        long startTime = System.currentTimeMillis();
        int maxCutValue = maxCut(graph);
        long endTime = System.currentTimeMillis();

        System.out.println("Maximum Cut Value: " + maxCutValue);
        System.out.println("Start Time: " + startTime + " ms");
        System.out.println("End Time: " + endTime + " ms");
        System.out.println("Execution Time: " + (endTime - startTime) + " ms");
    }

    public int maxCut(int[][] graph) {
        int numNodes = graph.length;
        Set<Integer> A = new HashSet<>();
        Set<Integer> B = new HashSet<>();
        for (int i = 0; i < numNodes; i++) {
            if (i < numNodes / 2) A.add(i);
            else B.add(i);
        }
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            int maxGain = Integer.MIN_VALUE;
            int bestA = -1, bestB = -1;
            for (int a : A) {
                for (int b : B) {
                    int gain = calculateGain(a, b, A, B, graph);
                    if (gain > maxGain) {
                        maxGain = gain;
                        bestA = a;
                        bestB = b;
                    }
                }
            }
            if (maxGain > 0) {
                A.remove(bestA);
                B.remove(bestB);
                A.add(bestB);
                B.add(bestA);
                improvement = true;
            }
        }
        return calculateCutValue(A, B, graph);
    }

    private int calculateGain(int a, int b, Set<Integer> A, Set<Integer> B, int[][] graph) {
        int externalA = 0, internalA = 0, externalB = 0, internalB = 0;
        for (int i = 0; i < graph.length; i++) {
            if (A.contains(i)) internalA += graph[a][i];
            else externalA += graph[a][i];
        }
        for (int i = 0; i < graph.length; i++) {
            if (B.contains(i)) internalB += graph[b][i];
            else externalB += graph[b][i];
        }
        return (externalA - internalA) + (externalB - internalB) - (graph[a][b] + graph[b][a]);
    }

    private int calculateCutValue(Set<Integer> A, Set<Integer> B, int[][] graph) {
        int cutValue = 0;
        for (int a : A) {
            for (int b : B) {
                cutValue += graph[a][b];
            }
        }
        return cutValue;
    }
}
