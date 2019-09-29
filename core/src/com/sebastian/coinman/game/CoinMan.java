package com.sebastian.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

	Random random;

	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	Texture coin;
	int coinCount;

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
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
		random = new Random();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, width, height);

		if (coinCount < 100) {
			coinCount++;
		}
		else {
			coinCount = 0;
			makeCoin();
		}

		for (int i = 0; i < coinXs.size(); i++) {
			batch.draw(coin, coinXs.get(i), coinYs.get(i));
			coinXs.set(i, coinXs.get(i) - 20);
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
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
