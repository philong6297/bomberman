package Exceptions;

public class InLoadLevelException extends GameException {
	public InLoadLevelException() {
	}
	
	public InLoadLevelException(String str) {
		super(str);
		
	}
	
	public InLoadLevelException(String str, Throwable cause) {
		super(str, cause);
		
	}
	
	public InLoadLevelException(Throwable cause) {
		super(cause);
		
	}
	
}
