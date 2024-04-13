# making a world

import turtle

class World:
    width = 0
    height = 0
    xBorder = 0
    yBorder = 0
    areas = dict() # empty dictionary

    class Area: # subclass
        pen = None
        align = "center"
        color = "white"
        font = ("candara", 22, "bold") # fixed font (as for now)
        xpos = 0
        ypos = 0
        text = ""

        def __init__(self, text, x, y, align = 'center'):
            # default setup/text configuration
            self.text = text # fixed text in area
            self.pen = turtle.Turtle()
            self.pen.speed(0)
            self.pen.shape("square")
            self.pen.color(self.color)
            self.xpos = x
            self.ypos = y
            self.align = align
            self.pen.penup()
            self.pen.hideturtle()

        def update(self, value, clearArea = False):
            self.pen.clear()
            self.pen.goto(self.xpos, self.ypos)
            if clearArea: # only display value (or msg), not the preset fixed text
                self.pen.write(value, align=self.align, font=self.font)
            else:
                self.pen.write(f"{self.text}{value}", align=self.align, font=self.font)
        
        def setColor(self, color):
            self.pen.color(color)

    def __init__(self, win, width, height) -> None:
        adj = 10 # small adjustment of text positioning
        self.width = width
        self.height = height
        self.xBorder = int(width/2)
        self.yBorder = int(height/2)

        win.title("Snake Game")
        win.bgcolor("blue")
        win.setup(width = self.width + 120, height = self.height + 120)
        win.tracer(0)

        # Make areas for writing text in world:
        self.areas["left"] = self.Area("PL 1: ", -adj - self.xBorder, self.yBorder + adj, "left")
        self.areas["center"] = self.Area("High Score: ", 0, self.yBorder + adj, "center")
        self.areas["right"] = self.Area("PL 2: ", self.xBorder, self.yBorder + adj, "right")
        self.areas["message"] = self.areas["center"] # area for messages during game

    # draw the Frame
    def makeFrame(self):
        frame = turtle.Turtle()
        frame.pen(pencolor="white", pensize=5)
        frame.penup()
        frame.goto(-(self.xBorder+8), -(self.yBorder+8))
        frame.pendown()
        for _ in range(2):
            frame.forward(self.width+16)
            frame.left(90)
            frame.forward(self.height+16)
            frame.left(90)
        frame.hideturtle()

    def printScore(self, score, *pl2Score): 
        self.areas["left"].update(score) 
        if len(pl2Score) > 0 and not pl2Score[0] == None:
            self.areas["right"].update(pl2Score[0])
            
    def printHighScore(self, value):
        self.areas["center"].setColor('yellow')
        self.areas["center"].update(value)
    
    def printMessage(self, msg):
        area = self.areas["message"]
        area.update(msg, True) # update area to show only message (clear all other text)

    def getXBorder(self):
        return self.xBorder

    def getYBorder(self):
        return self.yBorder