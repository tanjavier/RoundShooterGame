package Util

import Model.{Bullet, Enemy, Player}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break, breakable}

// This class handles the entire logic of the game as well as creating objects for each of the Model classes
class GameEngine() {
  var wave: Int = 1 // The current wave of the game
  var spawnTimer: Double = 1 // The spawn timer for the enemy to appear
  var player: Player = new Player() // Create new instance of Player class
  var enemyEntity: Enemy = new Enemy(wave * 10) // Create new instance of Enemy class
  var bulletEntity: Bullet = new Bullet(player.positionX, player.positionY, player.attack) // Create new instance of Bullet class
  var bullets = new ListBuffer[Bullet] // Use to store multiple bullets
  var enemies = new ListBuffer[Enemy] // Use to store multiple enemies
  ScoresTracker.scores = 0 // Set score to zero at the beginning of the game

  // Interface text to display the current wave of the game
  val waveBoard: Text = new Text(10, 30, "Wave: " + wave) // Set the position and contents of the text
  waveBoard.setFont(Font.font(20)) // Set the text size
  waveBoard.setFill(Color.White) // Set the text color

  // Interface text to display the current health of the player
  val healthBoard: Text = new Text(10, 60, "Health : " + player.health) // Set the position and contents of the text
  healthBoard.setFont(Font.font(20)) // Set the text size
  healthBoard.setFill(Color.White) // Set the text color

  // This method is used to handle the movements of the player in the game
  def playerMovementEngine(): Unit = {
    // If the keys are pressed, move the position of the player entity by the set speed
    if (MouseKeyboardInput.keyW) {
      if (player.positionY > 20) {
        player.entities.centerY = player.positionY - player.steps
      }
    }
    if (MouseKeyboardInput.keyA) {
      if (player.positionX > 20) {
        player.entities.centerX = player.positionX - player.steps
      }
    }
    if (MouseKeyboardInput.keyS) {
      if (player.positionY < 550) {
        player.entities.centerY = player.positionY + player.steps
      }
    }
    if (MouseKeyboardInput.keyD) {
      if (player.positionX < 850) {
        player.entities.centerX = player.positionX + player.steps
      }
    }
  }

  // This method is used to handle the shooting mechanics of the bullets by returning a Some or None of the Bullet class
  def bulletShootingEngine(): Option[Bullet] = {
    // If the player presses the SpaceBar, it will create a new instance of the Bullet class
    // The new instances is then store into the ListBuffer for bullets and return the Option of the new instances
    if (MouseKeyboardInput.keySpaceBar) {
      val newBullets = new Bullet(player.positionX, player.positionY, player.attack)
      bullets += newBullets
      Some(newBullets)
    } else {
      None // If not, the method returns None
    }
  }

  // This method is used to store the multiple bullets that the player shoots by returning a List of the Bullet class
  def bulletEngine(): ListBuffer[Bullet] = {
    // Create a new variable to store any temporary bullets
    val bulletTemp: ListBuffer[Bullet] = new ListBuffer[Bullet]
    // For every bullet created, set the center position of the bullet to the mouse location in the game
    // The bullets will also move away from the player's position at the a set speed
    for (i <- bullets) {
      i.entities.centerX = i.positionX - i.dX / i.dist * bulletEntity.steps
      i.entities.centerY = i.positionY - i.dY / i.dist * bulletEntity.steps
    }
    bulletTemp // Return List
  }

  // This method is used to handle the spawning mechanics of the enemies by returning a Some or None of the Enemy class
  def enemiesSpawnEngine(time: Double): Option[Enemy] = {
    // If the spawn timer is more than zero, enemies will not spawn in the game
    if (spawnTimer > 0) {
      // Deduct the spawn timer by the current time of the game
      spawnTimer -= time
      None // Return None
    } else {
      // When the spawn timer hits zero, enemies will start to spawn in the game
      spawnTimer = 1 - (wave * 0.025) // Set the spawning intervals for new enemies, the higher the wave the lesser the spawning intervals, meaning enemies will spawn faster
      // Set the amount of enemies allowed to spawn in each wave
      if (enemies.size < wave + 10) {
        // Create new instances of the enemies using the Enemy class
        // The new instances is then store into the ListBuffer for enemies and return the Option of the new instances
        val newEnemies = new Enemy(10 * wave)
        enemies += newEnemies
        Some(newEnemies)
      } else {
        None // Return None
      }
    }
  }

  // This method is used to handle the movements of the enemies in the game
  def enemiesMovementEngine(): ListBuffer[Enemy] = {
    // Create a new variable to store any temporary enemies
    val enemyTemp: ListBuffer[Enemy] = new ListBuffer[Enemy]
    // For every enemies that is still alive, set the position of the enemy to the same as the position of the player
    // This means that the enemies will move towards the player at a set speed
    for (i <- enemies) {
      if (i.isAlive) {
        val dX = player.positionX - i.positionX
        val dY = player.positionY - i.positionY
        val dist = math.sqrt(dX * dX + dY * dY)
        i.entities.centerX = i.positionX + dX / dist * enemyEntity.steps
        i.entities.centerY = i.positionY + dY / dist * enemyEntity.steps
        // If the player position and enemy position is more than the calculated distance, the player's health will be reduce by the amount of the enemy's attack
        // The enemies that manages to touch the player will also be deleted from the ListBuffer
        if (i.entities.radius.toDouble + player.entities.radius.toDouble > dist) {
          player.health -= i.attack
          enemyTemp += enemies.remove(enemies.indexOf(i))
        }
      }
    }
    enemyTemp // Return List
  }

  // This method is used to handle the bullets collision with the enemies by returning a List of the Enemy and Bullet classes
  def bulletCollisionEngine(): (ListBuffer[Enemy], ListBuffer[Bullet]) = {
    val enemiesDestroyed = new ListBuffer[Enemy] // Store the enemies that were eliminated
    val bulletsUsed = new ListBuffer[Bullet] // Store the bullets that hit the enemies
    // For every bullet and enemy, check their distance from one another
    for (i <- bullets) {
      breakable { // Use breakable to terminate the loop
        for (j <- enemies) {
          val dX = i.positionX - j.positionX
          val dY = i.positionY - j.positionY
          val dist = math.sqrt(dX * dX + dY * dY)
          // If their position is more than the calculated distance, the enemies' health will be deducted based on the bullet's damage value
          if (i.entities.radius.toDouble + j.entities.radius.toDouble > dist) {
            j.health -= i.damage
            // Remove the bullets that hit the enemies from the List
            bulletsUsed += bullets.remove(bullets.indexOf(i))
            // If the enemies' health is zero or less, remove the enemies from the List and increase the score by one
            if (j.health <= 0) {
              enemiesDestroyed += enemies.remove(enemies.indexOf(j))
              ScoresTracker.scores += 1
              // If the score reaches any value divisible by five, increase the wave by one
              if (ScoresTracker.scores % 5 == 0) {
                wave += 1
              }
            }
            break() // Break the loop
          }
        }
      }
    }
    (enemiesDestroyed, bulletsUsed) // Return both List
  }

  // This method is used to handle the death of the player by returning a Boolean
  def deathEngine(): Boolean = {
    // If the player's health is zero or lesser, this method will a true
    if (player.health <= 0) {
      true
    } else {
      // else false
      false
    }
  }

  // This method is used to handle the interface of the game
  def interfaceEngine(wave: Int, health: Int): Unit = {
    // The interface is updated with the current values
    waveBoard.setText(s"Wave: $wave")
    healthBoard.setText(s"Health: $health")
  }
}