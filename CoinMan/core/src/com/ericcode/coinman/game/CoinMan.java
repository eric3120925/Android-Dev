package com.ericcode.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState = 0;
	int pause =0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY =0;
	Rectangle manRectangle;
	BitmapFont font;
	Texture dizzy;

	int score = 0;
	int gameState =0;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	Texture bomb;
	int bombCount;

	Random random;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		manY =Gdx.graphics.getHeight()/2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	public void makeCoin(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		if(height > Gdx.graphics.getHeight()-coin.getHeight()){
			height -= coin.getHeight();
		}else if(height <coin.getHeight()){
			height += coin.getHeight();;
		}
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		if(height > Gdx.graphics.getHeight()-bomb.getHeight()){
			height -= bomb.getHeight();
		}else if(height<bomb.getHeight()){
			height +=bomb.getHeight();
		}
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	/*render () - it keeps executing until the app stop*/
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState ==1){
			// GAME IS LIVE
			/*create Coin*/
			if(coinCount<100){
				coinCount++;
			}else {
				coinCount =0;
				makeCoin();
			}
			coinRectangles.clear();
			for (int i =0; i< coinXs.size(); i++){
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i)-4);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			/*create BOMB*/
			if(bombCount<250){
				bombCount++;
			}else {
				bombCount=0;
				makeBomb();
			}
			bombRectangles.clear();
			for (int i=0; i< bombXs.size(); i++){
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i)-8);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}


			/*man jump*/
			if(Gdx.input.justTouched()){ //make the man jump when tapping the screen
				velocity =-10;
			}

			/*man moving action*/
			if(pause< 8){
				pause++;
			} else {
				pause =0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}

			/*gravity*/
			velocity += gravity;
			manY -= velocity;

			if(manY <= 0){
				manY =0;
			}else if(manY > Gdx.graphics.getHeight()-man[manState].getHeight()){
				manY = Gdx.graphics.getHeight()-man[manState].getHeight();
			}
		}else if(gameState ==0){
			// Waiting to start
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState == 2){
			// GAME OVER
			if(Gdx.input.justTouched()){
				gameState=1;
				manY =Gdx.graphics.getHeight()/2;
				score =0;
				velocity =0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount=0;

				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount=0;
			}
		}

		if(gameState==2) {
			batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}else {
			batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 -man[manState].getWidth()/2, manY, man[manState].getWidth(), man[manState].getHeight());
		for(int i =0; i< coinRectangles.size(); i++){
			if(Intersector.overlaps(manRectangle, coinRectangles.get(i))){
				Gdx.app.log("Coin", "Collision!!");
				score++;

				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for(int i =0; i< bombRectangles.size(); i++){
			if(Intersector.overlaps(manRectangle, bombRectangles.get(i))){
				Gdx.app.log("Bomb", "Collision!!");
				gameState=2;

			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
