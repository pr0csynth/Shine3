// Pair.java

package procsynth.shine3.utils;

import java.util.logging.Logger;

/**
 * A class to associate two values.
 * 
 * @see procsynth.shine3.papplet.UI
 */
public final class Pair<A, B> {

	private final Logger log = Logger.getLogger(this.getClass().getName());

	private final A first;
	private final B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair otherPair = (Pair) other;
			return ((this.first == otherPair.first
					|| (this.first != null && otherPair.first != null && this.first.equals(otherPair.first)))
					&& (this.second == otherPair.second || (this.second != null && otherPair.second != null
							&& this.second.equals(otherPair.second))));
		}
		return false;
	}

	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public A A() {
		return first;
	}

	public B B() {
		return second;
	}

}