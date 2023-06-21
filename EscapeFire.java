/* You are given a 0-indexed 2D integer array grid of size m x n which represents a field. Each cell has one of three 
values:
1)- 0 represents grass,
2)- 1 represents fire,
3)- 2 represents a wall that you and fire cannot pass through.
You are situated in the top-left cell, (0, 0), and you want to travel to the safehouse at the bottom-right cell, 
(m - 1, n - 1). Every minute, you may move to an adjacent grass cell. After your move, every fire cell will spread to 
all adjacent cells that are not walls.
Return the maximum number of minutes that you can stay in your initial position before moving while still safely 
reaching the safehouse. If this is impossible, return -1. Note that even if the fire spreads to the safehouse 
immediately after you have reached it, it will be counted as safely reaching the safehouse. A cell is adjacent to 
another cell if the former is directly north, east, south, or west of the latter (i.e., their sides are touching).
* Eg 1 : grid = [[0,2,0,0,0,0,0],[0,0,0,2,2,1,0],[0,2,0,0,1,2,0],           Output = 3  
*                [0,0,2,2,2,0,2],[0,0,0,0,0,0,0]]                                       
* Eg 2 : grid = [[0,0,0,0],[0,1,2,0],[0,2,0,0]]                             Output = -1 
* Eg 3 : grid = [[0,0,0,0,2,2,2,0],[0,2,2,0,0,1,2,0],[0,2,2,2,0,0,2,0],     Output = 5  
*                [0,2,0,2,2,2,2,0],[0,0,0,0,0,0,0,0]]                                   
*/
import java.util.*;
public class EscapeFire
{
      public int MaximumMinutes(int grid[][])
      {
            int fire[][] = new int[grid.length][grid[0].length];     //* Matrix to check Fire Spread -> O(N x M)
            int walk[][] = new int[grid.length][grid[0].length];     //* Matrix to check time to walk -> O(N x M)
            Queue<int[]> burnedCell = new LinkedList<int[]>();     //* Queue for Fire Matrix -> O(N x M)
            Queue<int[]> walkQueue = new LinkedList<int[]>();      //* Queue for Walk Matrix -> O(N x M)
            walkQueue.add(new int[]{0, 0});     // Starting node as (0,0)...
            walk[0][0] = 0;
            for(int i = 0; i < grid.length; i++)    //! Grid Traversal -> O(N x M)
            {
                  Arrays.fill(walk[i], -1);    // Filling the walk matrix...
                  Arrays.fill(fire[i], -1);    // Filling the fire matrix...
                  for(int j = 0; j < grid[0].length; j++)
                  {
                        if(grid[i][j] == 1)     // Storing the fire cells in the burned Queue...
                        {
                              burnedCell.add(new int[]{i, j});
                              fire[i][j] = 0;    // Setting the distance of primary fire cells as zero...
                        }
                  }
            }
            // Matrix for checking the time one can stay on a cell before fire arrives...
            int arrival[][] = new int[grid.length][grid[0].length];    //* Stay Matrix Check -> O(N x M)
            for(int i = 0; i < grid.length; i++)    //! Filling the Matrix -> O(N)
                  Arrays.fill(arrival[i], 0);
            TimeWhenCellsWillCatchFire(fire, grid, burnedCell);    //! Function Call -> O(N x M)
            // The Fire Queue may contain more than one cell index...
            TimeWhenCellsWillCatchFire(walk, grid, walkQueue);     //! Function Call -> O(N x M)
            // The Walk Queue will contain only one cell index (0, 0)...
            int Max = Integer.MIN_VALUE;        // Maximizing the wait possible...
            for(int i = 0; i < grid.length; i++)     //! Grid Traversal -> O(N x M)
            {
                  for(int j = 0; j < grid[0].length; j++)
                  {
                        if((i == 0) && (j == 0))    // top left cell case...
                              arrival[0][0] = fire[0][0] - walk[0][0] + 2;
                        if(grid[i][j] != 2)     // If the current cell is not a wall...
                        {
                              arrival[i][j] = fire[i][j] - walk[i][j];    // Getting the wait possible...
                              if(arrival[i][j] > 0)
                                    Max = Math.max(Max, arrival[i][j]);   // Getting the maximum Wait cell...
                        }
                  }
            }
            if(Max == Integer.MIN_VALUE)  return -1;   // If one cannot reach the house...
            return Max - 1;    // Otherwise we will move, just one unit time before the maximum wait...
      }
      public void TimeWhenCellsWillCatchFire(int fire[][], int grid[][], Queue<int[]> queue)
      {
            int time = 1;     // Counter to store the time of fire spread and walk for each cell...
            while(!queue.isEmpty())    //! Breadth First Search -> O(N x M)
            {
                  int queueSize = queue.size();     // Getting the Queue size...
                  for(int i = 0; i < queueSize; i++)
                  {
                        int temp[] = queue.poll();
                        if((temp[0] > 0) && (grid[temp[0] - 1][temp[1]] != 2) && (fire[temp[0] - 1][temp[1]]
                        == -1))     // For Upwards...
                        {
                              queue.add(new int[]{temp[0] - 1, temp[1]});   // Storing element in the Queue...
                              fire[temp[0] - 1][temp[1]] = time;
                        }
                        if((temp[0] < grid.length - 1) && (grid[temp[0] + 1][temp[1]] != 2) && (fire[temp[0] + 1]
                        [temp[1]] == -1))    // For Downwards...
                        {
                              queue.add(new int[]{temp[0] + 1, temp[1]});   // Storing element in the Queue...
                              fire[temp[0] + 1][temp[1]] = time;
                        }
                        if((temp[1] > 0) && (grid[temp[0]][temp[1] - 1] != 2) && (fire[temp[0]][temp[1] - 1]
                        == -1))     // For Leftwards...
                        {
                              queue.add(new int[]{temp[0], temp[1] - 1});   // Storing element in the Queue...
                              fire[temp[0]][temp[1] - 1] = time;
                        }
                        if((temp[1] < grid[0].length - 1) && (grid[temp[0]][temp[1] + 1] != 2) && (fire[temp[0]]
                        [temp[1] + 1] == -1))     // For Rightwards...
                        {
                              queue.add(new int[]{temp[0], temp[1] + 1});    // Storing element in the Queue...
                              fire[temp[0]][temp[1] + 1] = time;
                        }
                  }
                  time++;    // Incrementing the time...
            }
      }
      public void DisplayGrid(int grid[][])    //! Displaying the Grid -> O(N x M)
      {
            System.out.println("Grid !! ");
            for(int i = 0; i < grid.length; i++)
            {
                  for(int j = 0; j < grid[0].length; j++)
                        System.out.print(grid[i][j]+", ");
                  System.out.println();
            }
      }
      public static void main(String args[])
      {
            //? Test Case - I
            int mat1[][] = {{0,2,0,0,0,0,0}, {0,0,0,2,2,1,0}, {0,2,0,0,1,2,0},
                            {0,0,2,2,2,0,2}, {0,0,0,0,0,0,0}};
            //? Test Case - II
            int mat2[][] = {{0,0,0,0}, {0,1,2,0}, {0,2,0,0}};
            //? Test Case - III
            int mat4[][] = {{0,0,0,0,2,2,2,0}, {0,2,2,0,0,1,2,0}, {0,2,2,2,0,0,2,0},
                            {0,2,0,2,2,2,2,0}, {0,0,0,0,0,0,0,0}};
            EscapeFire escapeFire = new EscapeFire();        // Object creation...
            System.out.println("Maximum Wait Time Possible (Ist Case): "+escapeFire.MaximumMinutes(mat1));
            System.out.println("Maximum Wait Time Possible (IInd Case): "+escapeFire.MaximumMinutes(mat2));
            System.out.println("Maximum Wait Time Possible (IIIrd Case): "+escapeFire.MaximumMinutes(mat4));
      }
}




//! Time Complexity -> O(N x M)
//* Space Complexity -> O(N x M)