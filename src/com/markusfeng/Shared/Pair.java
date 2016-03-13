package com.markusfeng.Shared;

import java.io.Serializable;

/**
 * The Pair class represents two typed Objects together. It is
 * analogous to a 2-tuple in storing typed Objects. Applications
 * include putting multiple Objects in an index of a Collection,
 * or returning more than one value from a method. Multiple
 * Pairs can be nested to simulate a 3-tuple or more. Note that
 * the pair syntax is limited by the java generics system.
 *
 * @author Markus Feng
 *
 * @param <T> the type of the first value
 * @param <S> the type of the second value
 */
public class Pair<T, S> implements Serializable{

	private static final long serialVersionUID = -8076809629855557338L;
	private static final int LOW_MASK = 0x0000FFFF;
	private static final int HIGH_MASK = 0xFFFF0000;

	/**
	 * The the first item
	 */
	protected T valueOne;

	/**
	 * The second item
	 */
	protected S valueTwo;

	/**
	 * Creates an empty pair
	 */
	protected Pair(){

	}

	/**
	 * Creates a new Pair with the same values as the given Pair
	 * @param original the Pair to obtain values from
	 */
	public Pair(Pair<? extends T, ? extends S> original){
		this(original.getValueOne(), original.getValueTwo());
	}

	/**
	 * Creates a new Pair with the given values
	 * @param valueOne the value of the first item
	 * @param valueTwo the value of the second item
	 */
	public Pair(T valueOne, S valueTwo){
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	public T getValueOne(){
		return valueOne;
	}

	public S getValueTwo(){
		return valueTwo;
	}

	@Override
	public String toString(){
		T v1 = getValueOne();
		S v2 = getValueTwo();
		String valueOneString = v1 == null ? "null" : v1.toString();
		String valueTwoString = v2 == null ? "null" : v2.toString();
		return "Pair: [" + valueOneString + "," + valueTwoString + "]";
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Pair){
			Pair<?, ?> p = (Pair<?, ?>) o;
			if(getValueOne().equals(p.getValueOne()) && getValueTwo().equals(p.getValueTwo())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		return getValueOne().hashCode() ^ getValueTwo().hashCode() << 16 ^ HIGH_MASK ^ getValueTwo().hashCode() >> 16
				^ LOW_MASK;
	}

	/**
	 * Creates a new Pair with the given values
	 * @param v1 the value of the first item
	 * @param v2 the value of the second item
	 * @return a new Pair with the given values
	 */
	public static <T, S> Pair<T, S> make(T v1, S v2){
		return new Pair<T, S>(v1, v2);
	}

	/**
	 * Creates a new Pair with the same values as the given Pair
	 * @param original the Pair to obtain values from
	 * @return a new Pair with the same values as the given Pair
	 */
	public static <T, S> Pair<T, S> make(Pair<T, S> pair){
		return new Pair<T, S>(pair);
	}

}
