import java.util.Arrays;
import java.util.Scanner;

public class Cinema {

  private char[][] grid;
  final char FREE = 'S';
  final char BOOKED = 'B';

  final int rows;
  final int seats;
  private int purchasedTickets = 0;
  private int currentIncome = 0;
  final private int totalIncome;


  public static void main(String[] args) throws IllegalAccessException {
    Scanner scanner = new Scanner(System.in);
    int inputRows;
    int inputSeats;

    while (true) {
      System.out.println("Enter the number of rows:");
      inputRows = scanner.nextInt();
      System.out.println("Enter the number of seats in each row:");
      inputSeats = scanner.nextInt();
      if (inputRows > 0 && inputSeats > 0) {
        break;
      } else {
        System.out.println("\nWrong input!\n");
      }
    }

    Cinema cinema = new Cinema(inputRows, inputSeats);
    cinema.initializeGrid();

    while (true) {
      printMenu();
      int menuInput = scanner.nextInt();
      switch (menuInput) {
        case 1: {
          cinema.printGrid();
          break;
        }
        case 2: {
          cinema.buyTicket();
          break;
        }
        case 3: {
          cinema.printStatistics();
          break;
        }
        case 0:
          return;
        default:
          throw new IllegalAccessException("False menu input");
      }
    }
  }

  Cinema(int rows, int seats) {
    this.rows = rows;
    this.seats = seats;
    this.totalIncome = getTotalIncome(rows, seats);
  }

  private void initializeGrid() {
    grid = new char[this.rows][this.seats];
    for (char[] row : grid) {
      Arrays.fill(row, FREE);
    }
  }

  private static void printMenu() {
    String menu = """

        1. Show the seats
        2. Buy a ticket
        3. Statistics
        0. Exit""";
    System.out.println(menu);
  }

  private void printGrid() {
    System.out.println("\nCinema:");
    StringBuilder stringBuilder = new StringBuilder(" ");
    for (int i = 1; i <= grid[0].length; i++) {
      stringBuilder.append(String.format(" %d", i));
    }
    int rowIndex = 1;
    for (char[] row : grid) {
      stringBuilder.append(String.format("%n%d", rowIndex));
      rowIndex++;
      for (char seat : row) {
        stringBuilder.append(String.format(" %c", seat));
      }
    }
    System.out.println(stringBuilder);
  }

  private void buyTicket() {
    Scanner scanner = new Scanner(System.in);
    boolean successfullyBooked = false;

    System.out.println();
    while (!successfullyBooked) {
      System.out.println("Enter a row number:");
      int row = scanner.nextInt();
      System.out.println("Enter a seat number in that row:");
      int seat = scanner.nextInt();

      if (row < 1 || seat < 1 || row > grid.length || seat > grid[0].length) {
        System.out.println("\nWrong input!\n");
        continue;
      }
      if (!isSeatAvailable(row, seat)) {
        System.out.println("\nThat ticket has already been purchased!\n");
        continue;
      }

      int ticketPrice = getTicketPrice(this.rows, this.seats, row);
      bookSeat(row, seat);
      System.out.printf("%nTicket price: $%d%n", ticketPrice);
      currentIncome += ticketPrice;
      successfullyBooked = true;
    }
  }

  private int getTicketPrice(int rows, int seats, int row) {
    if (rows * seats <= 60) {
      return 10;
    } else if (row <= rows / 2) {
      return 10;
    } else {
      return 8;
    }
  }

  private boolean isSeatAvailable(int row, int seat) {
    return grid[row - 1][seat - 1] == FREE;
  }

  private void bookSeat(int row, int seat) {
    grid[row - 1][seat - 1] = BOOKED;
    purchasedTickets++;
  }

  private void printStatistics() {
    String statistics = String.format(
        "%nNumber of purchased tickets: %d%n"
            + "Percentage: %.2f%%%n"
            + "Current income: $%d%n"
            + "Total income: $%d",
        purchasedTickets,
        getOccupancyPercentage(),
        currentIncome,
        totalIncome);
    System.out.println(statistics);
  }


  private float getOccupancyPercentage() {
    return 100f * purchasedTickets / (grid.length * grid[0].length);
  }

  private int getTotalIncome(int rows, int seats) {
    if (rows * seats <= 60) {
      return 10 * rows * seats;
    } else {
      return (int) ((rows / 2) * seats * 10 + Math.ceil(rows / 2.0) * seats * 8);
    }
  }
}