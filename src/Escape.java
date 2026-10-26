import java.util.*;

public class Escape {

    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int[][] g = Graphs.open(w, broken);

        int deadEnds = 0;
        for (int i = 0; i < n; i++) {
            if (g[i].length == 1) deadEnds++;
        }

        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];

        int[] stack = new int[n];
        int top = 0;

        stack[top++] = 0;
        int visitedCount = 0;

        // Directions: up, right, down, left
        int[] dirs = {-cols, 1, cols, -1};

        while (top > 0) {
            int u = stack[--top];

            if (!visited[u]) {
                visited[u] = true;
                visitedCount++;

                if (u == n - 1) {
                    break;
                }

                // Push in reverse order: left, down, right, up
                for (int i = 3; i >= 0; i--) {
                    int nextCell = u + dirs[i];
                    boolean isOpen = false;
                    for (int v : g[u]) {
                        if (v == nextCell) {
                            isOpen = true;
                            break;
                        }
                    }
                    if (isOpen && !visited[nextCell]) {
                        stack[top++] = nextCell;
                        parent[nextCell] = u;
                    }
                }
            }
        }

        int curr = n - 1;
        int[] tempPath = new int[n];
        int idx = 0;

        if (visited[n - 1]) {
            while (curr != -1) {
                tempPath[idx++] = curr;
                curr = parent[curr];
            }
        }

        int[] path = new int[idx];
        for (int i = 0; i < idx; i++) {
            path[i] = tempPath[idx - 1 - i];
        }

        return new Result(path, visitedCount, deadEnds);
    }
}