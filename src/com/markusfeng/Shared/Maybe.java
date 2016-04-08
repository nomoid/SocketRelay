package com.markusfeng.Shared;

import java.util.NoSuchElementException;

import com.markusfeng.SocketRelay.Compatibility.Function;

/**
 * A container class that can contain a null value
 *
 * @author Markus Feng
 *
 * @param <T> the type of the value contained
 */
public class Maybe<T>{

	protected boolean present;
	protected T t;

	public Maybe(T t){
		present = true;
		this.t = t;
	}

	public Maybe(){
		present = false;
	}

	/**
	 * Returns whether the maybe is present
	 * @return whether the maybe is present
	 */
	public boolean isPresent(){
		return present;
	}

	/**
	 * Returns the value present in the Maybe
	 * @return the value present in the Maybe
	 * @throws NoSuchElementException if no value is present in the Maybe
	 */
	public T get() throws NoSuchElementException{
		if(!isPresent()){
			throw new NoSuchElementException("Cannot get the object of an unfilled Maybe!");
		}
		return t;
	}

	/**
	 * Returns whether a value is present, and the value is not null
	 * @return whether a value is present, and the value is not null
	 */
	public boolean isNonNull(){
		return isPresent() && get() != null;
	}

	/**
	 * Creates a new empty Maybe
	 * @return an empty Maybe
	 */
	public static <S> Maybe<S> empty(){
		return new Maybe<S>();
	}

	/**
	 * Creates a new filled Maybe with the given value
	 * @param s the value of the Maybe
	 * @return a new filled Maybe with the given value
	 */
	public static <S> Maybe<S> with(S s){
		return new Maybe<S>(s);
	}

	/**
	 * Creates a new filled Maybe with the given non-null value
	 * @param s the value of the Maybe
	 * @return a new filled Maybe with the given non-null value
	 * @throws NullPointerException if the input is null
	 */
	public static <S> Maybe<S> withNonNull(S s) throws NullPointerException{
		if(s == null){
			throw new NullPointerException("Input is null");
		}
		return with(s);
	}

	/**
	 * Creates a new filled Maybe with the given non-null value,
	 * or a new empty Maybe with the given null value.
	 * @param s the value of the Maybe, or null if the Maybe is empty
	 * @return a new Maybe that is filled or unfilled depending on the value
	 */
	public static <S> Maybe<S> withNullAsEmpty(S s){
		if(s == null){
			return empty();
		}
		else{
			return with(s);
		}
	}

	@Override
	public String toString(){
		if(!isPresent()){
			return "None";
		}
		else{
			return "Value: " + t;
		}
	}

	public static <T, S> Maybe<S> compose(Maybe<T> maybe, Function<T, S> function){
		if(maybe.isPresent()){
			return Maybe.with(function.apply(maybe.get()));
		}
		else{
			return Maybe.empty();
		}
	}
}
