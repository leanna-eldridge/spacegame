package cs2.game

import scalafx.scene.image.Image
import cs2.util.Vec2

/** An boss representation for a simple game based on sprites. Handles all
 *  information regarding the boss's position, movements, and abilities.
 *
 *  @param pic the image representing the boss
 *  @param initPos the initial position of the '''center''' of the boss
 *  @param bulletPic the image of the bullets fired by this boss
 */
class Boss(pic:Image, initPos:Vec2, bulletPic:Image, var hp:Int) extends Enemy(pic, initPos, bulletPic) {
    private val step = 8

    /** moves the enemy sprite one "step" to the left.  The amount of this
     *  movement will likely need to be tweaked in order for the movement to feel
     *  natural.
     *
     *  @return none/Unit
     */
    def moveLeft():Unit = { this.move(Vec2(-step, 0))} //negative x direction, no y direction

    /** moves the enemy sprite one "step" to the right (see note above)
     *
     *  @return none/Unit
     */
    def moveRight():Unit = { this.move(Vec2(step, 0))} //positive x direction, no y direction

    /** move the enemy sprite one "step" in the upwards direction (see note above)
     * 
     * @return none/Unit
     */
    def moveUp():Unit = { this.move(Vec2(0, -step))} //no x direction, negative y direction

    /** move the enemy sprite one "step" in the downwards direction (see note above)
     * 
     * @return none/Unit
     */
    def moveDown():Unit = { this.move(Vec2(0, step))} //no x direction, positive y direction
}