package com.markusfeng.SocketRelay.Switching;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class SendingObjects{

	public static final long DEFAULT_ID = 0L;
	protected static Random random = new Random();
	protected static final Set<Long> DEFAULT_EXCLUDED = Collections
			.unmodifiableSet(new HashSet<Long>(Arrays.asList(DEFAULT_ID)));

	private SendingObjects(){

	}

	public static <T> SendingObject<T> make(long sender, long recipient, T t){
		return new GenericSendingObject<T>(sender, recipient, t);
	}

	public static long generateID(Set<Long> excluded){
		long id = random.nextLong();
		while(illegalID(id) || excluded.contains(id)){
			id = random.nextLong();
		}
		return id;
	}

	public static boolean illegalID(long id){
		return DEFAULT_EXCLUDED.contains(id);
	}

	protected static class GenericSendingObject<T> implements SendingObject<T>{

		protected T t;
		protected long sender;
		protected long recipient;

		public GenericSendingObject(long sender, long recipient, T t){
			this.t = t;
			this.sender = sender;
			this.recipient = recipient;
		}

		@Override
		public T getObject(){
			return t;
		}

		@Override
		public long getSender(){
			return sender;
		}

		@Override
		public long getRecipient(){
			return recipient;
		}

	}
}
