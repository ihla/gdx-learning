package co.joyatwork.tweenanimation;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	public static final int SKEW_X2X3 = 1;
	public static final int POSITION_XY = 2;
	public static final int WIDTH_HEIGHT = 3;

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case SKEW_X2X3:
				float[] vs = target.getVertices();
				returnValues[0] = vs[SpriteBatch.X2] - target.getX();
				returnValues[1] = vs[SpriteBatch.X3] - target.getX() - target.getWidth();
				return 2;
			case POSITION_XY:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
			case WIDTH_HEIGHT:
				returnValues[0] = target.getWidth();
				returnValues[1] = target.getHeight();
				return 2;
		}
		
		assert false;
		return -1;
	}

	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {				
			case SKEW_X2X3:
				float x2 = target.getX();
				float x3 = x2 + target.getWidth();
				float[] vs = target.getVertices();
				vs[SpriteBatch.X2] = x2 + newValues[0];
				vs[SpriteBatch.X3] = x3 + newValues[1];
				break;
			case POSITION_XY:
				target.setX(newValues[0]);
				target.setY(newValues[1]);
				break;
			case WIDTH_HEIGHT:
				target.setSize(newValues[0], newValues[1]);
				break;
		}
	}
}
