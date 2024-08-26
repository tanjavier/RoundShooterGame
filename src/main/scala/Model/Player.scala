package Model

import scalafx.scene.paint.Color

// This is a class that extends from the Unit class, meaning that this is a subclass of the Unit class
// This class has an additional variables which is steps
class Player() extends Unit(100, 10,450,300, 30, Color.Blue) {
  val steps: Int = 10 // Store the speed of the Player
}