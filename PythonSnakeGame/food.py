# food for snakes...

import random
import turtle

# returns one of four headings
def rnd_heading():
    return 90 * int(random.random() * 4)

def rnd_factor():
    return 0.5 + random.random()

class Food(turtle.Turtle):

    def __init__(self, x, y, type) -> None:
        intervall = 5 # basic time interval
        self.__type = type
        self.__visible = 10000 # time interval of showing food
        self.__hidden = 0 # time interval of hiding food
        self.__timer = 10000

        turtle.Turtle.__init__(self)
        self.penup()
        self.hideturtle()
        if type == 1: # common type, always show
            self.shape('circle')
            self.color('red')
            self.__points = 1
            
        elif type == 2: # more rare
            self.shape('triangle')
            self.color('yellow')
            self.__points = 3
            self.__visible = intervall * 2
            self.__hidden = intervall
            

        elif type == 3: # most rare
            self.shape('turtle')
            self.color('white')
            self.__points = 5
            self.__visible = intervall
            self.__hidden = intervall * 3
            
        self.setheading(rnd_heading()) # random heading
        self.speed(0)
        self.goto(x, y)
        self.showturtle()

        if self.__type != 1:
            self.__timer = int(self.__hidden * rnd_factor()) # first move at random time
            
    def get_type(self):
        return self.__type
    
    def get_visible(self):
        return self.__visible

    def get_hidden(self):
        return self.__hidden    
    
    def get_timer(self):
        return self.__timer

    def set_timer(self, seconds): # also resets heading
        self.__timer += seconds
        self.setheading(rnd_heading())

    def reset_timer(self): # also resets heading
        if self.__type != 1:
            self.__timer = int(self.__hidden * rnd_factor())
            self.setheading(rnd_heading())

    def get_points(self):
        return self.__points
