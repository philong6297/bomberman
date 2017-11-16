package Level;


import Exceptions.InLoadLevelException;

public interface ILevel {

    void loadLevel(String path) throws InLoadLevelException;
}
