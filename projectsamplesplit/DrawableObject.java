import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public abstract class DrawableObject
{
   public DrawableObject(float x, float y)
   {
      this.x = x;
      this.y = y;
   }

   //positions
   private float x;
   private float y;
   private float speedX, speedY;
   private boolean playerMovingToNewGrid;
   private boolean distanceIncreased = false;
   
   //takes the position of the player and calls draw me with appropriate positions
   public void draw(float playerx, float playery, GraphicsContext gc, boolean isPlayer)
   {
      //the 300,300 places the player at 300,300, if you want to change it you will have to modify it here
      if(isPlayer)
         drawMe(playerx,playery,gc);
      else
         drawMe(-playerx+300+x,-playery+300+y,gc);
   }
   
   //this method you implement for each object you want to draw. Act as if the thing you want to draw is at x,y.
   //NOTE: DO NOT CALL DRAWME YOURSELF. Let the the "draw" method do it for you. I take care of the math in that method for a reason.
   public abstract void drawMe(float x, float y, GraphicsContext gc);
   public void act(boolean left, boolean right, boolean down, boolean up)
   {
      //Each “tick” should add .1 force to the player’s speed in that direction
      if (up)
         speedY -= .1;
      if (down)
         speedY += .1;
      if (left)
         speedX -= .1;
      if (right)
         speedX += .1;
      // if key is not held down for that particular axis, the force is reduced by .025
      if (!up && !down) {
         // If a key is not pushed and -.25 < force < .25, set that force to 0
         if (Math.abs(speedY) >= 0.25) {
            if (speedY > 0)
               // if the speed was going down
               speedY -= .025;
            else
               // if the speed was going up
               speedY += .025;
         } else {
         // if not, set force to 0
            speedY = 0;
         }  
      }        
      if (!left && !right) {
      // If a key is not pushed and -.25 < force < .25, set that force to 0
         if (Math.abs(speedX) >= 0.25) {
            if (speedX > 0)
            // if speed was going right
               speedX -= .025;
            else
            // if speed was going left 
               speedX += .025;
         } else {
         // if not, set force to 0
            speedX = 0;
         } 
      }
         
      // max is 5 or -5 for each axis
      if (speedX > 5) {
         speedX = 5;
      } else if (speedX < -5) {
         speedX = -5;
      }
      if (speedY > 5) {
         speedY = 5;
      } else if (speedY < -5) {
         speedY = -5;
      }
      
      int prevXSquare = (int)x/100;
      int prevYSquare = (int)y/100;

   // turn constant speed into variable speed
      x += speedX;
      y += speedY;
      
      int currXSquare = (int)x/100;
      int currYSquare = (int)y/100;
      if (prevXSquare != currXSquare || prevYSquare!= currYSquare)
         playerMovingToNewGrid = true;
   
   }
   
   public float getX(){
      return x;}
   public float getY(){
      return y;}
   public void setX(float x_){x = x_;}
   public void setY(float y_){y = y_;}
   
   public boolean playerMovingToNewGrid() {
      if (playerMovingToNewGrid) {
         playerMovingToNewGrid = false;
         return true;
      }
      return false;
   }
   public double distance(DrawableObject other)
   {
      return (Math.sqrt((other.x-x)*(other.x-x) +  (other.y-y)*(other.y-y)   ));
   }
}