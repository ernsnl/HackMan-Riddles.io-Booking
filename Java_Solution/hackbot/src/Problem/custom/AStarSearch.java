package custom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarSearch  {


	public static Node AStarSearch(Problem p){
		Node node = new Node(p.initial);
		if(p.isGoal(node)){
			return node;
		}
		ArrayList<Node> frontier = new ArrayList<Node>();
		LinkedList<Node> explored = new LinkedList<Node>();
		frontier.add(node);
		while(frontier.size()> 0){
			node = frontier.remove(0);
			if(p.isGoal(node)){
				return node;
			}
			explored.add(node);
			for(Node child : node.expandNode(p)){
				if(!explored.contains(child) && !frontier.contains(child)){
					frontier.add(child);
				}
				else if(frontier.contains(child)){
					Node incumbent = frontier.get(frontier.indexOf(child));
					if(p.getHeuristic(child) < p.getHeuristic(incumbent)){
						frontier.remove(frontier.get(frontier.indexOf(child)));
						frontier.add(child);
					}
				}
			}
		}
		return node;
	}
}