package com.sebastian.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	private SpriteBatch batch;

	private Texture[] man;
	private Texture dizzyMan;
	private Texture coin;
	private Texture bomb;
	private Texture background;

	private int screenWidth;
	private int screenHeight;
	private int manState;
	private int pause = 0;
	private float velocity = 0;
	private int manY;

	private int score = 0;
	private int gameState;
	private BitmapFont font;

	private Random random = new Random();

	private ArrayList<Integer> coinXs = new ArrayList<>();
	private ArrayList<Integer> coinYs = new ArrayList<>();
	private ArrayList<Rectangle> coinRectangles = new ArrayList<>();
	private int coinCount;

	private ArrayList<Integer> bombXs = new ArrayList<>();
	private ArrayList<Integer> bombYs = new ArrayList<>();
	private ArrayList<Rectangle> bombRectangles = new ArrayList<>();
	private int bombCount;

	private void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	private void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	private void playGame() {
		if (bombCount < 200)
			bombCount++;
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

		if (coinCount < 100)
			coinCount++;
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

		if (Gdx.input.justTouched())
			velocity = -60;

		if (pause < 8)
			pause++;
		else {
			pause = 0;
			if (manState < 3)
				manState++;
			else
				manState = 0;
		}

		velocity += 2f;
		manY -= velocity;

		if (manY <= 0)
			manY = 0;
	}

	private void resetGame() {
		gameState = 1;
		manY = screenHeight / 2 - man[0].getHeight() / 2;
		score = 0;
		velocity = 0;
		coinXs.clear();
		coinYs.clear();
		coinRectangles.clear();
		coinCount = 0;
		bombXs.clear();
		bombYs.clear();
		bombRectangles.clear();
		bombCount = 0;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		dizzyMan = new Texture("dizzy-1.png");
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		background = new Texture("bg.png");

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		manY = screenHeight / 2 - man[0].getHeight() / 2;

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		background = new Texture("bg.png");
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, screenWidth, screenHeight);

		if (gameState == 1) {
			// Game is live
			playGame();
		}
		else if (gameState == 0) {
			// Waiting to start
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else if (gameState == 2) {
			// Game over
			if (Gdx.input.justTouched()) {
				resetGame();
			}
		}

		if (gameState == 2)
			batch.draw(dizzyMan, screenWidth / 3f - man[0].getWidth() / 2f, manY);
		else
			batch.draw(man[manState], screenWidth / 3f - man[0].getWidth() / 2f, manY);

		Rectangle manRectangle = new Rectangle(screenWidth / 3f - man[0].getWidth() / 2f, manY, man[manState].getWidth(), man[manState].getHeight());
		for (int i = 0; i < coinRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
				score++;
				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i = 0; i < bombRectangles.size(); i++)
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i)))
				gameState = 2;

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
