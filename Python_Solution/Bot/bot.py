import random
import sys
import time

from . import astar as _a
from . import problem

def heuristic(node):
    return node.state.calculate_man_distance(node.state.my_id, node.state.goal)

class Bot:

    def __init__(self):
        self.game = None

    def setup(self, game):
        self.game = game

    def do_turn(self):
        p = problem.Problem(self.game.field, self.game.my_botid)
        if(p.goal):
            solution = _a.astar_search(p, heuristic).solution()        
            self.game.issue_order(solution[0][1])
        else:
            self.game.issue_order_pass()
