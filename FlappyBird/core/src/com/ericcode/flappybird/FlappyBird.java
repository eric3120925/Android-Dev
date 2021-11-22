package com.ericcode.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;



public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch; // batch render the Texture
	Texture background;
	ShapeRenderer shapeRenderer; // shapeRenderer render the shape ex: Circle objects

	Texture [] birdAnimation;
	int flapState = 0;
	int stateCount = 0;

	float birdY = 0;
	float velocity =0;
	Circle birdCircle;

	int score = 0;
	int scoreTube = 0;
	BitmapFont font;

	int gameState =0;
	float gravity = 0.5f;

	Texture topTube;
	Texture bottomTube;
	float gap  = 400;
	float maxTubeOffset;
	Random randomGeneration;

	float tubeVelocity = 4;

	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	Texture gameover;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		// Bird object
		birdAnimation = new Texture[2];
		birdAnimation[0] = new Texture("bird.png");
		birdAnimation[1] = new Texture("bird2.png");



		// Tube objects
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() /2 - gap /2 -100;
		randomGeneration = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() *3/4;

		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startGame();

	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birdAnimation[0].getHeight()/2;
		for(int i = 0; i< numberOfTubes; i++){

			tubeOffset[i] = (randomGeneration.nextFloat()- 0.5f) * (Gdx.graphics.getHeight() - gap - 200); // randomGeneration.nextFloat(): 0~1

			tubeX[i] = Gdx.graphics.getWidth()/2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState ==1) {

			if(tubeX[scoreTube] < Gdx.graphics.getWidth() /2){
				score++;
				Gdx.app.log("Score", Integer.toString(score));

				if (scoreTube < numberOfTubes -1){
					scoreTube ++;
				}else {
					scoreTube =0;
				}
			}

			if(Gdx.input.justTouched()){
				velocity = -10;
			}

			// tube drawing
			for(int i = 0; i< numberOfTubes; i++) {

				if (tubeX[i] < -topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
				}else {
					tubeX[i] -= tubeVelocity;


				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}



			velocity += gravity;
			birdY -= velocity;

			if(birdY <=0 ) {
				gameState = 2;
			}else if(birdY > Gdx.graphics.getHeight() - birdAnimation[0].getHeight()){
				birdY =  Gdx.graphics.getHeight() - birdAnimation[0].getHeight();
			}

		}else if( gameState ==0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			}

		} else if(gameState ==2){
			batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getWidth()/2);
			if(Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score = 0;
				scoreTube =0;
				velocity =0;
			}
		}

		if (stateCount < 5) {
			stateCount++;
		} else {
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}
			stateCount = 0;
		}
		batch.draw(birdAnimation[flapState], Gdx.graphics.getWidth() / 2 - birdAnimation[flapState].getWidth() / 2, birdY);

		// score drawing
		font.draw(batch, String.valueOf(score), 100,200);

		batch.end();


		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birdAnimation[flapState].getHeight() /2, birdAnimation[flapState].getWidth()/2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for(int i = 0; i< numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


			// Collision detect
			if(Intersector.overlaps(birdCircle, topTubeRectangles[i])|| Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){

				gameState = 2;
			}
		}

		//shapeRenderer.end();


	}

}
