package com.manavgirotra.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.sql.Time;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameOver;
	Texture[] birds;
	Texture[] tubes;
	int flapState;
	int score =0;
	int scoringTube=0;
	BitmapFont font;
	int gameState = 0;
	float birdY = 0 ;
	float velocity = 0;
	float gravity = 1;
    float gap = 400;
    float maxTubeOfSet ;
    Random random = new Random( );
    float tubeVelocity=8;
    int numberOfTubes=4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOfSet = new float[numberOfTubes];
    float distanceBetweenTubes;
    Circle birdCircle ;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    ShapeRenderer shapeRenderer ;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture( "bg.png" );
		birds = new Texture[2];
		tubes = new Texture[2];
		tubes[0]= new Texture( "toptube.png" );
		tubes[1] = new Texture( "bottomtube.png" );
		birds[0] = new Texture( "bird.png" );
		birds[1] = new Texture( "bird2.png" );
		gameOver = new Texture( "gameOver.png" );
		maxTubeOfSet = Gdx.graphics.getHeight()/2-gap/2-100;
		distanceBetweenTubes =Gdx.graphics.getWidth()*3/4;
      //  shapeRenderer = new ShapeRenderer(  );
        font = new BitmapFont(  );
        font.setColor( Color.WHITE );
        font.getData().setScale( 7 );
        birdCircle = new Circle(  );
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();



	}

	public void startGame()
    {
        birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

        for(int i =0;i<numberOfTubes;i++)
        {
            tubeOfSet[i] = (random.nextFloat()-(float) 0.5)*(Gdx.graphics.getHeight()-gap-200);
            tubeX[i] = Gdx.graphics.getWidth()/2-tubes[0].getWidth()/2+Gdx.graphics.getWidth() + i*distanceBetweenTubes ;

            topTubeRectangles[i] = new Rectangle(  );
            bottomTubeRectangles[i] = new Rectangle(  );
        }
    }

	@Override
	public void render () {

	    if(tubeVelocity<15)
        {
            tubeVelocity += 0.0000000000000000000025;
        }

        birdCircle.set( Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2-8 );

        batch.begin();
        batch.draw( background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

        if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2)
        {
            score++;
            Gdx.app.log( "Score", String.valueOf( score ) );
            if(scoringTube<numberOfTubes-1)
                scoringTube++;
            else
                scoringTube=0;
        }

        if(gameState == 1) {
            if(Gdx.input.justTouched() && birdY<Gdx.graphics.getHeight())
            {
                velocity  = -20;

            }
            for(int i =0;i<numberOfTubes;i++)
            {
                if(tubeX[i]<-tubes[0].getWidth())
                {
                    tubeX[i] = numberOfTubes*distanceBetweenTubes;
                    tubeOfSet[i] = (random.nextFloat()-(float) 0.5)*(Gdx.graphics.getHeight()-gap-200);
                }
                else {
                    tubeX[i] -= tubeVelocity;

                }
                    batch.draw( tubes[0], tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfSet[i] );
                    batch.draw( tubes[1], tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[1].getHeight() + tubeOfSet[i] );

                    topTubeRectangles[i]=new Rectangle( tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfSet[i],tubes[0].getWidth(),tubes[0].getHeight());
                    bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tubes[1].getHeight() + tubeOfSet[i],tubes[1].getWidth(),tubes[1].getHeight());

                if (Intersector.overlaps( birdCircle, topTubeRectangles[i] )|| Intersector.overlaps( birdCircle,bottomTubeRectangles[i] ))
                {
                    gameState=2;
                    Gdx.input.vibrate( 100 );
                }
            }


            if(birdY>0 ) {
                velocity += gravity;
                birdY -= velocity;
            }
            else
            {
                gameState=2;
            }

        }
	    else if(gameState==0)
        {
            if(Gdx.input.justTouched())
            {
                gameState = 1;
            }
        }
	    else if(gameState==2)
        {
            batch.draw( gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2 -gameOver.getHeight()/2);

            if(Gdx.input.justTouched())
            {
                gameState = 1;
                startGame();
                scoringTube=0;
                score=0;
                velocity=0;
                tubeVelocity=8;
            }

        }
        if (flapState == 0)
            flapState = 1;
        else
            flapState = 0;
        batch.draw( birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY );

        font.draw( batch,String.valueOf( score ),100,200 );



        batch.end();


    }
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
