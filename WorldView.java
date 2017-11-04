import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

final class WorldView
{

   protected WorldModel world;

   protected Viewport viewport;
   protected int tileWidth;
   protected int tileHeight;
   protected PApplet screen;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public void shiftView(int colDelta, int rowDelta)
   {
      int newCol = Functions.clamp(viewport.col + colDelta, 0,
              world.numCols - viewport.numCols);
      int newRow = Functions.clamp(viewport.row + rowDelta, 0,
              world.numRows - viewport.numRows);

      viewport.shift(newCol, newRow);
   }

   public void drawBackground()
   {
      for (int row = 0; row < viewport.numRows; row++)
      {
         for (int col = 0; col < viewport.numCols; col++)
         {
            Point worldPoint = viewport.viewportToWorld(col, row);
            Optional<PImage> image = world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               screen.image(image.get(), col * tileWidth,
                       row * tileHeight);
            }
         }
      }
   }

   public void drawEntities()
   {
      for (Entity entity : world.entities)
      {
         Point pos = entity.getPosition();

         if (viewport.contains(pos))
         {
            Point viewPoint = viewport.worldToViewport(pos.x, pos.y);
            screen.image(entity.getCurrentImage(),
                    viewPoint.x * tileWidth, viewPoint.y * tileHeight);
         }
      }
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }
}
