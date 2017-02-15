// EngineExceptions.java

package procsynth.shine3.engine;

/**
 *	Regroup all engine related exceptions
 *	
 *	@author procsynth - Antoine Pintout
 *	@since  v0.0.1
 */
public class EngineExceptions{

	/** Occurs when trying to instantiate a block class that is'nt available or a non-final class */
	public static class UnknowBlockClass extends Exception {
		UnknowBlockClass(){
			super("This Block class is unknown, verify that it is final.");
		}
	}

}