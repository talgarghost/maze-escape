import java.util.*;

public class Zones {

    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);

        int[] order = new int[n];
        int orderIdx = 0;
        boolean[] vis = new boolean[n];
        int[] edgeIdx = new int[n];

        int[] stack = new int[n];
        int top = 0;

        // Pass 1
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                stack[top++] = i;
                vis[i] = true;

                while (top > 0) {
                    int u = stack[top - 1];
                    if (edgeIdx[u] < from[u].length) {
                        int slideIdx = from[u][edgeIdx[u]++];
                        int v = s.to()[slideIdx];
                        if (!vis[v]) {
                            vis[v] = true;
                            stack[top++] = v;
                        }
                    } else {
                        order[orderIdx++] = stack[--top];
                    }
                }
            }
        }

        // Pass 2
        int[][] into = Graphs.slidesInto(s);
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int currentZone = 0;

        for (int i = n - 1; i >= 0; i--) {
            int start = order[i];
            if (zoneOf[start] == -1) {
                stack[top++] = start;
                zoneOf[start] = currentZone;

                while (top > 0) {
                    int u = stack[--top];
                    for (int slideIdx : into[u]) {
                        int v = s.from()[slideIdx];
                        if (zoneOf[v] == -1) {
                            zoneOf[v] = currentZone;
                            stack[top++] = v;
                        }
                    }
                }
                currentZone++;
            }
        }
        return zoneOf;
    }

    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        int[] stages = new int[zoneCount];
        Arrays.fill(stages, 1);

        long[] cost = new long[zoneCount];
        Arrays.fill(cost, Long.MAX_VALUE);
        cost[zoneOf[0]] = 0;

        int[][] from = Graphs.slidesFrom(s);

        // Group cells by zone for O(V+E) performance
        int[] head = new int[zoneCount];
        Arrays.fill(head, -1);
        int[] next = new int[s.n()];
        for (int i = 0; i < s.n(); i++) {
            int z = zoneOf[i];
            next[i] = head[z];
            head[z] = i;
        }

        for (int z = 0; z < zoneCount; z++) {
            for (int u = head[z]; u != -1; u = next[u]) {
                for (int slideIdx : from[u]) {
                    int v = s.to()[slideIdx];
                    int zoneV = zoneOf[v];

                    if (z != zoneV) {
                        stages[zoneV] = Math.max(stages[zoneV], stages[z] + 1);
                        if (cost[z] != Long.MAX_VALUE) {
                            cost[zoneV] = Math.min(cost[zoneV], cost[z] + s.time()[slideIdx]);
                        }
                    }
                }
            }
        }

        long maxStages = 0;
        for (int st : stages) {
            maxStages = Math.max(maxStages, st);
        }

        return new long[]{maxStages, cost[zoneOf[s.n() - 1]]};
    }
}