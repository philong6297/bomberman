package Level;


import Exceptions.InLoadLevelException;

public interface ILevel {

	public void loadLevel(String path) throws InLoadLevelException;
}
