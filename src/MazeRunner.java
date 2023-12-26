
/*Author: Anurag Bhattarai
 *Date  : 11/21/2023
 *Purpose: In this program we implemented prims algorithm to find the maze and applied BFS to find the shortest path.
 */





import java.util.*;

public class MazeRunner {
    private int rows;
    private int cols;
    private char[][] maze;
    private Random random;

    // Constructor to initialize MazeRunner with rows, columns, maze grid, and Random object
    public MazeRunner(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new char[rows][cols];
        this.random = new Random();
    }

    // Method to generate the maze using Prim's algorithm
    public void generateMaze() {
        // Initialize maze with walls ('#')
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], '#');
        }

        // Randomly select start and end points for the maze
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);
        generatePaths(startRow, startCol);

        // Set 'S' as the start point and 'E' as the end point in the maze
        maze[startRow][startCol] = 'S';
        int endRow = random.nextInt(rows);
        int endCol = random.nextInt(cols);
        maze[endRow][endCol] = 'E';
    }

    // Recursive method to create paths in the maze using Prim's algorithm
    private void generatePaths(int row, int col) {
        PriorityQueue<int[]> walls = new PriorityQueue<>(Comparator.comparingInt(a -> random.nextInt()));
        walls.addAll(getWalls(row, col));

        while (!walls.isEmpty()) {
            int[] wall = walls.poll();
            int x = wall[0];
            int y = wall[1];
            int nx = wall[2];
            int ny = wall[3];

            if (maze[nx][ny] == '#') {
                maze[x][y] = maze[(x + nx) / 2][(y + ny) / 2] = ' ';
                generatePaths(nx, ny);
                walls.addAll(getWalls(nx, ny));
            }
        }
    }

    // Method to retrieve walls for a given cell
    private List<int[]> getWalls(int row, int col) {
        List<int[]> walls = new ArrayList<>();
        if (row >= 2) walls.add(new int[]{row, col, row - 2, col});
        if (row < rows - 2) walls.add(new int[]{row, col, row + 2, col});
        if (col >= 2) walls.add(new int[]{row, col, row, col - 2});
        if (col < cols - 2) walls.add(new int[]{row, col, row, col + 2});
        return walls;
    }

    // Method to display the maze grid
    public void displayMaze() {
        for (char[] row : maze) {
            System.out.println(row);
        }
    }

    // Method to solve the maze using BFS and mark the solution path in the maze
    public List<int[]> solveMaze() {
        int[] start = findStart();
        Queue<int[]> queue = new LinkedList<>();
        Map<int[], int[]> parent = new HashMap<>();
        queue.offer(start);
        parent.put(start, null);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (maze[x][y] == 'E') {
                List<int[]> path = new ArrayList<>();
                while (parent.get(current) != null) {
                    path.add(current);
                    current = parent.get(current);
                }
                path.add(start);
                Collections.reverse(path);
                markSolutionPath(path);
                return path;
            }

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                int[] next = new int[]{nx, ny};

                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && maze[nx][ny] != '#' && !parent.containsKey(next)) {
                    queue.offer(next);
                    parent.put(next, current);
                }
            }
        }
        return null;
    }

    // Method to mark the solution path in the maze grid
    private void markSolutionPath(List<int[]> path) {
        for (int[] step : path) {
            if (maze[step[0]][step[1]] != 'S' && maze[step[0]][step[1]] != 'E') {
                maze[step[0]][step[1]] = 'o';
            }
        }
    }

    // Method to find the start point ('S') in the maze
    private int[] findStart() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Main method to run the maze generation, solving, and displaying functionalities
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the maze runner.");
        System.out.print("How many columns? ");
        int columns = scanner.nextInt();
        System.out.print("How many rows? ");
        int rows = scanner.nextInt();

        MazeRunner mazeRunner = new MazeRunner(rows, columns);
        mazeRunner.generateMaze();

        System.out.println(rows + "x" + columns + " Maze");
        mazeRunner.displayMaze();

        System.out.print("Would you like to see the solution (y/n)? ");
        char choice = scanner.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            List<int[]> solution = mazeRunner.solveMaze();
            if (solution != null) {
                System.out.println(rows + "x" + columns + " Maze Solution");
                mazeRunner.displayMaze();
            } else {
                System.out.println("No solution found.");
            }
        }

        System.out.println("Thank you for using Maze Runner!");
        scanner.close();
    }
}
