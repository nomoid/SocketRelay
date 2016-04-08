package com.markusfeng.SocketRelay.Packet;

import com.markusfeng.Shared.Maybe;

public interface ParsingSystem<T, S>{

	Maybe<S> parse(T t);

	Maybe<T> unparse(S s);
}
