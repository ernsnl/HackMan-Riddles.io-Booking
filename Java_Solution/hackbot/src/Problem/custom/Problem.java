package custom;

import java.awt.Point;
import java.util.ArrayList;

import bot.BotState;
import field.*;
import move.MoveType;

public class Problem {

	public BotState initial;
	private Point goal;
	
	public Problem(BotState initial){
		this.initial = initial;
		this.setGoal(initial);
	}
	
	public Boolean setGoal(BotState state){
		this.goal = calculateClosest(state);
		return goal != null;
	}
	
	private Point calculateClosest(BotState state){
		FieldManip currentField = state.getFieldManip();
		int i = 0, j = 0;
		Point goalPoint = new Point();
		ArrayList<Point> weaponPositions = currentField.getWeaponPositions();
		if(weaponPositions.size() > 0){
			goalPoint = weaponPositions.get(0);
			int currentMan =  calculateManDistance(currentField.getMyPosition(), goalPoint);
			for(Point x : weaponPositions){
				int tempMan = calculateManDistance(currentField.getMyPosition(), x);
				if(currentMan > tempMan ){
					goalPoint = x;
					currentMan = tempMan;
					j = i;
				}
				i++;
			}
			int enemyMan = calculateManDistance(currentField.getOpponentPosition(), goalPoint);
			if(enemyMan <= currentMan){
				currentField.removeWeapon(j);
				state.setFieldManip(currentField);
				goalPoint = calculateClosest(state);
			}
			return goalPoint;
		}
		ArrayList<Point> snippetPositions = currentField.getSnippetPositions();
		i = 0; j = 0;
		if(snippetPositions.size() > 0){
			goalPoint = snippetPositions.get(0);
			int currentMan =  calculateManDistance(currentField.getMyPosition(), goalPoint);
			for(Point x : snippetPositions){
				int tempMan = calculateManDistance(currentField.getMyPosition(), x);
				if(currentMan > tempMan ){
					goalPoint = x;
					currentMan = tempMan;
					j = i;
				}
				i++;
			}
			int enemyMan = calculateManDistance(currentField.getOpponentPosition(), goalPoint);
			if(enemyMan <= currentMan){
				currentField.removeSnippet(j);
				state.setFieldManip(currentField);
				goalPoint = calculateClosest(state);
			}	
			return goalPoint;
		}
		return null;
	}
	
	public ArrayList<MoveType> getActions(Node state){
		Field currentField = state.getCurrentState().getFieldManip();
		return currentField.getValidMoveTypes();
	}
	
	public BotState getResult(Node state, MoveType move){
		Node result = new Node(state.getCurrentState());
		result.getCurrentState().setFieldManip(result.getCurrentState().getFieldManip().UpdateField(move));
		return result.getCurrentState();
	}
	
	public Boolean isGoal(Node state){
		Point myPos = state.getCurrentState().getField().getMyPosition();
		return myPos.x == this.goal.x && this.goal.y == myPos.y;
	}
	
	public int getPathCost(int c){
		return c+1;
	}
	
	public int getHeuristic(Node n){
		return n.pathCost + this.calculateManDistance(n.getCurrentState().getField().getMyPosition(), this.goal);
	}
	
	public int calculateManDistance(Point playerPos, Point goal){
		 return Math.abs( playerPos.x - goal.x)  + Math.abs(playerPos.y - goal.y);
	}
}
