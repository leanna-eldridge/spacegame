package cs2.util

class Vec2 (var x:Double, var y:Double) {
  /** DO NOT MODIFY THE FOLLOWING TOSTRING METHOD **/
  override def toString():String = "("+x+","+y+")"
  
  //Methods for addition and subtraction of vectors
  def +  (other:Vec2):Vec2 = { Vec2(this.x + other.x, this.y + other.y) }
  def += (other:Vec2):Unit = { this.x = this.x + other.x; this.y = this.y + other.y }
  
  def -  (other:Vec2):Vec2 = { Vec2(this.x - other.x, this.y - other.y) }
  def -= (other:Vec2):Unit = { this.x = this.x - other.x; this.y = this.y - other.y }

  //Methods for multiplication and division of vectors by a scalar (non-vector)
  def *  (scalar:Double):Vec2 = { Vec2(this.x * scalar, this.y * scalar) }
  def *= (scalar:Double):Unit = { this.x = this.x * scalar; this.y = this.y * scalar }

  def /  (scalar:Double):Vec2 = { Vec2(this.x / scalar, this.y / scalar) }
  def /= (scalar:Double):Unit = { this.x = this.x / scalar; this.y = this.y / scalar }

  //Methods to determine the length of a vector (magnitude and length should return the same value)
  def magnitude():Double = { math.sqrt((math.pow(this.x, 2) + (math.pow(this.y, 2)))) }
  def length():Double = { this.magnitude() }
  
  //Methods to calculate the dot product (same returns)
  def dot(other:Vec2):Double = { this.x * other.x + this.y * other.y }
  def **(other:Vec2):Double = { this.dot(other) }
  
  //Methods to determine the angle between 2 vectors (same returns)
  def angleBetween(other:Vec2):Double = { math.acos((this.dot(other)) / (this.magnitude() * other.magnitude())) }
  def <>(other:Vec2):Double = { this.angleBetween(other) }

  //Methods to return a new vector that is in the same direction, but length 1 (same returns)
  def normalize():Vec2 = { this / this.magnitude() }
  def unary_~ : Vec2 = { this.normalize() }

  //A clone operator can be useful when making "deep" copies of objects
  override def clone():Vec2 = { Vec2(this.x, this.y) }
}

object Vec2 {
  //These apply methods can be used for constructing Vec2 instances without saying "new"
  /** DO NOT CHANGE THE FOLLOWING THREE APPLY METHODS**/
  def apply(myX:Double, myY:Double):Vec2 = { new Vec2(myX, myY) }
  def apply(toCopy:Vec2):Vec2 = { new Vec2(toCopy.x, toCopy.y) }
  def apply():Vec2 = { new Vec2(0, 0) }

  def main(args:Array[String]):Unit = {
    /** Your solution to the physics problem described should be calculated here.
     *  Remember to print out your answer using println.
     */
    
    val sanAntonio:Vec2 = Vec2(29.4241, 98.4936)
    val austin:Vec2 = Vec2(30.2672, 97.7431)

    val betweenAS:Vec2 = austin - sanAntonio
    val velocity:Double = betweenAS.magnitude() / 1.5

    val direction:Vec2 = betweenAS.normalize()
    val betweenAX:Vec2 = (direction * velocity) * 4

    val result:Vec2 = sanAntonio + betweenAX

    println(result)
    
  }
}
