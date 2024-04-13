# making a game
import snakes

# factor for controlling game speed (lower delay = higher speed)
def factor(sec):
    progress = 1 + int(sec/90)
    f = 0.75 + progress * 0.05
    if f < 1: 
        return f
    return 1 # max speed (lowest possible delay in game)

class Game:
    wld = None # world
    score = 0 # player 1 score
    pl2Score = None # default set to None
    highScore = 0
    pl1 = None # player 1
    pl2 = None # player 2
    diffColor1 = "green" # different color
    diffColor2 = "pink" # another color
    diffHeadColor = "lightgrey" # player 2 head color
    snakes = [] # list for holding snakes
    playerMode = 'one'
    # player 1 starts here
    start1_X_units = 0
    start1_Y_units = 5
    # player 2 starts here
    start2_X_units = 0
    start2_Y_units = -5

    def __init__(self, world, unit, players) -> None:
        self.wld = world
        if not players == 2:
            self.pl1 = snakes.Snakes(0, 0, unit) # only one player: start at (0, 0)
            self.pl2 = self.pl1 # default, in order to use alternative keys for controlling
            self.snakes.append(self.pl1)
        else:
            self.playerMode = 'two' 
            self.pl2Score = 0
            self.pl1 = snakes.Snakes(self.start1_X_units, self.start1_Y_units, unit)
            self.pl2 = snakes.Snakes(self.start2_X_units, self.start2_Y_units, unit, 
                                     self.diffColor1, self.diffColor2, self.diffHeadColor)
            self.snakes.append(self.pl1)
            self.snakes.append(self.pl2)
        world.printScore(self.score, self.pl2Score)
        world.printHighScore(self.highScore)
            
    def updateScore(self, addPoints, playerNumber):
        if playerNumber == 1:
            self.score += addPoints
        elif playerNumber == 2:
            self.pl2Score += addPoints

    def beatenHighScore(self):
        if not self.playerMode == 'two':
            if self.score > self.highScore:
                self.highScore = self.score
                return True
            return False # less than high score in one player mode
        else: # only player that did NOT CRASH might set a new high score in 2-player-mode
            if self.pl1.getCrashed(): # do not use pl1-score if crashed
                if not self.pl2.getCrashed(): 
                    if self.pl2Score > self.highScore:
                        self.highScore = self.pl2Score # pl2 new high score
                        return True
            # pl1 did not crash, meaning pl2 DID crash
            elif self.score > self.highScore: 
                self.highScore = self.score # pl1 new high score
                return True
            # both players crashed or no one beat the high Score
            return False
                
    def resetScore(self):
        self.score = 0
        if self.playerMode == 'two':
            self.pl2Score = 0

    def winner(self):
        if self.playerMode == 'two':
            if self.pl1.getCrashed():
                if self.pl2.getCrashed():
                    return "It's a draw!" # both players crashed, no winner
                else:
                    return "Player 2 won!"
            else: 
                return "Player 1 won!"
        else:
            return False

    # resetting (with no change in game mode)
    def reset(self):
        self.resetScore()
        self.pl1.reset()
        if self.pl2:
            self.pl2.reset()
        
        
    def getSnakes(self):
        return self.snakes # returns snakes
    
    def getMode(self):
        return self.playerMode