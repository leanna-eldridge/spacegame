package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.canvas.Canvas

/** A graphical sprite object used for gaming or other visual displays
 *  
 *  @constructor create a new sprite based on the given image and initial location
 *  @param img the image used to display this sprite
 *  @param pos the initial position of the '''center''' of the sprite in 2D space
 */
abstract class Sprite (protected val img:Image, protected var pos:Vec2) {

  /** moves the sprite a relative amount based on a specified vector
   *  
   *  @param direction - an offset that the position of the sprite should be moved by
   *  @return none/Unit
   */
  def move(direction:Vec2):Unit = { pos += direction } //currently adds offset of direction (x and y values) to current position **UNTESTED**
  
  /** moves the sprite to a specific location specified by a vector (not a relative movement)
   *  
   *  @param location - the new location for the sprite's position
   *  @return none/Unit
   */
  def moveTo(location:Vec2):Unit = { pos = location } //changes pos vector to equal location vector
  
  /** Method to display the sprite at its current location in the specified Graphics2D context
   *  
   *  @param g - a GraphicsContext object capable of drawing the sprite
   *  @return none/Unit
   */
  def display(g:GraphicsContext):Unit = { g.drawImage(img, pos.x, pos.y) } //draws passed in avatar at curr value of pos (NOTE THERE ARE NO VALUES FOR IMG SIZE)

  /** 
    * @return current position of sprite as Vec2
    * 
    */
  def position():Vec2 = { pos }

  /**
    * Method to determine if the sprite is currently on screen
    *
    * @param c
    * @return boolean indicating whether sprite is onscreen
    */
  def isOnscreen(c:Canvas):Boolean = {
    (this.pos.x > 0 //checks if offscreen left
    && this.pos.x + this.img.width.value < c.width.value //checks if offscreen right
    && this.pos.y > 0 //checks if offscreen left
    && this.pos.y + this.img.height.value < c.height.value) //checks if offscreen right
  }

  /**
    * Method to determine if one sprite intersects another
    *
    * @param other
    * @return boolean indicating presence of intersection
    */
  def intersects(other:Sprite):Boolean = {

    //x1,y1 is lower left vertice of rectangle
    //x2,y2 is upper right vertice of rectangle

    val r1x1 = this.pos.x //x1
    val r1y1 = this.pos.y + this.img.height.value //y1
    val r1x2 = this.pos.x + this.img.width.value //x2
    val r1y2 = this.pos.y //y2
                    
    val r2x1 = other.pos.x //x1
    val r2y1 = other.pos.y + other.img.height.value //y1
    val r2x2 = other.pos.x + other.img.width.value //x2
    val r2y2 = other.pos.y //y2
                   

    //boolean checks for intersection by checking if rectangles don't overlap and negating result
    
    !(r1x2 <= r2x1 || //checks if r1 is to the left of r2
      r1y2 >= r2y1 || //checks if r1 is below r2
      r1x1 >= r2x2 || //checks if r1 is to the right of r2
      r1y1 <= r2y2    //checks if r1 is above r2
    )
  }
  
}
