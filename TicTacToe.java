
import java.io.*;
import java.util.*;
public class TicTacToe
{
  //please change the matrix_size in node class and board initilization manually
  public static int matrix_size=4;

  public static void main(String[] args)
  {
    //Please uncomment the board you want to run (4*4 or 3*3)
  char board[][]={{'-','-','-','-'},{'-','-','-','-'},{'-','-','-','-'},{'-','-','-','-'}};
  //  char board[][]={{'-','-','-'},{'-','-','-'},{'-','-','-'}};//Initial empty make function here

    //create an empty node for Initial state
    node initial_game=new node();
    initial_game.setter(board,null);

    //NOTE:
    //1) These functions generate the best move for A (X Player) -> uncomment the one you want to try
    //2) To get the best move for B: Run for A -> change the board according to the best move for A -> Re-Run for B (O Player) with MinPlayer

    //Minimax Algorithm
    //int revenue=MaxPlayer(initial_game); //Change MaxPlayer to MinPlayer to get the best move for B player
    //print_array(initial_game.child.matrix);
    //System.out.println(revenue);

    //Alphabeta Pruning Algorithm
    //int revenue=MaxPlayer_AB(initial_game,-1000,1000);
    //print_array(initial_game.child.matrix);
    //System.out.println(revenue);

    //Monte Carlo Tree Search Algorithm
     MCTS(initial_game, 10000,'X'); // X is initial move to be added at odd depth

  }


  /***************************************************** Minimax Algorithm *************************************************/
  // the profit for all functions is as following: 1 if A wins | 0 if Draw | -1 if B wins

  static int MaxPlayer(node temp)//X player A
  {
    //reached leaf node -> return profit
    if(game_finished(temp.matrix)==1)//A has won (X)
    {
      return 1;
    }
    else if(game_finished(temp.matrix)==2)//B has won (O)
    {
      return -1;
    }
    else if(game_finished(temp.matrix)==-1)//draw
    {
      return 0;
    }

    // Game is still going on...

    int parent_cost=-1000;

    //check all possible moves
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        //if empty slot then create a child Add X
        if(temp.matrix[i][j]!='X'&&temp.matrix[i][j]!='O') //possible move
        {
          node new_child=new node();
          new_child.setter(temp.matrix,null); //setting child's data same as parent
          new_child.matrix[i][j]='X'; //adding the new play

          int child_cost=MinPlayer(new_child);
          if(child_cost>parent_cost)//new cost is better the stored cost
          {
            //store best child as temp's child
            temp.child=new_child;
            parent_cost=child_cost;
          }
        }
      }
    }
    //eventually the parent will choose the one with highest cost from his children
    return parent_cost;
  }

  static int MinPlayer(node temp)//O player B
  {
    //reached leaf node -> return profit
    if(game_finished(temp.matrix)==1)//A has won (X)
    {
      return 1;
    }
    else if(game_finished(temp.matrix)==2)//B has won (O)
    {
      return -1;
    }
    else if(game_finished(temp.matrix)==-1)//draw
    {
      return 0;
    }

    //Game is still going on...

    int parent_cost=1000;

    //check all possible moves
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        //if empty slot then create a child Add O
        if(temp.matrix[i][j]!='X'&&temp.matrix[i][j]!='O') //possible move
        {
          node new_child=new node();
          new_child.setter(temp.matrix,null); //setting child's data same as parent
          new_child.matrix[i][j]='O'; //adding the new play

          int child_cost=MaxPlayer(new_child);
          if(child_cost<parent_cost)//new cost is better the stored cost
          {
            //store best child as temp's child
            temp.child=new_child;
            parent_cost=child_cost;
          }
        }
      }
    }
    //eventually the parent will choose the one with lowest cost from his children
    return parent_cost;
  }


  /***************************************************** Alphabeta Pruning Algorithm *************************************************/
  static int MaxPlayer_AB(node temp,int alpha,int beta)//X player A , Initial Alpha=-1000, Initial Beta=1000;
  {
    //reached leaf node -> return profit
    if(game_finished(temp.matrix)==1)//A has won (X)
    {
      return 1;
    }
    else if(game_finished(temp.matrix)==2)//B has won (O)
    {
      return -1;
    }
    else if(game_finished(temp.matrix)==-1)//draw
    {
      return 0;
    }

    // Game is still going on...
    int parent_cost=-1000;

    //check all possible moves
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        //if empty slot then create a child Add X
        if(temp.matrix[i][j]!='X'&&temp.matrix[i][j]!='O') //possible move
        {
          node new_child=new node();
          new_child.setter(temp.matrix,null); //setting child's data same as parent
          new_child.matrix[i][j]='X'; //adding the new play

          int child_cost=MinPlayer_AB(new_child,Math.max(parent_cost, alpha),beta);
          if(child_cost>=beta)
          {
            return beta;
          }
          else if(child_cost>parent_cost)//new cost is better the stored cost
          {
            //store best child as tempp's child
            temp.child=new_child;
            parent_cost=child_cost;
          }
        }
      }
    }
    //eventually the parent will choose the one with highest cost from his children
    return parent_cost;
  }

  static int MinPlayer_AB(node temp,int alpha,int beta)//O player B
  {
    //reached leaf node -> return profit
    if(game_finished(temp.matrix)==1)//A has won (X)
    {
      return 1;
    }
    else if(game_finished(temp.matrix)==2)//B has won (O)
    {
      return -1;
    }
    else if(game_finished(temp.matrix)==-1)//draw
    {
      return 0; // no profit for either of the players
    }

    // Game is still going on...
    int parent_cost=1000; //test value

    //check all possible moves
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        //if empty slot then create a child Add X
        if(temp.matrix[i][j]!='X'&&temp.matrix[i][j]!='O') //possible move
        {
          node new_child=new node();
          new_child.setter(temp.matrix,null); //setting child's data same as parent
          new_child.matrix[i][j]='O'; //adding the new play

          int child_cost=MaxPlayer_AB(new_child,alpha,Math.min(parent_cost,beta));
          if(child_cost<=alpha)
          {
            return alpha;
          }
          else if(child_cost<parent_cost)//new cost is better the stored cost
          {
            //store best child as tempp's child
            temp.child=new_child;
            parent_cost=child_cost;
          }
        }
      }
    }//eventually the parent will choose the one with highest cost from his children
    return parent_cost;
  }


  /***************************************************** Monte Carlo Algorithm *************************************************/
  static void MCTS(node root, int trials,char move)//move is the move to be played in the root node
  {
    int counter=0; //count number of trials

    //mark root as visited
    root.visited=true;
    root.parent=null;
    //check all possible children() -> add them to possible children list -> and set their parent too
    possible_move_adder(root,move);

    while(counter<trials)
    {
      //Selection we always start from the root node
      //calculate UCT for all childrens of root -> loop through its children
      for(int i=0;i<root.possible_children.size();i++)
      {
        calculate_UCT(root.possible_children.get(i));
      }
      //pick the node with best UCT value to be expanded

      node best_UCT=root.possible_children.get(0);
      for(int i=0;i<root.possible_children.size();i++)
      {
        if(best_UCT.UCT<root.possible_children.get(i).UCT)
        {
          best_UCT=root.possible_children.get(i);
        }
      }
      //here we have the best UCT node from first level
      if(best_UCT.visited==true)// apply UCT more times on its children (which are already added)
      {
        while(true)
        {
          for(int i=0;i<best_UCT.possible_children.size();i++)
          {
            calculate_UCT(best_UCT.possible_children.get(i));
          }
          //pick the node with new best UCT value
          node new_best_UCT=best_UCT.possible_children.get(0);
          for(int i=0;i<best_UCT.possible_children.size();i++)
          {
            if(new_best_UCT.UCT<best_UCT.possible_children.get(i).UCT)
            {
              new_best_UCT=best_UCT.possible_children.get(i);
            }
          }
          //here we will have the new_best_UCT node with highest UCT value
          //no use of older best_UCT from here on
          best_UCT=new_best_UCT;
          if(best_UCT.visited==false)
          {
            //not sure about this one ---------------------> debug maybe neeeded
            best_UCT.visited=true;
            //add its children
            possible_move_adder(best_UCT,move);

            break; //found new best_UCT
          }
        }
      }
      //else highest UCT and not visited
      else
      {
        //mark it as visited
        best_UCT.visited=true;
        //add its children
        possible_move_adder(best_UCT,move);
      }

      //radndom play starting from best_UCT
      int win=Random_player(best_UCT,move);
      //update phase:
      updater(best_UCT,win);

      counter++;
    }
    //According to https://en.wikipedia.org/wiki/Monte_Carlo_tree_search the node with max_visited times should be chosen
    //pick the optimal move based on the calculation
    //pick the one with most number of simulations
    node best_visited=root.possible_children.get(0);
    for(int i=0;i<root.possible_children.size();i++)
    {
      if(best_visited.visited_times<root.possible_children.get(i).visited_times)//get the one with max visited_times
      {
        best_visited=root.possible_children.get(i);
      }
    }
    print_array(best_visited.matrix); //node with maximum number of simulations
  }

  /***************************************************** Auxiliary Functions  *************************************************/
  //function to update the visited times, winning times based on parent -> null chain
  static void updater(node temp, int win)
  {
    while(temp!=null)
    {
      if(win==1)
      {
        temp.winning_times=temp.winning_times+1;
        temp.visited_times=temp.visited_times+1;
      }
      else
      {
        temp.visited_times=temp.visited_times+1;
      }
      temp=temp.parent;
    }
  }

  //This function plays randomly from the Best UCT node and returns 1 upon winning
  static int Random_player(node temp, char move)
  {
    Random r=new Random();
    int new_depth=temp.depth+1;//this is to decide what kind of play should be done //kind of creating a new node

    //copying the array into a new one for random play
    char random_game_board[][]=new char[matrix_size][matrix_size];
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        random_game_board[i][j]=temp.matrix[i][j];
      }
    }

    while(true)
    {

      if(game_finished(random_game_board)==1&&move=='X')//X has won and the player was X
      {
        return 1;
      }
      else if(game_finished(random_game_board)==2&&move=='O')// O has won and the player was O
      {
        return 1;
      }
      else if((game_finished(random_game_board)==2&&move=='X')||(game_finished(random_game_board)==1&&move=='O')||(game_finished(random_game_board)==-1))//draw or loss
      {
        return 0; // no profit for either of the players
      }

      //pick a random i and j
      while(true)
      {
        int random_i=r.nextInt(((matrix_size-1) - 0) + 1) + 0;
        int random_j=r.nextInt(((matrix_size-1) - 0) + 1) + 0;
        if (random_game_board[random_i][random_j]!='X'&&random_game_board[random_i][random_j]!='O')
        {
          //valid move (good i,j)
          if(new_depth%2!=0)//odd depth add the same move
          {
            random_game_board[random_i][random_j]=move; //adding the new possible play
          }
          else
          {
            if(move=='X')
            {
              random_game_board[random_i][random_j]='O';
            }
            else
            {
              random_game_board[random_i][random_j]='X';
            }
          }
          break;
        }
      }
      new_depth++;//switch move
    }

  }


  //function to calculate UCT value for each node and update it
  static void calculate_UCT(node temp)
  {
    if(temp.visited_times==0)
    {
      temp.UCT= 10000; //infinity UCT
    }
    else
    {
      double value=(temp.winning_times/temp.visited_times)+Math.sqrt(2*Math.log(temp.parent.visited_times)/temp.visited_times);
      temp.UCT=value;
    }
  }

  //function to check all possible moves and add them to possible_children list
  static void possible_move_adder(node temp,char move)
  {
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        if(temp.matrix[i][j]!='X'&&temp.matrix[i][j]!='O') //possible move
        {
          node new_child=new node();
          new_child.setter(temp.matrix,null); //setting child's data same as parent
          new_child.depth=temp.depth+1;

          if(new_child.depth%2!=0)//odd depth add the same move as root original
          {
            new_child.matrix[i][j]=move; //adding the new possible play
          }
          else//even depth
          {
            if(move=='X')
            {
              new_child.matrix[i][j]='O';
            }
            else
            {
              new_child.matrix[i][j]='X';
            }
          }

          new_child.parent=temp;
          temp.possible_children.add(new_child);
        }

      }
    }
  }

  //function to check if game has finished and how won if finished
  static int game_finished(char matrix[][]) // 1,2 is winner // 0 still going on // -1 draw
  {
    // If there is any winner return 1 for A_XPlayer  .. return 2 for B_OPlayer
    if(matrix_size==3)
    {
      if(matrix[0][0]==matrix[1][0]&&matrix[1][0]==matrix[2][0])//column 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }

      }

      else if(matrix[0][1]==matrix[1][1]&&matrix[1][1]==matrix[2][1])//column 2 winner
      {
        if(matrix[0][1]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][1]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[0][2]==matrix[1][2]&&matrix[1][2]==matrix[2][2]) // Column 3 Winner
      {
        if(matrix[0][2]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][2]=='O')
        {
          return 2;// B has won
        }
      }
      //row winner
      else if(matrix[0][0]==matrix[0][1]&&matrix[0][1]==matrix[0][2])//Row 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[1][0]==matrix[1][1]&&matrix[1][1]==matrix[1][2])//row 2 winner
      {
        if(matrix[1][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[1][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[2][0]==matrix[2][1]&&matrix[2][1]==matrix[2][2])//column 1 winner
      {
        if(matrix[2][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[2][0]=='O')
        {
          return 2;// B has won
        }
      }
      //diagonal winner
      else if(matrix[0][0]==matrix[1][1]&&matrix[1][1]==matrix[2][2])//Diagonal 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[0][2]==matrix[1][1]&&matrix[1][1]==matrix[2][0]) //Diagonal 2 Winner
      {
        if(matrix[0][2]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][2]=='O')
        {
          return 2;// B has won
        }
      }

      //No winner found: either going on or draw
      //is there any block left?
      int counter=0;
      for(int i=0;i<3;i++)
      {
        for(int j=0;j<3;j++)
        {
          if(matrix[i][j]=='X'||matrix[i][j]=='O')
          {
            counter++;
          }
        }
      }
      if(counter!=9)// game is still going on not all blocks are filled
      {
        return 0;
      }
    }


    else if(matrix_size==4)
    {
      if(matrix[0][0]==matrix[1][0]&&matrix[1][0]==matrix[2][0]&&matrix[2][0]==matrix[3][0])//column 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }

      }

      else if(matrix[0][1]==matrix[1][1]&&matrix[1][1]==matrix[2][1]&&matrix[2][1]==matrix[3][1])//column 2 winner
      {
        if(matrix[0][1]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][1]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[0][2]==matrix[1][2]&&matrix[1][2]==matrix[2][2]&&matrix[2][2]==matrix[3][2]) // Column 3 Winner
      {
        if(matrix[0][2]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][2]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[0][3]==matrix[1][3]&&matrix[1][3]==matrix[2][3]&&matrix[2][3]==matrix[3][3]) // Column 4 Winner
      {
        if(matrix[0][3]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][3]=='O')
        {
          return 2;// B has won
        }
      }
      //row winner
      else if(matrix[0][0]==matrix[0][1]&&matrix[0][1]==matrix[0][2]&&matrix[0][2]==matrix[0][3])//Row 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[1][0]==matrix[1][1]&&matrix[1][1]==matrix[1][2]&&matrix[1][2]==matrix[1][3])//row 2 winner
      {
        if(matrix[1][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[1][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[2][0]==matrix[2][1]&&matrix[2][1]==matrix[2][2]&&matrix[2][2]==matrix[2][3])//row3 winner
      {
        if(matrix[2][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[2][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[3][0]==matrix[3][1]&&matrix[3][1]==matrix[3][2]&&matrix[3][2]==matrix[3][3])//row4 winner
      {
        if(matrix[3][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[3][0]=='O')
        {
          return 2;// B has won
        }
      }
      //diagonal winner
      else if(matrix[0][0]==matrix[1][1]&&matrix[1][1]==matrix[2][2]&&matrix[2][2]==matrix[3][3])//Diagonal 1 winner
      {
        if(matrix[0][0]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][0]=='O')
        {
          return 2;// B has won
        }
      }
      else if(matrix[0][3]==matrix[1][2]&&matrix[1][2]==matrix[2][1]&&matrix[2][1]==matrix[3][0]) //Diagonal 2 Winner
      {
        if(matrix[0][3]=='X')
        {
          return 1; //A has won
        }
        else if(matrix[0][3]=='O')
        {
          return 2;// B has won
        }
      }

      //is there any block left
      int counter=0;
      for(int i=0;i<4;i++)
      {
        for(int j=0;j<4;j++)
        {
          if(matrix[i][j]=='X'||matrix[i][j]=='O')
          {
            counter++;
          }
        }
      }
      if(counter!=16)// game is still going on not all blocks are filled
      {
        return 0;
      }
    }

    //If all are filled and there is no winner -> draw
    return -1;

  }


  static void print_array(char matrix[][])
  {
    for(int i=0;i<matrix_size;i++)
    {
      for(int j=0;j<matrix_size;j++)
      {
        System.out.printf("%c ",matrix[i][j]);
      }
      System.out.println();
    }
  }
}

/***************************************************** Search Tree Node Class  *************************************************/
class node
{
  node child;//stores best child only used for Minimax and ABpruning

  char matrix[][]=new char[4][4];//please change the size here

  //These variables are used in MCTS only
  int depth;// used to check wether X should be played or O
  double UCT=10000; //Infinity UCT in the bginning
  int visited_times;
  int winning_times;
  node parent; //used for backtracking and updating
  boolean visited=false;
  Vector<node> possible_children=new Vector<node>(); //used for MCTS to store all possible moves from a node

  //This function sets the array and best child to null
  //Please change the loop upper bound for matrix 4*4
  void setter(char game_board[][],node new_node)
  {
    for(int i=0;i<4;i++) //copying the puzzle
    {
      for(int j=0;j<4;j++)
      {
        this.matrix[i][j]=game_board[i][j];
      }
    }
    this.child=new_node;
  }
}
