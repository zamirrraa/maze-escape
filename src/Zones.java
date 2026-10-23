import java.util.ArrayDeque;
import java.util.Arrays;

public class Zones {

    /**
     * Mission 4. Kosaraju algorithm using iterative DFS.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);
        int[][] into = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderSize = 0;

        // First pass: DFS on the original graph.
        int[] nextIndex = new int[n];
        ArrayDeque<Integer> stack = new ArrayDeque<>();

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            stack.push(start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                int cell = stack.peek();
                boolean moved = false;

                while (nextIndex[cell] < from[cell].length) {
                    int slide = from[cell][nextIndex[cell]++];
                    int neighbour = s.to()[slide];

                    if (!visited[neighbour]) {
                        visited[neighbour] = true;
                        stack.push(neighbour);
                        moved = true;
                        break;
                    }
                }

                if (!moved) {
                    if (nextIndex[cell] >= from[cell].length) {
                        order[orderSize++] = cell;
                        stack.pop();
                    }
                }
            }
        }

        // Second pass: DFS on the reversed graph.
        Arrays.fill(visited, false);
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);

        int zone = 0;

        for (int i = orderSize - 1; i >= 0; i--) {
            int start = order[i];

            if (visited[start]) {
                continue;
            }

            stack.push(start);
            visited[start] = true;
            zoneOf[start] = zone;

            while (!stack.isEmpty()) {
                int cell = stack.pop();

                for (int slide : into[cell]) {
                    int neighbour = s.from()[slide];

                    if (!visited[neighbour]) {
                        visited[neighbour] = true;
                        zoneOf[neighbour] = zone;
                        stack.push(neighbour);
                    }
                }
            }

            zone++;
        }

        return zoneOf;
    }

    /**
     * Mission 5. Find the longest chain of zones and cheapest route.
     */
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        long[][] edgeCost = new long[zoneCount][zoneCount];

        for (long[] row : edgeCost) {
            Arrays.fill(row, Long.MAX_VALUE);
        }

        for (int i = 0; i < s.from().length; i++) {
            int fromZone = zoneOf[s.from()[i]];
            int toZone = zoneOf[s.to()[i]];

            if (fromZone != toZone) {
                edgeCost[fromZone][toZone] =
                        Math.min(edgeCost[fromZone][toZone], s.time()[i]);
            }
        }

        int[] longest = new int[zoneCount];
        long[] distance = new long[zoneCount];

        Arrays.fill(distance, Long.MAX_VALUE);

        int start = zoneOf[0];
        int exit = zoneOf[s.n() - 1];

        longest[start] = 1;
        distance[start] = 0;

        for (int u = 0; u < zoneCount; u++) {
            for (int v = 0; v < zoneCount; v++) {
                if (edgeCost[u][v] == Long.MAX_VALUE) {
                    continue;
                }

                longest[v] = Math.max(longest[v], longest[u] + 1);

                if (distance[u] != Long.MAX_VALUE) {
                    distance[v] = Math.min(
                            distance[v],
                            distance[u] + edgeCost[u][v]
                    );
                }
            }
        }

        return new long[]{longest[exit], distance[exit]};
    }

}
