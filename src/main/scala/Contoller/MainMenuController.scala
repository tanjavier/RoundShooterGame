import scalafxml.core.macros.sfxml

@sfxml
// This class is used to handle the Main Menu FXML display
class MainMenuController {
  // This method is used to handle to game start button in the game
  def handleGameStart(): Unit = {
    MainGame.displayStartGame()
  }

  // This method is used to handle to how to play button in the game
  def handleInstruction(): Unit = {
    MainGame.displayInstruction()
  }

  // This method is used to handle to quit button in the game
  def handleQuit(): Unit = {
    System.exit(0)
  }
}