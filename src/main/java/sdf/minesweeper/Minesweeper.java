package sdf.minesweeper;

import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

  private int[][] boardShown = new int[10][10];
  private int[][] boardHidden = new int[10][10];

  //Game setting
  private int numberOfBombs = 10;

  public void startMinesweeper(){
    System.out.println
      ("\n\n------------- Welcome to Minesweeper ! -------------\n");
    System.out.printf("-----There are a total of %d bombs. GOOD LUCK!------\n\n",numberOfBombs);
    setRandomBombs();

    boolean gameContinue = true;
    while(gameContinue){
      showVisibleMap();
      gameContinue = playMove();
      if(checkWinProgress()){
        showHiddenMap();
        System.out.println
        ("\n------------- Congrats, you WON!!! -------------");
        break;
      }
    }
  }

  public boolean playMove(){
    Scanner sc= new Scanner(System.in);
    try{
      System.out.print("Where would you like to dig? Input as row,col: ");
      String[] input = sc.nextLine().split(",");
      int i = Integer.parseInt(input[0]);
      int j = Integer.parseInt(input[1]);

      if(i<0 || i>9 || j<0 || j>9){
        System.out.println("Input wrong!");
        return true;
      }
      if(boardShown[i][j]!=0){
        System.out.println("Input already entered!");
        return true;
      }
      if(boardHidden[i][j]==100){
        showHiddenMap();
        System.out.print("Oops! You hit a mine!\n---------- GAME OVER ----------");
        sc.close();
        return false;
      }else if(boardHidden[i][j]==0)
        checkSelectionSurroundings(i,j);
      else
        showRandomSurroundings(i,j);  
      return true;
    }catch(Exception e){
      System.out.println("Input Wrong!");
      return true;
    }
  }

  public void setRandomBombs(){
    int var=0;
    while(var!=numberOfBombs){
      Random random = new Random();
      int i = random.nextInt(10);
      int j = random.nextInt(10);
      boardHidden[i][j] = 100;
      var++;
    }
    buildMapBackground();
  }

  public void buildMapBackground(){
    for(int i=0; i<10; i++){
      for(int j=0; j<10; j++){
        int count=0;
        if(boardHidden[i][j]!=100){

          if(i!=0){
              if(boardHidden[i-1][j]==100) 
                count++;
              if(j!=0)
                if(boardHidden[i-1][j-1]==100) 
                  count++;
          }

          if(i!=9){
            if(boardHidden[i+1][j]==100) 
              count++;
            if(j!=9)
              if(boardHidden[i+1][j+1]==100) 
                count++;
          }

          if(j!=0){
            if(boardHidden[i][j-1]==100) 
              count++;
            if(i!=9)
              if(boardHidden[i+1][j-1]==100) 
                count++;
          }

          if(j!=9){
            if(boardHidden[i][j+1]==100) 
              count++;
            if(i!=0)
              if(boardHidden[i-1][j+1]==100) 
                count++;
          }

          boardHidden[i][j] = count;
        }
      }
    }
  }

  public void showVisibleMap(){
    System.out.print("\t ");
    for(int i=0; i<10; i++){
      System.out.print(" " + i + "  ");
    }
    System.out.print("\n");
    for(int i=0; i<10; i++){
      System.out.print(i + "\t| ");
      for(int j=0; j<10; j++){
        if(boardShown[i][j]==0)
            System.out.print(" ");
        else if(boardShown[i][j]==50)
            System.out.print("0");
        else
            System.out.print(boardShown[i][j]);
        System.out.print(" | ");
      }
      System.out.print("\n");
    }
  }

  public void showHiddenMap(){
    System.out.print("\t ");
    for(int i=0; i<10; i++){
      System.out.print(" " + i + "  ");
    }
    System.out.print("\n");
    for(int i=0; i<10; i++){
      System.out.print(i + "\t| ");
      for(int j=0; j<10; j++){
        if(boardHidden[i][j]==0)
          System.out.print("0");
        else if(boardHidden[i][j]==100)
          System.out.print("X");
        else
          System.out.print(boardHidden[i][j]);
        System.out.print(" | ");
      }
      System.out.print("\n");
    }
  }

  public void checkSelectionSurroundings(int i, int j){
    boardShown[i][j] = 50;
    if(i!=0){ 
      syncShownHiddenBoard((i-1),j);
      if(boardShown[i-1][j]==0){
        boardShown[i-1][j] = 50;
        if(j!=0) {
          syncShownHiddenBoard((i-1),(j-1));
          if(boardShown[i-1][j-1]==0) 
            boardShown[i-1][j-1]=50;
        }
      }
    }

    if(j!=0){
      syncShownHiddenBoard(i,(j-1));
      if(boardShown[i][j-1]==0) 
        boardShown[i][j-1] = 50;
        if(i!=9){
          syncShownHiddenBoard((i+1),(j-1));
          if(boardShown[i+1][j-1]==0) 
            boardShown[i+1][j-1] = 50;
        }
    }
     
    if(i!=9){
      syncShownHiddenBoard((i+1),j);
      if(boardShown[i+1][j]==0) 
        boardShown[i+1][j]=50;
        if(j!=9){
          syncShownHiddenBoard((i+1),(j+1));
          if(boardShown[i+1][j+1]==0) 
            boardShown[i+1][j+1] = 50;
        }
    }
    
    if(j!=9){
      syncShownHiddenBoard(i,(j+1));
      if(boardShown[i][j+1]==0) 
        boardShown[i][j+1] = 50;
        if(i!=0){
          syncShownHiddenBoard((i-1),(j+1));
          if(boardShown[i-1][j+1]==0) 
            boardShown[i-1][j+1] = 50;
      }
    }
  }

  public void showRandomSurroundings(int i, int j){
    Random random = new Random();
    int x = random.nextInt()%4;

    syncShownHiddenBoard(i,j);

    if(x==0){
      if(i!=0){
        if(boardHidden[i-1][j]!=100){
          syncShownHiddenBoard((i-1),j);
          if(boardShown[i-1][j]==0)  
            boardShown[i-1][j] = 50;
        }
      }
      if(j!=0){
        if(boardHidden[i][j-1]!=100){
          syncShownHiddenBoard(i,(j-1));
          if(boardShown[i][j-1]==0)  
            boardShown[i][j-1] = 50;
        }
      }
      if(i!=0 && j!=0){
        if(boardHidden[i-1][j-1]!=100){
          syncShownHiddenBoard((i-1),(j-1));
          if(boardShown[i-1][j-1]==0)  
            boardShown[i-1][j-1] = 50;
        }
      }
    }
    else if(x==1){
      if(i!=0){
        if(boardHidden[i-1][j]!=100){
          syncShownHiddenBoard((i-1),j);
          if(boardShown[i-1][j]==0)  
            boardShown[i-1][j] = 50;
        }
      }
      if(j!=9){
        if(boardHidden[i][j+1]!=100){
          syncShownHiddenBoard(i,(j+1));
          if(boardShown[i][j+1]==0)  
            boardShown[i][j+1] = 50;
        }
      }
      if(i!=0 && j!=9){
        if(boardHidden[i-1][j+1]!=100){
          syncShownHiddenBoard((i-1),(j+1));
          if(boardShown[i-1][j+1]==0)  
            boardShown[i-1][j+1] = 50;
        }
      }
    }
    else if(x==2){
      if(i!=9){
        if(boardHidden[i+1][j]!=100){
          syncShownHiddenBoard((i+1),j);
          if(boardShown[i+1][j]==0)  
            boardShown[i+1][j] = 50;
        }
      }
      if(j!=9){
        if(boardHidden[i][j+1]!=100){
          syncShownHiddenBoard(i,(j+1));
          if(boardShown[i][j+1]==0)  
            boardShown[i][j+1] = 50;
        }
      }
      if(i!=9 && j!=9){
        if(boardHidden[i+1][j+1]!=100){
          syncShownHiddenBoard((i+1),(j+1));
          if(boardShown[i+1][j+1]==0)  
            boardShown[i+1][j+1] = 50;
        }
      }
    }
    else{
      if(i!=9){
        if(boardHidden[i+1][j]!=100){
          syncShownHiddenBoard((i+1),j);
          if(boardShown[i+1][j]==0)  
            boardShown[i+1][j] = 50;
        }
      }
      if(j!=0){
        if(boardHidden[i][j-1]!=100){
          syncShownHiddenBoard(i,(j-1));
          if(boardShown[i][j-1]==0)  
            boardShown[i][j-1] = 50;
        }
      }
      if(i!=9 && j!=0){
        if(boardHidden[i+1][j-1]!=100){
          syncShownHiddenBoard((i+1),(j-1));
          if(boardShown[i+1][j-1]==0)  
            boardShown[i+1][j-1] = 50;
        }
      }
    }
  }

  public boolean checkWinProgress(){
    for(int i=0; i<10; i++){
      for(int j=0; j<10; j++){
        if(boardShown[i][j]==0){
          if(boardHidden[i][j]!=100)
                      return false;
        }
      }
    }
    return true;
  }

  public void syncShownHiddenBoard(int i, int j){
    boardShown[i][j] = boardHidden[i][j];
  }

  
  
}
