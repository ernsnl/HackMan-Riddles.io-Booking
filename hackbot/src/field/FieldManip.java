package field;

import java.awt.Point;

import move.MoveType;

public class FieldManip extends Field{

	public FieldManip(Field copy) throws Exception {
		super(copy);
	}
    
    // DONE__TODO: Get Valid Moves fonksiyonu deðiþtirilen matrixe göre adapte olmasý lazým
    // Hani buglarý duvar yaptýðýmýzdaki durum mesela
	@Override
	public boolean isPointValid(Point p){
		int x = p.x;
        int y = p.y;
        
        //checks for walls + bugs + opponent with a weapon
        return x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight() &&
                !(this.getField()[x][y].contains("x") || this.getField()[x][y].contains("E") || 
                (this.getField()[x][y].contains(this.getOpponentId()) && this.isOpponentHasWeapon()));
	}
    
	// TODO: Çok basit anlamlý yapýldý. Kompleks yapý için gözden geçirilecek!
    public FieldManip UpdateField(MoveType move){
    	// TODO: Move type a göre Field güncellenecek. 
    	// Güncellenecek property ler 
    	// My position
    	// Eðer kompleks bir yapý istiyorsan buglarý da güncelleyebilirsin
    	// Ama bunun için opponent poisitons listesini güncellemen gerekir.
    	
    	int myX = this.getMyPosition().x;
        int myY = this.getMyPosition().y;
        this.getField()[myX][myY]=".";
        
    	switch(move){
    		case UP:
    			this.setMyPosition(new Point(myX, ++myY));
    			if(this.getOpponentPosition() == this.getMyPosition())
    				this.getField()[myX][++myY]+=this.getMyId();
    			else this.getField()[myX][++myY]=this.getMyId();
    			break;
    		case DOWN:
    			this.setMyPosition(new Point(myX, --myY));
    			if(this.getOpponentPosition() == this.getMyPosition())
    				this.getField()[myX][++myY]+=this.getMyId();
    			else this.getField()[myX][++myY]=this.getMyId();
    			break;
    		case RIGHT:
    			this.setMyPosition(new Point(++myX, myY));
    			if(this.getOpponentPosition() == this.getMyPosition())
    				this.getField()[myX][++myY]+=this.getMyId();
    			else this.getField()[myX][++myY]=this.getMyId();
    			break;
    		case LEFT:
    			this.setMyPosition(new Point(--myX, myY));
    			if(this.getOpponentPosition() == this.getMyPosition())
    				this.getField()[myX][++myY]+=this.getMyId();
    			else this.getField()[myX][++myY]=this.getMyId();
    			break;
    		case PASS: //no change
    			break;
    		default: //error
    			break;
    	}
    	return this;
    }
    
    
    public void removeWeapon(int index){
    	if(this.getWeaponPositions().size() > 0){
    		this.getWeaponPositions().remove(index);
    	}
    }
    
    public void removeSnippet(int index){
    	if(this.getSnippetPositions().size() > 0){
    		this.getSnippetPositions().remove(index);
    	}
    }
}
