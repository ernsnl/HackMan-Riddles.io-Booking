

import java.util.ArrayList;
import bot.BotState;
import move.Move;

public class Node{
	
	private BotState currentState;
	private int pathCost;
	private Node parent;
	private Move action;
	
	private Node(BotState state){
		this.currentState = state;
		this.pathCost = 0;
		this.parent = null;
		this.action = null;
	}
	
	private Node(BotState state, Node parent, Move action,int pathCost){
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
	 
	
	private ArrayList<BotState> expandNode(){
		ArrayList<BotState> result = new ArrayList<BotState>();
		// TO DO: Expand the node according to available action
		
		return result;
	}

	
	private Move getFinalAction(){
		Node currentNode = this;
		Move move = new Move();
		while (currentNode.getParent() != null){
			move = currentNode.action;
			currentNode = currentNode.getParent();
		}
		return move;
	}
	
}
