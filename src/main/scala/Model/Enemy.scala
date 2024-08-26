package Model

import scalafx.scene.paint.Color
import scala.util.Random

// This is a class that extends from the Unit class, meaning that this is a subclass of the Unit class
// This class has two additional variables which are steps and isAlive
class Enemy(health: Int) extends Unit(health, 10, Random.nextInt(900), Random.nextInt(600), 30, Color.LightBlue) {
  val steps: Int = 1 // Use to store the speed of the Enemy
  val isAlive: Boolean = true // Use to store whether the Enemy is alive or not
}