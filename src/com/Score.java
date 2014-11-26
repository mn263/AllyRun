package com;

import com.modelUtils.*;
import org.lwjgl.util.vector.*;

public class Score {

	private ObjUtils m;
	private static final float DIGIT_DIST = 0.6f;

	private int digitIndex;

	private float totalZchange = 0;

	public DigitModel zero, one, two, three, four, five, six, seven, eight, nine;

	private DigitModel activeDigit;

	public Score(int digitIndex) {
		this.digitIndex = digitIndex;
		this.m = ObjUtils.getInstance();
		loadDigit(0);
		activeDigit = zero;
	}

	public void updateValue(int digit) {
		switch (digit) {
			case 0: activeDigit = zero;
				break;
			case 1: if (one == null) loadDigit(digit);
				activeDigit = one; break;
			case 2: if (two == null) loadDigit(digit);
				activeDigit = two; break;
			case 3: if (three == null) loadDigit(digit);
				activeDigit = three; break;
			case 4: if (four == null) loadDigit(digit);
				activeDigit = four; break;
			case 5: if (five == null) loadDigit(digit);
				activeDigit = five; break;
			case 6: if (six == null) loadDigit(digit);
				activeDigit = six; break;
			case 7: if (seven == null) loadDigit(digit);
				activeDigit = seven; break;
			case 8: if (eight == null) loadDigit(digit);
				activeDigit = eight; break;
			case 9: if (nine == null) loadDigit(digit);
				activeDigit = nine; break;
			default: break;
		}
		try {
			activeDigit.render(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDigit(int digit) {
		switch (digit) {
			case 0:
				zero = DigitModel.getModel(m.OBJ_PATH + "score/zero.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				zero.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				zero.render(true);
				break;
			case 1:
				one = DigitModel.getModel(m.OBJ_PATH + "score/one.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				one.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				one.render(true);
				break;
			case 2:
				two = DigitModel.getModel(m.OBJ_PATH + "score/two.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				two.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				two.render(true);
				break;
			case 3:
				three = DigitModel.getModel(m.OBJ_PATH + "score/three.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				three.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				three.render(true);
				break;
			case 4:
				four = DigitModel.getModel(m.OBJ_PATH + "score/four.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				four.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				four.render(true);
				break;
			case 5:
				five = DigitModel.getModel(m.OBJ_PATH + "score/five.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				five.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				five.render(true);
				break;
			case 6:
				six = DigitModel.getModel(m.OBJ_PATH + "score/six.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				six.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				six.render(true);
				break;
			case 7:
				seven = DigitModel.getModel(m.OBJ_PATH + "score/seven.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				seven.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				seven.render(true);
				break;
			case 8:
				eight = DigitModel.getModel(m.OBJ_PATH + "score/eight.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				eight.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				eight.render(true);
				break;
			case 9:
				nine = DigitModel.getModel(m.OBJ_PATH + "score/nine.obj", new Vector3f(18.0f, 13.0f, -20.0f), new Vector3f(1f, 1f, 1f), new Vector3f(0f, 0f, -15f));
				nine.updateTranslate(new Vector3f(-(digitIndex * DIGIT_DIST), 0, 0));
				nine.render(true);
				break;
		}
	}

	public void adjustWithScreen(float zChange) {
		totalZchange += zChange;
		activeDigit.updateTranslate(totalZchange);
//		activeDigit.updateTranslate(0);
		activeDigit.render(true);
	}
}
