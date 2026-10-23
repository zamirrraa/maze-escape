import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**

 * Missions 1 and 2. Building the maze out of the walls.
 */
public class Maze {

    /**

     * Mission 1. Process walls in the order of the file.
     */
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet dsu = new DisjointSet(w.a().length);
        List<Integer> broken = new ArrayList<>();

        for (int i = 0; i < w.a().length; i++) {
            int a = w.a()[i];
            int b = w.b()[i];

            if (dsu.find(a) != dsu.find(b)) {
                dsu.union(a, b);
                broken.add(i);
            }

        }

        return broken.stream().mapToInt(Integer::intValue).toArray();
    }

    /**

     * Mission 2. Kruskal: process walls from cheapest to most expensive.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        int numberOfCells = Math.max(w.a().length, w.b().length);

        for (int i = 0; i < w.a().length; i++) {
            numberOfCells = Math.max(numberOfCells,
                    Math.max(w.a()[i], w.b()[i]) + 1);
        }

        DisjointSet dsu = new DisjointSet(numberOfCells);
        List<Integer> broken = new ArrayList<>();

        Integer[] indexes = new Integer[w.a().length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, (i, j) -> {
            int compare = Long.compare(w.cost()[i], w.cost()[j]);
            return compare != 0 ? compare : Integer.compare(i, j);
        });

        for (int i : indexes) {
            if (dsu.find(w.a()[i]) != dsu.find(w.b()[i])) {
                dsu.union(w.a()[i], w.b()[i]);
                broken.add(i);
            }
        }

        return broken.stream().mapToInt(Integer::intValue).toArray();
    }

    /**

     * Mission 2. Prim with a priority queue.
     */
    public static long primCost(Input.Walls w) {
        int numberOfCells = 0;

        for (int i = 0; i < w.a().length; i++) {
            numberOfCells = Math.max(numberOfCells,
                    Math.max(w.a()[i], w.b()[i]) + 1);
        }

        if (numberOfCells == 0) {
            return 0;
        }

        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < numberOfCells; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < w.a().length; i++) {
            graph.get(w.a()[i]).add(i);
            graph.get(w.b()[i]).add(i);
        }

        boolean[] visited = new boolean[numberOfCells];
        PriorityQueue<Integer> queue = new PriorityQueue<>((i, j) -> {
            int compare = Long.compare(w.cost()[i], w.cost()[j]);
            return compare != 0 ? compare : Integer.compare(i, j);
        });

        long totalCost = 0;
        int visitedCount = 0;

        for (int start = 0; start < numberOfCells; start++) {
            if (visited[start]) {
                continue;
            }

            visited[start] = true;
            visitedCount++;
            queue.addAll(graph.get(start));

            while (!queue.isEmpty()) {
                int wall = queue.poll();
                int a = w.a()[wall];
                int b = w.b()[wall];

                if (visited[a] && visited[b]) {
                    continue;
                }

                int next = visited[a] ? b : a;

                visited[next] = true;
                visitedCount++;
                totalCost += w.cost()[wall];
                queue.addAll(graph.get(next));
            }

        }

        return totalCost;
    }
}
