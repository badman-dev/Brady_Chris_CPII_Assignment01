import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Scanner;

public class TicTacToeFrame extends JFrame {
    JPanel titlePnl;
    JPanel gamePnl;
    JPanel turnPnl;
    JLabel titleLbl;
    JLabel turnLbl;

    private static TicTacToeTile[] ticBtns;

    private static final int ROW = 3;
    private static final int COL = 3;
    private static String[][] board = new String[ROW][COL];

    boolean finished = false;
    boolean playing = true;
    String player = "X";
    int moveCnt = 0;
    final int MOVES_FOR_WIN = 5;
    final int MOVES_FOR_TIE = 7;
    public TicTacToeFrame() {
        createTitlePanel();
        createGamePanel();
        createTurnPanel();

        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();

        setTitle("Tic Tac Toe");
        setSize(360, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        clearBoard();
        display();
    }

    private void createTitlePanel() {
        titlePnl = new JPanel();
        titleLbl = new JLabel("TIC TAC TOE");
        titleLbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        titlePnl.add(titleLbl);
        add(titlePnl, BorderLayout.NORTH);
    }

    private void createGamePanel() {
        gamePnl = new JPanel();
        gamePnl.setLayout(new GridLayout(3,3));
        ticBtns = new TicTacToeTile[]{
                new TicTacToeTile(0,0),
                new TicTacToeTile(0,1),
                new TicTacToeTile(0,2),
                new TicTacToeTile(1,0),
                new TicTacToeTile(1,1),
                new TicTacToeTile(1,2),
                new TicTacToeTile(2,0),
                new TicTacToeTile(2,1),
                new TicTacToeTile(2,2)
        };

        for (TicTacToeTile ticBtn : ticBtns)
        {
            ticBtn.addActionListener((ActionEvent ae) -> playerMove(ticBtn.getRow(), ticBtn.getCol()));
            gamePnl.add(ticBtn);
        }
        add(gamePnl, BorderLayout.CENTER);
    }

    private void createTurnPanel() {
        turnPnl = new JPanel();
        turnLbl = new JLabel("TURN LABEL");

        turnPnl.add(turnLbl);
        add(turnPnl, BorderLayout.SOUTH);
    }

    private void playerMove(int row, int col) {
        if (!playing) {
            return;
        }
        if (!isValidMove(row, col)) {
            JOptionPane.showMessageDialog(null, "Illegal move!");
            return;
        }

        board[row][col] = player;
        moveCnt++;

        if(moveCnt >= MOVES_FOR_WIN)
        {
            if(isWin(player))
            {
                display();
                System.out.println("Player " + player + " wins!");
                playing = false;
                JOptionPane.showMessageDialog(null, "Player " + player + " wins!");
                int answer = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play again?", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    clearBoard();
                    display();
                    playing = true;
                }
            }
        }
        if(moveCnt >= MOVES_FOR_TIE)
        {
            if(isTie())
            {
                display();
                System.out.println("It's a Tie!");
                playing = false;
                JOptionPane.showMessageDialog(null, "It's a tie!");
                int answer = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play again?", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    clearBoard();
                    display();
                    playing = true;
                }
            }
        }
        if (!playing) {

        }
        if(player.equals("X"))
        {
            player = "O";
        }
        else
        {
            player = "X";
        }

        display();
    }

    private static void clearBoard()
    {
        // sets all the board elements to a space
        for(int row=0; row < ROW; row++)
        {
            for(int col=0; col < COL; col++)
            {
                board[row][col] = " ";
            }
        }
    }
    private void display()
    {
        // shows the Tic Tac Toe game
        for(int row=0; row < ROW; row++)
        {
            System.out.print("| ");
            for(int col=0; col < COL; col++)
            {
                for (TicTacToeTile ticBtn : ticBtns) {
                    if (ticBtn.getRow() == row && ticBtn.getCol() == col) {
                        ticBtn.setText(board[row][col]);
                    }
                }
                System.out.print(board[row][col] + " | ");
            }
            System.out.println();
        }

        turnLbl.setText("Player " + player + "'s turn.");

    }
    private static boolean isValidMove(int row, int col)
    {
        boolean retVal = false;
        if(board[row][col].equals(" "))
            retVal = true;

        return retVal;

    }
    private static boolean isWin(String player)
    {
        if(isColWin(player) || isRowWin(player) || isDiagnalWin(player))
        {
            return true;
        }

        return false;
    }
    private static boolean isColWin(String player)
    {
        // checks for a col win for specified player
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals(player) &&
                    board[1][col].equals(player) &&
                    board[2][col].equals(player))
            {
                return true;
            }
        }
        return false; // no col win
    }
    private static boolean isRowWin(String player)
    {
        // checks for a row win for the specified player
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals(player) &&
                    board[row][1].equals(player) &&
                    board[row][2].equals(player))
            {
                return true;
            }
        }
        return false; // no row win
    }
    private static boolean isDiagnalWin(String player)
    {
        // checks for a diagonal win for the specified player

        if(board[0][0].equals(player) &&
                board[1][1].equals(player) &&
                board[2][2].equals(player) )
        {
            return true;
        }
        if(board[0][2].equals(player) &&
                board[1][1].equals(player) &&
                board[2][0].equals(player) )
        {
            return true;
        }
        return false;
    }

    // checks for a tie before board is filled.
    // check for the win first to be efficient
    private static boolean isTie()
    {
        boolean xFlag = false;
        boolean oFlag = false;
        // Check all 8 win vectors for an X and O so
        // no win is possible
        // Check for row ties
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals("X") ||
                    board[row][1].equals("X") ||
                    board[row][2].equals("X"))
            {
                xFlag = true; // there is an X in this row
            }
            if(board[row][0].equals("O") ||
                    board[row][1].equals("O") ||
                    board[row][2].equals("O"))
            {
                oFlag = true; // there is an O in this row
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a row win
            }

            xFlag = oFlag = false;

        }
        // Now scan the columns
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals("X") ||
                    board[1][col].equals("X") ||
                    board[2][col].equals("X"))
            {
                xFlag = true; // there is an X in this col
            }
            if(board[0][col].equals("O") ||
                    board[1][col].equals("O") ||
                    board[2][col].equals("O"))
            {
                oFlag = true; // there is an O in this col
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a col win
            }
        }
        // Now check for the diagonals
        xFlag = oFlag = false;

        if(board[0][0].equals("X") ||
                board[1][1].equals("X") ||
                board[2][2].equals("X") )
        {
            xFlag = true;
        }
        if(board[0][0].equals("O") ||
                board[1][1].equals("O") ||
                board[2][2].equals("O") )
        {
            oFlag = true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }
        xFlag = oFlag = false;

        if(board[0][2].equals("X") ||
                board[1][1].equals("X") ||
                board[2][0].equals("X") )
        {
            xFlag =  true;
        }
        if(board[0][2].equals("O") ||
                board[1][1].equals("O") ||
                board[2][0].equals("O") )
        {
            oFlag =  true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }

        // Checked every vector so I know I have a tie
        return true;
    }
}
