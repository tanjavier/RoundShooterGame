import Util.ScoresTracker
import scalafx.scene.control.Label
import scalafxml.core.macros.sfxml

@sfxml
// This class is used to handle the Game Over FXML display
class GameOverController(var scoreLabel: Label) {

  // Set the scoreLabel variable to the value in the ScoresTracker object
  scoreLabel.setText("Score: " + ScoresTracker.scores)

  // This method is used to handle to retry button in the game
  def handleRetry(): Unit = {
    MainGame.displayStartGame()
  }

  // This method is used to handle to main menu button in the game
  def handleMainMenu(): Unit = {
    MainGame.displayMainMenu()
  }
}