package com.sebastian.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int width;
	int height;
	int manState;
	int pause = 0;
	float gravity = 2f;
	float velocity = 0;
	int manY;
	Rectangle manRectangle;

	Random random;

	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<>();
	ArrayList<Integer> bombYs = new ArrayList<>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	Texture bomb;
	int bombCount;

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		manY = height / 2 - man[0].getHeight() / 2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, width, height);

		if (bombCount < 200) {
			bombCount++;
		}
		else {
			bombCount = 0;
			makeBomb();
		}

		bombRectangles.clear();
		for (int i = 0; i < bombXs.size(); i++) {
			batch.draw(bomb, bombXs.get(i), bombYs.get(i));
			bombXs.set(i, bombXs.get(i) - 15);
			bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
		}

		if (coinCount < 100) {
			coinCount++;
		}
		else {
			coinCount = 0;
			makeCoin();
		}

		coinRectangles.clear();
		for (int i = 0; i < coinXs.size(); i++) {
			batch.draw(coin, coinXs.get(i), coinYs.get(i));
			coinXs.set(i, coinXs.get(i) - 20);
			coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
		}

		if (Gdx.input.justTouched()) {
			velocity = -60;
		}

		if (pause < 8) {
			pause++;
		}
		else {
			pause = 0;
			if (manState < 3) {
				manState++;
			}
			else {
				manState = 0;
			}
		}

		velocity += gravity;
		manY -= velocity;

		if (manY <= 0) {
			manY = 0;
		}

		batch.draw(man[manState], width / 4 - man[0].getWidth() / 2, manY);
		manRectangle = new Rectangle(width / 4 - man[0].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());
		for (int i = 0; i < coinRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
				Gdx.app.log("Coin", "Collided");
			}
		}
		for (int i = 0; i < bombRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
				Gdx.app.log("Bomb", "Collided");
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
