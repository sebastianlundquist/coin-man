package com.sebastian.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int width;
	int height;
	int manState;
	int pause = 0;
	float gravity = 1f;
	float velocity = 0;
	int manY;
	
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
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, width, height);

		if (Gdx.input.justTouched()) {
			velocity = -40;
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
