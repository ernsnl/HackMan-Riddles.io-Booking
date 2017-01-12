package custom;

import java.util.ArrayList;
import bot.BotState;
import move.MoveType;

public class Node{
	
	private BotState currentState;
	public int pathCost;
	public Node parent;
	public MoveType action;
	
	public Node(BotState state){
		this.currentState = state;
		this.pathCost = 0;
		this.parent = null;
		this.action = null;
	}
	
	public Node(BotState state, Node parent, MoveType action,int pathCost){
		this.currentState = state;
		this.pathCost = pathCost;
		this.parent = parent;
		this.action = action;
	}
	
	public Node getParent(){
		return this.parent;
	}
	
	 public BotState getCurrentState(){
		 return this.currentState;
	 }
	 	
	public ArrayList<Node> expandNode(Problem problem){
		ArrayList<Node> result = new ArrayList<Node>();
		ArrayList<MoveType> moves = problem.getActions(this);
		for (MoveType m : moves){
			Node temp = new Node(problem.getResult(this, m), this, m, problem.getPathCost(this.pathCost));
			result.add(temp);
		}
		return result;
	}

	public MoveType getFinalAction(){
		Node currentNode = this;
		MoveType move = MoveType.PASS;
		while (currentNode.getParent() != null){
			move = currentNode.action;
			currentNode = currentNode.getParent();
		}
		return move;
	}
	
}
