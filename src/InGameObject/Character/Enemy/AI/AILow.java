package InGameObject.Character.Enemy.AI;

public class AILow extends AIAbstract {

	@Override
    public int calculateDirection(int start, int end) {
        return random.nextInt(4);
	}

}
