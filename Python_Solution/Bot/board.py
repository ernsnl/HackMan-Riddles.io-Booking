import copy
import sys
import math
import time

from . import player
from . import point

PLAYER1, PLAYER2, EMPTY, BLOCKED, BUG, WEAPON, CODE = [0, 1, 2, 3, 4, 5, 6]

S_PLAYER1, S_PLAYER2, S_EMPTY, S_BLOCKED, S_BUG, S_WEAPON, S_CODE = ['0', '1', '.', 'x', 'e', 'w', 'c']

CHARTABLE = [(PLAYER1, S_PLAYER1), (PLAYER2, S_PLAYER2), (EMPTY, S_EMPTY), (BLOCKED, S_BLOCKED), (BUG, S_BUG), (WEAPON, S_WEAPON), (CODE, S_CODE)]

DIRS = [
    ((-1, 0), "up"),
    ((0, 1), "right"),
    ((1, 0), "down"),
    ((0, -1), "left")
]

class Board:

    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.my_id = 0
        self.cell = [[[EMPTY] for col in range (0, width)] for row in range(0, height)]
        self.board_players = [player.Player(), player.Player()]
        self.snippets = []
        self.weapons = []
        self.bugs = []
        self.goal = None

    def parse_cell_char(self, players, row, col, char):
        result = -1
        if char == S_PLAYER1:
            players[0].row = row
            players[0].col = col
            self.board_players[0].row = row
            self.board_players[0].col = col
        elif char == S_PLAYER2:
            players[1].row = row
            players[1].col = col
            self.board_players[1].row = row
            self.board_players[1].col = col
        elif char == S_BUG.lower():
            bug = point.Point(row,col)
            self.bugs.append(bug)
        elif char == S_CODE.lower():
            code = point.Point(row,col)
            self.snippets.append(code)
        elif char == S_WEAPON.lower():
            code = point.Point(row,col)
            self.weapons.append(code)
        for (i, symbol) in CHARTABLE:
            if symbol == char:
                result = char
                break
        return result

    def parse_cell(self, players, row, col, data):
        cell = []
        for char in data:
            cell.append(self.parse_cell_char(players, row, col, char))
        return cell

    def parse(self, players, data):
        self.snippets = []
        self.weapons = []
        self.bugs = []
        cells = data.split(',')
        #sys.stderr.write("num cells in field = " + str(len(cells)) + "\n")
        #sys.stderr.write("width = " + str(self.width) + "\n")
        #sys.stderr.write("height = " + str(self.height) + "\n")
        col = 0
        row = 0
        for cell in cells:
            if (col >= self.width):
                col = 0
                row +=1
            self.cell[row][col] = self.parse_cell(players, row, col, cell)
            col += 1

    def in_bounds (self, row, col):
        return row >= 0 and col >= 0 and col < self.width and row < self.height

    def legal_moves(self, my_id):
        my_player = self.board_players[my_id]
        other_player = self.board_players[1-my_id]
        #print(str(my_player.row) + " " + str(my_player.col))
        #sys.stderr.write("my player loc = " + str(my_player.row) + ", " + str(my_player.col) + "\n")
        result = []
        for ((o_row, o_col), order) in DIRS:
            t_row = my_player.row + o_row
            t_col = my_player.col + o_col
            #sys.stderr.write("Testing " + str(t_row) + ", " + str(t_col) + "\n")
            if(self.in_bounds(t_row, t_col)):
                #sys.stderr.write("legal\n")
                if (not str(S_BLOCKED) in self.cell[t_row][t_col]):
                    result.append(((o_row, o_col), order))

                ## TO DO: More sophisticated behaivor can be implemented
                ## For example blocking movement if there is a bug in the area or enemy
            else:
                pass
                #sys.stderr.write("illegal\n")
        return result

    def find_closest(self, my_id):
        goal_point = None
        goal_man_dis = 0
        if(len(self.weapons) > 0):
            goal_point = self.weapons[0]
            goal_man_dis = self.calculate_man_distance(my_id, goal_point)
            for wep in self.weapons:
                temp_dis = self.calculate_man_distance(my_id, wep)
                if(temp_dis < goal_man_dis):
                    goal_point = wep
                    goal_man_dis = temp_dis

            enemy_dis = self.calculate_man_distance(1 - my_id, goal_point)
            if(enemy_dis < goal_man_dis):
                self.weapons.remove(goal_point)
                goal_point = self.find_closest(my_id)
            self.goal = goal_point
            return goal_point

        if(len(self.snippets) > 0):
            goal_point = self.snippets[0]
            goal_man_dis = self.calculate_man_distance(my_id, goal_point)
            for snip in self.snippets:
                temp_dis = self.calculate_man_distance(my_id, snip)
                if(temp_dis < goal_man_dis):
                    goal_point = snip
                    goal_man_dis = temp_dis

            """enemy_dis = self.calculate_man_distance(1 - my_id, goal_point)
            if(enemy_dis < goal_man_dis):
                self.snippets.remove(goal_point)
                goal_point = self.find_closest(my_id)"""
            self.goal = goal_point
            return goal_point

        self.goal = goal_point
        return goal_point

    def calculate_man_distance(self, id, any_point):
        return math.fabs(self.board_players[id].row - any_point.row) + math.fabs(self.board_players[id].col - any_point.col)

    def update_state(self, my_id, action):
        #TO DO: Can give some randomness to the bugs and enemy player
        #But it makes the search undeterministic
        #We can flip a coin to represents bugs if there are any.
        #We can try to predict the movement of the enemy
        copy_state = copy.deepcopy(self)
        my_player = copy_state.board_players[my_id]
        info = copy_state.cell[my_player.row][my_player.col]
        copy_state.cell[my_player.row][my_player.col] = S_EMPTY
        my_player.row += action[0][0]
        my_player.col += action[0][1]
        copy_state.board_players[my_id] = my_player
        copy_state.cell[my_player.row][my_player.col] = info


        return copy_state

    def is_goal(self, my_id, goal):
        my_player = self.board_players[my_id]
        return my_player.row == goal.row and my_player.col == goal.col

    def output_cell(self, cell):
        done = False
        for (i, symbol) in CHARTABLE:
            if i in cell:
                if not done:
                    sys.stderr.write(symbol)
                done = True
                break
        if not done:
            sys.stderr.write("!")
            done = True


    def output(self):
        for row in self.cell:
            sys.stderr.write("\n")
            for cell in row:
                self.output_cell(cell)
        sys.stderr.write("\n")
        sys.stderr.flush()
