// BlockEngine.java

package procsynth.shine3.engine;

import procsynth.shine3.engine.blocks.*;
import procsynth.shine3.engine.world.*;
import procsynth.shine3.Shine3;

import java.util.*;

/**
 *	
 *	@author procsynth - Antoine Pintout
 *	@since  v0.0.1
 */
public class BlockEngine extends Thread{

	/**	List available blocks */
	private List<Class<?>> availableBlocks = new ArrayList<>();
	/** @return available blocks */
	public List<Class<?>> getAvailableBlocks(){return new ArrayList(availableBlocks);}
	/**	List available factories */
	private List<Class<?>> availableFactories = new ArrayList<>();

	/** Stores all blocks currently in the engine */
	private List<Block> blocks = new ArrayList();
	/** @return the block tree */
	public List<Block> getBlocks(){return new ArrayList(blocks);}
	/** Stores all blocks resolved block for the current tick */
	private List<Block> resolved = new ArrayList();

	/** Stores number of ticks since start */
	private long ticks = 0L;
	public long getTicks(){return ticks;}


	/**
	*	Stores real time tickRate measure in tick per second.
	*	@see #getTickRate
	*/
	private float tickRate = 10f;
	public float getTickRate(){return tickRate;}
	/** 
	*	Stores tickRate target as period of time in nanosecond.
	*	@see #setTickRate
	*/
	private long  targetPeriod = (long)(1000000000L / 42f); // target is 42 tps

	/** Instanciate a BlockEngine and start the engine thread. */
	public BlockEngine(){

		availableBlocks = Shine3.modules.getClasses(Block.class);
		availableFactories = Shine3.modules.getClasses(BlockFactory.class);

		this.start();
		new World();

		// A test block
		try{
			addBlock(Not.class);
			addBlock(Hertz.class);
			addBlock(Or.class);
			addBlock(availableBlocks.get(0));

			blocks.get(0).getInputs().get("A").setSource(blocks.get(1).getOutputs().get("hertz"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		// TODO start thread
	}

	/** 
	*	Engine main loop, manage tickRate.
	*	@see #setTickRate
	*/
	public void run(){

		long tickRateLastNanos = 0L;
      	long overSleepTime = 0L;

      	Thread.currentThread().setName("Shine3 Engine");

		while(true){
			long now = System.nanoTime();
			double rate = 1000000.0 / ((now - tickRateLastNanos) / 1000000.0);
			float instantaneousRate = (float) (rate / 1000.0);
			tickRate = (tickRate * 0.9f) + (instantaneousRate * 0.1f);
			tickRateLastNanos = now;

			tick();
			ticks++;

			long afterTime = System.nanoTime();
			long timeDiff = afterTime - now;
			long sleepTime = (targetPeriod - timeDiff) - overSleepTime;

			if (sleepTime > 0){  // some time left in this cycle
				try{
					Thread.sleep(sleepTime / 1000000L, (int) (sleepTime % 1000000L));
				} catch (InterruptedException ex){}

				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			}else{
				overSleepTime = 0;
			}
		}
	}

	/** Engine main logic */
	private void tick(){

		// clear resolved block list:
		resolved = new ArrayList();
		// resolve block tree
		for(Block b : blocks){
			// if block not already resolve
			if(resolved.indexOf(b) == -1){
				resolve(b);
			}
		}

	}

	/**
	*	Resolve block tree with post-order traversal in order to have the freshest
	*	input values when a block is ticked.
	*	@param origin the block to resolve
	*/
	private void resolve(Block origin){
		// already put the origin block in the resolved list to avoid recursive loop
		// if the block tree is looping at some point
		resolved.add(origin);

		for(Input i : origin.getInputs().values()){
			Block source = i.getSourceBlock();
			if(source != null && resolved.indexOf(source) == -1){
				resolve(source);
			}
		}
		// fetch new values
		origin.updateInputs();

		// then tick
		origin.tick();
	}

	/**
	*	Set a target tick rate.
	* 
	*	@param tps is the target tickRate in tick per second.
	*/
	public void setTickRate(float tps){
		targetPeriod = (long) (1000000000.0 / tps);
	}

	/** 
	*	Add a new block to the engine
	*	
	* 	@param blockClass the class to create the object from
	*	
	*	@throws EngineExceptions.UnknowClass if the class definition is unknown
	*	@throws InstantiationException if instanciation goes wrong.
	*	@throws IllegalAccessException if instanciation goes wrong.
	*/
	public void addBlock(Class<?> blockClass) throws EngineExceptions.UnknowClass, InstantiationException, IllegalAccessException{
		if(availableBlocks.indexOf(blockClass) == -1){
			throw new EngineExceptions.UnknowClass();
		}
		Block b = (Block)blockClass.newInstance();
		blocks.add(b);
		System.out.println(b.getIdString());
	}
}

