
// Connect 4 AI game using Minimax algorithm with alpha-beta pruning
// The AI uses a heuristic evaluation function to score the board position
import java.util.Scanner;

public class Connect4AI {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '.';
    private static final int DEPTH = 5;
    private char[][] board = new char[ROWS][COLS];
    private int difficulty = 1; // Default AI difficulty level
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    
    public Connect4AI() {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                board[i][j] = EMPTY;
    }

    public void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7");
    }
    
    public boolean isValidMove(int col) {
        return board[0][col] == EMPTY;
    }
    
    public boolean makeMove(int col, char player) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY) {
                board[i][col] = player;
                return true;
            }
        }
        return false;
    }

    public void undoMove(int col) {
        for (int i = 0; i < ROWS; i++) {
            if (board[i][col] != EMPTY) {
                board[i][col] = EMPTY;
                break;
            }
        }
    }

    public boolean isWin(char player) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (checkDirection(r, c, 1, 0, player) ||
                    checkDirection(r, c, 0, 1, player) ||
                    checkDirection(r, c, 1, 1, player) ||
                    checkDirection(r, c, 1, -1, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int r, int c, int dr, int dc, char player) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int nr = r + dr * i, nc = c + dc * i;
            if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS && board[nr][nc] == player)
                count++;
            else
                break;
        }
        return count == 4;
    }

    public boolean isFull() {
        for (int j = 0; j < COLS; j++) {
            if (board[0][j] == EMPTY) return false;
        }
        return true;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int scorePosition(char player) {
        int score = 0;
        
        int[][] evaluation_board;
        if (difficulty == 1) {
            evaluation_board = new int[][] {
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3}
        };
        } else if (difficulty == 2) {
            evaluation_board = new int[][] { 
            {3, 3, 3, 3, 3, 3, 3},
            {3, 4, 4, 4, 4, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 5, 6, 5, 4, 3},
            {3, 4, 4, 4, 4, 4, 3},
            {3, 3, 3, 3, 3, 3, 3}
        };
        } else {
            evaluation_board = new int[][] { 
            {3, 4, 5, 7, 5, 4, 3},
            {4, 6, 8, 10, 8, 6, 4},
            {5, 8, 11, 13, 11, 8, 5},
            {5, 8, 11, 13, 11, 8, 5},
            {4, 6, 8, 10, 8, 6, 4},
            {3, 4, 5, 7, 5, 4, 3}
        };
        }
        

        int player_score = 0;
        int ai_score = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == PLAYER) {
                    player_score += evaluation_board[r][c];
                } else if (board[r][c] == AI) {
                    ai_score += evaluation_board[r][c];
                }
            }
        }

        score = ai_score - player_score;
        return score;
    }

    public int minimax(int depth, boolean maximizing, int alpha, int beta) {
     
        if (isWin(PLAYER)) return -1000000;
        if (isWin(AI)) return 1000000;
        if (depth == 0 || isFull()) return scorePosition(AI);

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (isValidMove(col)) {
                    makeMove(col, AI);
                    int eval = minimax(depth - 1, false, alpha, beta);
                    undoMove(col);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (isValidMove(col)) {
                    makeMove(col, PLAYER);
                    int eval = minimax(depth - 1, true, alpha, beta);
                    undoMove(col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }
  
    public int bestMove() {
        int bestVal = Integer.MIN_VALUE;
        int move = -1;
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                makeMove(col, AI);
                int moveVal = minimax(DEPTH, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                undoMove(col);
                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    move = col;
                }
            }
        }
        return move;
    }
    public char[][] getBoard() {
        return board;
    }
    
   
}
