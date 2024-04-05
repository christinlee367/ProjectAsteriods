import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;


public class Main extends Application
{
   FlowPane fp;
   
   Canvas theCanvas = new Canvas(600,600);
   TestObject thePlayer = new TestObject(300,300); 
   TestObject origin = new TestObject(300,300); 
   ArrayList<MineObject> mineList = new ArrayList<MineObject>(); 
   MineObject mo = new MineObject(300,300);
   AnimationHandler ah; 
   int highScore;
   
   public void start(Stage stage)
   {
      try {
         File file = new File("output.txt");
         if (file.exists()) {
            Scanner scan = new Scanner(file);
            highScore = scan.nextInt();
            scan.close();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      fp = new FlowPane();
      fp.getChildren().add(theCanvas);
      gc = theCanvas.getGraphicsContext2D();
      drawBackground(300,300,gc);
      
      fp.setOnKeyPressed(new KeyListenerDown());
      fp.setOnKeyReleased(new KeyListenerUp());
      
      ah = new AnimationHandler();
      ah.start();
   
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      fp.requestFocus();
      
   }
   
   GraphicsContext gc;
   
   
   
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
     //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
   //figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
     //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
     //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   boolean up, down, left, right;
   public class KeyListenerDown implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
      // Particular mouse action
         if (event.getCode() == KeyCode.A) 
         {
            left = true;
         }
         if (event.getCode() == KeyCode.D)  
         {
            right = true;
         }
         if (event.getCode() == KeyCode.W)  
         {
            up = true;
         }
         if (event.getCode() == KeyCode.S)  
         {
            down = true;
         }
      }
   } 
   
   public class KeyListenerUp implements EventHandler<KeyEvent>  
   {
      public void handle(KeyEvent event) 
      { 
         if (event.getCode() == KeyCode.A) 
         {
            left = false;
         }
         if (event.getCode() == KeyCode.W)  
         {
            up = false;
         }
         if (event.getCode() == KeyCode.S)  
         {
            down = false;
         }
         if (event.getCode() == KeyCode.D)  
         {
            right = false;
         }
      }
   } 

   // tick in the animationHandler
   // boolean in the act/ key handler
   private boolean lose = false;
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      { 
         gc.clearRect(0,0,600,600);
         
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc);
         // drawableObject act method 
         gc.setFill(Color.WHITE); 
         gc.fillText("Score: " + (int)thePlayer.distance(origin), 0,20);
         gc.fillText("High Score: " + highScore, 0,45);
            if ((int)thePlayer.distance(origin) > highScore)
               highScore = (int)thePlayer.distance(origin);
          
          thePlayer.act(left, right, down, up);
         // Losing: hitting a mine
         /*for (int i = 0; i < mineList.size(); i++) {
            MineObject m = mineList.get(i);
            if (m.distance(thePlayer) <= 20) {
               lose = true;
               mineList.remove(m);
               ah.stop();
            }
         }*/
         
         
         try {
            if (lose && highScore < (int)thePlayer.distance(origin)) {
               highScore = (int)thePlayer.distance(origin);
               FileOutputStream fos = new FileOutputStream("output.txt", false); 
               gc.fillText("High Score: " + highScore, 0,45); 
               fos.write(String.valueOf(highScore).getBytes()); 
               fos.close();
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         if (!lose) {
         //example calls of draw - this should be the player's call for draw
            thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter
         }
         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         //m.draw(thePlayer.getX(),thePlayer.getY(),gc,false);
         if (thePlayer.playerMovingToNewGrid()) {
            createMines();
         }
         
         // display the mines
         for (int i = 0; i < mineList.size(); i++) {
            MineObject m = mineList.get(i);
            m.advanceColor();
            m.draw(thePlayer.getX(),thePlayer.getY(),gc,false);
            //System.out.println(mineList.size() + " the player (" + thePlayer.getX() + "," + thePlayer.getY() + "), mine (" + m.getX() + "," + m.getY() + ")");
            //System.out.println(" coordinates of mine: x:" + m.getX() + " y: " + m.getY());
         
         }
         // delete the mines
         for (int i = 0; i < mineList.size(); i++) {
            MineObject m = mineList.get(i);
            if (thePlayer.distance(m) >= 800) {
               mineList.remove(i);
               i--;
            }
         }
      }
      
      private void createMines() {
      // grid position of the player
         int cgridx = ((int)thePlayer.getX())/100;
         int cgridy = ((int)thePlayer.getY())/100;
         
         // top left BOX corner coordinates and conversion to double
         int topLeftXBox =  cgridx - 5;
         // the X: top center point of the top left box
         float realLeftX = (topLeftXBox * 100) + 50; 
         // the Y: top center point of the top left box
         int topLeftY = cgridy - 5;
         float realTopLeftY = (topLeftY * 100) + 50;
         
         //top right BOX corner coordinates and conversion to double
         int topRightX = cgridx + 4;
         float realRightX = (topRightX * 100) + 50;
         int topRightY = cgridy - 4;
         float realRightY = (topRightY * 100) + 50;
         
         //bottom left corner coordinates and conversion to double
         int botLeftX = cgridx - 5;
         float realBotLeftX = (botLeftX * 100) + 50;
         int botLeftY = cgridy + 4;
         float realBotLeftY = (botLeftY * 100) + 50;
         
         ArrayList<MineObject> gridList = new ArrayList<MineObject>();
      
         // create left column of mines (save center real coordinate for each box)
         for (int i = 0; i < 10; i++) {
            int y = topLeftY + i; 
            float yCoor = (y * 100) + 50;
            MineObject m = new MineObject(realLeftX, yCoor);
            gridList.add(m);
         }
         // create right column of mines (save center real coordinate for each box)
         for (int i = 0; i < 10; i++) {
            int y = topRightY + i; 
            float yCoor = (y * 100) + 50;
            MineObject m = new MineObject(realRightX, yCoor);
            gridList.add(m);
         }
         // create top row of 8 mines (save center real coordinate for each box)
         for (int i = 1; i < 9; i++) {
            int x = topLeftXBox + i;
            float xCoor = (x * 100) + 50;
            MineObject m = new MineObject(xCoor, realTopLeftY);
            gridList.add(m);
         }
         // create bottom row of 8 mines (save center real coordinate for each box)
         for (int i = 1; i < 9; i++) {
            int x = botLeftX + i;
            float xCoor = (x * 100) + 50;
            MineObject m = new MineObject(xCoor, realBotLeftY);
            gridList.add(m);
         }
         
         // Value of N - max amount of mines 
         
         Random rand = new Random();
         int N = (int)(thePlayer.distance(origin)) / 1000;
         if ((int)(thePlayer.distance(origin)) < 1000) {
            N = 0;
         }
         
         for (int i = 0; i < gridList.size(); i++) {
            MineObject m = gridList.get(i);
               // generate new random coordinates of x and y in the box boundaries
               // make the mine in a random place inside a box
               
            for (int j = 0; j < N; j++) {
               int probability = rand.nextInt(10 - 1 + 1) + 1;
               if (probability < 4) {
                  // left corner coordinate
                  System.out.println("N: " + N);
                  float newRanMineX = rand.nextFloat(((m.getX()+100) - m.getX()) + 1) + m.getX();
                  float newRanMineY = rand.nextFloat(((m.getY()+100) - m.getY()) + 1) + m.getY();
                  MineObject mobj = new MineObject(newRanMineX, newRanMineY);
                  //System.out.println("Created Mine: " + newRanMineX + "," + newRanMineY);
                  mineList.add(mobj);
               // max amount N of mines (so subtract as finished after calculating each probability)
               }

            } 
             // check next box         
         }
      }
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}

