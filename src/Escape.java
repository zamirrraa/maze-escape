import java.util.Arrays;

/**

 * Mission 3. Escape using iterative DFS.
 */
public class Escape {

    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int rows = w.rows();
        int cols = w.cols();
        int total = w.cells();
        int exit = total - 1;

        int[][] open = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int[] neighbours : open) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[total];
        int[] parent = new int[total];
        Arrays.fill(parent, -1);

        int[] stack = new int[total];
        int top = 0;
        stack[0] = 0;
        visited[0] = true;
        int visitedCount = 1;

        while (top >= 0) {
            int cell = stack[top];

            if (cell == exit) {
                break;
            }

            int[] neighbours = {
                    cell - cols,
                    cell + 1,
                    cell + cols,
                    cell - 1
            };

            int next = -1;

            for (int neighbour : neighbours) {
                if (neighbour < 0 || neighbour >= total) {
                    continue;
                }

                // Do not move between different rows horizontally.
                if (Math.abs(neighbour - cell) == 1
                        && neighbour / cols != cell / cols) {
                    continue;
                }

                if (!visited[neighbour] && isOpen(open[cell], neighbour)) {
                    next = neighbour;
                    break;
                }
            }

            if (next != -1) {
                parent[next] = cell;
                visited[next] = true;
                visitedCount++;
                stack[++top] = next;
            } else {
                top--;
            }
        }

        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }

        int length = 0;
        for (int cell = exit; cell != -1; cell = parent[cell]) {
            length++;
        }

        int[] path = new int[length];
        int cell = exit;

        for (int i = length - 1; i >= 0; i--) {
            path[i] = cell;
            cell = parent[cell];
        }

        return new Result(path, visitedCount, deadEnds);

    }

    private static boolean isOpen(int[] neighbours, int target) {
        for (int neighbour : neighbours) {
            if (neighbour == target) {
                return true;
            }
        }
        return false;
    }
}
