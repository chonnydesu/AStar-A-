
import java.util.Random;  import java.util.List;  import java.util.ArrayList;  import java.util.Collections;  import java.util.Scanner;  import java.util.PriorityQueue;  import java.util.Comparator;  import java.util.HashSet;  import java.util.Set;

public class Main 
{
  public static void main(String[] args) 
  {
    Node[][] board = randomBoard();    /** finds neighboring nodes **/

    printBoard(board);                 /** print board **/

    Scanner scan = new Scanner(System.in);

    while (true) 
    {
      System.out.println("");
      System.out.println("Enter starting node (Row 0-14): ");
      int startRow = scan.nextInt();
      System.out.println("Enter starting node (Col 0-14): ");
      int startCol = scan.nextInt();

      System.out.println("Enter goal node (Row 0-14): ");
      int goalRow = scan.nextInt();
      System.out.println("Enter goal node (Col 0-14): ");
      int goalCol = scan.nextInt();

      Node startNode = board[startRow][startCol];
      Node endNode = board[goalRow][goalCol];

      List<Node> path = findPath(board, startNode, endNode);

      if (path != null && startNode.getType() != 1 && endNode.getType() != 1)             /** Solution Found **/
      {
        System.out.println("");
        System.out.println("Path found!");
        System.out.println("");
        System.out.println("Start node -> A        Goal Node -> B");
        System.out.println("");

        for (Node node : path)     /** Re-assign Values and re-print **/
        {
          node.setType(2);
        }
        startNode.setType(3);
        endNode.setType(4);

        printBoard(board);
        System.exit(0);
        
      } 
      else 
      {
        System.out.println("No path found, try again!");
      }
    } 
  }

  /** generate random obstacles **/
  private static Node[][] randomBoard() 
  {
      Node[][] board = new Node[15][15];
      Random random = new Random();

      for (int row = 0; row < 15; row++) 
      {
        for (int col = 0; col < 15; col++) 
        {
          int type = random.nextDouble() < .10 ? 1 : 0;    /** obstacles are "1" **/
          board[row][col] = new Node(row, col, type);
        }
      }
      return board;
  }

  /** prints entire board **/
  private static void printBoard(Node[][] board) 
  {
      for (Node[] row : board) 
      {
          for (Node node : row) 
          {
            if (node.getType() == 0)         /** open **/
            {
              System.out.print("__");
            }
            else if (node.getType() == 1)    /** Obstacle **/
            {
              System.out.print("[]");
            }
            else if (node.getType() == 2)    /** path **/
            {
              System.out.print("X_");
            }
            else if (node.getType() == 3)    /** start **/
              {
                System.out.print("A_");
              }
            else if (node.getType() == 4)    /** finish **/
              {
                System.out.print("B_");
              }
          }
          System.out.println();
      }
  }

  /** calculate 'H' value **/
  private static int heuristic(Node node1, Node node2) 
  {
      return ( Math.abs(node1.getRow() - node2.getRow()) ) 
           + ( Math.abs(node1.getCol() - node2.getCol()) );
  }

  /** finds neighboring nodes **/
  private static List<Node> getNeighbors(Node[][] board, Node node) 
  {
    List<Node> neighbors = new ArrayList<Node>();

    int[] col = { -1, 0, 1, -1, 1, -1, 0, 1 };
    int[] row = { -1, -1, -1, 0, 0, 1, 1, 1 };

    /** cycle through each neighbor **/
    for (int i = 0; i < 8; i++)                   
    {
      int newRow = node.getRow() + row[i];
      int newCol = node.getCol() + col[i];

      /** checks if the node is within board bounds **/
      if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) 
      {
        neighbors.add(board[newRow][newCol]);
      }
    }
    return neighbors;
  }

  /** A star algorithm **/
  private static List<Node> findPath(Node[][] world, Node start, Node end) 
  {
    PriorityQueue<Node> openList = new PriorityQueue<Node>(Comparator.comparingInt(Node::getF));

    Set<Node> closedList = new HashSet<Node>();

    start.setG(0);
    start.setH(heuristic(start, end));
    start.setF();
    openList.add(start);

    while (!openList.isEmpty()) 
    {
      Node current = openList.poll();

      if (current.equals(end))       /** check for solution **/
      {
        return solution(current);
      }

      closedList.add(current);

      for (Node neighbor : getNeighbors(world, current)) 
      {
        /** check if node is already in list or is an obstacle **/
        if (closedList.contains(neighbor) || neighbor.getType() == 1) 
        {
          continue;
        }

        int tentativeG = current.getG() + 1;  /** check if G value is better than before **/

        if (tentativeG < neighbor.getG() || !openList.contains(neighbor)) 
        {
          neighbor.setParent(current);
          neighbor.setG(tentativeG);
          neighbor.setH(heuristic(neighbor, end));
          neighbor.setF();

          if (!openList.contains(neighbor)) 
          {
            openList.add(neighbor);
          }
        }
      }
    }
    return null;     /** NO SOLUTION **/
  }
  
  /** retraces the best path from END to START **/
  private static List<Node> solution(Node node) 
  {
    List<Node> solution = new ArrayList<Node>();
    
    while (node != null) 
    {
      solution.add(node);
      node = node.getParent();
    }
    
    Collections.reverse(solution);  /** flip **/
    return solution;
  }
  
}

