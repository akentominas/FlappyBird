package com.akentominas.fluppybirddark;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.BitSet;
import java.util.Random;

public class DarkFluppyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shape;
	Texture birds[];
	int state = 0;
	float birdY = 0;
	float flapSpeed = 0;
	Circle birdCircle;
	int gameState = 0;
	private Sound squag;
	private Sound passed;
	private Sound faild;
	int score = 0;
	int scoreTure;
	BitmapFont font;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	float gap = 450;
	float maxTubeDistence;
	Random random;
	float tubeSpeed = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeDistence = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bk.jpg");
		gameOver = new Texture("gameover.png");
		squag = Gdx.audio.newSound(Gdx.files.internal("suag.mp3"));
		faild = Gdx.audio.newSound(Gdx.files.internal("dun.mp3"));
		//faild.resume();
		passed = Gdx.audio.newSound(Gdx.files.internal("passed.mp3"));


		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		//shape = new ShapeRenderer();

		 birds = new Texture[2];
		 birds[0] = new Texture("bird.png");
		 birds[1] = new Texture("bird2.png");
		 birdY = Gdx.graphics.getHeight()/2 - birds[0].getWidth()/2;

		 topTube = new Texture("toptube.png");
		 bottomTube = new Texture("bottomtube.png");
		 maxTubeDistence = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		 random = new Random();
		 distanceBetweenTubes = Gdx.graphics.getWidth()*3 / 4;

		 topTubeRectangles = new Rectangle[numberOfTubes];
		 bottomTubeRectangles = new Rectangle[numberOfTubes];

		 if (gameState == 2) {
		 	playGameOver();
		 }

		 for (int i = 0; i < numberOfTubes; i++) {
			 tubeDistence[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() -gap - 300);
			 tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			 topTubeRectangles[i] = new Rectangle();
			 bottomTubeRectangles[i] = new Rectangle();
		 }


	}

	public void playGameOver() {
		faild.play();
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1){
			if (tubeX[scoreTure] < Gdx.graphics.getWidth() / 2) {
				score++;

				if (scoreTure < numberOfTubes-1) {
					scoreTure++;
					passed.play();
				} else {
					scoreTure = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				squag.play();
				flapSpeed = -30;

			}

			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < - topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeDistence[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 300);
				} else {
					tubeX[i] = tubeX[i] - tubeSpeed;
				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeDistence[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeDistence[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeDistence[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeDistence[i], bottomTube.getWidth(), bottomTube.getHeight());
			}



		if (birdY > 0) {
			flapSpeed += 2;
			birdY -= flapSpeed;
		} else {
			playGameOver();
			gameState = 2;

		}
		}else if (gameState == 0){
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else if (gameState == 2) {

			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth()/2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight()/2);

			if (Gdx.input.justTouched()) {

				faild.stop();
				gameState = 1;
				birdY = Gdx.graphics.getHeight()/2 - birds[0].getWidth()/2;
				for (int i = 0; i < numberOfTubes; i++) {
					tubeDistence[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() -gap - 300);
					tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

					topTubeRectangles[i] = new Rectangle();
					bottomTubeRectangles[i] = new Rectangle();
				}
				score = 0;
				scoreTure = 0;
				flapSpeed = 0;

			}
		}




		if (state == 0) {
			state = 1;
		}
		else {
			state = 0;
		}

		batch.draw(birds[state], Gdx.graphics.getWidth()/2 - birds[state].getWidth()/2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[state].getHeight() / 2, birds[state].getHeight()/2);

		//shape.begin(ShapeRenderer.ShapeType.Filled);
		//shape.setColor(Color.RED);
		//shape.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {
			//shape.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeDistence[i], topTube.getWidth(), topTube.getHeight());
			//shape.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeDistence[i], bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

				playGameOver();
				gameState = 2;
				faild.dispose();
				faild = Gdx.audio.newSound(Gdx.files.internal("dun.mp3"));
			}
		}
		//shape.end();


	}

}
