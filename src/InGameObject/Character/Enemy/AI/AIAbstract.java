package InGameObject.Character.Enemy.AI;

import java.util.Random;

public abstract class AIAbstract {

    protected final int UP = 0;
    protected final int RIGHT = 1;
    protected final int DOWN = 2;
    protected final int LEFT = 3;
    protected Random random = new Random();

    public abstract int calculateDirection();
}
