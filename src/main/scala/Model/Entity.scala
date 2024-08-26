package Model

import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

// This is an abstract class that is mainly used to create and denote the entities of the game
abstract class Entity(coordinateX: Double, coordinateY: Double, size: Double, color: Color) {
  // Create entities in the form of Circles
  val entities: Circle = new Circle {centerX = coordinateX; centerY = coordinateY; radius = size; fill = color}
  // Find the center position of any entity in the X-axis
  def positionX: Double = entities.centerX.toDouble
  // Find the center position of any entity in the Y-axis
  def positionY: Double = entities.centerY.toDouble
}

