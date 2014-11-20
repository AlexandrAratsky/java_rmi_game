package game.enigne;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = 797114087962339063L;
	private static int playerCount = 0;
	private int id;
	private String name;
	private int score = 0;

	public Player(String name) {
		playerCount++;
		if (playerCount>GameBoard.Options.MAX_PLAYERS_NUM) assert(true);
		id = playerCount;
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void addScore(int points) {
		score+=points;
	}
	public void clearScore() {
		score = 0;
	}
	public int getID() {
		return id;
	}
	public String getName() {
		return name;
	}
}
