package InGameObject.Character.Enemy.AI;

import java.util.Random;

public abstract class AIAbstract {
	
	protected Random random = new Random();
	
	public abstract int calculateDirection();
}
