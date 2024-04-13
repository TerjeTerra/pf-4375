'''
PYTHON SNAKE GAME
This file is controlling game set-up and play, including
controlling snake(s) based on keys pressed.
It also keeps track of time, sets fixed variables and has methods 
for handling the food.
'''

# import required modules
import turtle
import time
import random
import math
import food as fd
import world
import game
import menu

START_DELAY = 0.15 
delay = START_DELAY

# TIME CONTROL
# ************
startTime = time.time()
seconds = 0
pausedTime = 0.0
paused = True
quit = False

# INITIAL VALUES
# **************

# General standard unit (snake head and segments are 1 x 1 UNIT)
UNIT = 20

# Window size
WIDTH_M = 800 # medium
HEIGHT_M = 600
WIDTH_S = int(WIDTH_M/2) # small
HEIGHT_S = int(HEIGHT_M/2)
WIDTH_L = int(WIDTH_M * 1.3) # large
HEIGHT_L = int(HEIGHT_M * 1.3)


# Set-up from menu in terminal
# ****************************

# size of world
width = 0
height = 0
choice = menu.menuLoop(menu.startMenu, 3)
if choice == 1:
    width = WIDTH_S
    height = HEIGHT_S
elif choice == 2:
    width = WIDTH_M
    height = HEIGHT_M
elif choice == 3:
    width = WIDTH_L
    height = HEIGHT_L

# players
players = menu.menuLoop(menu.playerOptions, 2)

# CREATE WORLD AND GAME
# *********************
# Creating a window screen with frame
wn = turtle.Screen() # singleton instance of a Screen object
wor = world.World(wn, width, height) # initielle verdier (som ikke bestemmes av game)
wor.makeFrame() # frame

# Import coordinates of edges
XBORDER = wor.getXBorder()
YBORDER = wor.getYBorder()

# Creating a game (in world)
gm = game.Game(wor, UNIT, players)
snakes = gm.getSnakes() # get and hold the snake(s)

# SNAKES 
# ******
# Assigning key directions

# General heading method
def heading(snake, newDir, illegalDir):
    global paused
    if paused:
        paused = False
    # NOTE: a key push will only start the game, not change heading of snake
    else: 
        if snake.getDirection() != illegalDir: # has to be legal direction
            snake.setDirection(newDir)

# specific methods
def goup(snake):
    heading(snake, "up", "down")

def godown(snake):
    heading(snake, "down", "up")

def goleft(snake):
    heading(snake, "left", "right")
    
def goright(snake):
    heading(snake, "right", "left")

def grow(snake):
    global delay
    snake.addSegment() # Adding segment
    delay -= 0.001 # adjust delay (speed) when snake has grown

# Gameplay help functions
def forSnakes(method_name, *parameters): # * arbitrary number of parameters
    results =[]
    for snake in snakes:
        method = getattr(snake, method_name)
        results.append(method(*parameters)) # like calling snake.method_name(parameters)
    return results

def withinBorder():
    tooClose = int(0.5 * UNIT)
    XLimit = XBORDER-tooClose
    YLimit = YBORDER-tooClose
    return forSnakes('isWithin', XLimit, YLimit)


# FOOD (methods and initialization)
# *********************************
    
# Help methods for getting random coordinates within (min distance from) border
minFromBorder = int(1.5 * UNIT)
def getRndX():    
    x = random.randint(-(XBORDER - minFromBorder), (XBORDER - minFromBorder))
    return x
def getRndY():
    y = random.randint(-(YBORDER - minFromBorder), (YBORDER - minFromBorder))
    return y

# method for moving a single food object
def moveFood(food):
    fortsett = True
    while fortsett:
        # each random (X,Y) results in four different (xi, yi) positions 
        # if program runs slow, consider moving food to nearest position %UNIT    
        X = getRndX()
        Y = getRndY()
        xPair = (math.floor(X/UNIT) * UNIT, math.ceil(X/UNIT) * UNIT) 
        yPair = (math.floor(Y/UNIT) * UNIT, math.ceil(Y/UNIT) * UNIT)

        occupied = False
        for i in xPair:
            for j in yPair:
                res = forSnakes('isOccupied', i, j)
                if True in res:
                    occupied = True
                    break
        
        if not occupied: # ok, move food to the free position (x, y)
            food.goto(X, Y) 
            fortsett = False

# "The kitchen": checks each food individually if it is time to hide/show the food
def foodAppearance(food): 
    global seconds
    if seconds > food.get_timer(): # compares game time with the timer from the object
        if food.xcor() < XBORDER: # checks if the food is on the board
            food.goto(XBORDER + 500, YBORDER + 500)
            food.set_timer(food.get_hidden()) # changes the timer with how long it should be hidden
        else: # if not, move it onto the board
            moveFood(food)
            food.set_timer(food.get_visible()) # changes the timer with how long it should be displayed

# Initialization of food (count, type)
t1 = 3 # count of type 1
t2 = 2 # count of type 2
t3 = 1 # count of type 3
allFood = []
# create food and place out of border (for now)
for i in range(0,t1,1): 
    allFood.append(fd.Food((XBORDER + 500), (YBORDER + 500), 1))
    moveFood(allFood[i])
for i in range(0,t2,1): 
    allFood.append(fd.Food((XBORDER + 500), (YBORDER + 500), 2)) # start outside the board
for i in range(0,t3,1): 
    allFood.append(fd.Food((XBORDER + 500), (YBORDER + 500), 3)) # start outside the board


# PRINTING/DISPLAY FUNCIONS
# *************************
def showMessageFor(msg, sec):
    wor.printMessage(msg)
    wn.update()
    time.sleep(sec)

def printScore():
    if not gm.playerMode == 'two':
            wor.printScore(gm.score)
    else:
        wor.printScore(gm.score, gm.pl2Score)

def printHighScore():
    wor.printHighScore(gm.highScore)
    wn.update()


# SCORING, TIMING AND OTHER CONTROLLING FUNCTIONS
# ***********************************************
def scoring(food, snakeNo): # points from food, snake number (0 or 1)
    gm.updateScore(food.get_points(), snakeNo + 1)
    printScore()

# RESET between each round: 
# reset timing, show messages, reset game (including snakes and scores) and food
def reset():
    pauseGame()
    wn.update() # show any updates (of transformed head, for instance) 
    global delay
    global startTime
    global seconds
    global pausedTime
    delay = START_DELAY
    time.sleep(1)
    startTime = time.time() # resetting time
    seconds = 0
    pausedTime = 0

    # update scores and graphic
    winner = gm.winner()
    if winner:
        showMessageFor(winner, 2)

    if gm.beatenHighScore():
        showMessageFor("New High Score!!", 2)

    gm.reset()

    printHighScore()
    # gm.resetScore()
    printScore()

    # reset and move food
    for food in allFood:
        food.goto(XBORDER + 500, YBORDER + 500)  # Mat flytte seg unna vei (midlertidig)
        food.reset_timer() # resetter timer for hver food
        if food.get_type() == 1: # flytt på all mat av type 1
            moveFood(food)

def quitGame():
    global quit
    quit = True

def pauseGame(): # stopper/"fryser" spillet
    global paused
    paused = True

def stopPause(): # fortsetter spillet
    global paused
    paused = False

# SNAKE CONTROLS (KEYS)
# *********************
 
wn.listen()
# case-sensitive control
wn.onkeypress(lambda: goup(gm.pl1), "w")
wn.onkeypress(lambda: godown(gm.pl1), "s")
wn.onkeypress(lambda: goleft(gm.pl1), "a")
wn.onkeypress(lambda: goright(gm.pl1), "d")

# pause and quit
wn.onkeypress(quitGame, "Q") # Completely stops the game.
wn.onkeypress(pauseGame, "q") # Pauses the game.

# alternative keys
wn.onkeypress(lambda: goup(gm.pl2), "Up")
wn.onkeypress(lambda: godown(gm.pl2), "Down")
wn.onkeypress(lambda: goleft(gm.pl2), "Left")
wn.onkeypress(lambda: goright(gm.pl2), "Right")


# Main Gameplay
# *************
menu.infoGameStarted() # Info in user terminal

while not quit:

    wn.update()
  
    collision = False
    while not paused:
        # Check events 

        # 1) Touching food 
        touched = forSnakes ('isEating', allFood) # returns food-object if snake is eating, else None
        # Handle food
        for i in range(0, len(touched)):
            eaten = touched[i]
            if isinstance(eaten, fd.Food):
                eaten.goto(XBORDER + 500, YBORDER + 500) # move food temporarily away from the frame
                # setting the food's internal timer
                eaten.set_timer(seconds - eaten.get_timer() + eaten.get_hidden()) 
                grow(snakes[i]) # growing (snake at pos i)
                scoring(eaten, i) # scoring (snake at pos i)
        
        # 2) Collision with border?
        if False in withinBorder():
            collision = True

        # 3) Check if colliding with the other snake in 2-player-mode
        if gm.getMode() == 'two':
            whoops1 = gm.pl1.isCrashingInto(gm.pl2.getWhereIs())
            whoops2 = gm.pl2.isCrashingInto(gm.pl1.getWhereIs())
            if whoops1: # player 1 is crashing
                gm.pl1.headTransformed()
                if whoops2: # if crashed into each other
                    gm.pl2.headTransformed()
                collision = True
            if whoops2: # player 2 (only) crashed into player 1
                gm.pl2.headTransformed()
                collision = True

        # 4) Check if colliding with itself? If not, get the new position of head
                # (that is the next position the head will be moving to)
        newPos = forSnakes('newHeadPos')
        if None in newPos: # meaning: head will collide
            collision = True

        # RESET if a collision (or more) has appeared 
        if collision:
            reset()
            break
        
        # MOVE snake(s).
        gm.pl1.move(newPos[0])
        if gm.getMode() == 'two':
            gm.pl2.move(newPos[1])

        # Food management: disappear and reappear based on time
        elapsed = time.time() - pausedTime - startTime
        tempSeconds = int(elapsed) # checking time temporarily
        if tempSeconds > seconds:
            seconds = tempSeconds # counts seconds one by one
           
            # check food Appearence
            for food in allFood:
                foodAppearance(food) # visit the "kitchen" :-)
        
            # Increase speed...
            if seconds % 15 == 0: # ... at fixed intervals
                delay = delay * game.factor(seconds) # increase speed (reduce delay)
        
        time.sleep(delay)
        wn.update()  
    
    pausedTime += delay
    time.sleep(delay)

wn.bye() # Quits the game and closes the window
wn.mainloop()
