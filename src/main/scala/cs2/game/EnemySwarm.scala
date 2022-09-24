package cs2.game

import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.image.Image
import cs2.util.Vec2
import scala.util.Random
import scala.collection.mutable.Buffer
import scalafx.scene.canvas.Canvas

/** contains the control and logic to present a coordinated set of Enemy objects.
 *  For now, this class generates a "grid" of enemy objects centered near the
 *  top of the screen.
 *
 *  @param nRows - number of rows of enemy objects
 *  @param nCols - number of columns of enemy objects
 */
class EnemySwarm(private val pic:Image, private val bulletPic:Image, private val nRows:Int, private val nCols:Int, private val initPos:Vec2) { //NOTE: change to intake bullet image and enemy image

  //enemies - instantiate new enemies and add to list based on nRows, nCols

  var currX = initPos.x
  var currY = initPos.y
  var enemies = Buffer[Enemy]()
  for(r <- 1 to nRows) {
    for(c <- 1 to nCols) {
      enemies +:= new Enemy(pic,Vec2(currX,currY),bulletPic)
      currX += pic.width.value + bulletPic.width.value
    }
    currX = initPos.x
    currY += pic.height.value + 10
  }

  /** method to display all Enemy objects contained within this EnemySwarm
   *
   *  @param g - the GraphicsContext to draw into
   *  @return none/Unit
   */
  def display(g:GraphicsContext):Unit = { 
    for(e <- enemies) { 
      e.display(g) 
    } 
  }

  /** overridden method of ShootsBullets. Creates a single, new bullet instance
   *  originating from a random enemy in the swarm. (Not a bullet from every
   *  object, just a single from a random enemy)
   *
   *  @return Bullet - the newly created Bullet object fired from the swarm
   */
  def shoot():Bullet = { 
    //pick random enemy to have bullet originate from
    //have random enemy shoot
    (enemies(Random.nextInt(enemies.length))).shoot()
  }

  /**
    * @param other
    * @return boolean indicating whether or not there is an intersection present
    */
  def intersects(other:Sprite):Boolean = {
    var isIntersecting = false
    for(e <- enemies) {
      if(e.intersects(other)) {
        isIntersecting = true
      }
    }
    isIntersecting
  }

  /**
    * @param b
    * @return boolean indicating whether or not the given bullet hit an enemy
    */
  def hitBy(b:Bullet):Boolean = {
    var doesHit = false
    var remove = Buffer[Enemy]()
    for(e <- enemies) {
      if(e.intersects(b)) {
        doesHit = true
        remove += e
      }
    }
    enemies --= remove
    doesHit
  }

  /**
    * 
    * @return boolean indicating whether the enemyswarm is empty (if any enemy is left)
    */
  def isEmpty():Boolean = {
    if(enemies.length == 0) true
    else false
  }

  var xvel = 2.5
  var yvel = 10
  /**
    * moves all enemies in enemy swarm one step in a direction
    * direction of movement depends on current location of swarm
    * 
    * @param c
    */
  def move(c:Canvas):Unit = {
    var changeX = false
    var changeY= false
    //check curr position of each enemy in swarm, determine how to update pos of enemies
    for(e <- enemies) {
      if(e.position.x > c.width.value - pic.width.value - 5 || e.position.x < 0 + 5) { //plus or minus 5 for aesthetic value
        changeX = true
      }
      if(e.position.y > c.height.value - pic.height.value - 5 || e.position.y < 0 + 5) {
        changeY = true
      }
    }

    //update xvel and yvel as needed
    if(changeX) { 
      xvel = xvel*(-1)
    }
    if(changeY) {
      yvel = yvel*(-1)
    }

    //update curr position of each enemy in swarm
    for(e <- enemies) {
      if(changeX) { //when x direction changes, move down/up some
        e.move(Vec2(xvel, yvel))
      } else if(changeY) {
        e.move(Vec2(0, yvel)) //when y direction changes, need to move in that direction some to keep direction change
      } else {
        e.move(Vec2(xvel, 0)) //otherwise just move left/right
      }
    }
  }

  // need to change clone below to include some way to clone current enemy condition in enemy swarm 
  override def clone():EnemySwarm = { 
    val clone = new EnemySwarm(this.pic, this.bulletPic, this.nRows, this.nCols, Vec2(this.currX, this.currY))
    var enemyClone = Buffer[Enemy]()
    for(e <- enemies) {
      enemyClone +:= e.clone()
    }
    clone.enemies = enemyClone
    clone
  }
  
}
