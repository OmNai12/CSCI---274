import java.util.HashSet;
import java.util.Set;

public class KernighanLinMaxCut {

    public static void main(String[] args) {
        // Example graph represented as an adjacency matrix
        int[][] graph = {
                {0, 3, 1, 6},
                {3, 0, 5, 2},
                {1, 5, 0, 4},
                {6, 2, 4, 0}
        };

        // Record the start time
        long startTime = System.currentTimeMillis();

        // Call the maxCut method and calculate the result
        int maxCutValue = maxCut(graph);

        // Record the end time
        long endTime = System.currentTimeMillis();

        // Print the results and timestamps
        System.out.println("Maximum Cut Value: " + maxCutValue);
        System.out.println("Start Time: " + startTime + " ms");
        System.out.println("End Time: " + endTime + " ms");
        System.out.println("Execution Time: " + (endTime - startTime) + " ms");
    }

    public static int maxCut(int[][] graph) {
        int numNodes = graph.length;

        // Initial partitions A and B
        Set<Integer> A = new HashSet<>();
        Set<Integer> B = new HashSet<>();

        // Initially divide nodes equally between A and B
        for (int i = 0; i < numNodes; i++) {
            if (i < numNodes / 2) A.add(i);
            else B.add(i);
        }

        boolean improvement = true;

        while (improvement) {
            improvement = false;
            int maxGain = Integer.MIN_VALUE;
            int bestA = -1, bestB = -1;

            // Calculate gain for all possible swaps
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

            // Perform the best swap if it improves the cut value
            if (maxGain > 0) {
                A.remove(bestA);
                B.remove(bestB);
                A.add(bestB);
                B.add(bestA);
                improvement = true;
            }
        }

        // Return the maximum cut value
        return calculateCutValue(A, B, graph);
    }

    private static int calculateGain(int a, int b, Set<Integer> A, Set<Integer> B, int[][] graph) {
        int externalA = 0, internalA = 0, externalB = 0, internalB = 0;

        // Calculate external and internal weights for a
        for (int i = 0; i < graph.length; i++) {
            if (A.contains(i)) internalA += graph[a][i];
            else externalA += graph[a][i];
        }

        // Calculate external and internal weights for b
        for (int i = 0; i < graph.length; i++) {
            if (B.contains(i)) internalB += graph[b][i];
            else externalB += graph[b][i];
        }

        // Gain is the difference in cut value after swapping a and b
        return (externalA - internalA) + (externalB - internalB) - (graph[a][b] + graph[b][a]);
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
