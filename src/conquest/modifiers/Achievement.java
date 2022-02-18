package conquest.modifiers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import conquest.Conquest;
import conquest.Resources;
import conquest.entityPackage.playerPackage.Player;

public class Achievement {
	private static List<Achievement> allAchievements = new ArrayList<>();
	Predicate<Player> requirement;
	Runnable reward;
	boolean completed;
	BufferedImage image;
	String name, desc;
	AchievementDifficulty difficulty;
	
	public static enum AchievementDifficulty {
		VERY_EASY(Color.GREEN),
		EASY(new Color(0, 100, 0)),
		MEDIUM(Color.YELLOW),
		HARD(Color.ORANGE),
		VERY_HARD(Color.MAGENTA),
		IMPOSSIBLE(Color.RED);
		
		Color color;
		AchievementDifficulty(Color color) {
			this.color = color;
		}
		
		public Color getColor() { return color; }
	}
	
	Achievement(String name, String desc, BufferedImage image, Predicate<Player> requirement, Runnable reward, AchievementDifficulty difficulty) {
		this.name = name;
		this.desc = desc;
		this.image = image;
		this.requirement = requirement;
		this.reward = reward;
		this.difficulty = difficulty;
		completed = false;
	}
	
	public void test() {
		if (completed) return;
		completed = requirement.test(Conquest.player);
		if (completed && reward != null) reward.run();
	}
	
	public static void init() {
		//name, desc, image, predicate, reward, difficulty
		Achievement openGame = new Achievement("Started Playing Conquest",
				"Thanks for joining in on the journey",
				Resources.getImage(Resources.ImageEnum.SHIP),
				player -> Conquest.game.started,
				null,
				AchievementDifficulty.EASY);
		
		Achievement kill10Enemies = new Achievement("Killed 10 Enemies",
				"Starting to get the hang of things",
				Resources.getImage(Resources.ImageEnum.DAMAGED_SHIP),
				player -> Conquest.player.enemiesDestroyed > 10,
				null,
				AchievementDifficulty.MEDIUM);
		
		allAchievements.add(openGame);
		allAchievements.add(kill10Enemies);
	}
	
	public static void testAll() {
		allAchievements.stream().forEach(Achievement::test);
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public AchievementDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(AchievementDifficulty difficulty) {
		this.difficulty = difficulty;
	}
	
	public static List<Achievement> getAllAchievements() {
		return allAchievements;
	}
	
	
	
}
