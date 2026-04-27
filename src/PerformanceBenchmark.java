// Measures and outputs execution time in nanoseconds
public class PerformanceBenchmark {

    // Records and prints execution time for a search task
    public static void benchmark(String label, Runnable searchTask) {
        long start = System.nanoTime();
        searchTask.run();
        long end = System.nanoTime();
        long duration = end - start;
        System.out.printf("%s: %d ns\n", label, duration);
    }
}