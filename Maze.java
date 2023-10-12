import java.util.*;

public class Maze {
	
	//variables
	private int columns;
	private int rows;
	private int cellCount;
	DisjSets disjSet;
	Cell[][] maze;
	
	//class to keep track of the cells needed for the maze
	//hidden from user
	private static class Cell {
		
		//variables
		int cellValue;			
		boolean firstWall, secondWall;		
		
		//constructor
		Cell(int cellValue, boolean firstWall, boolean secondWall){
			this.cellValue = cellValue;
			this.firstWall = firstWall;
			this.secondWall = secondWall;
		}
	}
	//constructor
	public Maze(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		cellCount = rows * columns;
		disjSet = new DisjSets(cellCount);
		maze = constructMaze(rows, columns);
	}
	//method to create and initialize 2d array for maze
	public Cell[][] constructMaze(int rows, int columns){
		
		//create 2d array
		Cell[][] newMaze = new Cell[rows][columns];
		
		int value = 0;
		boolean firstWall = true;
		boolean secondWall = true;
		
		//initialize the cells of the maze
		for(int i = 0; i < rows; i++) {
			
			for(int j = 0; j < columns; j++) {
				
				//assign a cell to each position in the array
				newMaze[i][j] = new Cell(value, firstWall, secondWall);
				value = value + 1;
			}
		}
		
		//return the array
		return newMaze;
	}
	//method to construct the path of the maze using the algorithm
	//algorithm = all cells except the first and last have all their walls
	//choose a random wall to remove, remove wall if the cells surrounding the chosen wall are not already connected
	//repeat until the first and last cells are connected - ie. a path is created
	public void findPath() {
		
		//use the Random class to help get a random wall for algorithm
		Random rand1 = new Random();
		Random rand2 = new Random();
		
		//begin algorithm with all cells having all their walls except the top left (first) cell and bottom right (last) cell
		Cell lastCell = maze[rows-1][columns-1];
		removeWall(lastCell, 0);
		removeWall(lastCell, 1);
		
		//variables
		int l = cellCount - 1;
		int r = rows - 1;
		int c = columns - 1;
		
		//continue until starting cell and finish cell are connected, traverse the array
		while(!(disjSet.find(0)==disjSet.find(l))) {
			
			//get a random cell
			int getColumn = rand1.nextInt(columns);
			int getRow = rand1.nextInt(rows);
			
			//find the random cell in array
			Cell getCell = maze[getRow][getColumn];
			int getValue = getCell.cellValue;
			
			//use the random cell to find the wall to remove and the cell on the other side of the wall
			int x = 0;
			
			//if the cell has two walls
			if(getCell.firstWall == true && getCell.secondWall == true) {
				
				//determine the position of the cell in the maze
				//if the cell is not in the last column or last row
				if(getColumn != c && getRow != r) {
				
					//choose a wall to remove randomly
					int selectWall = rand2.nextInt(2);
				
					//first wall = 0, second wall = 1
					if(selectWall == 0) {
					
						x = disjSet.find(getValue + 1);
						removeWall(getCell, 0);
					
					} else {
					
						x = disjSet.find(getValue + columns);
						removeWall(getCell, 1);
					}
				
				} 
				//if the random cell is the last cell
				if(getCell == lastCell) {
				
					//nothing is done
					continue;
				}
				//if the random cell is in the last row
				if(getRow == r) {
				
					//remove the first wall (vertical wall)
					x = disjSet.find(getValue + 1);
					removeWall(getCell, 0);
				
				} 
				//if the random cell is in the last column
				if(getColumn == c) {
				
					//remove the second wall (bottom wall)
					x = disjSet.find(getValue + columns);
					removeWall(getCell, 1);
				}
			//if the cell has one wall
			} else if(getCell.firstWall == true || getCell.secondWall == true) {
				
				//determine the position of the cell in the maze
				//if the cell is not in the last column or last row
				if(getColumn != c && getRow != r) {
				
					//choose a wall to remove randomly
					int selectWall = rand2.nextInt(2);
				
					//first wall = 0, second wall = 1
					if(selectWall == 0) {
					
						x = disjSet.find(getValue + 1);
						removeWall(getCell, 0);
					
					} else {
					
						x = disjSet.find(getValue + columns);
						removeWall(getCell, 1);
					}
				
				} 
				//if the random cell is the last cell
				if(getCell == lastCell) {
				
					//nothing is done
					continue;
				}
				//if the random cell is in the last row
				if(getRow == r) {
				
					//remove the first wall (vertical wall)
					x = disjSet.find(getValue + 1);
					removeWall(getCell, 0);
				
				} 
				//if the random cell is in the last column
				if(getColumn == c) {
				
					//remove the second wall (bottom wall)
					x = disjSet.find(getValue + columns);
					removeWall(getCell, 1);
				}
				
			//if the cell has no walls
			} else {
				
				continue;
			}
			//once the position is determined
			//if the cells are not in the same set, merge together
			int y = disjSet.find(getValue);
			if(x != y) {
				
				disjSet.union(x, y);
			}
			
		}
			
	}
	//method to remove wall
	public void removeWall(Cell cell, int w) {
		
		//if the wall is the first wall
		if(w == 0) {
			
			//if the wall exists, then remove
			if(cell.firstWall == true) {
				
				cell.firstWall = false;
			}
		//if the wall is the second wall
		} else {
			
			//if the wall does exist, remove
			if(cell.secondWall == true) {
				
				cell.secondWall = false;
				
			}
		}
	}
	//method to display maze (2d array)
	public String toString() {
		
		String str = " ";
		
		//draw the top wall for the border of the maze
		for(int i=0; i<columns-1; i++) {
			
			str = str + " _";
		}
		str = str + "\n";
		
		//draw the walls for the cells in the maze
		for(int j=0; j<rows; j++) {
			
			//draw the left border of the maze
			str = str + "|";
			
			for(int k=0; k<columns; k++) {
				
				//if the wall exists, print the wall
				if(maze[j][k].secondWall == true) {
					
					str = str + "_";
					
				//print a blank space
				} else {
					
					str = str + " ";
				}
				//if the wall exists, print the wall
				if(maze[j][k].firstWall == true) {
					
					str = str + "|";
				
				//print a blank space
				} else {
					
					str = str + " ";
				}
			}
			str = str + "\n";
			
		}
		return str + "\n";
	}
	//main function
	public static void main(String[] args) {
		
		//ask user for maze specifications
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the number of rows for the maze (1-20): ");
		int rows = scan.nextInt();
		System.out.println("Enter the number of columns for the maze (1-20): ");
		int columns = scan.nextInt();
		
		//create an object to create a maze using user's specifications
		Maze mz = new Maze(rows, columns);
		
		//display maze to user
		System.out.println("The generated maze is: ");
		mz.findPath();
		System.out.println(mz);
		
		//close the scanner
		scan.close();
	}

}
