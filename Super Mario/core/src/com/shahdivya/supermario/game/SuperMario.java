package com.shahdivya.supermario.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

public class SuperMario extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture man[];
    Texture manLarge[];
    Texture life;
    Texture drizzy;
    Texture drizzyLarge;
    int manstate = 0;
    int pause =0;
    float gravity = 0.2f;
    float velocity = 0;
    int manY;
    int x = 0;
    int score = 0;
    int gamestate = 0;
    BitmapFont font;
    Rectangle manRect;
    Rectangle manRectLarge;
    Texture coin;
    Texture bomb;
    Random random;
    Music music;
    ArrayList<Integer> coinsX = new ArrayList<Integer>();
    ArrayList<Integer> coinsY = new ArrayList<Integer>();
    ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
    int coincount;
    ArrayList<Integer> lifeX = new ArrayList<Integer>();
    ArrayList<Integer> lifeY = new ArrayList<Integer>();
    ArrayList<Rectangle> lifeRectangles = new ArrayList<Rectangle>();
    int lifecount;
    ArrayList<Integer> bombX = new ArrayList<Integer>();
    ArrayList<Integer> bombY = new ArrayList<Integer>();
    ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
    int bombCount;
    Sound sound;
    private void makeBomb()
    {
        float height =random.nextFloat()*Gdx.graphics.getHeight();
        if (height > Gdx.graphics.getHeight()-bomb.getHeight())
        {
            height =Gdx.graphics.getHeight()-bomb.getHeight();
        }
        bombY.add((int) height);
        bombX.add(Gdx.graphics.getWidth());
    }
    private void makeCount()
    {
        float height =random.nextFloat()*Gdx.graphics.getHeight();
        if (height > Gdx.graphics.getHeight()-coin.getHeight())
        {
            height =Gdx.graphics.getHeight()-coin.getHeight();
        }
        coinsY.add((int) height);
        coinsX.add(Gdx.graphics.getWidth());
    }
    private void makeLife()
    {
        float height =random.nextFloat()*Gdx.graphics.getHeight();
        if (height > Gdx.graphics.getHeight()-life.getHeight())
        {
            height =Gdx.graphics.getHeight()-life.getHeight();
        }
        lifeY.add((int) height);
        lifeX.add(Gdx.graphics.getWidth());
    }
    @Override
    public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0] =new Texture("frame-1.png");
        man[1] =new Texture("frame-2.png");
        man[2] =new Texture("frame-3.png");
        man[3] =new Texture("frame-4.png");
        drizzy = new Texture("dizzy-1.png");
        manLarge = new Texture[4];
        manLarge[0] =new Texture("frame-1-Large.png");
        manLarge[1] =new Texture("frame-2-Large.png");
        manLarge[2] =new Texture("frame-3-Large.png");
        manLarge[3] =new Texture("frame-4-Large.png");
        drizzyLarge = new Texture("dizzy-1-Large.png");
        manY = Gdx.graphics.getHeight()/2;
        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        life = new Texture("heart.png");
        random = new Random();
        font = new BitmapFont();
        font.setColor(Color.ROYAL);
        font.getData().scale(5);
        sound = Gdx.audio.newSound(Gdx.files.internal("MARIO-DIES-SOUND-EFFECT.ogg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("2019-10-21_-_Feels_Good_-_David_Fesliyan.mp3"));
        music.play();
        music.setLooping(true);
    }

    @Override
    public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        if (gamestate == 1 )
        {
            //Live
            if (coincount<100)
            {
                coincount++;
            }
            else {
                makeCount();
                coincount = 0;
            }
            if (bombCount<300)
            {
                bombCount++;
            }else {
                makeBomb();
                bombCount = 0;
            }
            if (x==0)
            {
                if (lifecount<1000)
                {
                    lifecount++;
                }else {
                    makeLife();
                    lifecount = 0;
                }
                lifeRectangles.clear();
                for (int i=0;i<lifeX.size();i++)
                {
                    batch.draw(life,lifeX.get(i),lifeY.get(i));
                    lifeX.set(i,lifeX.get(i)-8);
                    lifeRectangles.add(new Rectangle(lifeX.get(i),lifeY.get(i),life.getWidth(),life.getHeight()));
                }
            }



            coinRectangles.clear();
            for (int i=0;i<coinsX.size();i++)
            {
                batch.draw(coin,coinsX.get(i),coinsY.get(i));
                if (score<=20)
                {
                    coinsX.set(i,coinsX.get(i)-4);
                }else if (score>20 && score<=40)
                {
                    coinsX.set(i,coinsX.get(i)-8);
                }else {
                    coinsX.set(i,coinsX.get(i)-12);
                }
                coinRectangles.add(new Rectangle(coinsX.get(i),coinsY.get(i),coin.getWidth(),coin.getHeight()));
            }
            bombRectangles.clear();
            for (int i=0;i<bombX.size();i++)
            {
                batch.draw(bomb,bombX.get(i),bombY.get(i));
                if (score<=20)
                {
                    bombX.set(i,bombX.get(i)-8);
                }else if (score>20 && score<=40)
                {
                    bombX.set(i,bombX.get(i)-12);
                }else {
                    bombX.set(i,bombX.get(i)-14);
                }
                bombRectangles.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
            }

            if (Gdx.input.justTouched())
            {
                velocity = -10;
            }
            if (pause<10)
            {
                pause++;
            }
            else {
                pause=0;
                if (manstate < 3) {
                    manstate++;
                } else {
                    manstate = 0;
                }
            }
            velocity += gravity;
            manY -= velocity;
            if (manY<0)
            {
                manY = 0;
            }
            if (x==1)
            {
                if (manY>Gdx.graphics.getHeight()-manLarge[manstate].getHeight())
                {
                    manY = Gdx.graphics.getHeight()-manLarge[manstate].getHeight();
                }
            }
            if (manY>Gdx.graphics.getHeight()-man[manstate].getHeight())
            {
                manY = Gdx.graphics.getHeight()-man[manstate].getHeight();
            }
        }
        else if (gamestate == 0)
        {
            // Wait to begin
            if (Gdx.input.justTouched())
            {
                gamestate = 1;
            }
        }
        else if (gamestate == 2 && x==0)
        {
            //Game Over

                if (Gdx.input.justTouched()) {
                    sound.play();
                    gamestate = 1;
                    manY = Gdx.graphics.getHeight() / 2;
                    score = 0;
                    velocity = 0;
                    coinsX.clear();
                    coinsY.clear();
                    coinRectangles.clear();
                    coincount = 0;
                    bombX.clear();
                    bombY.clear();
                    bombRectangles.clear();
                    bombCount = 0;
                }
        }
        else if (gamestate==2 && x==1)
        {
            gamestate = 1;
            x =0;
        }
        if (x==0)
        {
            if (gamestate==2)
            {
                batch.draw(drizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
            }else {
                batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
            }
            manRect = new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
        }
        else {
            batch.draw(manLarge[manstate],Gdx.graphics.getWidth()/2-manLarge[manstate].getWidth()/2,manY);
            manRectLarge = new Rectangle(Gdx.graphics.getWidth()/2-manLarge[manstate].getWidth()/2,manY,manLarge[manstate].getWidth(),manLarge[manstate].getHeight());
        }
        for (int i=0;i<coinRectangles.size();i++)
        {
            if (x==0) {
                if (Intersector.overlaps(coinRectangles.get(i), manRect)) {
                    score++;
                    coinRectangles.remove(i);
                    coinsX.remove(i);
                    coinsY.remove(i);
                    break;
                }
            }
            if (x==1) {
                if (Intersector.overlaps(coinRectangles.get(i), manRectLarge)) {
                    score++;
                    coinRectangles.remove(i);
                    coinsX.remove(i);
                    coinsY.remove(i);
                    break;
                }
            }

        }
        for (int i=0;i<bombRectangles.size();i++)
        {
            if (x==0)
            {
                if (Intersector.overlaps(manRect, bombRectangles.get(i))) {
                    System.out.println(x);
                    System.out.println("I dead");
                    Gdx.app.log("Bombs!!", "Dead");
                    gamestate = 2;
                    break;
                }
            }
            if (x==1)
            {
                if (Intersector.overlaps(manRectLarge,bombRectangles.get(i)))
                {
                    gamestate=2;
                    break;
                }
            }
        }
        for (int i=0;i<lifeRectangles.size();i++)
        {
            if (Intersector.overlaps(lifeRectangles.get(i),manRect))
            {
                x=1;
                lifeRectangles.remove(i);
                lifeX.remove(i);
                lifeY.remove(i);
                break;
            }
        }
        font.draw(batch,String.valueOf(score),100,200);

        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        //sound.dispose();
    }
}
