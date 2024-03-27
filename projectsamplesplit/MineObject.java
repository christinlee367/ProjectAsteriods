import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*; 

//this is an example object
public class MineObject extends DrawableObject
{
   static int way = 1;
   static double colorValue=Math.random();
         
   public MineObject(float x, float y)
   {
      super(x,y);
   }
   
   public static void advanceColor() {
      colorValue += 0.01f * way;
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
   
   public void drawMe(float x, float y, GraphicsContext gc)
   {
         // oscillate between two colors
      
      gc.setFill(Color.WHITE.interpolate(Color.RED, colorValue));
      gc.fillOval(x, y, 10,10);
   }



}