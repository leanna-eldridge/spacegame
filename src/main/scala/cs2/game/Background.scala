package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

/** Representation of a bullet/projectile for a simple game based on sprites.
 *  Handles all information regarding a bullet's position, movements, and 
 *  abilities.
 *  
 *  @param pic the image representing the background piece
 *  @param initPos the initial position of the top left corner of the background
 *  @param vel the initial velocity of the background
 */
class Background(pic:Image, initPos:Vec2, private var vel:Vec2) extends Sprite(pic, initPos) {

  /** advances the position of the background over a single time step
   * 
   *  @return none/Unit
   */
  def timeStep():Unit = { this.move(vel) } //Note the presence of velocity as an argument

  /**
    * @return clone of Bullet at current pos
    */
  override def clone():Background = { new Background(this.pic, this.pos.clone(), this.vel.clone()) }
  
}