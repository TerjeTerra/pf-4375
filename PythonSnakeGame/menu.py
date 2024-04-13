# Menu (in terminal)
# [might change to a graphical interface in future version]

startMenu = '''
    ~~~~~~~~~~~~~~~~~~~~:-<
      PYTHON SNAKE GAME  
    ~~~~~~~~~~~~~~~~~~~~:-<
                          
    HOW TO PLAY :
        - Use keys ASDW or arrows to set direction of your snake's head
        - Avoid crashing into walls, yourself or other snakes
        - Eat food, grow and earn points:
                
                RED circle      1 point
                YELLOW triangle 3 points
                WHITE turtle    5 points

        - 'Q' toggles paused game
        - SHIFT + 'Q' will quit the game if paused
          
    MAKE A CHOICE of board size:
        1. small
        2. medium
        3. large
        '''        

playerOptions = '''
    MAKE A CHOICE of numbers of players:
        1. 1 player
        2. 2 players
        '''

def infoGameStarted():
    print("Game has started in a new window.")
    print("Go to window and push a snake keyboard control key to start.")

def get_choice(prompt):
    while True:
        choice = input(prompt)
        try:
            return int(choice)
        except ValueError:
            print("Please enter a valid number.")

def menuLoop(text, nrOfChoices):
    print(text)
    while True:
        choice = get_choice("Enter your choice here: ")
        if 1 <= choice <= nrOfChoices:
            return choice # valid choice, the only way to exit loop
        elif choice == 0:
            print(text) # show menu again
        else:
            print(f"Choose a number from 1 to {nrOfChoices}. ('0' will show menu again)")
