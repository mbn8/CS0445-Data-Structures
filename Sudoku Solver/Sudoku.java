import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	private static int recentRow; //used along with an array to store the row of the recently changed cell
	private static int recentColumn; //used along with an array to store the column of the recently changed cell

    static boolean isFullSolution(int[][] board) {
    	
    	//The board is not a full solution if it contains at least one zero
    	for (int i = 0; i < 9; i++) {
    		for(int j = 0; j < 9; j++){
                if (board[i][j] == 0) {
                    return false;
                }
    		}
        }
    	
        //The solution is known to be complete, check if it is valid
    	for(int row = 0; row < 9; row++){
    		for(int column = 0; column < 9; column++){
    			if (reject(board, new int[]{row, column})) {
    	            return false;
    	        }
    		}
    	}
   
        // The solution is complete and valid
        return true;
        
    } //end isFullSolution
    
    static boolean reject(int[][] board, int[] coordinates) {
    	int row = coordinates[0];
    	int column = coordinates[1];
    	
    	//Check if the number is already in the row
    	  for(int j = 0; j < 9; j++){ 
          	if(board[row][column] == board[row][j] && column != j){
          		return true;
          	}			
          }
        
        //Check if the number is already in the column
    	  for(int i = 0; i < 9; i++){
          	if(board[row][column] == board[i][column] && row != i){
          		return true;
          	}	
          }
        
        //Pinpoint which 3x3 block the recently added number is in
        int columnBox = 0;
        int rowBox = 0;
        
        switch (row){
        	case 0: case 1: case 2:
        		rowBox = 0;
        		break;
        	case 3: case 4: case 5:
        		rowBox = 1;
        		break;
        	case 6: case 7: case 8:
        		rowBox = 2;
        } //end switch statement
        
        switch (column){
	    	case 0: case 1: case 2:
	    		columnBox = 0;
	    		break;
	    	case 3: case 4: case 5:
	    		columnBox = 1;
	    		break;
	    	case 6: case 7: case 8:
	    		columnBox = 2;
        } //end switch statement
        
        
        //Compare the recently added number to every other number in its 3x3 box
        for(int i = 0; i < 3; i++){
        	int rowCell = (rowBox * 3) + i;
        	
        	
        	for(int j = 0; j < 3; j++){
            	int columnCell = (columnBox * 3) + j;
        		if(board[row][column] == board[rowCell][columnCell] && row != rowCell && column != columnCell){
        			return true;
        		}	
        	} //end inner for loop
        	
        } //end outer for loop
        
        //There is no conflict, so do not reject
        return false;
        
    } //end reject
    
    static int[][] extend(int[][] board, int[] coordinates) {
    	
    	//Initialize the new partial solution
        int[][] temp = new int[9][9];
        
        //Copy the board argument into a new array array
        for(int i = 0; i < 9; i++){
        	for(int j = 0; j < 9; j++){
        		temp[i][j] = board[i][j];
        	}
        }
        
        //Set the next 0 in the board to 1, and keep track of the row and column
        for (int i = 0; i < 9; i++) {
        	for(int j = 0; j < 9; j++){
        		
        		if(temp[i][j] == 0){
        			temp[i][j] = 1;
        			
        			coordinates[0] = i;
        			coordinates[1] = j;
        			
        			recentRow = i;
        			recentColumn = j;
        			
        			return temp;
        		}
        		
        	} //end inner foor loop
        } //end outer for loop

        // If we reached this point, all numbers were already placed, so we cannot extend. Return null
        return null;
      
    } //end extend
    
    static int[][] next(int[][] board, int[] coordinates) {
    	int temp[][] = new int[9][9];
    	int row = coordinates[0];
    	int column = coordinates[1];
    	
    	//Copy the board argument into a new array array
    	for(int i = 0; i < 9; i++){
    		for(int j = 0; j < 9; j++){
    			temp[i][j] = board[i][j];
    		}
    	}
    	
    	if(temp[row][column] == 9){
    		//Cannot increase the number anymore
        	return null;
    	}
    	
        //Increase the number in the current cell
    	temp[row][column]++;
        return temp;
        
    } //end next

    static void testIsFullSolution() {
    	System.out.println("\nTESTISFULLSOLUTION\n------------------");
    	
    	//Case 1
    	int[][] fullSolution = new int[][] {
    		{8, 3, 5, 4, 1, 6, 9, 2, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 1, 7, 2, 9, 3, 6, 5, 8},
    		{5, 6, 9, 1, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 8, 5, 2, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 3, 9, 4},
    		{9, 8, 1, 3, 4, 5, 2, 7, 6},
    		{3, 7, 4, 9, 6, 2, 8, 1, 5} };
    		
    	System.out.println("Case 1 Test Board: ");
    	printBoard(fullSolution);
		boolean isFull = isFullSolution(fullSolution);
		
		//This is an example of a full solution
		System.out.println("The first test case should be a full solution. Result of isFullSolution: " + isFull);
		System.out.println();
    	
    	//Case 2
    	int[][] emptyBoard = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0} };
			
		System.out.println("Case 2 Test Board: ");
    	printBoard(emptyBoard);
		isFull = isFullSolution(emptyBoard);
		
		//RejectS because of the 0 values
		System.out.println("The second test case should NOT be a full solution. Result of isFullSolution: " + isFull);
    	System.out.println();
    	
		//Case 3
    	int[][] rejected = new int[][] {
    		{8, 3, 5, 1, 0, 6, 9, 2, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 5, 7, 2, 9, 3, 6, 5, 8},
    		{5, 6, 9, 0, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 8, 5, 2, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 0, 9, 4},
    		{9, 8, 1, 3, 4, 5, 2, 7, 6},
    		{3, 7, 4, 9, 6, 2, 0, 1, 0} };
    		
		System.out.println("Case 3 Test Board: ");
    	printBoard(rejected);
		isFull = isFullSolution(rejected);
		
		//Reject because of the 0 values
		System.out.println("The third test case should NOT be a full solution. Result of isFullSolution: " + isFull);
		System.out.println();
    		
    	//Case 4
        rejected = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 9, 9} };
        		
		System.out.println("Case 4 Test Board: ");
    	printBoard(rejected);
		isFull = isFullSolution(rejected);
		
		//Reject because of rejected[8][7] and rejected[8][8]
		System.out.println("The fourth test case should NOT be a full solution. Result of isFullSolution: " + isFull);
		System.out.println();
		
		//Case 5
		fullSolution = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 9} };
    		
		System.out.println("Case 5 Test Board: ");
    	printBoard(fullSolution);
		isFull = isFullSolution(fullSolution);
		
		//This is an example of a full solution
		System.out.println("The fifth test case should be a full solution. Result of isFullSolution: " + isFull);
		System.out.println();
		
		//Case 6
		rejected = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 0} };
    		
		System.out.println("Case 6 Test Board: ");
    	printBoard(rejected);
		isFull = isFullSolution(rejected);
		
		//Reject because rejected[8][8] is 0
		System.out.println("The sixth test case should NOT be a full solution. Result of isFullSolution: " + isFull);
		System.out.println();
    	
    } //end testIsFullSolution

    static void testReject() {
    	System.out.println("\nTESTREJECT\n----------");
    	boolean testReject = false;
    	
    	//Case 1
    	int[][] rejected = new int[][] {
    		{8, 3, 5, 1, 0, 6, 9, 2, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 5, 7, 2, 9, 3, 6, 0, 8},
    		{5, 6, 9, 0, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 8, 5, 2, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 0, 9, 4},
    		{9, 8, 1, 3, 4, 5, 2, 7, 6},
    		{3, 7, 4, 9, 6, 2, 0, 1, 0}
    	};
    	
    	System.out.println("Case 1 Test Board: ");
    	printBoard(rejected);
    	testReject = reject(rejected, new int[]{0, 2}); //Reject because two numbers in the same 3x3 box
    	System.out.println("The first test case should be rejected at (0,2). Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 2
    	int[][] fullSolution = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 9} };
    	
		System.out.println("Case 2 Test Board: ");
    	printBoard(fullSolution);
    	testReject = reject(fullSolution, new int[]{8, 8});
    	System.out.println("The second test case should NOT be rejected anywhere. Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 3
    	rejected = new int[][] {
    		{8, 0, 5, 4, 1, 6, 0, 0, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 1, 7, 2, 9, 3, 6, 5, 8},
    		{5, 0, 9, 1, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 0, 5, 0, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 3, 9, 4},
    		{9, 8, 1, 0, 4, 5, 0, 7, 6},
    		{3, 0, 4, 9, 6, 2, 8, 1, 5} };

		System.out.println("Case 3 Test Board: ");
    	printBoard(rejected);
    	testReject = reject(rejected, new int[]{0, 1}); //Reject because the starting number is 0; cannot be expanded to a full solution with 0
    	System.out.println("The third test case should be rejected at (0,1). Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 4
    	int[][] accepted = new int[][] {
    		{8, 0, 5, 4, 1, 6, 0, 0, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 1, 7, 2, 9, 3, 6, 5, 8},
    		{5, 0, 9, 1, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 0, 5, 0, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 3, 9, 4},
    		{9, 8, 1, 0, 4, 5, 0, 7, 6},
    		{3, 0, 4, 9, 6, 2, 8, 1, 5} };

		System.out.println("Case 4 Test Board: ");
    	printBoard(accepted);
    	testReject = reject(accepted, new int[]{0, 0});
    	System.out.println("The fourth test case should NOT be rejected at (0,0). Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 5
    	accepted = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{0, 6, 0, 5, 3, 7, 2, 8, 4},
    		{0, 0, 0, 4, 1, 9, 6, 3, 5},
    		{0, 0, 0, 2, 8, 6, 1, 7, 0} };
    		
		System.out.println("Case 5 Test Board: ");
    	printBoard(accepted);
    	testReject = reject(accepted, new int[]{6, 1});
    	System.out.println("The fifth test case should NOT be rejected at (6,1). Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 6
    	rejected = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 0, 6, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{0, 6, 0, 5, 3, 7, 2, 8, 4},
    		{0, 0, 0, 4, 1, 9, 6, 3, 5},
    		{0, 0, 0, 2, 8, 6, 1, 7, 0} };
    		
		System.out.println("Case 6 Test Board: ");
    	printBoard(rejected);
    	testReject = reject(rejected, new int[]{8, 5}); //Reject because two numbers in the same column
    	System.out.println("The sixth test case should be rejected at (8,5). Result of reject: " + testReject);
    	System.out.println();
    	
    	//Case 7
    	rejected = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 0, 6, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 0, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 9},
    		{0, 6, 0, 5, 3, 7, 2, 8, 4},
    		{0, 0, 0, 4, 1, 9, 6, 3, 5},
    		{0, 0, 0, 2, 8, 6, 1, 7, 0} };
    		
		System.out.println("Case 7 Test Board: ");
    	printBoard(rejected);
    	testReject = reject(rejected, new int[]{5, 3}); //Reject because two numbers in the same row
    	System.out.println("The seventh test case should be rejected at (5,3). Result of reject: " + testReject);
    	System.out.println();
    	
    } //end testReject

    static void testExtend() {
    	System.out.println("\nTESTEXTEND\n----------");
    	
    	//Case 1
    	int[][] board = new int[][] {
    		{8, 3, 5, 1, 0, 6, 9, 2, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 5, 7, 2, 9, 3, 6, 5, 8},
    		{5, 6, 9, 0, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 8, 5, 2, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 0, 9, 4},
    		{9, 8, 1, 3, 4, 5, 2, 7, 6},
    		{3, 7, 4, 9, 6, 2, 0, 1, 0} };
    		
    	System.out.println("Case 1 Initial Board: ");
    	printBoard(board);
    	board = extend(board, new int[]{recentRow, recentColumn});
    	System.out.println("After extend, the cell at (0,4) should be 1. \n\nCase 1 Extended Board:");
    	printBoard(board);
    	System.out.println();
    		
    	//Case 2
		board = new int[][] {
    		{4, 1, 3, 7, 4, 6, 3, 2, 9},
    		{0, 0, 0, 6, 0, 2, 3, 8, 0},
    		{0, 0, 2, 0, 0, 8, 0, 5, 0},
    		{2, 0, 0, 0, 0, 0, 5, 4, 0},
    		{0, 0, 5, 0, 7, 0, 6, 0, 0},
    		{0, 3, 9, 0, 0, 0, 0, 0, 1},
    		{0, 6, 0, 8, 0, 0, 2, 0, 0},
    		{0, 7, 4, 9, 0, 6, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0, 0, 0, 5} };
    		
		System.out.println("Case 2 Initial Board: ");
    	printBoard(board);
    	board = extend(board, new int[]{recentRow, recentColumn});
    	System.out.println("After extend, the cell at (1,0) should be 1. \n\nCase 2 Extended Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 3
    	board = new int[][] {
    		{0, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 9} };
    		
		System.out.println("Case 3 Initial Board: ");
    	printBoard(board);
    	board = extend(board, new int[]{recentRow, recentColumn});
    	System.out.println("After extend, the cell at (0,0) should be 1. \n\nCase 3 Extended Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 4
    	board = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 0} };
    	
		System.out.println("Case 4 Initial Board: ");
    	printBoard(board);
    	board = extend(board, new int[]{recentRow, recentColumn});
    	System.out.println("After extend, the cell at (8,8) should be 1. \n\nCase 4 Extended Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 5
    	board = new int[][]{
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 9} };
    	
		System.out.println("Case 5 Initial Board: ");
    	printBoard(board);
    	board = extend(board, new int[]{recentRow, recentColumn});
    	System.out.println("This is a full solution. The result should be No assignment (null). \n\nCase 5 Extended Board:");
    	printBoard(board);
    	System.out.println();
    	
    } //end testExtend 

    static void testNext() {
    	System.out.println("\nTESTNEXT\n--------");
    	
    	//Case 1
    	int[][] board = new int[][] {
    		{8, 3, 5, 1, 1, 6, 9, 2, 7},
    		{2, 9, 6, 8, 5, 7, 4, 3, 1},
    		{4, 5, 7, 2, 9, 3, 6, 5, 8},
    		{5, 6, 9, 0, 3, 4, 7, 8, 2},
    		{1, 2, 3, 6, 7, 8, 5, 4, 9},
    		{7, 4, 8, 5, 2, 9, 1, 6, 3},
    		{6, 5, 2, 7, 8, 1, 0, 9, 4},
    		{9, 8, 1, 3, 4, 5, 2, 7, 6},
    		{3, 7, 4, 9, 6, 2, 0, 1, 0} };
    		
    	System.out.println("Case 1 Initial Board:");
    	printBoard(board);
    	board = next(board, new int[]{0, 4});
    	System.out.println("The current cell is at (0,4) and is 1. After next, it should be 2. \n\nCase 1 Next Board:");
    	printBoard(board);
    	System.out.println();

    	//Case 2
    	board = new int[][] {
    		{4, 1, 3, 7, 4, 6, 3, 2, 9},
    		{0, 0, 0, 6, 0, 2, 3, 8, 0},
    		{0, 0, 2, 0, 0, 8, 0, 5, 0},
    		{2, 0, 0, 0, 0, 0, 5, 4, 0},
    		{0, 0, 5, 0, 7, 0, 6, 0, 0},
    		{0, 3, 9, 0, 0, 0, 0, 0, 1},
    		{0, 6, 0, 8, 0, 0, 2, 0, 0},
    		{0, 7, 4, 9, 0, 6, 0, 0, 0},
    		{0, 0, 0, 0, 0, 0, 0, 0, 5} };
    		
		System.out.println("Case 2 Initial Board:");
    	printBoard(board);
    	board = next(board, new int[]{1, 0});
    	System.out.println("The current cell is at (1,0) and is 0. After next, it should be 1. \n\nCase 2 Next Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 3
    	board = new int[][] {
    		{6, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 9} };
		
		System.out.println("Case 3 Initial Board:");
    	printBoard(board);
    	board = next(board, new int[]{0, 0});
    	System.out.println("The current cell is at (0,0) and is 6. After next, it should be 7. \n\nCase 3 Next Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 4
    	board = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 8} };
    			
		
		System.out.println("Case 4 Initial Board:");
    	printBoard(board);
    	board = next(board, new int[]{8, 8});
    	System.out.println("The current cell is at (8,8) and is 8. After next, it should be 9. \n\nCase 4 Next Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 5
    	board = new int[][]{
    		{5, 3, 4, 6, 7, 8, 9, 0, 2},
    		{6, 7, 2, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 0, 5, 6, 7},
    		{8, 5, 9, 0, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 0, 9, 1},
    		{7, 1, 3, 9, 0, 4, 8, 5, 6},
    		{9, 6, 0, 5, 3, 7, 2, 0, 4},
    		{2, 8, 7, 0, 1, 9, 0, 3, 5},
    		{3, 0, 5, 2, 0, 6, 1, 7, 9} };
    		
		System.out.println("Case 5 Initial Board: ");
    	printBoard(board);
    	board = next(board, new int[]{0, 6});
    	System.out.println("The current cell is at (0,6) is already 9. After next, the Sudoku should be No assignment (null). \n\nCase 5 Next Board:");
    	printBoard(board);
    	System.out.println();
    	
    	//Case 6
    	board = new int[][] {
    		{5, 3, 4, 6, 7, 8, 9, 1, 2},
    		{6, 7, 1, 1, 9, 5, 3, 4, 8},
    		{1, 9, 8, 3, 4, 2, 5, 6, 7},
    		{8, 5, 9, 7, 6, 1, 4, 2, 3},
    		{4, 2, 6, 8, 5, 3, 7, 9, 1},
    		{7, 1, 3, 9, 2, 4, 8, 5, 6},
    		{9, 6, 1, 5, 3, 7, 2, 8, 4},
    		{2, 8, 7, 4, 1, 9, 6, 3, 5},
    		{3, 4, 5, 2, 8, 6, 1, 7, 0} };
    			
		System.out.println("Case 6 Initial Board:");
    	printBoard(board);
    	board = next(board, new int[]{1, 2}); 
    	board = next(board, new int[]{1, 2});
    	System.out.println("The current cell is at (1,2). After two calls of next, it should be 3. \n\nCase 6 Next (x2) Board:");
    	printBoard(board);
    	
    	board = next(board, new int[]{1, 2});
    	System.out.println("After another call of next, the value at (1,2) should be 4. \n\nCase 6 Next Board:");
    	printBoard(board);
    	
    } //end testNext

    static void printBoard(int[][] board) {
        if (board == null) {
            System.out.println("No assignment");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                System.out.println("----+-----+----");
            }
            for (int j = 0; j < 9; j++) {
                if (j == 2 || j == 5) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    static int[][] readBoard(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
        int[][] board = new int[9][9];
        int val = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
                } catch (Exception e) {
                    val = 0;
                }
                board[i][j] = val;
            }
        }
        return board;
    }

    static int[][] solve(int[][] board, int[] coordinates) {
    	
    	//If the very first cell in the Sudoku is a 0, go right into extending
    	if(board[0][0] == 0 && recentRow == 0 && recentColumn == 0){
    		coordinates = new int[2];
    		int[][] attempt = extend(board, coordinates);
            while (attempt != null) {
                int[][] solution = solve(attempt, coordinates);
                if (solution != null) 
                	return solution;
                attempt = next(attempt, coordinates);
            }
    	}
    	
        if (reject(board, coordinates)) 
        	return null;
        if (isFullSolution(board)) 
        	return board;
        coordinates = new int[2];
        int[][] attempt = extend(board, coordinates);
        while (attempt != null) {
            int[][] solution = solve(attempt, coordinates);
            if (solution != null) 
            	return solution;
            attempt = next(attempt, coordinates);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args[0].equals("-t")) {
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else {
            int[][] board = readBoard(args[0]);
            int[] coordinatess = new int[2];
            
            System.out.println("The initial Sudoku is: ");
            printBoard(board);
    		System.out.println("\nThe solved Sudoku is: ");
    		printBoard(solve(board, coordinatess));
            return;
        }
        
    } //end main method
}