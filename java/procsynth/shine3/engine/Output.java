// Output.java

package procsynth.shine3.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * An output is a part of a block that may be connected to one or many block
 * input.
 *
 * @author procsynth - Antoine Pintout
 * @since v0.0.1
 *
 * @see Engine
 * @see Block
 */
public class Output<T> {

	private final Logger log = Logger.getLogger(this.getClass().getName());

	/** The parent block. */
	private Block parent;

	/** The value of the output */
	private T value;
	/** The default value of the output. */
	private final T defaultValue;
	/** The display name of the output. */
	private String name;

	/** @return the display name of the output. */
	public String getName() {
		return name;
	}


	/** Internal pool holding all available outputs */
	private static Map<String, Output<?>> outputPool = new HashMap<>();

	public Output(Block parent, String name, T defaultValue) {
		this.parent = parent;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public Output(String name, T defaultValue) {
		this.parent = null;
		this.name = name;
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the output value
	 * 
	 * @return the value of the output
	 */
	public T value() {
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Set the value
	 * 
	 * @param value
	 *            the value of the output
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Get the parent block
	 * 
	 * @return the block to which this output belongs
	 */
	public Block getParent() {
		return parent;
	}

}