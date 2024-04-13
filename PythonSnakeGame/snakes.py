# Careful... Snakes!

import turtle
import coordinateManager as cm

class Snakes(turtle.Turtle):
    length = 0 # length of the TAIL (head not included)
    START_LENGTH = 4
    segments = []# tail segments
    isHere = None # keeps track of coordinates of snake
    tailColor1 = "orange"
    tailColor2 = "brown"
    headColor = "white"
    direction = "right" # fixed direction at start
    unit = None
    crashed = False
    x = 0
    y = 0

    def __init__(self, x, y, unit, *colors) -> None:
        # self.__type = type # Player or computer [idea for future development]
        
        # store start position
        self.x = x
        self.y = y
        self.length = 0
        self.segments = []
        self.crashed = False
        self.isHere = cm.CoordinateManager()

        # set colors (optional)
        if len(colors) == 3:
            self.tailColor1 = colors[0]
            self.tailColor2 = colors[1]
            self.headColor = colors[2]
        
        # make turtle-object
        turtle.Turtle.__init__(self) 
        self.unit = unit
        self.penup()
        self.hideturtle()
        self.shape("square")
        self.color(self.headColor)
        self.speed(0)
        self.showturtle()
        
        # create snake's tail segments left of the head
        self.placeSnake(x, y) # place at start position
                   

    def addSegment(self):
        new_segment = turtle.Turtle()
        new_segment.speed(0)
        new_segment.shape("square")
        if self.length % 2 == 0: # every other segment has different color
            new_segment.color(self.tailColor1)  
        else:
            new_segment.color(self.tailColor2)
        new_segment.penup()
        self.segments.append(new_segment)
        self.length += 1
    
    def placeSnake(self, x, y): # place the snake with tail at start position
        self.goto(x * self.unit, y * self.unit)
        self.isHere.add(self.xcor(), self.ycor())
        for i in range(self.START_LENGTH):
            self.addSegment()
            new = self.segments[i]
            new.goto(self.unit * (x - i - 1), self.unit * y)
            self.isHere.add(new.xcor(), new.ycor()) # adding position at end of isHere

    def reset(self):
        for segment in self.segments:
           segment.goto(5000, 5000) # place outside of frame/window
        self.segments.clear()
        self.crashed = False
        self.isHere.clear()
        self.length = 0
        self.setheading(0)
        self.shearfactor(0)
        self.setDirection("right")
        self.placeSnake(self.x, self.y)        

    def headTransformed(self): # use when collided
        self.setheading(5) # awkward orientation of head :-)
        self.shearfactor(-0.2) # more awkward layout
        self.crashed = True
    
    def newHeadPos(self):
        # calculate new pos
        if self.direction == "up":
            newX = self.xcor()
            newY = self.ycor() + self.unit    
        if self.direction == "down":
            newX = self.xcor()
            newY = self.ycor() - self.unit  
        if self.direction == "left":
            newX = self.xcor() - self.unit
            newY = self.ycor()
        if self.direction == "right":
            newX = self.xcor() + self.unit
            newY = self.ycor()

        # check if occupied by itself
        if self.isHere.contains(newX, newY):
            return None
        return (newX, newY)

    def move(self, coord): # includes update of isHere-coordinates
    
        # 1) move tail

        # a) help method for moving single segment
        def moveSegm(i): # move single segment
            x = self.segments[i - 1].xcor()
            y = self.segments[i - 1].ycor()
            self.segments[i].goto(x, y)
        
        # b) move last segment and keep track of where the snake is (has it grown?)
        last = self.isHere.remove_last() # get and remove previous known pos of end of tail 
        lastSegm = self.segments[-1]
        moveSegm(self.length-1)
        if last == (lastSegm.xcor(), lastSegm.ycor()): # snake has grown since last time
            self.isHere.add(last[0], last[1]) # adding (back again) position of prev. last element, 
                                    # which still is position of last element, since snake has grown

        # c) move the rest of the tail segments
        for index in range(self.length - 2, 0, -1): # iterate from second last segment
            moveSegm(index)
            
        if len(self.segments) > 0: # first segm. gets head's coordinates
            x = self.xcor()
            y = self.ycor()
            self.segments[0].goto(x,y)
        
        # 3) move head of snake
        self.goto(coord)
        self.isHere.add_first(coord[0], coord[1]) # update beginning of isHere


    # Methods for CHECKING

    # check if snake within given limits
    def isWithin(self, xLim, yLim): 
        if (self.xcor() > xLim or self.xcor() < -xLim
            or self.ycor() > yLim or self.ycor() < -yLim):
            self.headTransformed() # method used for checking border collision...
            # ...resulting in transformed head of itself
            return False # is NOT within limits
        return True
    
    # is the snake eating?
    def isEating(self, allFood): 
        for food in allFood:
            if self.distance(food) < self.unit:
                return food # if eating
        return None # if not eating
    
    # is the snake crashing with another snake?
    def isCrashingInto(self, other):
        return other.contains(self.xcor(), self.ycor())

    # is coordinates occupied by this snake?
    def isOccupied(self, xCor, yCor):
        return self.isHere.contains(xCor, yCor)

    # GET and SET methods
    def getDirection(self):
        return self.direction
    
    def setDirection(self, direction):
        self.direction = direction

    def getHeadPos(self):
        return (self.xcor(), self.ycor())
    
    def getCrashed(self):
        return self.crashed
    
    def getWhereIs(self):
        return self.isHere

   