// Names: Holten Bradley
// x500s: bradl515

import java.util.Random;

public class MyMaze{
    Cell[][] maze;
    int startRow;
    int endRow;

    public MyMaze(int rows, int cols, int startRow, int endRow) {
        maze = new Cell[rows][cols];
        this.startRow = startRow;
        this.endRow = endRow;
        for(int i=0; i < maze.length; i++){
            for(int j=0; j<maze[0].length; j++){
                maze[i][j] = new Cell();
            }
        }
    }

    /* TODO: Create a new maze using the algorithm found in the writeup. */
    public static MyMaze makeMaze(int rows, int cols, int startRow, int endRow) {
        MyMaze labyrinth = new MyMaze(rows, cols, startRow, endRow);
        labyrinth.maze[startRow][0].setVisited(true);

        Stack1Gen<int[]> stack = new Stack1Gen<int[]>();
        stack.push(new int[]{startRow, 0});


        while(!stack.isEmpty()){
            int[] temp = stack.top();

            if(temp[0] == endRow && temp[1] == cols -1){ //stops making stuff
                labyrinth.maze[endRow][cols - 1].setRight(false);
                labyrinth.maze[endRow][cols - 1].setVisited(true);
                stack.pop();
            }else{
                //check if all neighbors are visited
                boolean allVisited = true;
                if(temp[0] - 1 >= 0 && !labyrinth.maze[temp[0] - 1][temp[1]].getVisited()){
                    allVisited = false;
                }
                if(temp[1] + 1 < cols && !labyrinth.maze[temp[0]][temp[1] + 1].getVisited()){
                        allVisited = false;
                }
                if(temp[0] + 1 < rows && !labyrinth.maze[temp[0] + 1][temp[1]].getVisited()){
                        allVisited = false;
                }
                if(temp[1] - 1 >= 0 && !labyrinth.maze[temp[0]][temp[1] - 1].getVisited()){
                        allVisited= false;
                }

                if(allVisited){  //if all are visited, pop the top
                    stack.pop();
                }


                Random random = new Random();
                int direction = random.nextInt(4);
                if (direction == 0 && temp[0] - 1 >= 0 && !labyrinth.maze[temp[0] - 1][temp[1]].getVisited()){//up
                        stack.push(new int[]{temp[0] - 1, temp[1]});
                        labyrinth.maze[temp[0] - 1][temp[1]].setVisited(true);
                        labyrinth.maze[temp[0] - 1][temp[1]].setBottom(false);

                }else if (direction == 1 && temp[1] + 1 < cols && !labyrinth.maze[temp[0]][temp[1] + 1].getVisited()) {//right
                        stack.push(new int[]{temp[0], temp[1] + 1});
                        labyrinth.maze[temp[0]][temp[1] + 1].setVisited(true);
                        labyrinth.maze[temp[0]][temp[1]].setRight(false);

                }else if (direction == 2 && temp[0] + 1 < rows && !labyrinth.maze[temp[0] + 1][temp[1]].getVisited()){//down
                        stack.push(new int[]{temp[0] + 1, temp[1]});
                        labyrinth.maze[temp[0] + 1][temp[1]].setVisited(true);
                        labyrinth.maze[temp[0]][temp[1]].setBottom(false);

                }else if (direction == 3 && temp[1] - 1 >= 0 && !labyrinth.maze[temp[0]][temp[1] - 1].getVisited()){//left
                        stack.push(new int[]{temp[0], temp[1] - 1});
                        labyrinth.maze[temp[0]][temp[1] - 1].setVisited(true);
                        labyrinth.maze[temp[0]][temp[1] - 1].setRight(false);

                }
            }

        }//stack
        for(int i=0; i<labyrinth.maze.length; i++)
            for(int j=0; j<labyrinth.maze[0].length; j++){
                labyrinth.maze[i][j].setVisited(false);
            }
        return labyrinth;
    }

    /* TODO: Print a representation of the maze to the terminal */
    public void printMaze() {
        System.out.print("|");//make the top
        for(int l=0; l<maze[0].length; l++){
            System.out.print("---|");
        }
        System.out.print("\n");

        int i = 0;
        while(i< maze.length){
            int j =0;
            if(i>0) {
                while (j < maze[0].length) {
                    if (maze[i - 1][j].getBottom()) {
                        System.out.print("|---");
                    }else {
                        System.out.print("|   ");
                    }
                    j++;
                }
                j=0;
                System.out.print("|\n");
            }
            if(i!=startRow) {//starts the line
                System.out.print("|");
            }else{
                System.out.print(" ");
            }
            j = 0;
            while(j< maze[0].length){
                    if(i==endRow && j==maze[0].length - 1 && !maze[endRow][maze[0].length -1].getVisited()){
                        System.out.print("    ");
                    }else{
                        if(maze[i][j].getVisited() && !maze[i][j].getRight()) {
                            System.out.print(" *  ");
                        }else if(maze[i][j].getVisited() && maze[i][j].getRight()){
                            System.out.print(" * |");
                        }else if(!maze[i][j].getVisited() && maze[i][j].getRight()){
                            System.out.print("   |");
                        }else{
                            System.out.print("    ");
                        }
                    }
                    j++;
            }//j
            j=0;
            System.out.print("\n");
            i++;
        }//i
        System.out.print("|");
        for(int l=0; l<maze[0].length; l++){
            System.out.print("---|");
        }
        System.out.println();
        System.out.println();

    }

    /* TODO: Solve the maze using the algorithm found in the writeup. */
    public void solveMaze() {
        Q2Gen<int[]> queue = new Q2Gen<int[]>(maze.length * maze[0].length);
        queue.add(new int[]{startRow, 0});
        maze[startRow][0].setVisited(true);
        while(queue.length() != 0){
            int[] temp = queue.remove();
            if(temp[0] == endRow && temp[1] == maze[0].length - 1){
                maze[temp[0]][temp[1]].setVisited(true);
                break;
            }else {
                if (temp[0] - 1 >= 0 && !maze[temp[0] - 1][temp[1]].getBottom() && !maze[temp[0] - 1][temp[1]].getVisited()) {//up             /*making the if-statement inside avoids an out of bounds error*/
                    maze[temp[0] - 1][temp[1]].setVisited(true);
                    queue.add(new int[]{temp[0] - 1, temp[1]});

                }
                if (temp[1] + 1 < maze[0].length && !maze[temp[0]][temp[1]].getRight() && !maze[temp[0]][temp[1] + 1].getVisited()) {//right
                    maze[temp[0]][temp[1] + 1].setVisited(true);
                    queue.add(new int[]{temp[0], temp[1] + 1});
                }
                if (temp[0] + 1 < maze.length && !maze[temp[0]][temp[1]].getBottom() && !maze[temp[0] + 1][temp[1]].getVisited()){//down
                    maze[temp[0] + 1][temp[1]].setVisited(true);
                    queue.add(new int[]{temp[0] + 1, temp[1]});
                }
                if (temp[1] - 1 >= 0 && !maze[temp[0]][temp[1] - 1].getRight() && !maze[temp[0]][temp[1] - 1].getVisited()){//left
                    maze[temp[0]][temp[1] -1].setVisited(true);
                    queue.add(new int[]{temp[0], temp[1] - 1});
                }
            }

        }
        if (!maze[endRow][maze[0].length -1].getVisited()) {
            System.out.println("\nMaze is unsolvable.");
        }else{
            System.out.println("\nMaze has been solved!");
        }

        this.printMaze();
    }

    public static void main(String[] args){
        /* Any testing can be put in this main function */
        MyMaze mine = new MyMaze(5, 5, 2, 4);
        mine.printMaze();
        mine = makeMaze(5, 25, 2, 4);
        mine.printMaze();
        mine.solveMaze();
    }
}
