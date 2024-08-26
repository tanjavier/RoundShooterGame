package Model

import scalafx.scene.paint.Color

abstract class Unit(var health: Int, var attack: Int, coordinateX: Double, coordinateY: Double, size: Double, color: Color) extends Entity(coordinateX: Double, coordinateY: Double, size: Double, color: Color) {
  // This is an abstract class that is a subclass of the parent class Entity
  // In this abstract class, two new variables are created which are the health and attack variables
}