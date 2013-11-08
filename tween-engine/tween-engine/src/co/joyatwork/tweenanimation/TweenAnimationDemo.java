package co.joyatwork.tweenanimation;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class TweenAnimationDemo implements ApplicationListener {
	private static final String TAG = "TweenAnimationDemo";
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private TweenManager tweenManager;
	private ShapeRenderer shapeRenderer;
	private float viewPortWidth;
	private float viewPortHeight;

	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera((viewPortWidth = 1), (viewPortHeight = h/w));
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		tweenManager = new TweenManager();
		
		//texture = new Texture(Gdx.files.internal("data/bubble.png"));
		//texture = new Texture(Gdx.files.internal("data/bobargb8888-32x32.png"));
		texture = new Texture(Gdx.files.internal("data/particle.png"));
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture);
		
		sprite = new Sprite(region);
		float sw = 0.1f;
		Gdx.app.log(TAG, "sprite original size w x h " + sprite.getWidth() + " x " + sprite.getHeight());
		sprite.setSize(sw, sw * sprite.getHeight() / sprite.getWidth());
		Gdx.app.log(TAG, "sprite scaled size w x h " + sprite.getWidth() + " x " + sprite.getHeight());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		Gdx.app.log(TAG, "sprite origin x,y " + sprite.getOriginX() + "," + sprite.getOriginY());
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		Gdx.app.log(TAG, "sprite position x,y " + sprite.getX() + "," + sprite.getY());
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.call(windCallback).start(tweenManager);
	}

	private final TweenCallback windCallback = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			Tween.to(sprite, SpriteAccessor.POSITION_XY, 10f).target(sprite.getX(), viewPortHeight/2 - sprite.getHeight())
				.ease(Bounce.OUT)
				.delay(2.0f)
				//.repeatYoyo(1, 1f)
				//.repeat(-1, 0.001f)
				.setCallback(windCallback)
				.start(tweenManager);
		}
	};

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
		texture.dispose();
	}

	@Override
	public void render() {		
		tweenManager.update(Gdx.graphics.getDeltaTime());

		//Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		// x axis
		shapeRenderer.setColor(1f, 0f, 0f, 1f);
		shapeRenderer.line(-viewPortWidth/2, 0f, viewPortWidth/2, 0f);
		// y axis
		shapeRenderer.setColor(0f, 0f, 1f, 1f);
		shapeRenderer.line(0f, -viewPortHeight/2, 0f, viewPortHeight/2);
		// bounding box
		shapeRenderer.setColor(0f, 1f, 0f, 1f);
		shapeRenderer.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
		shapeRenderer.end();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();

		// origin cross
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0f, 1f, 0f, 1f);
		float originCrossX = sprite.getX() + sprite.getOriginX();
		float originCrossY = sprite.getY() + sprite.getOriginY();
		shapeRenderer.line(originCrossX - sprite.getWidth()/2, originCrossY, originCrossX + sprite.getWidth()/2, originCrossY);
		shapeRenderer.line(originCrossX, originCrossY - sprite.getHeight()/2, originCrossX, originCrossY + sprite.getHeight()/2);
		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
