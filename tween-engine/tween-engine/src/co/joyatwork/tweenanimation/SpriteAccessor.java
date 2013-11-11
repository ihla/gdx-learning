package co.joyatwork.tweenanimation;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	public static final int SKEW_X2X3 = 1;
	public static final int POSITION_XY = 2;
	public static final int WIDTH_HEIGHT = 3;
	public static final int REC_SCALE_XY = 4;
	private static final String TAG = "SpriteAccessor";

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case SKEW_X2X3: {
				float[] vs = target.getVertices();
				returnValues[0] = vs[SpriteBatch.X2] - target.getX();
				returnValues[1] = vs[SpriteBatch.X3] - target.getX() - target.getWidth();
				return 2;
			}
			case POSITION_XY:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
			case WIDTH_HEIGHT:
				returnValues[0] = target.getWidth();
				returnValues[1] = target.getHeight();
				Gdx.app.log(TAG, "get x,y " + returnValues[0] + "," + returnValues[1]);
				return 2;
			case REC_SCALE_XY: {
				float[] vs = target.getVertices();
				
				// delta x
				returnValues[0] = Math.abs(TweenAnimationDemo.round(
							(vs[SpriteBatch.X4] - vs[SpriteBatch.X1] - target.getWidth())/2, TweenAnimationDemo.DECIMAL_DIGITS));
				// delta y
				returnValues[1] = Math.abs(TweenAnimationDemo.round(
						(vs[SpriteBatch.Y2] - vs[SpriteBatch.Y1] - target.getHeight())/2, TweenAnimationDemo.DECIMAL_DIGITS));
				/*
				// delta x
				returnValues[0] = Math.abs(
							(vs[SpriteBatch.X4] - vs[SpriteBatch.X1] - target.getWidth())/2);
				// delta y
				returnValues[1] = Math.abs(
						(vs[SpriteBatch.Y2] - vs[SpriteBatch.Y1] - target.getHeight())/2);
				*/
				//Gdx.app.log(TAG, "get x,y " + returnValues[0] + "," + returnValues[1]);
				return 2;
			}
				
		}
		
		assert false;
		return -1;
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {				
			case SKEW_X2X3: {
				float x2 = target.getX();
				float x3 = x2 + target.getWidth();
				float[] vs = target.getVertices();
				vs[SpriteBatch.X2] = x2 + newValues[0];
				vs[SpriteBatch.X3] = x3 + newValues[1];
				break;
			}
			case POSITION_XY:
				target.setX(newValues[0]);
				target.setY(newValues[1]);
				break;
			case WIDTH_HEIGHT:
				target.setSize(newValues[0], newValues[1]);
				break;
			case REC_SCALE_XY: {
				//Gdx.app.log(TAG, "set x,y " + newValues[0] + "," + newValues[1]);
				float x1 = target.getX();
				float x4 = x1 + target.getWidth();
				float y1 = target.getY();
				float y2 = y1 + target.getHeight();
				float[] vs = target.getVertices();
				
				vs[SpriteBatch.X1] = TweenAnimationDemo.round(x1 - newValues[0], TweenAnimationDemo.DECIMAL_DIGITS);
				vs[SpriteBatch.Y1] = TweenAnimationDemo.round(y1 + newValues[1], TweenAnimationDemo.DECIMAL_DIGITS);
				
				vs[SpriteBatch.X2] = TweenAnimationDemo.round(x1 - newValues[0], TweenAnimationDemo.DECIMAL_DIGITS);
				vs[SpriteBatch.Y2] = TweenAnimationDemo.round(y2 - newValues[1], TweenAnimationDemo.DECIMAL_DIGITS);
				
				vs[SpriteBatch.X4] = TweenAnimationDemo.round(x4 + newValues[0], TweenAnimationDemo.DECIMAL_DIGITS);
				vs[SpriteBatch.Y4] = TweenAnimationDemo.round(y1 + newValues[1], TweenAnimationDemo.DECIMAL_DIGITS);
				
				vs[SpriteBatch.X3] = TweenAnimationDemo.round(x4 + newValues[0], TweenAnimationDemo.DECIMAL_DIGITS);
				vs[SpriteBatch.Y3] = TweenAnimationDemo.round(y2 - newValues[1], TweenAnimationDemo.DECIMAL_DIGITS);
				/*
				vs[SpriteBatch.X1] = x1 - newValues[0];
				vs[SpriteBatch.Y1] = y1 + newValues[1];
				
				vs[SpriteBatch.X2] = x1 - newValues[0];
				vs[SpriteBatch.Y2] = y2 - newValues[1];
				
				vs[SpriteBatch.X4] = x4 + newValues[0];
				vs[SpriteBatch.Y4] = y1 + newValues[1];
				
				vs[SpriteBatch.X3] = x4 + newValues[0];
				vs[SpriteBatch.Y3] = y2 - newValues[1];
				*/
				break;
			}
		}
	}
}
