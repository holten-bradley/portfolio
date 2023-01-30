import math, turtle, random
class Lander(turtle.Turtle):
    '''
    Purpose: create a lander that can turn and thrust against gravity
    Instance Variables: posx: position in x coordinate, posy: position in y coordinate
        velx: velocity in the x direction, vely: velocity in the y direction
    Methods: move() is the gravity and bouncing off the side walls
    thrust() is the use of the up key to thrust the lander in the direction it is facing
    left_turn() is how the lander turns left(10degrees)
    right_turn() is how the lander turns right(10degrees)
    '''
    def __init__(self, posx, posy, velx, vely):
        turtle.Turtle.__init__(self)
        self.penup()
        self.setpos(posx, posy)
        self.vx = velx
        self.vy = vely
        self.left(90)
        self.speed(0)
        self.fuel = 50
    def move(self):
        self.vy -= 0.0486
        newx = self.xcor() + self.vx
        newy = self.ycor() + self.vy
        self.setpos(newx, newy)
        if newx < 10:
            newx = 10
            self.vx *= -1
        if newx > 990:
            newx = 990
            self.vx *= -1
    def thrust(self):
        if self.fuel > 0:
            self.fuel -= 1
            self.angle = math.radians(self.heading())
            self.vx += math.cos(self.angle)
            self.vy += math.sin(self.angle)
            print("{} fuel remaining.".format(self.fuel))
        else:
            print("Out of fuel.")
    def left_turn(self):
        if self.fuel > 0:
            self.left(10)
            self.fuel -= 1
            print("{} fuel remaining.".format(self.fuel))
        else:
            print("Out of fuel.")
    def right_turn(self):
        if self.fuel > 0:
            self.right(10)
            self.fuel -= 1
            print("{} fuel remaining.".format(self.fuel))
        else:
            print("Out of fuel.")



class Game:
    '''
    Purpose: to run the game and check for results
    Instance Variables: none but in __init__():set up the turtle world, make a player/lander, and make meteors in the sky
    Methods: gameloop: checks for win, loss, meteor hit, collision, crash landing, etc.
    '''
    def __init__(self):
        turtle.setworldcoordinates(0, 0, 1000, 1000)
        turtle.delay(0)
        self.player = Lander(random.uniform(100, 900), random.uniform(500, 900), random.uniform(-5, 5), random.uniform(-5, 0))
        self.player.turtlesize(2)
        self.list_meteors = [Meteors('Red')]
        self.count = 0
        self.gameloop()
        turtle.onkeypress(self.player.thrust, 'Up')
        turtle.onkeypress(self.player.left_turn, 'Left')
        turtle.onkeypress(self.player.right_turn, 'Right')
        turtle.listen()
        turtle.mainloop()

    def gameloop(self):
        while self.count % 5 == 0 and len(self.list_meteors) < 15:
            self.count += 1
            self.list_meteors.append(Meteors('Red'))
        if self.player.ycor() < 15:
            if self.player.vx < 3 and self.player.vx > -3 and self.player.vy < 3 and self.player.vy > -3:
                turtle.write("Successful landing!")
            else:
                turtle.write("You crashed!")
        else:
            meteor_hit = False
            for ele in self.list_meteors:
                if abs(self.player.xcor() - ele.xcor()) <= 15 and abs(self.player.ycor() - ele.ycor()) <= 15:
                    turtle.write("You hit a meteor!")
                    meteor_hit = True
            if meteor_hit == False:
                self.player.move()
                for ele in self.list_meteors:
                    ele.moving_meteors()
                self.count += 1
                turtle.Screen().ontimer(self.gameloop, 30)


class Meteors(Lander):
    '''
    Purpose: to make my life harder
    Instance Variables: color: color , see lander
    Methods: moving_meteors: moves meteors
    '''
    def __init__(self, color):
        Lander.__init__(self, random.uniform(100, 900), 1000, random.uniform(-5, 5), random.uniform(-5, 0))
        self.color(color)
        self.shape('circle')
        self.moving_meteors()
    def moving_meteors(self):
        self.vy -= 0.0486
        newx = self.xcor() + self.vx
        newy = self.ycor() + self.vy
        self.setpos(newx, newy)
        if newx < 10:
            newx = 10
            self.vx *= -1
        if newx > 990:
            newx = 990
            self.vx *= -1
        if newy < 10:
            newy = 10
            self.vy *= -1






Game()
