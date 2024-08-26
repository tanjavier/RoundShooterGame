package Model

import scalafx.scene.paint.Color
import Util.MouseKeyboardInput.{mouseX, mouseY}

// This is an abstract class that is a subclass of the parent class Entity
class Bullet(val X: Double, val Y: Double, val damage: Int) extends Entity(X, Y, 5, Color.White) {
  // In this class, three new variables are created which are steps, targetX, and targetY
  val steps: Int = 15 // Use to store the speed of a Bullet
  var locationX: Double = mouseX // Use to store the location of the mouse in the X-axis
  var locationY: Double = mouseY // Use to store the location of the mouse in the Y-axis
  // Calculation to find the initial position and the current position of the Bullet
  def dX(): Double = X - locationX
  def dY(): Double = Y - locationY
  def dist(): Double = math.sqrt(dX * dX + dY * dY)
}