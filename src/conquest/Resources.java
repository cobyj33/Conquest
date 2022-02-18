package conquest;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Resources {
	private static Map<ImageEnum, BufferedImage> ImageMap = new EnumMap<>(ImageEnum.class);
	private static Map<FontEnum, Font> FontMap = new EnumMap<>(FontEnum.class);
	private static Map<SoundEnum, Clip> SoundMap = new EnumMap<>(SoundEnum.class);
	private static boolean muted;
	public static final int SPRITESIZE = 32;
	
	public static enum ImageEnum {
		BASICBULLET, BOOMERANG, HEART, SHIP, SPACE, FRAMEBACKGROUND, 
		SPEAKER_MUTE, SPEAKER_ONEBAR, SPEAKER_TWOBAR, SPEAKER_THREEBAR, DAMAGED_SHIP,
		GOLDEN_SHIP, SWORD, SHIELD;
	}
	
	public static enum FontEnum {
		STANDARDFONT;
	}
	
	public static enum SoundEnum {
		BOSS_INTRO, BUTTON_CLICK, GUN_SHOT, HELICOPTER, ITEM_BOUGHT, 
		LASER, MAIN_MENU_MUSIC, POWERUP_DROPPED, HOVER, POWERUP_PICKUP, 
		SHIP_DESTROYED, STRONG_BULLET;
	}
	
	public static final int BOSS_INTRO = 0,
			BUTTON_CLICK = 1,
			GUN_SHOT = 2,
			HELICOPTER = 3,
			ITEM_BOUGHT = 4,
			LASER = 5,
			MAIN_MENU_MUSIC = 6,
			POWERUP_DROPPED = 7,
			HOVER = 8,
			POWERUP_PICKUP = 9,
			SHIP_DESTROYED =  10,
			STRONG_BULLET = 11;
	
	public static void init() {
		try {
			BufferedImage sheet = ImageIO.read(new File("Resources/Images/Conquest SpriteSheet.png"));
			
			ImageMap.put(ImageEnum.BASICBULLET, sheet.getSubimage(0, 0, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.BOOMERANG, sheet.getSubimage(32, 0, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.HEART, sheet.getSubimage(64, 0, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.SHIELD, sheet.getSubimage(96, 0, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.SWORD, sheet.getSubimage(128, 0, SPRITESIZE, SPRITESIZE));
			
			
			ImageMap.put(ImageEnum.SHIP, sheet.getSubimage(0, 32, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.DAMAGED_SHIP, sheet.getSubimage(32, 32, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.GOLDEN_SHIP, sheet.getSubimage(64, 32, SPRITESIZE, SPRITESIZE));
			
			ImageMap.put(ImageEnum.SPACE, ImageIO.read(new File("Resources/Images/Space.png")));
			ImageMap.put(ImageEnum.FRAMEBACKGROUND, ImageIO.read(new File("Resources/Images/frameBackground.jpg")));
			
			//BufferedImage volumeDisplays = ImageIO.read(new File("Resources/Images/volume displays.png"));
			ImageMap.put(ImageEnum.SPEAKER_MUTE, sheet.getSubimage(0, 32 * 7, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.SPEAKER_ONEBAR, sheet.getSubimage(32, 32 * 7, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.SPEAKER_TWOBAR, sheet.getSubimage(64, 32 * 7, SPRITESIZE, SPRITESIZE));
			ImageMap.put(ImageEnum.SPEAKER_THREEBAR, sheet.getSubimage(96, 32 * 7, SPRITESIZE, SPRITESIZE));
		}	catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			FontMap.put(FontEnum.STANDARDFONT, Font.createFont(Font.TRUETYPE_FONT, new File("Resources/Fonts/NovaSquare-Regular.ttf")));
			
			FontMap.values().forEach(font -> g.registerFont(font));
		} catch (IOException | FontFormatException e1) {
			System.out.println("Could not load all font files");
			e1.printStackTrace();
		}
		
		String soundPath = "Resources/Sounds/";
		addSound(soundPath + "Menu Music.wav", SoundEnum.MAIN_MENU_MUSIC);
		addSound(soundPath + "button click.wav", SoundEnum.BUTTON_CLICK);
		addSound(soundPath + "gun sound.wav", SoundEnum.GUN_SHOT);
		addSound(soundPath + "boss intro.wav", SoundEnum.BOSS_INTRO);
		addSound(soundPath + "helicopter.wav", SoundEnum.HELICOPTER);
		addSound(soundPath + "item bought.wav", SoundEnum.ITEM_BOUGHT);
		addSound(soundPath + "Laser.wav", SoundEnum.LASER);
		addSound(soundPath + "powerup dropped.wav", SoundEnum.POWERUP_DROPPED);
		addSound(soundPath + "hover.wav", SoundEnum.HOVER);
		addSound(soundPath + "powerup Pickup.wav", SoundEnum.POWERUP_PICKUP);
		addSound(soundPath + "ship Destroyed.wav", SoundEnum.SHIP_DESTROYED);
		addSound(soundPath + "strong Bullet.wav", SoundEnum.STRONG_BULLET);
	}
	
	private static void addSound(String filePath, SoundEnum id) {
		try {
			Clip temp = AudioSystem.getClip();
			temp.open(AudioSystem.getAudioInputStream(new File("Resources/Sounds/Menu Music.wav")));
			SoundMap.put(id, temp);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getImage(ImageEnum choice) {
		if (ImageMap.containsKey(choice)) {
			return (ImageMap.get(choice));
		} else {
			System.out.println("Invalid choice");
			return new BufferedImage(10, 10, 10);
		}
	}
	
	public static Font getFont(FontEnum choice) {
		if (FontMap.containsKey(choice)) {
			return FontMap.get(choice);
		} else {
			System.out.println("Invalid choice");
			return new Font("Times New Roman", Font.PLAIN, 12);
		}
	}
	
	public static void playSound(SoundEnum choice) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			if (muted) { return; }
			current.setMicrosecondPosition(0);
			current.start();
		} else {
			System.out.println("Invalid choice");
		}
	}
	
	public static void repeatSoundFor(SoundEnum choice, int time) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			if (muted) { return; }
			current.loop((int) (time / current.getMicrosecondLength() * 1000) + 1);
			Main.scheduler.schedule(() -> {
				current.stop();
				current.setMicrosecondPosition(0);
			}, time, TimeUnit.MILLISECONDS);
			
		} else {
			System.out.println("Invalid choice");
		}
	}
	
	public static Clip getSound(int choice) {
		return SoundMap.get(choice);
	}
	
	public static void stopSound(int choice) {
		if (SoundMap.containsKey(choice)) {
			Clip current = SoundMap.get(choice);
			if (current.isRunning())
				current.stop();
		} else {
			System.out.println("Invalid choice");
		}
	}
	
	public static void mute() {
		if (!muted) {
			muted = true;
			SoundMap.values().forEach(clip -> {
				if (clip == null) { return; }
				clip.stop(); 
				});
		} else {
			muted = false;
			System.out.println("restarting");
			playSound(SoundEnum.MAIN_MENU_MUSIC);
		}
	}
	
	public static boolean isMuted() { 
		return muted;
	}
	
	
}
