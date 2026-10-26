import java.util.*;

public class Maze {

    public static int[] breakWalls(Input.Walls w) {
        DisjointSet ds = new DisjointSet(w.cells());
        int[] temp = new int[w.count()];
        int brokenCount = 0;

        for (int i = 0; i < w.count(); i++) {
            if (ds.union(w.a()[i], w.b()[i])) {
                temp[brokenCount++] = i;
            }
        }
        return Arrays.copyOf(temp, brokenCount);
    }

    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] indices = new Integer[w.count()];
        for (int i = 0; i < w.count(); i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.comparingInt(i -> w.cost()[i]));

        DisjointSet ds = new DisjointSet(w.cells());
        int[] temp = new int[w.count()];
        int brokenCount = 0;

        for (int i : indices) {
            if (ds.union(w.a()[i], w.b()[i])) {
                temp[brokenCount++] = i;
            }
        }
        return Arrays.copyOf(temp, brokenCount);
    }

    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int[] head = new int[n];
        Arrays.fill(head, -1);

        int m = w.count() * 2;
        int[] to = new int[m];
        int[] cost = new int[m];
        int[] next = new int[m];
        int edgeCount = 0;

        for (int i = 0; i < w.count(); i++) {
            int u = w.a()[i], v = w.b()[i], c = w.cost()[i];

            to[edgeCount] = v; cost[edgeCount] = c; next[edgeCount] = head[u]; head[u] = edgeCount++;
            to[edgeCount] = u; cost[edgeCount] = c; next[edgeCount] = head[v]; head[v] = edgeCount++;
        }

        boolean[] visited = new boolean[n];
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new long[]{0, 0});

        long totalCost = 0;
        int nodesVisited = 0;

        while (!pq.isEmpty() && nodesVisited < n) {
            long[] curr = pq.poll();
            int u = (int) curr[0];

            if (visited[u]) continue;

            visited[u] = true;
            totalCost += curr[1];
            nodesVisited++;

            for (int e = head[u]; e != -1; e = next[e]) {
                int v = to[e];
                if (!visited[v]) {
                    pq.add(new long[]{v, cost[e]});
                }
            }
        }
        return totalCost;
    }
}