package cs2.units

class Volume(private var lit:Double = 0.0) {
  //Field is in the argument list above - the volume stored in LITERS

  //Basic math operators to add, subtract, and scale volumes
  def + (other:Volume):Volume = { new Volume(this.lit + other.lit) }
  def += (other:Volume):Unit  = { this.lit = this.lit + other.lit }

  def - (other:Volume):Volume = { new Volume(this.lit - other.lit) }
  def -= (other:Volume):Unit  = { this.lit = this.lit - other.lit }

  def * (scalar:Double):Volume = { new Volume(this.lit * scalar) } //NOTE: NOT to multiply 2 Volumes
  def *= (scalar:Double):Unit  = { this.lit = this.lit * scalar }

  def / (scalar:Double):Volume = { new Volume(this.lit / scalar) } //NOTE: NOT to divide 2 Volumes
  def /= (scalar:Double):Unit  = { this.lit = this.lit / scalar }

  //Getter functions that return in a variety of units
  def liters():Double = { lit }
  def milliliters():Double = { lit * 1000 }
  def gallons():Double = { lit * 0.264172 }
  def quarts():Double = { lit * 1.05669 }
  def pints():Double = { lit * 2.11338 }
  def cups():Double = { lit * 4.16667 }
  def teaspoons():Double = { lit * 202.884 }
  def tablespoons():Double = { lit * 67.628 }
}

object Volume {
  //"Constructor" apply methods operating in liters
  def apply():Volume = { new Volume(0) }
  def apply(amt:Double):Volume = { new Volume(amt) }

  //Alternative "static" methods to create volumes in other units
  def liters(amt:Double):Volume = { new Volume(amt) } //identical to an apply method
  def milliliters(amt:Double):Volume = { new Volume(amt / 1000) }
  def gallons(amt:Double):Volume = { new Volume(amt / 0.264172) }
  def quarts(amt:Double):Volume = { new Volume(amt / 1.05669) }
  def pints(amt:Double):Volume = { new Volume(amt / 2.11338) }
  def cups(amt:Double):Volume = { new Volume(amt / 4.16667) }
  def teaspoons(amt:Double):Volume = { new Volume(amt / 202.884) }
  def tablespoons(amt:Double):Volume = { new Volume(amt / 67.628) }
}

