import Util.{GameEngine, MouseKeyboardInput}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.Image
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.{Scene, layout}
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

import java.net.URL

object MainGame extends JFXApp {
  // Transform path of RootLayout.fxml to InputStream for resource location
  val rootResource: URL = getClass.getResource("RootLayout.fxml")
  // Initialize the loader object
  val loader: FXMLLoader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from fxml file
  loader.load()
  // Retrieve the root component BorderPane from the FXML
  val roots: layout.BorderPane = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage() {
    title = "Round Shooter"
    resizable = false
    width = 900
    height = 600
    // Set a windows icon
    icons += new Image(getClass.getResourceAsStream("/images/gameLogo.png"))
    scene = new Scene() {
      // Track the mouse movements from the user
      onMouseMoved = (me: MouseEvent) => {
        MouseKeyboardInput.mouseX = me.x
        MouseKeyboardInput.mouseY = me.y
      }

      // Change the variables based on the keys pressed by the user
      onKeyPressed = (ke: KeyEvent) => {
        ke.code match {
          case KeyCode.W => MouseKeyboardInput.keyW = true
          case KeyCode.A => MouseKeyboardInput.keyA = true
          case KeyCode.S => MouseKeyboardInput.keyS = true
          case KeyCode.D => MouseKeyboardInput.keyD = true
          case KeyCode.Space => MouseKeyboardInput.keySpaceBar = true
          case _ =>
        }
      }

      // Change the variables based on the keys released by the user
      onKeyReleased = (ke: KeyEvent) => {
        ke.code match {
          case KeyCode.W => MouseKeyboardInput.keyW = false
          case KeyCode.A => MouseKeyboardInput.keyA = false
          case KeyCode.S => MouseKeyboardInput.keyS = false
          case KeyCode.D => MouseKeyboardInput.keyD = false
          case KeyCode.Space => MouseKeyboardInput.keySpaceBar = false
          case _ =>
        }
      }
      // Set the CSS stylesheet of the GUI components
      stylesheets += getClass.getResource("Stylesheet.css").toString
      root = roots // Set the root to equal the RootLayout FXML display
    }
  }

  // This method is used to display the Start Game display in the game
  def displayStartGame(): Unit = {
    // Create a new instance of the GameEngine class
    val gameEngine = new GameEngine()
    var startTimer = 0L // Set timer to zero

    roots.center = {
      new AnchorPane() {
        // Insert the player entity
        children.add(gameEngine.player.entities)
        // Inset the wave text interface
        children.add(gameEngine.waveBoard)
        // Inset the player's health text interface
        children.add(gameEngine.healthBoard)

        // Use AnimationTimer to get smoother animations and movements
        val timer: AnimationTimer = AnimationTimer(t => {
          // Create a new variable to remove unwanted entities from the game
          val destroyedEntities = gameEngine.bulletCollisionEngine()
          val time = (t - startTimer) / 1e9

          // Call the player movement method
          gameEngine.playerMovementEngine()
          // Call the text interface method
          gameEngine.interfaceEngine(gameEngine.wave, gameEngine.player.health)

          // In case the player is pressing the SpaceBar, add new Bullet entities
          gameEngine.bulletShootingEngine() match {
            case Some(x) =>
              children.add(x.entities)
            case None =>
          }

          // In case the spawn timer is equal to zero, add new Enemy entities
          gameEngine.enemiesSpawnEngine(time) match {
            case Some(x) =>
              children.add(x.entities)
            case None =>
          }

          // For every bullet stored in the List, remove the bullets
          for (i <- gameEngine.bulletEngine()) {
            children.remove(i.entities)
          }

          // For every enemies stored in the List, remove the enemies
          for (i <- gameEngine.enemiesMovementEngine()) {
            children.remove(i.entities)
          }

          // For every enemy entities destroyed, remove from the List
          for (i <- destroyedEntities._1) {
            children.remove(i.entities)
          }

          // For every bullet entities destroyed, remove from the List
          for (i <- destroyedEntities._2) {
            children.remove(i.entities)
          }

          // If the true, stop the timer and change display to the Game Over display
          if (gameEngine.deathEngine()) {
            timer.stop
            displayGameOver()
          }

          // Set the timer to the current time
          startTimer = t
        })
        // start the timer
        timer.start()
      }
    }
  }

  // This method is used to display the How To Play display of the game
  def displayInstruction(): Unit = {
    val resource = getClass.getResource("HowToPlay.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // This method is used to display the Game Over display of the game
  def displayGameOver(): Unit = {
    val resource = getClass.getResource("GameOver.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // This method is used to display the Main Menu display of the game
  def displayMainMenu(): Unit = {
    val resource = getClass.getResource("MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Display the Main Menu
  displayMainMenu()
}

