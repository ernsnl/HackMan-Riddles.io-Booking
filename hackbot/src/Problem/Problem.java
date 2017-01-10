import java.awt.Point;
import java.util.ArrayList;

import bot.BotState;
import field.Field;
import move.MoveType;

public class Problem {

	private BotState initial;
	private Point goal;
	
	Problem(BotState initial){
		this.initial = initial;
		
	}
	
	private Point calculateClosest(Node state){
		Field currentField = state.getCurrentState().getField();
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
				}
			}
			int enemyMan = calculateManDistance(currentField.getOpponentPosition(), goalPoint);
			if(enemyMan <= currentMan){
				// TO DO : Remove the current selected weapon from the list of weapons
				// TO DO : Make a recursive call
			}
			return goalPoint;
		}
		ArrayList<Point> snippetPositions = state.getCurrentState().getField().getSnippetPositions();
		if(snippetPositions.size() > 0){
			goalPoint = snippetPositions.get(0);
			int currentMan =  calculateManDistance(currentField.getMyPosition(), goalPoint);
			for(Point x : snippetPositions){
				int tempMan = calculateManDistance(currentField.getMyPosition(), x);
				if(currentMan > tempMan ){
					goalPoint = x;
					currentMan = tempMan;
				}
			}
			int enemyMan = calculateManDistance(currentField.getOpponentPosition(), goalPoint);
			if(enemyMan <= currentMan){
				// TO DO : Remove the current selected snippet from the list of snippets
				// TO DO : Make a recursive call
			}	
			return goalPoint;
		}
		return null;
	}
	
	private ArrayList<MoveType> getActions(Node state){
		// To DO : change accordingly to the update
		Field currentField = state.getCurrentState().getField();
		return currentField.getValidMoveTypes();
	}
	
	private Node getResult(Node state, MoveType move){
		Field currentField = state.getCurrentState().getField();
		currentField = currentField.UpdateField(move);
		state.getCurrentState().setField(currentField);
		return state;
	}
	
	private Boolean isGoal(Node state){
		Point myPos = state.getCurrentState().getField().getMyPosition();
		return myPos.x == this.goal.x && this.goal.y == myPos.y;
	}
	
	private int getPathCost(int c){
		return c+1;
	}
	
	private int calculateManDistance(Point playerPos, Point goal){
		 return Math.abs( playerPos.x - goal.x)  + Math.abs(playerPos.y - goal.y);
	}
}
