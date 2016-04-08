package com.markusfeng.SocketRelay.Packet;

import java.util.function.Function;
import java.util.function.Supplier;

import com.markusfeng.Shared.Maybe;
import com.markusfeng.SocketRelay.Switching.SendingObject;
import com.markusfeng.SocketRelay.Switching.SendingObjects;

public final class ParsingSystems{

	public ParsingSystems(){

	}

	public static <T, S> Function<T, Maybe<S>> certainMaybe(final Function<T, S> function){
		return new Function<T, Maybe<S>>(){

			@Override
			public Maybe<S> apply(T t){
				return Maybe.with(function.apply(t));
			}

		};
	}

	public static <T, S> ParsingSystem<T, S> fromFunctions(final Function<T, Maybe<S>> forward,
			final Function<S, Maybe<T>> reverse){
		return new ParsingSystem<T, S>(){

			@Override
			public Maybe<S> parse(T t){
				return forward.apply(t);
			}

			@Override
			public Maybe<T> unparse(S s){
				return reverse.apply(s);
			}

		};
	}

	public static <T> ParsingSystem<SendingObject<T>, T> flatteningSystem(final Supplier<Long> sender,
			final Supplier<Long> recipient){
		return fromFunctions(new Function<SendingObject<T>, Maybe<T>>(){

			@Override
			public Maybe<T> apply(SendingObject<T> t){
				return Maybe.with(t.getObject());
			}

		}, new Function<T, Maybe<SendingObject<T>>>(){

			@Override
			public Maybe<SendingObject<T>> apply(T t){
				return Maybe.with(SendingObjects.make(sender.get(), recipient.get(), t));
			}

		});
	}

	public static <T> ParsingSystem<String, SendingObject<T>> sendingSystem(final ParsingSystem<String, T> system){
		return new ParsingSystem<String, SendingObject<T>>(){

			private static final String SEPARATOR = "#";
			private static final long DEFAULT_SENDER = 0;
			private static final long DEFAULT_RECIPIENT = 0;

			@Override
			public Maybe<SendingObject<T>> parse(String t){
				String parseString = t;
				long value = DEFAULT_RECIPIENT;
				int index = t.indexOf(SEPARATOR);
				if(index >= 0){
					try{
						String val = t.substring(0, index);
						if(val.length() > 0){
							value = Long.parseLong(val);
						}
						parseString = t.substring(index + SEPARATOR.length());
					}
					catch(NumberFormatException e){
						//TODO exception handling
						e.printStackTrace();
					}
				}
				Maybe<T> maybe = system.parse(parseString);
				if(maybe.isPresent()){
					return Maybe.with(SendingObjects.make(DEFAULT_SENDER, value, maybe.get()));
				}
				else{
					return Maybe.empty();
				}
			}

			@Override
			public Maybe<String> unparse(SendingObject<T> s){
				Maybe<String> maybe = system.unparse(s.getObject());
				if(maybe.isPresent()){
					return Maybe.with(s.getRecipient() + "#" + maybe);
				}
				else{
					return Maybe.empty();
				}
			}

		};
	}

	public static <T, S> ParsingSystem<S, T> inverse(final ParsingSystem<T, S> system){
		return new ParsingSystem<S, T>(){

			@Override
			public Maybe<T> parse(S t){
				return system.unparse(t);
			}

			@Override
			public Maybe<S> unparse(T s){
				return system.parse(s);
			}

		};
	}

	public static <T, S, R> ParsingSystem<T, R> join(final ParsingSystem<T, S> first, final ParsingSystem<S, R> second){
		return new ParsingSystem<T, R>(){

			@Override
			public Maybe<R> parse(T t){
				Maybe<S> maybe = first.parse(t);
				if(maybe.isPresent()){
					return second.parse(maybe.get());
				}
				else{
					return Maybe.empty();
				}
			}

			@Override
			public Maybe<T> unparse(R s){
				Maybe<S> maybe = second.unparse(s);
				if(maybe.isPresent()){
					return first.unparse(maybe.get());
				}
				else{
					return Maybe.empty();
				}
			}

		};
	}

	public static <T> ParsingSystem<T, T> identity(){
		return new ParsingSystem<T, T>(){

			@Override
			public Maybe<T> parse(T t){
				return Maybe.with(t);
			}

			@Override
			public Maybe<T> unparse(T s){
				return Maybe.with(s);
			}

		};
	}
}
