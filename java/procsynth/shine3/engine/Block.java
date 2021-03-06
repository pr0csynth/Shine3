// Block.java

package procsynth.shine3.engine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * A Block is a piece of program that has inputs and outputs. Blocks can by
 * wired together through their inputs and outputs to form a complex tree
 * program.
 * 
 * Each block keeps a list of which of its inputs are connected to which other
 * block's outputs. <br>
 * The Blocks subclasses are automatically discovered at runtime by looking in
 * the classpath. <br>
 * To be available to use in Shine the class has to extends this class or any
 * subclasses and to be declared final. <br>
 * To implement the block functionality you write a subclass that override the
 * constructor and the {@link #tick} method. The tick method is executed by the
 * engine at each loop after all of its inputs blocks have been ticked in order
 * to have the freshest input values. <br>
 * In the constructor you can set up the input and the outputs of the block
 * using {@link #newInput} and {@link #newOutput}. <br>
 * You retrieve the values of the inputs using the {@link #get} method and set
 * the values of the outputs using {@link #set}. <br>
 * <br>
 * A simple subclass: <br>
 * <br>
 * 
 * <pre>
 * public final class Echo extends Block {
 *
 * 	public Echo() {
 * 		newInput("ear", "");
 * 		newOutput("mouth", "");
 * 	}
 * 
 * 	public void tick() {
 * 		String heard = "I heard: " + (String) get("ear");
 * 		System.out.println(heard);
 * 		set("mouth", heard);
 * 	}
 * }
 * </pre>
 * 
 * @author procsynth - Antoine Pintout
 * @since v0.0.1
 *
 * @see Engine
 * @see BlockFactory
 */
public class Block {

	private final Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * A unique string that will be used to reconstruct the block object when needed.
	 *
	 * @see #generateBlockIdString
	 */
	private final String idString;
	/** the name that will be displayed */
	protected String displayName;
	/** List the inputs of the block. */
	private Map<String, Input> inputs = new LinkedHashMap();
	/** List the outputs of the block. */
	private Map<String, Output> outputs = new LinkedHashMap();

	/** Used to construct a totally new Block instance */
	public Block() {
		this.idString = generateBlockIdString();
		this.displayName = this.getClass().getSimpleName().toLowerCase();
	}

	/**
	 * Used to restore a Block instance.
	 *
	 * @param idString
	 *            the string to restore the block from
	 */
	public Block(String idString) {
		this.idString = idString;
	}

	/** @return the idString of the block instance. */
	public String getIdString() {
		return idString;
	}

	/** @return the list of inputs of this block */
	public Map<String, Input> getInputs() {
		return inputs;
	}

	/** @return the list of inputs of this block */
	public Map<String, Output> getOutputs() {
		return outputs;
	}

	/** @return the display name of the block */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * This method is called before each tick by the engine, it refresh all the
	 * inputs.
	 */
	public void updateInputs() {
		for (Input i : inputs.values()) {
			i.update();
		}
	}

	/**
	 * This method is called each tick by the engine. <br>
	 * This method is to be overridden to implement specific block functionality.
	 */
	public void tick() {
	}

	/**
	 * Generate a unique string that will be used to reconstruct the block object
	 * when needed.
	 * 
	 * It's structure is:
	 * <code>{fully qualified classname of block class or blockFactory class}:{uniqueid string}</code>
	 *
	 * @return the id string
	 */
	private String generateBlockIdString() {
		return this.getClass().getName() + ":" + UUID.randomUUID().toString();
	}

	/**
	 * Use this method to define a new input of the block instance.
	 *
	 * @param name
	 *            the name of the input
	 * @param defaultValue
	 *            the default value of the input
	 * @param <T>
	 *            the type of the value of the input
	 */
	protected <T> void newInput(String name, T defaultValue) {
		inputs.put(name, new Input<T>(name, defaultValue));
	}

	/**
	 * Use this method to define a new output of the block instance.
	 *
	 * @param name
	 *            the name of the output
	 * @param defaultValue
	 *            the default value of the output
	 * @param <T>
	 *            the type of the value of the output
	 */
	protected <T> void newOutput(String name, T defaultValue) {
		outputs.put(name, new Output<T>(this, name, defaultValue));
	}

	/**
	 * Use this method to set a value to an output.
	 *
	 * @param name
	 *            the name of the output
	 * @param newValue
	 *            the default value of the output
	 * @param <T>
	 *            the type of the value of the output
	 */
	protected <T> void set(String name, T newValue) {
		if (outputs.containsKey(name)) {
			outputs.get(name).setValue(newValue);
		}
	}

	/**
	 * Use this method to get the value of an input.
	 *
	 * @param name
	 *            the name of the input
	 * @param <T>
	 *            the type of the input
	 *
	 * @return the value of the input or null if the input does not exists
	 */
	protected <T> T get(String name) {
		if (inputs.containsKey(name)) {
			return (T) inputs.get(name).value();
		} else {
			return null;
		}
	}

}
