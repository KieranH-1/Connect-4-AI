import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4AIGUI extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private static final char EMPTY = '.';
    private JButton[][] buttons = new JButton[ROWS][COLS];
    private Connect4AI game = new Connect4AI();
    private boolean playerGoesFirst = true; 

    public Connect4AIGUI() {
        setTitle("Connect 4 AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(ROWS, COLS));
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                buttons[r][c] = new JButton();
                buttons[r][c].setFont(new Font("Arial", Font.BOLD, 20));
                buttons[r][c].setEnabled(false);
                boardPanel.add(buttons[r][c]);
            }
        }

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, COLS));
        for (int c = 0; c < COLS; c++) {
            int col = c;
            JButton dropButton = new JButton("Drop");
            dropButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game.isValidMove(col)) {
                        game.makeMove(col, PLAYER);
                        updateBoard();
                        if (game.isWin(PLAYER)) {
                            JOptionPane.showMessageDialog(null, "You win!");
                            resetGame();
                            return;
                        }
                        if (game.isFull()) {
                            JOptionPane.showMessageDialog(null, "It's a draw!");
                            resetGame();
                            return;
                        }

                        int aiMove = game.bestMove();
                        game.makeMove(aiMove, AI);
                        updateBoard();
                        if (game.isWin(AI)) {
                            JOptionPane.showMessageDialog(null, "AI wins!");
                            resetGame();
                        } else if (game.isFull()) {
                            JOptionPane.showMessageDialog(null, "It's a draw!");
                            resetGame();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid move. Try again.");
                    }
                }
            });
            controlPanel.add(dropButton);
        }

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);

        chooseSettings();

        if (!playerGoesFirst) {
            int aiMove = game.bestMove();
            game.makeMove(aiMove, AI);
        }

        resetGame();
    }

    private void chooseSettings() {
        String[] orderOptions = {"Go First", "Go Second"};
        int orderChoice = JOptionPane.showOptionDialog(
            this,
            "Do you want to go first or second?",
            "Choose Order",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            orderOptions,
            orderOptions[0]
        );
        playerGoesFirst = (orderChoice == 0);

        String[] difficultyOptions = {"Easy", "Medium", "Hard"};
        int difficultyChoice = JOptionPane.showOptionDialog(
            this,
            "Choose AI difficulty:",
            "AI Difficulty",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            difficultyOptions,
            difficultyOptions[0]
        );
        game.setDifficulty(difficultyChoice + 1);
    }

    private void updateBoard() {
        char[][] board = game.getBoard();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                buttons[r][c].setText(board[r][c] == EMPTY ? "" : String.valueOf(board[r][c]));
            }
        }
    }

    private void resetGame() {
        game = new Connect4AI();
        if (!playerGoesFirst) {
            int aiMove = game.bestMove();
            game.makeMove(aiMove, AI);
        }
        updateBoard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Connect4AIGUI().setVisible(true);
        });
    }
}