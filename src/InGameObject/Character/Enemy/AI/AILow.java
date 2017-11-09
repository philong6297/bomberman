package InGameObject.Character.Enemy.AI;

public class AILow extends AIAbstract {

	@Override
	public int calculateDirection() {
		return random.nextInt(4);
	}

}
