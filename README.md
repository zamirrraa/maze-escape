# Assignment 3. Maze Escape

**Student ID:** 251836
**Name:** Yermukan Zamira
**Group:** SE-2510
**Maze size (rows x columns):** 8 x 11

## Program output

The program output from `java -cp out Main`:

```text
Broken: 87
Kept: 70
Last broken wall: 116
MST cost: 26068
Heaviest broken wall: 869
Path length: 29
DFS visited: 80
Dead ends: 25
Zones: 11
Largest zone: 16
Is DAG: 0
Flow stages: 8
Fastest route cost: 22
```

My maze:

![maze](output/maze.png)

## Questions

### Mission 0. What do find and union do, and what makes them fast in your code?

The `find` method finds the representative of a set. The `union` method joins two different sets. My code uses path compression and union by size to make these operations efficient.

### Mission 1. Why is Broken always R*C - 1, and Kept always (R-1)*(C-1)?

The maze has 8 × 11 = 88 cells. To connect all cells without cycles, we need 88 - 1 = 87 broken walls. The total number of walls is 157, so 70 walls are kept. In a rectangular grid, this is equal to (R-1) × (C-1).

### Mission 2. My MST cost from Kruskal and from Prim, and why they are equal:

The MST cost from Kruskal is 26068, and the MST cost from Prim is also
