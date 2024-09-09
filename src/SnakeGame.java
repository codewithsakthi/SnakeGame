import java.util.*;

public class SnakeGame {
    private static int score = 1;
    private static int[] head = new int[]{0, 0};
    private static char[][] board;
    private static Set<String> body = new HashSet<>();
    private static int[] food = new int[2];
    private static Deque<int[]> dq = new LinkedList<>();
    private static final char Snake = 'S';
    private static final char Empty = '.';
    private static final char Food = 'F';
    private static final String Reset = "\u001b[00m";
    private static final String Red = "\u001b[31m";
    private static final String Smoke = "\u001b[25m";
    private static final String Neon = "\u001b[34m";
    private static int height, width;
    private static final int[][] directions = {
            {-1, 0},  // Up
            {1, 0},   // Down
            {0, -1},  // Left
            {0, 1}    // Right
    };

    private static void initialize(int h, int w) {
        height = h;
        width = w;
        board = new char[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = Empty;
            }
        }

        dq.add(head.clone());
        body.add(head[0] + "," + head[1]);
        board[head[0]][head[1]] = Snake;
        generateFood();
    }

    private static boolean isGameOver(int[] newHead) {
        return newHead[0] < 0 || newHead[0] >= height || newHead[1] < 0 || newHead[1] >= width || body.contains(newHead[0] + "," + newHead[1]);
    }

    private static boolean move(int dx, int dy) {
        int[] newHead = new int[]{head[0] + dx, head[1] + dy};

        if (isGameOver(newHead)) {
            System.out.println("Game Over!");
            return false;
        }

        boolean foodEaten = Arrays.equals(newHead, food);

        dq.addFirst(newHead);
        body.add(newHead[0] + "," + newHead[1]);
        board[newHead[0]][newHead[1]] = Snake;

        // If food is eaten, generate new food, otherwise remove the tail
        if (foodEaten) {
            score++;
            generateFood();
        } else {
            int[] tail = dq.removeLast();
            body.remove(tail[0] + "," + tail[1]);
            board[tail[0]][tail[1]] = Empty;
        }

        head = newHead;
        return true;
    }

    private static void generateFood() {
        Random random = new Random();
        while (true) {
            int row = random.nextInt(height);
            int col = random.nextInt(width);
            if (!body.contains(row + "," + col)) {
                food[0] = row;
                food[1] = col;
                board[row][col] = Food;
                break;
            }
        }
    }

    private static void playGame() {
        boolean gameOver = false;
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            printBoard();
            System.out.println("Enter direction (W/A/S/D)");
            char direction = scanner.next().toUpperCase().charAt(0);
            int dx = 0, dy = 0;

            switch (direction) {
                case 'W':
                    dx = directions[0][0];
                    dy = directions[0][1];
                    break;
                case 'S':
                    dx = directions[1][0];
                    dy = directions[1][1];
                    break;
                case 'A':
                    dx = directions[2][0];
                    dy = directions[2][1];
                    break;
                case 'D':
                    dx = directions[3][0];
                    dy = directions[3][1];
                    break;
                default:
                    System.out.println("Invalid input. Use W/A/S/D.");
                    continue;
            }

            if (!move(dx, dy)) {
                gameOver = true;
            }
        }
    }

    private static void printBoard() {
        int row = board.length;
        int col = board[0].length;
        System.out.println("Current Score :"+score);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                char c = board[i][j];
                switch (c) {
                    case Food:
                        System.out.print(Smoke + c + " " + Reset);
                        break;
                    case Snake:
                        System.out.print(Red + c + " " + Reset);
                        break;
                    case Empty:
                        System.out.print(Neon + c + " " + Reset);
                        break;
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter board height and width:");
        int height = scanner.nextInt();
        int width = scanner.nextInt();
        initialize(height, width);
        playGame();
    }
}
