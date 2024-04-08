import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*; 

//this is an example object
public class MineObject extends DrawableObject
{
   private int way = 1;
   private double colorValue=Math.random();
   // center coordinate of the mine object 
         
   public MineObject(float x, float y)
   {
      super(x,y);
   }
   
   public void advanceColor() {
      colorValue += 0.1f * way;
      // adding or substracting?
      if (colorValue > 1) {
         // oscillate between 0 to 1 color values 
         colorValue = 1;
         way = -1;
      }
      if (colorValue < 0) {
         // oscillate between 0 to 1 color values 
         colorValue = 0;
         way = 1;
      }
   }
   
  // public void createMines(int x, int y, 
   
   public void drawMe(float x, float y, GraphicsContext gc)
   {
         // oscillate between two colors
      gc.setFill(Color.WHITE.interpolate(Color.RED, colorValue));
      gc.fillOval(x, y, 10,10);
   }
   

}