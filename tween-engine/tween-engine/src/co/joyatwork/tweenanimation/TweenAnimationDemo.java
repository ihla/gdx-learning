package co.joyatwork.tweenanimation;

import java.math.BigDecimal;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Sine;
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

	protected float spriteWidth;
	protected float spriteHeight;

	protected float squeezeToWidth;
	protected float squeezeToHeight;
	private float deltaX;
	private float deltaY;
	protected static final float widthScaleFactor = 1.1f;
	protected static final float heightScaleFactor = 0.7f;

	 /**
     * Round to certain number of decimals
     * 
     * @param d
     * @param decimalPlace
     * @return
     */
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera((viewPortWidth = 1), (viewPortHeight = viewPortWidth * h/w));
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
		spriteWidth = sprite.getWidth();
		spriteHeight = sprite.getHeight();
		Gdx.app.log(TAG, "Y1,Y2 " + sprite.getVertices()[SpriteBatch.Y1] + "," + sprite.getVertices()[SpriteBatch.Y2]);
		
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		//Tween.call(moveAnimation).start(tweenManager);
		
		squeezeToWidth = widthScaleFactor * spriteWidth;
		squeezeToHeight = heightScaleFactor * spriteHeight;
		Gdx.app.log(TAG, "squeezeToWidth,squeezeToHeight " + squeezeToWidth + "," + squeezeToHeight);
		//Tween.call(widthHeightAnimation).start(tweenManager);
		
		
		deltaX = (widthScaleFactor - 1.0f) * (spriteWidth/2);
		deltaY = (1.0f - heightScaleFactor) * (spriteHeight/2);
		deltaX = round(deltaX, 3);
		deltaY = round(deltaY, 3);
		Gdx.app.log(TAG, "deltaX,deltaY " + deltaX + "," + deltaY);
		Tween.call(squeezeAnimation).start(tweenManager);

	}

	private final TweenCallback moveAnimation = new TweenCallback() {
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			Tween.to(sprite, SpriteAccessor.POSITION_XY, 10f)
				.target(sprite.getX(), viewPortHeight/2 - sprite.getHeight())
				//.ease(Bounce.INOUT)
				.ease(Linear.INOUT)
				//.ease(Elastic.INOUT)
				//.ease(Back.INOUT)
				//.ease(Sine.INOUT)
				.delay(2.0f)
				//.repeatYoyo(Tween.INFINITY, 0f)
				//.repeat(Tween.INFINITY, 0f)
				.setCallback(moveAnimation)
				.start(tweenManager);
		}
	};

	private final TweenCallback widthHeightAnimation = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {

			Tween.to(sprite, SpriteAccessor.WIDTH_HEIGHT, 5f)
				.target(squeezeToWidth, squeezeToHeight)
				.ease(Linear.INOUT)
				.delay(2f)
				.setCallback(widthHeightAnimation)
				.start(tweenManager);
			
		}
	};

	private final TweenCallback squeezeAnimation = new TweenCallback() {
		
		@Override
		public void onEvent(int type, BaseTween<?> source) {
			
			Tween.to(sprite, SpriteAccessor.REC_SCALE_XY, 5f)
				.target(deltaX, deltaY)
				.ease(Linear.INOUT)
				.delay(2f)
				.setCallback(squeezeAnimation)
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
		/*
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0f, 1f, 0f, 1f);
		float originCrossX = sprite.getX() + sprite.getOriginX();
		float originCrossY = sprite.getY() + sprite.getOriginY();
		shapeRenderer.line(originCrossX - sprite.getWidth()/2, originCrossY, originCrossX + sprite.getWidth()/2, originCrossY);
		shapeRenderer.line(originCrossX, originCrossY - sprite.getHeight()/2, originCrossX, originCrossY + sprite.getHeight()/2);
		shapeRenderer.end();
		*/
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
