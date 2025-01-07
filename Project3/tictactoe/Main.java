package tictactoe;

import java.util.Scanner;

public class Main {

    enum StateGame {
        GAMENOTFINISHED("Game not finished"),
        DRAW("Draw"),
        XWINS("X wins"),
        OWINS("O wins"),
        IMPOSSIBLE("Impossible");

        StateGame(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }

        String state;
    }

    enum StateInput {
        OCCUPIED("This cell is occupied! Choose another one!"),
        NOTNUMBER("You should enter numbers!"),
        COORDINATESLENGTH("Coordinates should be 2: number row and number column"),
        OUTOFRANGE("Coordinates should be from 1 to 3!");

        StateInput(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }

        String state;
    }

    enum Symbols {

        X('X'),
        O('O'),
        SPACE('_');

        Symbols(char symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return Character.toString(symbol);
        }

        private final char symbol;
    }

    private static Player currentPlayer;

    private static final String START_GRID = "_________";

    private static Player[] players = new Player[2];

    private static final int ROW = 3;

    private static final int COLUMN = 3;

    private static Playground playground;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        start(START_GRID);
        scanner.close();
    }

    private static void start(String startGrid) {
        if (startGrid.length() == ROW * COLUMN) {
            playground = new Playground(startGrid);

            playground.outputGrid();

            Player player1 = new Player(Symbols.X);
            Player player2 = new Player(Symbols.O);

            setPriority(player1, player2);

            StateGame stateGame = checkStateGame();
            while (stateGame == StateGame.GAMENOTFINISHED) {
                for (Player player : players) {
                    currentPlayer = player;
                    player.makeAMove();
                    stateGame = checkStateGame();
                    if (stateGame != StateGame.GAMENOTFINISHED) {
                        break;
                    }
                }
            }
            System.out.println(stateGame);
        }
    }

    private static void setPriority(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    private static class Player {

        public Player(Symbols symbol) {
            this.symbol = symbol;
        }

        private Symbols symbol;

        public Symbols getSymbol() {
            return symbol;
        }

        public void setSymbol(Symbols symbol) {
            this.symbol = symbol;
        }

        private void makeAMove() {
            inputUserData();
        }
    }

    private static void inputUserData() {
        int countCoordinates = 2;
        int[] coordinates = new int[countCoordinates];

        while (!isSuccessInputUserData(coordinates)) {
            coordinates = new int[countCoordinates];
        }

        playground.setCell(coordinates);
        playground.outputGrid();
    }

    private static boolean isSuccessInputUserData(int[] coordinates) {
        Scanner scannerString = new Scanner(scanner.nextLine());

        for (int i = 0; i < coordinates.length; i++) {
            if (scannerString.hasNextInt()) {
                int number = scannerString.nextInt();
                boolean outOfRange = number < 1 || number > ROW;
                if (outOfRange) {
                    System.out.println(StateInput.OUTOFRANGE);
                    scannerString.close();
                    return false;
                }
                coordinates[i] = number;
            } else {
                System.out.println(StateInput.NOTNUMBER);
                scannerString.close();
                return false;
            }
        }
        if (scannerString.hasNext()) {
            System.out.println(StateInput.COORDINATESLENGTH);
            scannerString.close();
            return false;
        }
        scannerString.close();
        if (playground.isOccupiedCell(coordinates[0], coordinates[1])) {
            System.out.println(StateInput.OCCUPIED);
            return false;
        }
        return true;
    }

    private static StateGame checkStateGame() {
        Winners winners = playground.getWinner();

        if (playground.checkStateImpossible(winners)) {
            return StateGame.IMPOSSIBLE;
        }
        if (winners.isXWins) {
            return StateGame.XWINS;
        }
        if (winners.isOWins) {
            return StateGame.OWINS;
        }
        if (playground.getCountSpace() == 0) {
            return StateGame.DRAW;
        }
        return StateGame.GAMENOTFINISHED;
    }

    private static class Winners {
        boolean isXWins;
        boolean isOWins;
    }

    private static class Playground {

        private class Tictactoe {

            private Symbols symbol;

            public Tictactoe(char tictactoe) {
                if (tictactoe == 'X') {
                    symbol = Symbols.X;
                } else {
                    symbol = tictactoe == 'O' ? Symbols.O : Symbols.SPACE;
                }
            }

            /**
             * @return the symbol
             */
            public Symbols getSymbol() {
                return symbol;
            }

            @Override
            public String toString() {
                return getSymbol().toString();
            }
        }

        private Tictactoe[][] grid = new Tictactoe[ROW][COLUMN];
        private int countX;
        private int countO;
        private int countSpace;

        private Playground(String firstStateGrid) {
            inputValues(firstStateGrid);
        }

        public void setCell(int[] coordinates) {
            Tictactoe tictactoe = new Tictactoe(currentPlayer.getSymbol().symbol);
            int row = coordinates[0] - 1;
            int col = coordinates[1] - 1;
            grid[row][col] = tictactoe;
        }

        private void inputValues(String userInput) {
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    int indexSymbol = row + col + (row * (grid.length - 1));
                    char symbol = userInput.charAt(indexSymbol);
                    Tictactoe tictactoe = new Tictactoe(symbol);
                    fillCount(tictactoe);
                    grid[row][col] = tictactoe;
                }
            }
        }

        public boolean isOccupiedCell(int i, int j) {
            return (grid[i - 1][j - 1].getSymbol() != Symbols.SPACE);
        }

        public int getCountSpace() {
            return countSpace;
        }

        public Main.Winners getWinner() {
            Winners winners = new Winners();
            checkLines(winners);
            return winners;
        }

        private void checkLines(Main.Winners winners) {
            checkHorizontalLine(winners);
            checkVerticalLine(winners);
            checkDiagonalLine(winners);
        }

        private void checkDiagonalLine(Main.Winners winners) {
            checkLeftToRightDiagonal(winners);
            checkRightToLeftDiagonal(winners);
        }

        private void checkLeftToRightDiagonal(Main.Winners winners) {
            boolean winX = true;
            boolean winO = true;
            for (int i = 0; i < grid.length; i++) {
                boolean conditionX = grid[i][i].getSymbol() == Symbols.X;
                boolean conditionO = grid[i][i].getSymbol() == Symbols.O;
                winX &= conditionX;
                winO &= conditionO;
            }
            winners.isXWins = !winners.isXWins ? winX : winners.isXWins;
            winners.isOWins = !winners.isOWins ? winO : winners.isOWins;
        }

        private void checkRightToLeftDiagonal(Main.Winners winners) {
            boolean winX = true;
            boolean winO = true;
            int maxIndex = grid.length - 1;
            for (int i = 0, j = maxIndex; i < grid.length; i++, j -= maxIndex) {
                boolean conditionX = grid[i][i + j].getSymbol() == Symbols.X;
                boolean conditionO = grid[i][i + j].getSymbol() == Symbols.O;
                winX &= conditionX;
                winO &= conditionO;
            }
            winners.isXWins = !winners.isXWins ? winX : winners.isXWins;
            winners.isOWins = !winners.isOWins ? winO : winners.isOWins;
        }

        private void checkVerticalLine(Main.Winners winners) {
            for (int row = 0; row < grid.length; row++) {
                boolean winX = true;
                boolean winO = true;
                for (int col = 0; col < grid[row].length; col++) {
                    winX &= grid[col][row].getSymbol() == Symbols.X;
                    winO &= grid[col][row].getSymbol() == Symbols.O;
                }
                winners.isXWins = !winners.isXWins ? winX : winners.isXWins;
                winners.isOWins = !winners.isOWins ? winO : winners.isOWins;
            }
        }

        private void checkHorizontalLine(Main.Winners winners) {
            for (int row = 0; row < grid.length; row++) {
                boolean winX = true;
                boolean winO = true;
                for (int col = 0; col < grid[row].length; col++) {
                    winX &= grid[row][col].getSymbol() == Symbols.X;
                    winO &= grid[row][col].getSymbol() == Symbols.O;
                }
                winners.isXWins = !winners.isXWins ? winX : winners.isXWins;
                winners.isOWins = !winners.isOWins ? winO : winners.isOWins;
            }
        }

        public boolean checkStateImpossible(Main.Winners winners) {
            return Math.abs(countO - countX) > 1
                    || (winners.isOWins && winners.isXWins);
        }

        private void fillCount(Tictactoe tictactoe) {
            switch (tictactoe.getSymbol()) {
                case X -> countX++;
                case O -> countO++;
                default -> countSpace++;
            }
        }

        private void outputGrid() {
            System.out.println("---------");
            for (int i = 0; i < ROW; i++) {
                System.out.print("| ");
                for (int j = 0; j < COLUMN; j++) {
                    System.out.print(grid[i][j] + " ");
                }
                System.out.println("|");
            }
            System.out.println("---------");

            // System.out.println("---------");
            // for (int i = 0; i < ROW * COLUMN; i++) {
            // if (i % COLUMN == 0) {
            // System.out.print("| ");
            // }
            // System.out.print(grid[i / ROW][i % COLUMN] + " ");
            // if (i % COLUMN == (COLUMN - 1)) {
            // System.out.println("|");
            // }
            // }
            // System.out.println("---------");

        }
    }

    private Main() {
    }

}