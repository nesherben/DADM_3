package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.counter.EndGameFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.HPCounter;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;




public class SpaceShipPlayer extends Sprite{

    private static final int INITIAL_BULLET_POOL_AMOUNT = 50;
    private static final long TIME_BETWEEN_BULLETS = 450;
    List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;

    private int maxX;
    private int maxY;
    private double speedFactor;

    public static boolean proType = false;


    public SpaceShipPlayer(GameEngine gameEngine, int ship){
        super(gameEngine, ship);
        if(!proType) {
            speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        }else{
            speedFactor = pixelFactor * 250d / 1000d; // We want to move at 100px per second on a 400px tall screen
        }
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController

        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if(timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            if(gameEngine.theInputController.isMoving) {
                Bullet bullet1 = getBullet();
                if (bullet1 == null) {
                    return;
                }
                Bullet bullet2 = getBullet();
                if (bullet2 == null) {
                    releaseBullet(bullet1);
                    return;
                }
                bullet1.init(this, positionX + width / 2, positionY, 0);
                bullet2.init(this, positionX + width / 2 - 75, positionY, 0);
                gameEngine.addGameObject(bullet1);
                gameEngine.addGameObject(bullet2);
                timeSinceLastFire = 0;
                gameEngine.onGameEvent(GameEvent.LaserFired);
            }
            if (gameEngine.theInputController.isFiring) {
                Bullet bullet3 = getBullet();
                if (bullet3 == null) {
                    return;
                }
                Bullet bullet4 = getBullet();
                if (bullet4 == null) {
                    releaseBullet(bullet3);
                    return;
                }
                Bullet bullet5 = getBullet();
                if (bullet5 == null) {
                    releaseBullet(bullet3);
                    releaseBullet(bullet4);
                    return;
                }
                Bullet bullet6 = getBullet();
                if (bullet6 == null) {
                    releaseBullet(bullet3);
                    releaseBullet(bullet4);
                    releaseBullet(bullet5);
                    return;
                }
                bullet3.init(this, positionX + width / 2, positionY, 1);
                bullet4.init(this, positionX + width / 2 - 75, positionY, 1);
                bullet5.init(this, positionX + width / 2, positionY, -1);
                bullet6.init(this, positionX + width / 2 - 75, positionY, -1);
                gameEngine.addGameObject(bullet3);
                gameEngine.addGameObject(bullet4);
                gameEngine.addGameObject(bullet5);
                gameEngine.addGameObject(bullet6);
                gameEngine.onGameEvent(GameEvent.LaserFired);
                timeSinceLastFire = 0;
            }
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
            if(HPCounter.HP > 0){
                HPCounter.HP--;
            }
            else{
                gameEngine.removeGameObject(this);
                ((ScaffoldActivity) gameEngine.mainActivity).navigateToFragment(new EndGameFragment());
            }
        }
    }
}
