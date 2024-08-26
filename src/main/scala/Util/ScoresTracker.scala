package Util

// This object is used to store the score of the game
object ScoresTracker {
  var score: Int = 0
  // Getter and Setter methods
  def scores: Int = score
  def scores_=(s: Int): Unit = {
    score = s
  }
}