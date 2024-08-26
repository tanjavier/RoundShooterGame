import scalafxml.core.macros.sfxml

@sfxml
// This class is used to handle the How To Play FXML display
class HowToPlayController {
  // This method is used to handle to close button in the game
  def handleClose(): Unit = {
    MainGame.displayMainMenu()
  }
}