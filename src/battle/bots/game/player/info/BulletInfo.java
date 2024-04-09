package battle.bots.game.player.info;

import battle.bots.game.objects.Bullet;
import battle.bots.game.player.Coordinate;
import battle.bots.game.util.ImmutablePoint;
import battle.bots.game.util.Vector;

public class BulletInfo extends Info {
    private final Bullet bullet;

    public BulletInfo(Bullet bullet) {
        super(new Coordinate(new ImmutablePoint((int) bullet.getX(), (int) bullet.getY()), Coordinate.Type.NORMAL));

        this.bullet = bullet;
    }

    public Vector getBulletVelocity() {
        return new Vector(this.bullet.getVelocity());
    }
}
