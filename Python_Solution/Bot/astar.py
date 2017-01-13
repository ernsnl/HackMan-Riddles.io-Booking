# Got it from https://github.com/aimacode/aima-python/blob/master/

from .utils import *
from .node import *

def best_first_graph_search(problem, f):
    """Search the nodes with the lowest f scores first.
    You specify the function f(node) that you want to minimize; for example,
    if f is a heuristic estimate to the goal, then we have greedy best
    first search; if f is node.depth then we have breadth-first search.
    There is a subtlety: the line "f = memoize(f, 'f')" means that the f
    values will be cached on the nodes as they are computed. So after doing
    a best first search you can examine the f values of the path returned."""
    f = memoize(f, 'f')
    node = Node(problem.initial)
    if problem.goal_test(node.state):
        return node
    frontier = PriorityQueue(min, f)
    frontier.append(node)
    explored = []
    while frontier:
        node = frontier.pop()
        if problem.goal_test(node.state):
            return node
        explored.append(node.state)
        for child in node.expand(problem):
            if not check_in_explored(explored, child) and not check_in(frontier, child):
                frontier.append(child)
            elif child in frontier:
                incumbent = frontier[child]
                if f(child) < f(incumbent):
                    del frontier[incumbent]
                    frontier.append(child)

    return None

def check_in_explored(list, node_object):
    for nobj in list:
        if (nobj.board_players[nobj.my_id].row == node_object.state.board_players[node_object.state.my_id].row and
            nobj.board_players[nobj.my_id].col == node_object.state.board_players[node_object.state.my_id].col):
                return True
    return False

def check_in(list, node_object):
    if(len(list) == 0):
        return False
    for (f, nobj) in list.A:
        if (nobj.state.board_players[nobj.state.my_id].row == node_object.state.board_players[node_object.state.my_id].row and
            nobj.state.board_players[nobj.state.my_id].col == node_object.state.board_players[node_object.state.my_id].col):
                return True
    return False

# ______________________________________________________________________________
# Informed (Heuristic) Search


def astar_search(problem, h=None):
    """A* search is best-first graph search with f(n) = g(n)+h(n).
    You need to specify the h function when you call astar_search, or
    else in your Problem subclass."""
    h = memoize(h or problem.h, 'h')
    return best_first_graph_search(problem, lambda n: n.path_cost + h(n))

# ______________________________________________________________________________
