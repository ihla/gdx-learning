package co.joyatwork.tweenanimation;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "tween-engine";
		cfg.useGL20 = false;
		cfg.width = 1205;
		cfg.height = 800;
		
		new LwjglApplication(new TweenAnimationDemo(), cfg);
	}
}
