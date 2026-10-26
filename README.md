# Assignment 3. Maze Escape

**Student Name:** Tagay Talgar
**Student ID:** 252955

## 1. Output
```text
Broken: 98
Kept: 80
Last broken wall: 139
MST cost: 29929
Heaviest broken wall: 762
Path length: 26
DFS visited: 43
Dead ends: 30
Zones: 11
Largest zone: 12
Is DAG: 0
Flow stages: 8
Fastest route cost: 15
```

## 2. Answers to Questions

* **Mission 1: Why Broken is always RC-1, and why Kept is always (R-1)*(C-1)?**
A fully connected maze without loops is mathematically a Minimum Spanning Tree (MST) of the grid graph. A tree connecting `V` vertices always has exactly `V-1` edges. Here, the number of cells (vertices) is `V = R*C`, so we must break exactly `R*C - 1` walls to connect them all. 
The total number of inner walls in a grid is `R*(C-1) + C*(R-1) = 2RC - R - C`. Subtracting the broken walls `(RC - 1)` gives the number of kept walls: `2RC - R - C - RC + 1 = RC - R - C + 1`, which perfectly factors into `(R-1)*(C-1)`.

* **Mission 3: Why is there exactly one path?**
Because the generated maze is a spanning tree. By the definition of a tree, there are no cycles (meaning no alternative intersecting routes), and the graph is fully connected (meaning there is always at least one path). Therefore, there is one and only one simple path between any two cells, including the start and the exit.

* **Mission 4: Why does the second pass go on the reversed graph, and why do the slides have no cycle exactly when Zones equals the number of cells?**
In Kosaraju's algorithm, the second pass uses the reversed graph to prevent the DFS from "escaping" the current Strongly Connected Component (SCC). Processing vertices in decreasing order of their finishing times on the reversed edges guarantees that we only visit nodes within the same SCC. 
If Zones equals the number of cells, it means every single cell is its own independent SCC of size 1. Since an SCC of size 1 cannot contain a loop with other cells, it proves that the entire graph is a Directed Acyclic Graph (DAG) with zero cycles.

* **Mission 5: Why is this topological order enough, and what is the cost of the pass?**
Processing the zones in topological order is enough because a zone can only have outgoing slides pointing to zones that appear later in the order. By the time the loop reaches a specific zone, all possible incoming routes to it have already been fully calculated, so we don't need a Priority Queue (like in Dijkstra). The time complexity (cost) of this pass is `O(V + E)`, where V is the number of cells and E is the number of slides, because we iterate over each cell and its outgoing slides exactly once.

## 3. Algorithm Complexity

| Algorithm | Time Complexity | Space Complexity |
| :--- | :--- | :--- |
| **DSU (find / union)** | O(α(V)) | O(V) |
| **Mission 1 (Build Maze)** | O(E * α(V)) | O(V + E) |
| **Mission 2 (Kruskal)** | O(E log E) | O(V + E) |
| **Mission 2 (Prim)** | O(E log V) | O(V + E) |
| **Mission 3 (DFS Escape)** | O(V + E) | O(V) |
| **Mission 4 (Kosaraju)** | O(V + E) | O(V + E) |
| **Mission 5 (Routes)** | O(V + E) | O(V + E) |

*(Note: `V` is the number of cells, `E` is the number of walls/slides, and `α` is the inverse Ackermann function).*

## 4. Benchmarking (Timing)

```text
size   kruskal_ms (5 runs, then average)   prim_ms (5 runs, then average)
100    17 12 15 21 17  avg 16              18 7 5 4 4  avg 8
300    142 132 73 71 67  avg 97            71 82 70 87 85  avg 79
1000   1267 1167 1241 1241 1223 avg 1228   1533 1304 1547 1449 1294 avg 1425
```
**Observation:**
As the maze size increases from 300x300 to 1000x1000, the number of vertices and edges grows by about 11 times. Accordingly, Kruskal's time increases from 97ms to 1228ms (~12.6x), and Prim's time increases from 79ms to 1425ms (~18x). This non-linear, predictable scaling clearly demonstrates and confirms the `O(E log V)` theoretical time complexity for both MST algorithms.

## 5. AI Use

* **Tool used:** Google Gemini
* **Purpose of use:** Used strictly as a learning assistant and tutor in accordance with the syllabus rules. I used the AI to explain the theoretical concepts behind Kosaraju's algorithm and how to implement DFS iteratively using an array-stack to avoid StackOverflow errors on the 1000x1000 mazes. The AI also helped clarify mathematical proofs (like the formula for kept walls) and verify my Time/Space complexity table. All generated structural concepts were analyzed, typed, and fully understood by me to ensure I can confidently explain every line of code during the defense.