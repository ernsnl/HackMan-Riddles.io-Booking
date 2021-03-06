/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package bot;

import java.util.HashMap;

import field.Field;
import field.FieldManip;
import player.Player;

/**
 * bot.BotState
 *
 * This class stores all settings of the game and the information about the
 * current state of the game. When calling this in BotStarter.doMove, you can trust that this state
 * has been update to current game state (because updates are sent before action request).
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotState {
    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_ROUNDS;

    private int roundNumber;
    private int timebank;
    private String myName;
    private HashMap<String, Player> players;

    private Field field;
    private FieldManip fieldManip;

	BotState() {
        this.field = new Field();
        this.players = new HashMap<>();
    }

    public void setTimebank(int value) {
        this.timebank = value;
    }

    public void setMaxTimebank(int value) {
        this.MAX_TIMEBANK = value;
    }

    public void setTimePerMove(int value) {
        this.TIME_PER_MOVE = value;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setMaxRounds(int value) {
        this.MAX_ROUNDS = value;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getTimebank() {
        return this.timebank;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public Field getField() {
        return this.field;
    }
    
    public void setField(Field field){
    	this.field = field;
    }

    public String getMyName() {
        return this.myName;
    }

    public int getMaxTimebank() {
        return this.MAX_TIMEBANK;
    }

    public int getTimePerMove() {
        return this.TIME_PER_MOVE;
    }

    public int getMaxRound() {
        return this.MAX_ROUNDS;
    }
    
    public void initFieldManip() throws Exception{
    	this.fieldManip = new FieldManip(this.field);
    	this.fieldManip.setOpponentHasWeapon(this.players.get("player"+this.fieldManip.getOpponentId()).hasWeapon());
    	//this attr is related with the state but to check isPointValid(), that attr is needed in FieldManip
    }
    
    public FieldManip getFieldManip() {
		return fieldManip;
	}

	public void setFieldManip(FieldManip fieldManip) {
		this.fieldManip = fieldManip;
	}
}
