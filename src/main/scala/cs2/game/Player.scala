package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

/** The player representation for a simple game based on sprites. Handles all
 *  information regarding the player's positions, movements, and abilities.
 *
 *  @param avatar the image representing the player
 *  @param initPos the initial position of the '''center''' of the player
 *  @param bulletPic the image of the bullets fired by this player
 */
class Player(avatar:Image, initPos:Vec2, private val bulletPic:Image) extends Sprite(avatar, initPos) {

  private val step = 8

  /** moves the player sprite one "step" to the left.  The amount of this
   *  movement will likely need to be tweaked in order for the movement to feel
   *  natural.
   *
   *  @return none/Unit
   */
  def moveLeft():Unit = { this.move(Vec2(-step, 0))} //negative x direction, no y direction

  /** moves the player sprite one "step" to the right (see note above)
   *
   *  @return none/Unit
   */
  def moveRight():Unit = { this.move(Vec2(step, 0))} //positive x direction, no y direction

  /** move the player sprite one "step" in the upwards direction (see note above)
   * 
   * @return none/Unit
   */
  def moveUp():Unit = { this.move(Vec2(0, -step))} //no x direction, negative y direction

  /** move the player sprite one "step" in the downwards direction (see note above)
   * 
   * @return none/Unit
   */
  def moveDown():Unit = { this.move(Vec2(0, step))} //no x direction, positive y direction

  /** creates a new Bullet instance beginning from the player, with an
   *  appropriate velocity
   *
   *  @return Bullet - the newly created Bullet object that was fired
   */
  def shoot():Bullet = { new Bullet(bulletPic, Vec2(this.pos.x + avatar.width.value/2 - bulletPic.width.value/2, this.pos.y + avatar.height.value/2 - bulletPic.height.value/2), Vec2(0,-2)) }

  /**
    * @return clone of Player at current position
    */
  override def clone():Player = { new Player(this.avatar, this.pos.clone(), this.bulletPic) }

}
